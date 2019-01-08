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
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import arl.common.meta.ArgMap;
import arl.common.meta.Initializable;
import arl.common.meta.InitializationException;
import arl.common.types.KeyValuePair;
import arl.graph.types.Arc;
import arl.nlp.parse.types.Affix;
import arl.nlp.parse.types.TreeToken;
import arl.nlp.parse.types.Parse;
import arl.nlp.crowdling.jsontypes.Sentence;
import arl.nlp.crowdling.jsontypes.TreeEditorGETResponse;
import arl.nlp.crowdling.jsontypes.TreeNode;

public class TreeEditorGetModule implements Initializable, GetModule {
	
	public final static String 	// param to control flattening of trees
								INIT_PARAM_FLAT_TREES = "FlatTrees",
								// param for punctuation filter to mark tokens as punctuation (invisible in tree on client side)
								INIT_PARAM_PUNCTUATION_FILTER = "PunctuationFilter",
								// direction of text (e.g., ltr for left-to-right)
								INIT_PARAM_TEXT_DIR = "TextDirection",
								// param for controlling whether to provide latest human-provided annotation for a given sentence
								INIT_PARAM_PROVIDE_LATEST = "ProvideLatestHuAnnotation",
								// param for minimum number of training examples to have seen before using parser
								INIT_PARAM_MIN_TRAINING_EXAMPLES = "MinTrainingExamples",
								INIT_PARAM_DEFAULT_DEP_LABEL = "DefaultDepLabel",
								// param controlling the probability (0.0-1.0)
								INIT_PARAM_AUTOPARSE_PROBABILITY = "Autoparse";
	
	private Random mRng = new Random(0);
	private boolean mFlatTrees = false;
	private boolean mProvideLatestHuVersion = false;
	private PunctuationFilter mPunctFilter;
	private String mTextDirection = "ltr";
	private int mMinTrainingExamples;
	private double mAutoParseProbability;
	
	private String mDefaultDepLabel = "root";
	
	@Override
	public void initialize(ArgMap argMap) throws InitializationException {
		// Verify there are no spurious argument names
		Initializable.checkArgNames(argMap, new HashSet<>(Arrays.asList(INIT_PARAM_FLAT_TREES,
																		INIT_PARAM_AUTOPARSE_PROBABILITY,
																		INIT_PARAM_PUNCTUATION_FILTER,
																		INIT_PARAM_TEXT_DIR,
																		INIT_PARAM_PROVIDE_LATEST,
																		INIT_PARAM_DEFAULT_DEP_LABEL,
																		INIT_PARAM_MIN_TRAINING_EXAMPLES)));
		mFlatTrees = argMap.getBooleanValue(INIT_PARAM_FLAT_TREES, true);
		mAutoParseProbability = argMap.getDoubleValue(INIT_PARAM_AUTOPARSE_PROBABILITY, 0);
		mProvideLatestHuVersion = argMap.getBooleanValue(INIT_PARAM_PROVIDE_LATEST, false);
		mTextDirection = argMap.getStringValue(INIT_PARAM_TEXT_DIR, mTextDirection);
		mMinTrainingExamples = argMap.getIntegerValue(INIT_PARAM_MIN_TRAINING_EXAMPLES, 0);
		mDefaultDepLabel = argMap.getStringValue(INIT_PARAM_DEFAULT_DEP_LABEL, mDefaultDepLabel);
		
		mPunctFilter = (PunctuationFilter)argMap.get(INIT_PARAM_PUNCTUATION_FILTER);
	}

