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

/*
 * ============================================================================
 *  Per the Apache 2.0 license redistribution requirements, the 
 *  University of Southern California copyright notice below is retained 
 *  in this file. This file includes code from one or more source files 
 *  of the following project (which is licensed under Apache 2.0).
 *     https://www.isi.edu/publications/licensed-sw/fanseparser/index.html
 *  This file contains modifications and is not identical to the original code.
 * ============================================================================    
 * 
 * Copyright 2011 University of Southern California 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0 
 *      
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 */

package arl.nlp.parse.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import arl.common.io.InstanceReadException;
import arl.common.meta.ArgMap;
import arl.common.meta.Initializable;
import arl.common.meta.InitializationException;
import arl.common.types.KeyValuePair;
import arl.graph.types.Arc;
import arl.nlp.parse.types.DefaultParse;
import arl.nlp.parse.types.DefaultTreeToken;
import arl.nlp.parse.types.MutableTreeToken;
import arl.nlp.parse.types.Parse;
import arl.nlp.parse.types.TreeToken;

/**
 * Designed to read in CoNLL-U format
 */
public class CoNLLUParseReader implements arl.common.io.InstanceReader<Parse> {
	
	public final static String PARAM_CANONICALIZE = "Canonicalize",
							   PARAM_VERBOSE = "Verbose",
							   PARAM_IGNORE_RANGES = "IgnoreRanges";
	
	private Map<String, String> mCanon = new HashMap<>();
	
	private BufferedReader mReader;
	
	private boolean mCanonicalize = true,
					mIgnoreRanges = false,
					mVerbose = false;
	
	private class CoNLLULine {
		public final static int WORD_NUM_INDEX = 0,
								WORD_FORM_INDEX = 1,
								LEMMA_INDEX = 2,
								UPOS_INDEX = 3,
								XPOS_INDEX = 4,
								FEATS_INDEX = 5,
								HEAD_NUM_INDEX = 6,
								DEPREL_INDEX = 7,
								DEPS_INDEX = 8,
								MISC_INDEX = 9;
		String form, lemma, upos, xpos, feats, dependency, head, deps, misc;
		String id;
		public CoNLLULine(String line) {
			// Should this be multiple tabs or a single tab?
			String[] split = line.split("\\t"); 
			id = split[WORD_NUM_INDEX];
			form = canon(split[WORD_FORM_INDEX]);
			lemma = canon(split[LEMMA_INDEX]);
			upos = canon(split[UPOS_INDEX]);
			xpos = canon(split[XPOS_INDEX]);
			feats = canon(split[FEATS_INDEX]);
			head = split[HEAD_NUM_INDEX];
			deps = split[DEPS_INDEX];
			misc = canon(split[MISC_INDEX]);
			dependency = canon(split[DEPREL_INDEX]);
		}
	}
	
	@Override
	public void initialize(ArgMap params) throws InitializationException {
		Initializable.checkArgNames(params, new HashSet<>(Arrays.asList(PARAM_CANONICALIZE, PARAM_VERBOSE, PARAM_IGNORE_RANGES)));
		mCanonicalize = params.getBooleanValue(PARAM_CANONICALIZE, mCanonicalize);
		mVerbose = params.getBooleanValue(PARAM_VERBOSE, mVerbose);
		mIgnoreRanges = params.getBooleanValue(PARAM_IGNORE_RANGES, mIgnoreRanges);
	}
	
