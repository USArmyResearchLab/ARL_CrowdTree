/*
 *
 * This file is part of ARL CrowdTree and is subject to the following:
 * 
 * Copyright 2018 United States Government and Nhien Phan
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 *   
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *  
 */

package arl.nlp.crowdling;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import arl.common.io.InstanceWriter;
import arl.common.meta.ArgMap;
import arl.common.meta.Initializable;
import arl.common.meta.InitializationException;
import arl.common.types.MutableKeyValuePair;
import arl.common.util.IOUtils;
import arl.graph.types.Arc;
import arl.nlp.parse.types.TreeToken;
import arl.nlp.parse.types.DefaultTreeToken;
import arl.nlp.parse.types.DefaultParse;
import arl.nlp.parse.types.Parse;
import arl.nlp.parse.types.MutableTreeToken;
import arl.nlp.punctuation.PunctuationAttacher;
import arl.nlp.crowdling.jsontypes.D3TreeNodeWrapper;
import arl.nlp.crowdling.jsontypes.KeyValuePair;
import arl.nlp.crowdling.jsontypes.Sentence;
import arl.nlp.crowdling.jsontypes.TreeEditorSubmissionObject;
import arl.nlp.crowdling.jsontypes.TreeNode;

public class TreeEditorPostModule implements PostModule {
	
	public final static String PARAM_PUNCT_ATTACH_HEURISTIC = "PunctuationHeuristic",
							   PARAM_REINSERT_PUNCT = "ReinsertPunctuation";
	
	
	private PunctuationAttacher<DefaultParse, DefaultTreeToken> mPunctuationAttacher;
	private boolean mReinsertPunctuation = true;
	
	@Override
	public void initialize(ArgMap argMap) throws InitializationException {
		// Verify there are no spurious argument names
		Initializable.checkArgNames(argMap, new HashSet<>(Arrays.asList(PARAM_PUNCT_ATTACH_HEURISTIC, PARAM_REINSERT_PUNCT)));
		mPunctuationAttacher = (PunctuationAttacher<DefaultParse, DefaultTreeToken>)argMap.get(PARAM_PUNCT_ATTACH_HEURISTIC);
		mReinsertPunctuation = argMap.getBooleanValue(PARAM_REINSERT_PUNCT, mReinsertPunctuation);
	}

	@Override
	public void doPost(HttpServletRequest request, 
					   HttpServletResponse response,
					   TreeEditorServlet servlet) throws ServletException, IOException {
		System.err.println(this.getClass().getName()+": doPost() begun");
		String requestId = UUID.randomUUID().toString();
		TreeEditorCommonModule common = (TreeEditorCommonModule)servlet.getServletContext().getAttribute(TreeEditorServlet.APP_COMMON_MODULE);
		try {
			long timeReceived = System.currentTimeMillis();
			// Read content (JSON object as a string)
		
			String requestContent = IOUtils.readUTF8StreamAsString(request.getInputStream());
	
			ObjectMapper mapper = new ObjectMapper();
			TreeEditorSubmissionObject submissionObject = mapper.readValue(requestContent, TreeEditorSubmissionObject.class);		
	
			common.ioLog(requestId + " post_submission_obj "+ mapper.writeValueAsString(submissionObject));
			
			int sid = -1;
			DefaultParse newTrainingExample = null;
			Sentence originalSentence;
			String assignmentId = submissionObject.getAssignmentId();
			if(assignmentId != null
					&& common.getAssignmentStorage().containsKey(assignmentId)) {
				originalSentence = common.getAssignmentStorage().get(assignmentId);
			}
			else {
				originalSentence = submissionObject.getOriginalSentence();
			}
			
			// Convert Java object to parse tree object
			newTrainingExample = d3TreeToMorphoState(submissionObject.getRoot());
			if(submissionObject.getComments() != null) {
				for(String comment : submissionObject.getComments()) {
					newTrainingExample.addComment(comment);
				}
			}
	
			// Get the id back
			sid = Integer.parseInt(originalSentence.getSid());
			// retrieve the original
			List<Parse> toAnnotateList = common.getToAnnotateList();
			DefaultParse original = (DefaultParse)toAnnotateList.get(sid);
	
			if(mReinsertPunctuation) {
				reinsertMissingPunctuation(original, newTrainingExample, mPunctuationAttacher);
			}
			newTrainingExample.addComment("receiptTime="+timeReceived);
			newTrainingExample.addComment("workerId="+submissionObject.getWorkerId());
			newTrainingExample.addComment("hitId="+submissionObject.getHitId());
			newTrainingExample.addComment("assignmentId="+submissionObject.getAssignmentId());
			newTrainingExample.addComment("workerNotes="+submissionObject.getNotes());
			
			InstanceWriter<Parse> annotWriter =  common.getAnnotationRecorder();
			synchronized(annotWriter) {
				annotWriter.appendInstance(newTrainingExample);
				annotWriter.flush();
			}
	
			// Add to list of training examples
			addTrainingExample(newTrainingExample, common, sid);
		
			System.err.println(this.getClass().getName()+": doPost() complete");
			common.ioLog(requestId + " post_complete");
		}
		catch(Exception e) {
			System.err.println(this.getClass().getName()+": doPost() exception");
			StringWriter swriter = new StringWriter();
			e.printStackTrace(new PrintWriter(swriter));
			common.ioLog(requestId + " post_exception " + swriter.toString());
			e.printStackTrace();
			
			// Not rethrowing exception with the idea that everything will return normally and
			// the client JavaScript code will then proceed to submit to MTurk.
			// Advantage: smoothness of experience on client side
			// Disadvantages: hides problems that need to be addressed
			//
			// TODO: It would be good to give some more thought to the proper handling of Exceptions
		}
	}
	
