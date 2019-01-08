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

package arl.nlp.parse.transition;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import arl.common.meta.ArgMap;
import arl.common.meta.Initializable;
import arl.common.meta.InitializationException;
import arl.common.types.KeyValuePair;
import arl.graph.types.Arc;
import arl.ml.transition.state.StateFactory;
import arl.nlp.diacritization.DiacriticUtils;
import arl.nlp.parse.types.Affix;
import arl.nlp.parse.types.DefaultAffix;
import arl.nlp.parse.types.DefaultParse;
import arl.nlp.parse.types.DefaultTreeToken;
import arl.nlp.parse.types.MorphoAnchor;
import arl.nlp.parse.types.MutableAffix;
import arl.nlp.parse.types.MutableTreeToken;
import arl.nlp.parse.types.Parse;
import arl.nlp.parse.types.TreeToken;

public class DefaultParseFactory implements StateFactory<Parse, Parse> {

	private static final long serialVersionUID = 1L;
	
	public static String PARAM_KEEP_POS = "KeepPos",
						 PARAM_KEEP_GLOSS = "KeepGloss",
						 PARAM_KEEP_PARSE = "KeepParse",
						 PARAM_KEEP_AFFIXES = "KeepAffixes",
						 PARAM_KEEP_CLITICS = "KeepClitics",
						 PARAM_KEEP_LEMMA = "KeepLemma",
						 PARAM_KEEP_FEATS = "KeepFeats",
						 PARAM_KEEP_LEXROOT = "KeepLexRoot",
						 PARAM_KEEP_COMMENTS = "KeepComments",
						 PARAM_KEEP_PROPERTIES = "KeepProperties",
						 PARAM_KEEP_RESUMPTIVE = "KeepResumptive",
						 PARAM_KEEP_DIACRITICS = "KeepDiacritics";
	private boolean mKeepPos = true, 
					mKeepGloss = true, 
					mKeepParse = true, 
					mKeepAffixes = true,
					mKeepLemma = true,
					mKeepFeats = true,
					mKeepComments = true,
					mKeepProperties = true,
					mKeepLexRoot = true,
					mKeepClitics = true,
					mKeepResumptive = true,
					mKeepDiacritics = true;
	
	@Override
	public void initialize(ArgMap args) throws InitializationException {
		Initializable.checkArgNames(args, new HashSet<>(Arrays.asList(PARAM_KEEP_POS, PARAM_KEEP_GLOSS, PARAM_KEEP_PARSE,
				PARAM_KEEP_AFFIXES, PARAM_KEEP_CLITICS, PARAM_KEEP_LEMMA, PARAM_KEEP_LEXROOT, PARAM_KEEP_RESUMPTIVE, PARAM_KEEP_DIACRITICS, PARAM_KEEP_FEATS, PARAM_KEEP_COMMENTS, PARAM_KEEP_PROPERTIES)));
		mKeepPos = args.getBooleanValue(PARAM_KEEP_POS, mKeepPos);
		mKeepGloss = args.getBooleanValue(PARAM_KEEP_GLOSS, mKeepGloss);
		mKeepParse = args.getBooleanValue(PARAM_KEEP_PARSE, mKeepParse);
		mKeepAffixes = args.getBooleanValue(PARAM_KEEP_AFFIXES, mKeepAffixes);
		mKeepClitics = args.getBooleanValue(PARAM_KEEP_CLITICS, mKeepClitics);
		mKeepLemma = args.getBooleanValue(PARAM_KEEP_LEMMA, mKeepLemma);
		mKeepLexRoot = args.getBooleanValue(PARAM_KEEP_LEXROOT, mKeepLexRoot);
		mKeepResumptive = args.getBooleanValue(PARAM_KEEP_RESUMPTIVE, mKeepResumptive);
		if(!mKeepClitics) {
			if(mKeepPos) System.err.println("WARNING: KeepClitics=false but KeepPos=true");
			if(mKeepAffixes) System.err.println("WARNING: KeepClitics=false but KeepAffixes=true");
			if(mKeepGloss) System.err.println("WARNING: KeepClitics=false but KeepGloss=true");
			if(mKeepLemma) System.err.println("WARNING: KeepClitics=false but KeepLemma=true");
			if(mKeepLexRoot) System.err.println("WARNING: KeepClitics=false but KeepRoot=true");
			if(mKeepResumptive) System.err.println("WARNING: KeepClitics=false but KeepResumptives=true");
		}
		mKeepDiacritics = args.getBooleanValue(PARAM_KEEP_DIACRITICS, mKeepDiacritics);
		mKeepComments = args.getBooleanValue(PARAM_KEEP_COMMENTS, mKeepComments);
		mKeepProperties = args.getBooleanValue(PARAM_KEEP_COMMENTS, mKeepProperties);
		mKeepFeats = args.getBooleanValue(PARAM_KEEP_COMMENTS, mKeepFeats);
	}
	
	@Override
	public Parse createState(Parse instance) {
		return copyState(instance, true, true, true, true, true, true, true, true, true, true, true, true);
	}

	@Override
	public DefaultParse createInitialState(Parse finalState) {
		return copyState(finalState, mKeepClitics, mKeepDiacritics, mKeepPos, mKeepAffixes, mKeepLemma, mKeepLexRoot, mKeepResumptive, mKeepParse, mKeepGloss, mKeepComments, mKeepProperties, mKeepFeats);
	}
	
