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

package arl.nlp.parse.io;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import arl.common.meta.ArgMap;
import arl.common.meta.Initializable;
import arl.common.meta.InitializationException;
import arl.common.types.KeyValuePair;
import arl.graph.types.Arc;
import arl.nlp.parse.types.Parse;
import arl.nlp.parse.types.TreeToken;

/**
 * Writes data out in CoNLL-U format
 */
public class CoNLLUParseWriter extends AbstractParseWriter {
	

	@Override
	public void initialize(ArgMap args) throws InitializationException {
		super.initialize(args);
		Initializable.checkArgNames(args, new HashSet<>(Arrays.asList(PARAM_APPEND,
																	  PARAM_AUTO_FLUSH,
																	  PARAM_ENCODING,
																	  PARAM_OUTPUT_FILE)));
	}
	
	

	public void insertComment(String comment) {
		writeComment(comment);
		if(mAutoFlush) {
			mWriter.flush();
		}
	}
	
	@Override
	public void appendInstance(Parse state) {
		writeComments(state);
		
		List<? extends TreeToken> allCores = state.getAllTreeTokens();
		Map<TreeToken, String> coreToId = new HashMap<>();
		Map<TreeToken, Integer> coreToIndex = new HashMap<>();
		{	
			int nonEmptyIndex = 0;
			int emptyIndex = 1;
			for(TreeToken core : allCores) {
				String id = null;
				if(core.isEmptyNode()) {
					id = nonEmptyIndex + "." + emptyIndex++;
				}
				else {
					emptyIndex = 1;
					id = "" + ++nonEmptyIndex;
				}
				coreToId.put(core, id);
			}
		}
		
		for(TreeToken core : allCores) {
			/*
			 * Current handling for multiword tokens
			 */
			if(core.getPrevClitic() == null && core.getNextClitic() != null) {
				writeRangeLine(coreToId, core);
			}
			writeWordLine(coreToIndex, coreToId, core);
		}
		mWriter.print('\n');
		
		if(mAutoFlush) {
			flush();
		}
	}
	
	private void writeRangeLine(Map<TreeToken, String> coreToId, TreeToken core) {
		 /* This works under the assumption that component words of the token are encoded as clitics and 
		  * that the multiword token text can be recovered by concatenating the text of the component words 
		  * together (which is a false assumption)
		  */
		String rangeStart = coreToId.get(core);
		String rangeEnd = rangeStart;
		StringBuilder buf = new StringBuilder();
		TreeToken current = core;
		buf.append(current.getText());
		while(current.getNextClitic() != null) {
			current = current.getNextClitic();
			//rangeEnd++;
			rangeEnd = coreToId.get(current);
			buf.append(current.getText());
		}
		mWriter.printf("%s\t%s\t_\t_\t_\t_\t_\t_\t_\t_\n", rangeStart+"-"+rangeEnd, buf.toString());
	}
	
	private void writeWordLine(Map<TreeToken, Integer> coreToIndex, Map<TreeToken, String> coreToId, TreeToken core) {
		String id, form, lemma, upostag, xpostag, feats, head, deprel, deps, misc;
		id = coreToId.get(core);
		form = getForm(core);
		lemma = getLemma(core);
		upostag = getUPOSTag(core);
		xpostag = getXPOSTag(core);
		
		feats = getFeats(core);
		
		head = getHead(core, coreToId);
		deprel = getDeprel(core);
		deps = getDeps(core, coreToId, coreToIndex);
		misc = getMisc(core, coreToId);
		
		mWriter.printf("%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\n", id, form, lemma, upostag, xpostag, feats, head, deprel, deps, misc);
	}
	
	protected void writeComments(Parse state) {
		Collection<String> comments = state.getComments();
		if(comments != null) {
			for(String comment : comments) {
				writeComment(comment);
			}
		}
	}
	
	protected void writeComment(String comment) {
		mWriter.print("#"+escapeComment(comment)+"\n");
	}
	