	private void addTrainingExample(Parse newTrainingExample, 
									TreeEditorCommonModule commonModule, 
									int sentenceId) {
		boolean trainOnLatestOnly = commonModule.getTrainOnLatestOnly();
		Vector<Parse> trainingInstances = commonModule.getTrainingInstances();
		if(trainingInstances != null) {
			Map<Integer, LinkedList<Parse>> submittedExamples = commonModule.getSubmittedExamples();
		
			System.err.println(this.getClass().getName()+": Latest only?: " + trainOnLatestOnly);
			if(trainOnLatestOnly) {
				synchronized(submittedExamples) {
					LinkedList<Parse> examples = submittedExamples.get(sentenceId);
					if(examples == null) {
						submittedExamples.put(sentenceId, examples = new LinkedList<>());
					}
					newTrainingExample.setProperty(Parse.SENTENCE_ID_PROPERTY_KEY, "submission:"+commonModule.getSubmissionCounter().getAndIncrement());
					trainingInstances.add(newTrainingExample);
					if(examples.size() > 0) {
						trainingInstances.remove(examples.getLast());
					}
					examples.add(newTrainingExample);
				}
			}
			else {
				trainingInstances.add(newTrainingExample);
			}
		}
	}
	
	private DefaultParse d3TreeToMorphoState(D3TreeNodeWrapper root) {
		Map<D3TreeNodeWrapper, D3TreeNodeWrapper> nodeToParent = new HashMap<>();
		buildChildToParentMap(root, nodeToParent);
		List<D3TreeNodeWrapper> allNodes = new ArrayList<>(nodeToParent.keySet());
		Collections.sort(allNodes, new Comparator<D3TreeNodeWrapper>() {
			@Override
			public int compare(D3TreeNodeWrapper t1, D3TreeNodeWrapper t2) {
				return Integer.parseInt(((TreeNode)t1.getData()).getOid())-Integer.parseInt(((TreeNode)t2.getData()).getOid());
			}
		});
		
		List<DefaultTreeToken> cores = new ArrayList<>();
		Map<DefaultTreeToken, D3TreeNodeWrapper> coreToTreeNode = new HashMap<>();
		Map<String, DefaultTreeToken> indexToCore = new HashMap<>();
		DefaultTreeToken prevCore = null;
		for(D3TreeNodeWrapper node : allNodes) {
			TreeNode data = (TreeNode)node.getData();
			DefaultTreeToken core = new DefaultTreeToken(data.getText(), data.getPos());
			core.setPos2(data.getPos2());
			core.setLemma(data.getLemma());
			core.setGloss(data.getGloss());
			
			if(data.getFeats() != null) {
				for(KeyValuePair kvp : data.getFeats()) {
					core.setMorphologyFeature(kvp.getK(), kvp.getV());
				}
			}
			if(data.getMisc() != null) {
				for(KeyValuePair kvp : data.getMisc()) {
					core.setProperty(kvp.getK(), kvp.getV());
				}
			}
			cores.add(core);
			core.setPrevSurfaceCore(prevCore);
			if(prevCore != null) prevCore.setNextSurfaceCore(core);
			coreToTreeNode.put(core, node);
			indexToCore.put(data.getOid(), core);
			prevCore = core;
		}
		for(D3TreeNodeWrapper node : allNodes) {
			TreeNode data = (TreeNode)node.getData();
			DefaultTreeToken core = indexToCore.get(data.getOid());
			String nextCliticId = data.getNextCliticId();
			if(nextCliticId != null) {
				DefaultTreeToken nextClitic = indexToCore.get(nextCliticId);
				core.setNextClitic(nextClitic);
				nextClitic.setPrevClitic(core);
			}
			String prevCliticId = data.getPrevCliticId();
			if(prevCliticId != null) {
				DefaultTreeToken prevClitic = indexToCore.get(prevCliticId);
				core.setPrevClitic(prevClitic);
				prevClitic.setNextClitic(core);
			}
		}
		for(DefaultTreeToken core : cores) {
			D3TreeNodeWrapper node = coreToTreeNode.get(core);
			D3TreeNodeWrapper parent = nodeToParent.get(node);
			DefaultTreeToken parentCore = indexToCore.get(((TreeNode)parent.getData()).getOid());
			if(parentCore != null) {
				Arc<TreeToken> headArc = new Arc<TreeToken>(core, parentCore, node.getData().getDep());
				core.setHeadArc(headArc);
				boolean precedes = Integer.parseInt(((TreeNode)parent.getData()).getOid()) > 
				Integer.parseInt(((TreeNode)node.getData()).getOid());
				parentCore.addChildArc(headArc, precedes);
			}
		}
		DefaultParse sentence = new DefaultParse(cores);
		
		return sentence;
	}
	
