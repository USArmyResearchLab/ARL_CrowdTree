/*
 * 
 * This file is part of ARL CrowdTree and is subject to the following:
 * 
 * Copyright 2018 U.S. Government and Nhien Phan
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

package arl.nlp.punctuation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import arl.common.meta.ArgMap;
import arl.common.meta.Initializable;
import arl.common.meta.InitializationException;
import arl.graph.types.Arc;
import arl.nlp.parse.types.DefaultParse;
import arl.nlp.parse.types.DefaultTreeToken;
import arl.nlp.parse.types.TreeToken;

import static arl.nlp.lang.UnicodeConstants.*;
// TODO: Make this code more robust to less frequent punctuation, etc.; test with Arabic, other data
public class DefaultPunctuationAttachmentHeuristic implements PunctuationAttacher<DefaultParse, DefaultTreeToken>, Initializable {

	private final static String SENTENCE_FINAL_PUNCTUATION = "[\\.\\?\\!" + AR_QMARK + AR_FULL_STOP + "]+";
	public final static String PARAM_PUNCT_DEP = "PunctDepLabel";
	
	private String mPunctDepLabel = "punct";
	
	@Override
	public void initialize(ArgMap argMap) throws InitializationException {
		Initializable.checkArgNames(argMap, new HashSet<>(Arrays.asList(PARAM_PUNCT_DEP)));
		mPunctDepLabel = argMap.getStringValue(PARAM_PUNCT_DEP, mPunctDepLabel);
	}
	
	@Override
	public void reattachPunctuation(DefaultParse ms, DefaultTreeToken root) {
		reattachPeriods(ms, root, mPunctDepLabel, mPunctDepLabel);
		
		// reattach paired punctuation
		for(String[] pair: new String[][] {
			{"``", "''"},
			{"\"", "\""},
			{"`", "'"},
			{"{", "}"},
			{"(", ")"},
			{"[", "]"},
		}) {
			reattachOpeningQuotes(ms, pair[0], pair[1], root, mPunctDepLabel, mPunctDepLabel);
			reattachClosingQuotes(ms, pair[0], pair[1], root, mPunctDepLabel, mPunctDepLabel);
		}
		reattachCommas(ms, root, mPunctDepLabel, mPunctDepLabel);
	}
	
	public static void reattachCommas(DefaultParse ms, 
									  DefaultTreeToken defaultAttachmentSite, 
									  String dependency,
									  String punctDepPattern) {
		List<DefaultTreeToken> allCores = new ArrayList<>(ms.getAllTreeTokens());
		int numCores = allCores.size();
		for(int p = 0; p < numCores; p++) {
			DefaultTreeToken core = allCores.get(p);
			String text = core.getText();
			if(text.matches("[,;:]|\\.\\.\\.|-+|"+EM_DASH+"|"+EN_DASH+"|"+ELLIPSIS)) {
				if(defaultAttachmentSite != core) {
					int minDistance = Integer.MAX_VALUE;
					TreeToken attachPoint = null;
					for(int childIndex = 0; childIndex < numCores; childIndex++) {
						if(childIndex != p) {
							DefaultTreeToken child = allCores.get(childIndex);
							Arc<TreeToken> headArc = child.getHeadArc();
							if(headArc != null 
							   // Don't attach to punctuation
							   && !headArc.getLabel().matches(punctDepPattern) 
							   && !isPunctuationString(child.getText())) {
								/*// A
								int headIndex = allCores.indexOf(headArc.getHead());
								if(headIndex != p &&
									Math.signum(headIndex-p)!=Math.signum(o-p)&&
									Math.abs(headIndex-o) < minDistance) {
									minDistance = Math.abs(headIndex-o);
									attachPoint = headArc.getHead();//child
								}*/
								
								// A2
								int headIndex = allCores.indexOf(headArc.getHead());
								int attachIndex = headIndex;
								if(attachIndex != p &&
									// Must pass over the punctuation
									Math.signum(headIndex-p)!=Math.signum(childIndex-p)&&
									// Prefer the shortest overlying arc
									Math.abs(headIndex-childIndex) < minDistance) {
									minDistance = Math.abs(attachIndex-childIndex);
									attachPoint = headArc.getHead();//
								}
								
								/*
								// B
								int headIndex = allCores.indexOf(headArc.getHead());
								if(headIndex != p &&
									Math.signum(headIndex-p)!=Math.signum(o-p)&&
									Math.abs(headIndex-o) < minDistance) {
									minDistance = Math.abs(headIndex-o);
									attachPoint = child;//headArc.getHead();//
								}*/
								
								
								
								
								// C
								/*Core head = headArc.getHead();
								int headIndex = allCores.indexOf(head);
								if(headIndex != p &&
									Math.signum(headIndex-p)!=Math.signum(o-p)&&
									Math.abs(headIndex-o) < minDistance) {
									minDistance = Math.abs(headIndex-o);
									
									int headPriority = getPriority(head.getPos());
									int childPriority = getPriority(child.getPos());
									
									if(headPriority >= childPriority) {
										attachPoint = headArc.getHead();//child
									}
									else {
										attachPoint = child;
									}
								}*/
							}
						}
					}
					if(attachPoint == null) {
						attachPoint = defaultAttachmentSite;
					}
					moveTo(ms, core, (DefaultTreeToken)attachPoint, dependency);
				}
			}
		}
	}
	
	/*public static int getPriority(String pos) {
		int priority = 0;
		if(pos.matches("MD|VBD|VBZ|VBP")) {
			priority = -1;
		}
		return priority;
	}*/
	
	public static void reattachClosingQuotes(DefaultParse ms, 
												String open, 
												String close, 
												DefaultTreeToken defaultAttachmentSite, 
												String dependency,
												String prepDepPattern) {
		List<DefaultTreeToken> allCores = new ArrayList<>(ms.getAllTreeTokens());
		for(int i = allCores.size()-1; i >= 0 ; i--) {
			DefaultTreeToken core = allCores.get(i);
			if(core != defaultAttachmentSite) {
				//if(core.getText().equals(close) && !(core.getText().equals("'")&&core.getHeadArc() != null)) {
				if(core.getText().equals(close) && !core.getPos().equals("POS")) {
					DefaultTreeToken openQuote = null;
					//1 find first closing quote to the right
					List<TreeToken> intermediates = new ArrayList<>();
					int count = 1;
					for(int j = i-1; j >= 0; j--) {
						DefaultTreeToken cq = allCores.get(j);
						if(cq.getText().equals(close)) count++;
						if(cq.getText().equals(open) && --count==0) {
							openQuote = cq;
							break;
						}
						if(!isPunctuationString(cq.getText())) {
							intermediates.add(cq);
						}
					}
					TreeToken attachPoint = null;
					if(openQuote != null) {
						Collections.reverse(intermediates);
						for(TreeToken c : intermediates) {
							Arc<TreeToken> outside = c.getHeadArc();
							if(outside == null || 
									(!intermediates.contains(outside.getHead())&&!outside.getLabel().matches(prepDepPattern))) {
								attachPoint = c;
								break;
							}
						}
					}
					if(attachPoint == null) {
						attachPoint = defaultAttachmentSite;
					}
					moveTo(ms, core, (DefaultTreeToken)attachPoint, dependency);
				}
			}
		}
	}
	
	public static void reattachOpeningQuotes(DefaultParse ms, 
											String open, 
											String close, 
											DefaultTreeToken defaultAttachmentSite, 
											String dependency,
											String punctDepLabel) {
		List<DefaultTreeToken> allCores = new ArrayList<>(ms.getAllTreeTokens());
		for(int i = 0; i < allCores.size(); i++) {
			DefaultTreeToken core = allCores.get(i);
			if(core != defaultAttachmentSite) {
				if(core.getText().equals(open)) {
					DefaultTreeToken closingQuote = null;
					//1 find first closing quote to the right
					List<DefaultTreeToken> intermediates = new ArrayList<>();
					int count = 1;
					final int numCores = allCores.size();
					for(int j = i+1; j < numCores; j++) {
						DefaultTreeToken cq = allCores.get(j);
						if(cq.getText().equals(open)) count++;
						if(cq.getText().equals(close)&&--count==0) {
							closingQuote = cq;
							break;
						}
						if(!isPunctuationString(cq.getText())) {
							intermediates.add(cq);
						}
					}
				
					DefaultTreeToken attachPoint = null;
					for(DefaultTreeToken c : intermediates) {
						Arc<TreeToken> outside = c.getHeadArc();
						if(outside == null || 
							(!intermediates.contains(outside.getHead()) &&!outside.getLabel().matches(punctDepLabel))
							) {
							attachPoint = c;
							break;
						}
					}
					if(attachPoint == null) {
						attachPoint = defaultAttachmentSite;
					}
					moveTo(ms, core, attachPoint, dependency);
				}
			}
		}
	}
	
	public static void reattachPeriods(DefaultParse ms, 
										DefaultTreeToken defaultAttachmentSite, 
										String dependency,
										String punctDepPattern) {
		List<DefaultTreeToken> allCores = new ArrayList<>(ms.getAllTreeTokens());
		for(DefaultTreeToken core : allCores) {
			if(core != defaultAttachmentSite) {
				String text = core.getText();
				if(text.matches(SENTENCE_FINAL_PUNCTUATION)) {
					if(!followedByAlphaNumericCharacters(core)) {
						moveTo(ms, core, defaultAttachmentSite, dependency);
					}
					else {
						Set<TreeToken> allPrevs = new HashSet<>();
						TreeToken pre = core;
						while((pre = pre.getPrevSurfaceTreeToken()) != null && !pre.getText().equals(text)) {
							allPrevs.add(pre);
						}
						
						TreeToken attachPoint = null;
						if(text.equals(".") &&
						   core.getPrevSurfaceTreeToken() != null &&
						   core.getPrevSurfaceTreeToken().getPos().equals("NNP")) { // what's this NNP thing for? Abbreviations?
							attachPoint = core.getPrevSurfaceTreeToken();
						}
						
						pre = core;
						while((pre = pre.getPrevSurfaceTreeToken()) != null && attachPoint == null) {
							Arc<TreeToken> headArc = pre.getHeadArc();
							if(headArc != null && 
							  !allPrevs.contains(headArc.getHead()) && 
							  !isPunctuationString(headArc.getChild().getText()) &&
							  !headArc.getLabel().matches(punctDepPattern)) {
								attachPoint = pre;
								break;
							}
						}
						/*pre = core;
						while((pre = pre.getPrevSurfaceCore()) != null && attachPoint == null) { 
							if(pre.getText().matches(".*[a-zA-Z0-9]+.*")) {
								attachPoint = pre;
								break;
							}
						}*/
						
						if(attachPoint == null) {
							attachPoint = defaultAttachmentSite;
						}
						moveTo(ms, core, (DefaultTreeToken)attachPoint, dependency);
					}
				}
			}
		}
	}
	
	public static boolean followedByAlphaNumericCharacters(DefaultTreeToken core) {
		boolean followedByAlphanumeric = false;
		TreeToken c = core;
		while((c = c.getNextSurfaceTreeToken()) != null) {
			//if(c.getText().matches(".*[a-zA-Z0-9]+.*")) {
			// Should support more than a-zA-Z0-9
			if(c.getText().matches(".*[\\p{IsAlphabetic}\\p{IsIdeographic}\\p{IsDigit}].*")) {
				followedByAlphanumeric = true;
				break;
			}
		}
		return followedByAlphanumeric;
	}
	
	public static void moveTo(DefaultParse ms, 
							  DefaultTreeToken core, 
							  DefaultTreeToken newParent,
							  String dependency) {
		Arc<TreeToken> headArc = core.getHeadArc();
		if(headArc != null) {
			headArc.setLabel(dependency);
			((DefaultTreeToken)headArc.getHead()).removeChildArc(headArc);
			headArc.setHead(newParent);
			if(precedes(core, newParent)) newParent.addPreChildArc(headArc);
			else newParent.addPostChildArc(headArc);
		}
		else {
			headArc = new Arc<TreeToken>(core, newParent, dependency);
			core.setHeadArc(headArc);
			if(precedes(core, newParent)) newParent.addPreChildArc(headArc);
			else newParent.addPostChildArc(headArc);
		}
	}
	
	public static boolean precedes(TreeToken x, TreeToken y) {
		boolean precedes = false;
		while(x != null) {
			x = x.getNextSurfaceTreeToken();
			if(x == y) {
				precedes = true;
				break;
			}
		}
		return precedes;
	}
	
	private static boolean isPunctuationString(String s) {
		return s.matches("\\p{IsPunctuation}+") &&	!s.equals("%");
		//return s.matches("[,\\.\\!\\?:;\\-`'\\(\\)\\\"\\]\\[\\{\\}]+");
	}
	
}