	protected String escapeComment(String comment) {
		return comment.replace("\n", "\\n").replace("\r", "\\r");
	}
	
	
	protected String getId(TreeToken core, Map<TreeToken, Integer> coreToIndex) {
		return ""+coreToIndex.get(core);
	}
	
	protected String getForm(TreeToken core) {
		return core.getText();
	}
	
	protected String getLemma(TreeToken core) {
		String lemma = core.getLemma();
		if(lemma == null) lemma = "_";
		return lemma;
	}
	
	protected String getFeats(TreeToken core) {
		//String feats = (String)core.getProperty(CoNLLUConstants.FEATS_FIELD);
		//if(feats == null) feats = "_";
		 
		// Code for filling out feats based upon the morphology features
		String feats;
		List<? extends KeyValuePair<String, String>> morphoFeatures = core.getMorphologyFeatures();
		if(morphoFeatures == null || morphoFeatures.size() == 0) {
			feats = "_";
		}
		else {
			StringBuilder buf = new StringBuilder();
			for(KeyValuePair<String, String> kvp : morphoFeatures) {
				if(buf.length() > 0) {
					buf.append("|");
				}
				buf.append(kvp.getKey()+"="+kvp.getValue());
			}
			feats = buf.toString();
		}
		return feats;
	}
	
	protected String getMisc(TreeToken core, Map<TreeToken, String> coreToId) {
		// Write out properties in the MISC field (TODO: may be nice if this sort of behavior was customizable)
		String misc = "_";
		Set<String> properties = core.getPropertyKeys();
		if(properties.size() > 0) {
			StringBuilder buf = new StringBuilder();
			for(String property : properties) {
				// UD Format specifies that MISC field is splittable on pipe characters
				if(buf.length() > 0) buf.append("|");
				buf.append(property + "=" + (core.getProperty(property)!=null?core.getProperty(property).toString().replace("|", "\\|"):""));
			}
			misc = buf.toString();
		}
		return misc;
	}
	
	protected String getDeps(TreeToken core, Map<TreeToken, String> coreToId, Map<TreeToken, Integer> coreToIndex) {
		List<Arc<TreeToken>> enhancedDeps = core.getEnhancedDeps();
		int numDeps = enhancedDeps.size();
		if(numDeps == 0) {
			return "_";
		}
		else {
			StringBuilder buf = new StringBuilder();
			// CoNLL-U format specifies that dependencies should be in ascending order
			Collections.sort(enhancedDeps, new Comparator<Arc<TreeToken>>() {
				@Override
				public int compare(Arc<TreeToken> a, Arc<TreeToken> b) {
					return coreToIndex.get(a.getChild())-coreToIndex.get(b.getChild());
				}
			});
			for(int i = 0; i < numDeps; i++) {
				Arc<TreeToken> dep = enhancedDeps.get(i);
				if(buf.length()>0) buf.append("|");
				buf.append(coreToId.get(dep.getHead())).append(":").append(dep.getLabel());
			}
			return buf.toString();
		}
	}
	
	protected String getXPOSTag(TreeToken core) {
		String xpostag = core.getPos();
		if(xpostag == null) xpostag = "_";
		return xpostag;
	}
	
	protected String getUPOSTag(TreeToken core) {
		String upostag = core.getPos2();
		/* Universal Dependency POS tag is not permitted to be unset; therefore, let's set it to something
		  if it is null; setting it to XPOSTAG seems like an OK option */
		if(upostag == null) upostag = getXPOSTag(core);
		return upostag;
	}
	
	protected String getHead(TreeToken core, Map<TreeToken, String> coreToId) {
		Arc<TreeToken> arc = core.getHeadArc();
		String head = arc==null?"0":""+coreToId.get(arc.getHead());
		return head;
	}
	
	protected String getDeprel(TreeToken core) {
		Arc<TreeToken> arc = core.getHeadArc();
		String deprel = arc==null?"root":arc.getLabel();
		return deprel;
	}

}