	public static DefaultParse copyState(Parse original, 
												boolean keepClitics,
												boolean keepDiacritics,
												boolean keepPos,
												boolean keepAffixes,
												boolean keepLemma,
												boolean keepLexRoot,
												boolean keepResumptive,
												boolean keepParse,
												boolean keepGloss,
												boolean keepComments,
												boolean keepProperties,
												boolean keepFeats) {
		List<MorphoAnchor> newAnchors = new LinkedList<>();
		List<Affix> newAffixAnchors = new LinkedList<>();
		List<? extends TreeToken> goldCores = new LinkedList<>(original.getAllTreeTokens());
		//Collections.sort(goldAnchors);

		
		MutableTreeToken prevNewCoreAnchor = null;
		TreeToken prevGoldCoreAnchor = null;
		Map<TreeToken, MutableTreeToken> oldToNew = new LinkedHashMap<>();
		Set<MorphoAnchor> processed = new HashSet<>();
		int coreIndex = 0;
		Map<TreeToken, Integer> coreToIndex = new HashMap<TreeToken, Integer>();
		for(ListIterator<? extends TreeToken> aIt = goldCores.listIterator(); aIt.hasNext();) {
			TreeToken goldAnchor = aIt.next();
			if(!processed.contains(goldAnchor)) {
				processed.add(goldAnchor);
				StringBuilder tbuf = new StringBuilder(goldAnchor.getText());
				
				if(!keepClitics) {
					TreeToken anchor = goldAnchor;
					while(anchor.getNextClitic() != null) {
						anchor = (DefaultTreeToken)anchor.getNextClitic();
						//System.err.println("Adding: " + anchor.getText());
						tbuf.append(anchor.getText());
						processed.add(anchor);
					}
				}
				
				DefaultTreeToken newAnchor = new DefaultTreeToken(keepDiacritics?tbuf.toString():DiacriticUtils.filterDiacritics(tbuf.toString()));
				coreIndex++;
				coreToIndex.put(newAnchor, coreIndex);
				
				oldToNew.put(goldAnchor, newAnchor);
				newAnchors.add(newAnchor);
				if(prevNewCoreAnchor != null) {
					prevNewCoreAnchor.setNextSurfaceCore(newAnchor);
					newAnchor.setPrevSurfaceCore(prevNewCoreAnchor);
					if(prevGoldCoreAnchor.getNextClitic() == goldAnchor) {
						prevNewCoreAnchor.setNextClitic(newAnchor);
						newAnchor.setPrevClitic(prevNewCoreAnchor);
					}
				}
				if(keepPos) newAnchor.setPos(goldAnchor.getPos());
				if(keepPos) newAnchor.setPos2(goldAnchor.getPos2());
				if(keepGloss) newAnchor.setGloss(goldAnchor.getGloss());
				if(keepLemma) newAnchor.setLemma(goldAnchor.getLemma());
				if(keepLexRoot) newAnchor.setRoot(goldAnchor.getLexRoot());
				if(keepFeats) {
					for(KeyValuePair<String, String> origMorphoFeat : goldAnchor.getMorphologyFeatures()) {
						newAnchor.setMorphologyFeature(origMorphoFeat.getKey(), origMorphoFeat.getValue());
					}
				}
				
				if(keepProperties) { 
					for(String propKey : goldAnchor.getPropertyKeys()) {
						newAnchor.setProperty(propKey, goldAnchor.getProperty(propKey));
					}
				}
				if(keepResumptive && goldAnchor.getProperty(Parse.RESUMPTIVE_FLAG)!=null) newAnchor.setProperty(Parse.RESUMPTIVE_FLAG, goldAnchor.getProperty(Parse.RESUMPTIVE_FLAG));
				
				
				if(keepAffixes) {
					for(Affix prefix : goldAnchor.getPrefixes()) {
						MutableAffix newPrefix = new DefaultAffix(newAnchor, keepDiacritics?prefix.getText():DiacriticUtils.filterDiacritics(prefix.getText()), true);
						newAnchors.add(newPrefix);
						if(keepPos) newPrefix.setPos(prefix.getPos());
						if(keepGloss) newPrefix.setGloss(prefix.getGloss());
						newAnchor.addPrefix(newPrefix);
						newAnchor.shrinkFromStart(newPrefix.getText().length());
						newAffixAnchors.add(newPrefix);
					}
					for(Affix suffix : goldAnchor.getSuffixes()) {
						MutableAffix newSuffix = new DefaultAffix(newAnchor, keepDiacritics?suffix.getText():DiacriticUtils.filterDiacritics(suffix.getText()), false);
						newAnchors.add(newSuffix);
						if(keepPos) newSuffix.setPos(suffix.getPos());
						if(keepGloss) newSuffix.setGloss(suffix.getGloss());
						newAnchor.addSuffix(newSuffix);
						newAnchor.shrinkFromEnd(newSuffix.getText().length());
						newAffixAnchors.add(newSuffix);
					}
				}
				prevNewCoreAnchor = newAnchor;
				prevGoldCoreAnchor = goldAnchor;
			}
		}
		
		
		
		if(keepParse) {
			for(TreeToken old : oldToNew.keySet()) {
				Arc<TreeToken> headArc = old.getHeadArc();
				if(headArc != null) {
					MutableTreeToken newChild = oldToNew.get(old);
					MutableTreeToken newParent = oldToNew.get(headArc.getHead());
					int childIndex = coreToIndex.get(newChild);
					int parentIndex = coreToIndex.get(newParent);
					Arc<TreeToken> newArc = new Arc<TreeToken>(newChild, newParent, headArc.getLabel());
					newChild.setHeadArc(newArc);
					if(parentIndex > childIndex) {
						newParent.addPreChildArc(newArc);
					}
					else {
						newParent.addPostChildArc(newArc);
					}
				}
			}
		}
		DefaultParse newState = new DefaultParse(newAnchors);
		if(keepComments) {
			for(String comment : original.getComments()) {
				newState.addComment(comment);
			}
		}
		return newState;
	}
	
	
	
}