	@Override
	public void doGet(HttpServletRequest request, 
					  HttpServletResponse response, 
					  TreeEditorServlet servlet) throws ServletException, IOException {
		response.setContentType("application/json;charset=UTF-8"); 
		
		TreeEditorCommonModule common = (TreeEditorCommonModule)servlet.getServletContext().getAttribute(TreeEditorServlet.APP_COMMON_MODULE);
		
		String requestId = UUID.randomUUID().toString();
		
		try {
			common.ioLog(requestId + " get_request " +  request.getQueryString());
			
			// If tree identifier was not an integer, let's fail fast
			int sentenceId = Integer.parseInt(request.getParameter("tree"));
			String assignmentId = request.getParameter("assignmentId");
			
			Sentence sentenceToReturn = null;
			if (assignmentId != null && !assignmentId.equals("false") && !assignmentId.equals("ASSIGNMENT_ID_NOT_AVAILABLE")
					&& common.getAssignmentStorage().containsKey(assignmentId)) {
				sentenceToReturn = common.getAssignmentStorage().get(assignmentId);
			} 
			else {
				List<Parse> toAnnotateList = common.getToAnnotateList();

				if (sentenceId >= 0 && sentenceId < toAnnotateList.size()) {
					// 1) get the sentence
					Parse ms = toAnnotateList.get(sentenceId);

					// 2) determine the parse structure to return
					boolean flattenTree = false;
					boolean providingHuAnnot = false;
					// 2.1) consider providing a previous human-provided version
					if (mProvideLatestHuVersion) {
						Map<Integer, LinkedList<Parse>> annotMap = common.getSubmittedExamples();
						synchronized (annotMap) {
							LinkedList<Parse> states = annotMap.get(sentenceId);
							if (states != null && states.size() > 0) {
								//System.err.println(this.getClass().getName()+": Providing previous human parse");
								ms = states.getLast();
								providingHuAnnot = true;
							}
						}
					}

					// 2.2) Consider providing a parsing model-provided version or a flat version
					if (!providingHuAnnot) {
						// Parse with parsing model
						Vector<Parse> trainingInstances = common.getTrainingInstances();
						double autoparseDraw;
						synchronized(mRng) {
							autoparseDraw = mRng.nextDouble();
						}
						//System.err.println("Provide parser output?:: " + mAutoParseProbability + " >= " + autoparseDraw
						//		+ " && " + mMinTrainingExamples + " <= " + (trainingInstances==null?"null":trainingInstances.size()));
						if (mAutoParseProbability >= autoparseDraw
								&& mMinTrainingExamples <= (trainingInstances==null?0:trainingInstances.size())
								&& common.getDecoder() != null) {
							//System.err.println(this.getClass().getName()+": Providing parser-generated parse");
							ms = (Parse) common.getDecoder().process(ms).x;
						} 
						else if (mFlatTrees) {
							//System.err.println(this.getClass().getName()+": Providing flat parse");
							flattenTree = true;
						}
					}

					// #3 convert the parse into JSON-like objects for Jackson
					TreeNode rootJson = convertToJson(ms, mPunctFilter, mDefaultDepLabel, "<<root>>", true,
							flattenTree);
					sentenceToReturn = new Sentence();
					sentenceToReturn.setRoot(rootJson);
					sentenceToReturn.setFulltext(toSentenceString(ms));
					sentenceToReturn.setDir(mTextDirection);
					sentenceToReturn.setSid("" + sentenceId);
					sentenceToReturn.setComments(new ArrayList<>(ms.getComments()));
					
					if(assignmentId != null && !assignmentId.equals("false")) {
						common.getAssignmentStorage().put(assignmentId, sentenceToReturn);
					}
				}
			}
			if(sentenceToReturn != null) {
				TreeEditorGETResponse getResponse = new TreeEditorGETResponse();
				getResponse.setSentence(sentenceToReturn);
				ObjectMapper mapper = new ObjectMapper();
				//String jsonString = mapper.writeValueAsString(sentenceToReturn);
				String jsonString = mapper.writeValueAsString(getResponse);
				common.ioLog(requestId + " get_response " +  jsonString);
				response.getWriter().print(jsonString);
			}
		} 
		catch (Exception e) {
			StringWriter swriter = new StringWriter();
			e.printStackTrace(new PrintWriter(swriter));
			common.ioLog(requestId + " get_exception " + swriter.toString());
			e.printStackTrace();
			// TODO: Should document the practical difference between throwing a ServletException vs. throwing a RuntimeException
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Take a <code>MorphoState</code> and, from the tokens, produce a <code>String</code> representation
	 */
	public static String toSentenceString(Parse state) {
		List<? extends TreeToken> allCores = state.getAllTreeTokens();
		StringBuilder buf = new StringBuilder();
		for(TreeToken c : allCores) {
			for(Affix a : c.getPrefixes()) buf.append(a.getText());
			buf.append(c.getCoreText());
			for(Affix a : c.getSuffixes()) buf.append(a.getText());
			if(c.getNextClitic() == null) buf.append(" ");
		}
		return buf.toString().trim();
	}
	
	
	/**
	 * Convert a <code>Parse</code> to a JSON-like Java object, optionally removing punctuation 
	 * and optionally flattening the tree
	 */
	private TreeNode convertToJson(Parse state, 
								   PunctuationFilter punctFilter,
								   String defaultDepLabel,
								   String rootNodeText,
								   boolean promoteChildrenOfPunctuation,
								   boolean flattenTree) {
		TreeNode rootJsonNode = new TreeNode();
		rootJsonNode.setText(rootNodeText);
		rootJsonNode.setOid("0");
		
		Map<TreeToken, TreeNode> coreToJnode = new HashMap<>();
		for(TreeToken c : state.getAllTreeTokens()) {
			coreToJnode.put(c, new TreeNode());
		}
		
		int id = 1;
		List<? extends TreeToken> cores = state.getAllTreeTokens();
		TreeNode prevJnode = null;
		for(TreeToken c : cores) {
			TreeNode jnode = coreToJnode.get(c);
			Arc<TreeToken> headArc = c.getHeadArc();
			
			//c.getPrefixes();
			jnode.setText(c.getText());
			jnode.setOid(""+id);
			jnode.setPos(c.getPos());
			jnode.setPos2(c.getPos2());
			jnode.setLemma(c.getLemma());
			jnode.setLroot(c.getLexRoot());
			jnode.setGloss(c.getGloss());
			jnode.setDep(headArc != null&&!flattenTree?""+headArc.getLabel():defaultDepLabel);
			List<? extends KeyValuePair<String, String>> morphFeats = c.getMorphologyFeatures();
			if(morphFeats != null && morphFeats.size() > 0) {
				List<arl.nlp.crowdling.jsontypes.KeyValuePair> featsCopy = new ArrayList<>();
				for(KeyValuePair<String,String> kvPair : morphFeats) {
					featsCopy.add(new arl.nlp.crowdling.jsontypes.KeyValuePair(kvPair.getKey(), kvPair.getValue()));
				}
				jnode.setFeats(featsCopy);
			}
			Set<String> propertyKeys = c.getPropertyKeys();
			if(propertyKeys != null && propertyKeys.size() > 0) {
				List<arl.nlp.crowdling.jsontypes.KeyValuePair> miscCopy = new ArrayList<>();
				for(String property : c.getPropertyKeys()) {
					Object value = c.getProperty(property);
					miscCopy.add(new arl.nlp.crowdling.jsontypes.KeyValuePair(property, value==null?null:value.toString()));
					// TODO: Declare a constant someplace...
					if(property.equals("SpaceAfter")
							&& value!=null
							&& value.toString().equals("false")) {
						jnode.setNoSpaceAfter(Boolean.TRUE);
					}
				}
				jnode.setMisc(miscCopy);
			}
			
			if(punctFilter != null) {
				boolean isPunct = punctFilter.isPunctuation(c.getText(), c.getPos());
				if(isPunct) {
					jnode.setPunct(isPunct);
				}
			}
			if(c.getNextClitic() != null) {
				jnode.setNextCliticId(coreToJnode.get(c.getNextClitic()).getOid());
				jnode.setNoSpaceAfter(Boolean.TRUE);
			}
			if(c.getPrevClitic() != null) {
				jnode.setPrevCliticId(coreToJnode.get(c.getPrevClitic()).getOid());
			}
			id++;
			prevJnode = jnode;
		}
		
		for(TreeToken c : state.getAllTreeTokens()) {
			TreeNode jnode = coreToJnode.get(c);
			Arc<TreeToken> arc = c.getHeadArc();
			TreeNode parent = (arc == null || flattenTree)?rootJsonNode:coreToJnode.get(arc.getHead());
			jnode.setPid(parent.getOid());
			if(parent.getChildren()==null) parent.setChildren(new ArrayList<>());
			parent.getChildren().add(jnode);
		}
		
		if(promoteChildrenOfPunctuation) {
			promoteChildrenOfPunctuation(rootJsonNode);
		}
		
		return rootJsonNode;
	}
	
	private void promoteChildrenOfPunctuation(TreeNode jsonNode) {
		List<TreeNode> children = jsonNode.getChildren();
		if(children != null) {
			for(int i = 0; i < children.size(); i++) {
				TreeNode child = children.get(i);
				if(child.getPunct()==Boolean.TRUE) {
					// If punctuation node happened to have children (e.g., from a parsing error)
					// then promote these children
					List<TreeNode> grandChildren = child.getChildren();
					if(grandChildren != null) {
						for(TreeNode gc : grandChildren) {
							children.add(gc);
							gc.setPid(jsonNode.getPid());
						}
						grandChildren.clear();
					}
				}
				else {
					promoteChildrenOfPunctuation(child);
				}
			}
		}
	}
}