	private void buildChildToParentMap(D3TreeNodeWrapper node, Map<D3TreeNodeWrapper, D3TreeNodeWrapper> childToParent) {
		List<D3TreeNodeWrapper> allChildren = new ArrayList<>();
		List<D3TreeNodeWrapper> children = node.getChildren();
		if(children != null) allChildren.addAll(children);
		for(D3TreeNodeWrapper child : allChildren) {
			childToParent.put(child, node);
			buildChildToParentMap(child, childToParent);
		}
	}
	
	// TODO: consider creating an interface + implementation for this
	public static String reinsertMissingPunctuation(DefaultParse completeVersion,
			DefaultParse punctMissingVersion,
			PunctuationAttacher<DefaultParse, DefaultTreeToken> punctuationReattachRoutine) {
		List<? extends TreeToken> cores1 = completeVersion.getAllTreeTokens();
		List<? extends TreeToken> cores2 = punctMissingVersion.getAllTreeTokens();
		TreeToken rootC = null;
		for (TreeToken c : cores2) {
			if (c.getHeadArc() == null) {
				rootC = c;
				break;
			}
		}
		TreeToken first1 = cores1.get(0);
		TreeToken first2 = cores2.get(0);

		StringBuilder debugBuf = new StringBuilder("<debug>");
		try {
			TreeToken a1 = first1;
			TreeToken a2 = first2;
			DefaultTreeToken prev1 = null;
			DefaultTreeToken prev2 = null;
			while (a1 != null) {
				prev1 = (DefaultTreeToken) a1;

				debugBuf.append((a1 == null ? "null" : a1.getText()) + " : " + (a2 == null ? "null" : a2.getText()))
						.append('\n');
				if (a2 == null) {
					if (prev2 != null) {
						a2 = new DefaultTreeToken(a1.getText(), a1.getPos());
						prev2.setNextSurfaceCore(a2);
						((DefaultTreeToken) a2).setPrevSurfaceCore(prev2);

						punctMissingVersion.addAnchor(a2);
						prev2 = (DefaultTreeToken) a2;
						a2 = a2.getNextSurfaceTreeToken();
					}
				} else if (a1.getText().equals(a2.getText())) {
					prev2 = (DefaultTreeToken) a2;
					a2 = a2.getNextSurfaceTreeToken();
				} else {
					DefaultTreeToken newCore = new DefaultTreeToken(a1.getText(), a1.getPos());
					if (prev2 != null)
						prev2.setNextSurfaceCore(newCore);
					newCore.setPrevSurfaceCore(prev2);
					newCore.setNextSurfaceCore(a2);
					((MutableTreeToken) a2).setPrevSurfaceCore(newCore);
					punctMissingVersion.addAnchor(newCore);
					prev2 = newCore;
				}
				a1 = (DefaultTreeToken) a1.getNextSurfaceTreeToken();
			}
		} catch (Exception e) {
			System.err.println(debugBuf.toString());
			e.printStackTrace();
			throw new RuntimeException();
		}
		debugBuf.append("preattach");
		punctuationReattachRoutine.reattachPunctuation(punctMissingVersion, (DefaultTreeToken) rootC);
		return debugBuf.append("</debug>").toString();
	}
	
	
}