	@Override
	final public Parse readInstance() throws InstanceReadException {
		Parse parse = null;
		try {
			List<DefaultTreeToken> words = new ArrayList<>();
			List<Arc<TreeToken>> arcs = new ArrayList<>();
			
			String line = null;
			
			Map<String, DefaultTreeToken> idToWord = new HashMap<>();
			
			List<String> comments = new ArrayList<>();
			
			List<CoNLLULine> rangeLines = new ArrayList<>();
			List<CoNLLULine> wordLines = new ArrayList<>();
			List<CoNLLULine> emptyWordLines = new ArrayList<>();
			
			DefaultTreeToken prevWord = null;
			while((line = mReader.readLine()) != null) {
				if(line.trim().equals("")) {
					if(words.size() == 0) continue;
					else break;
				}
				else if(line.startsWith("#")) {
					if(line.length() > 1) {
						if(wordLines.size()==0&&rangeLines.size()==0&&emptyWordLines.size()==0) {
							// Save comments appearing before the sentence at the sentence level
							comments.add(line.substring(1));
						}
						else {
							// Comments appearing elsewhere will be ignored in the current implementation
						}
					}
				}
				else {
					CoNLLULine conlluLine = new CoNLLULine(line);
					
					if(!conlluLine.id.matches("[0-9]+((-|\\.)[0-9]+)?")) throw new RuntimeException("Invalid ID field value: " + conlluLine.id);
					
					if(conlluLine.id.contains("-")) {
						rangeLines.add(conlluLine);
					}
					// Empty node (e.g., for handling ellipsis)
					/*else if(conlluLine.id.contains(".")) {
						System.err.println("Warning: empty node support not implemented");
						emptyWordLines.add(conlluLine);
					}*/
					// Word line
					else {
						wordLines.add(conlluLine);
						DefaultTreeToken word = new DefaultTreeToken(conlluLine.form, null, conlluLine.id.contains("."));
						word.setPos(conlluLine.xpos.equals("_")?null:conlluLine.xpos);
						word.setPos2(conlluLine.upos.equals("_")?null:conlluLine.upos);
						word.setLemma(conlluLine.lemma.equals("_")?null:conlluLine.lemma);
						processFeats(word, conlluLine.feats);
						processMisc(word, conlluLine.misc);
						words.add(word);
						idToWord.put(conlluLine.id, word);
						
						word.setPrevSurfaceCore(prevWord);
						if(prevWord != null) prevWord.setNextSurfaceCore(word);
						prevWord = word;
					}
				}
			}
			
			if(!mIgnoreRanges) {
				// Add clitic links
				int numRangeLines = rangeLines.size();
				for(int lnum = 0; lnum < numRangeLines; lnum++) {
					CoNLLULine rangeLine = rangeLines.get(lnum);
					String[] parts = rangeLine.id.split("-");
					int startToken = Integer.parseInt(parts[0]);
					int endToken = Integer.parseInt(parts[1]);
				
					MutableTreeToken prev = null;
					for(int i = startToken; i <= endToken; i++) {
						MutableTreeToken word = idToWord.get(""+i);
						word.setPrevClitic(prev);
						if(prev != null) prev.setNextClitic(word);
						prev = word;
					}
				}
			}
			
			// Add dependency relations
			int numWordLines = wordLines.size();
			for(int lnum = 0; lnum < numWordLines; lnum++) {
				CoNLLULine conlluLine = wordLines.get(lnum);
				int headNum = Integer.parseInt(conlluLine.head);
				processDeps(idToWord.get(conlluLine.id), conlluLine.deps, idToWord);
				
				if(headNum > 0) {
					MutableTreeToken child = idToWord.get(conlluLine.id);
					MutableTreeToken head = idToWord.get(""+headNum);
					Arc<TreeToken> newArc = new Arc<TreeToken>(child, head, conlluLine.dependency);
					arcs.add(newArc);
					child.setHeadArc(newArc);
					int wordId = Integer.parseInt(conlluLine.id);
					if(wordId < headNum) head.addPreChildArc(newArc);
					else head.addPostChildArc(newArc);
				}
			}
			
			int numTokens = words.size();
			if(numTokens > 0) {
				parse = new DefaultParse(words);
				for(String comment : comments) {
					parse.addComment(comment);
				}
			}
		} 
		catch (IOException e) {
			throw new InstanceReadException(e);
		}
		return parse;
	}
	
	
	
	private void processDeps(DefaultTreeToken word, String deps, Map<String, DefaultTreeToken> idToCore) {
		if(!deps.equals("_")) {
			for(String pair : deps.split("\\|")) {
				int firstColon = pair.indexOf(":");
				String id = pair.substring(0, firstColon);
				String label = pair.substring(firstColon+1);
				word.addEnhancedDep(new Arc<TreeToken>(word, idToCore.get(id), label));
			}
		}
	}
	
	private void processFeats(DefaultTreeToken word, String feats) {
		if(!feats.equals("_")) {
			for(String pair : feats.split("\\|")) {
				String[] split = pair.split("=");
				word.setMorphologyFeature(split[0], split[1]);
			}
		}
	}
	
	private void processMisc(TreeToken word, String misc) {
		if(!misc.equals("_")) {
			//  [^\\]\|
			for(String pair : misc.split("[^\\\\]\\|")) {
				String[] split = pair.replace("\\|", "|").split("=");
				word.setProperty(split[0], split.length>1?split[1]:null);
			}
		}
	}

	@Override
	public void setSource(InputStream istream) {
		try {
			mReader = new BufferedReader(new InputStreamReader(istream, "UTF-8"));
		} 
		catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
	
	private String canon(String s) {
		String retValue;
		if(mCanonicalize) {
			retValue = mCanon.get(s);
			if(retValue == null) mCanon.put(s, retValue = s);
		}
		else {
			retValue = s;
		}
		return retValue;
	}
	
}
