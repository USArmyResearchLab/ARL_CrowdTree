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

package arl.nlp.parse.types;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import arl.common.types.KeyValuePair;
import arl.common.types.ListOfLists;
import arl.graph.types.Arc;

public class DefaultTreeToken implements MutableTreeToken {

	private static final long serialVersionUID = 1L;

	// Hold the information
	private String mText;
	
	private String mPos, mPos2, mRoot, mLemma, mGloss;
	private DefaultTreeToken mSurfacePrev, mSurfaceNext, mPrevClitic, mNextClitic;
	private List<DefaultAffix> mPrefixes = new ArrayList<>(),
								mSuffixes = new ArrayList<>();
	private List<Arc<TreeToken>> mLeftChildArcs = new ArrayList<>(),
							mRightChildArcs = new ArrayList<>();
	private Arc<TreeToken> mHeadArc;
	
	private Map<String, Object> mProperties = new LinkedHashMap<>();
	private List<Arc<TreeToken>> mEnhancedDeps = new ArrayList<>(0);
	private List<KeyValuePair<String, String>> mMorphologyFeatures = new ArrayList<>(0);
	
	private boolean mIsEmptyNode = false;
	
	public DefaultTreeToken(String text) {
		assert(text.length() != 0);
		mText = text;
	}
	
	public DefaultTreeToken(String text, String pos) {
		this(text);
		mPos = pos;
	}
	
	public DefaultTreeToken(String text, String pos, boolean isEmpty) {
		this(text, pos);
		mIsEmptyNode = isEmpty;
	}

	@Override
	public DefaultTreeToken getPrevClitic() {
		return mPrevClitic;
	}

	@Override
	public DefaultTreeToken getNextClitic() {
		return mNextClitic;
	}

	@Override
	public DefaultTreeToken getNextSurfaceTreeToken() {
		return mSurfaceNext;
	}

	@Override
	public DefaultTreeToken getPrevSurfaceTreeToken() {
		return mSurfacePrev;
	}

	@Override
	public List<DefaultAffix> getPrefixes() {
		return mPrefixes;
	}

	@Override
	public List<DefaultAffix> getSuffixes() {
		return mSuffixes;
	}
	
	@Override
	public List<DefaultAffix> getAffixes() {
		List<DefaultAffix> affixes = new ArrayList<>();
		affixes.addAll(mPrefixes);
		affixes.addAll(mSuffixes);
		return affixes;
	}

	@Override
	public boolean hasPrefix() {
		return mPrefixes.size() > 0;
	}

	@Override
	public boolean hasSuffix() {
		return mSuffixes.size() > 0;
	}

	@Override
	public List<Arc<TreeToken>> getChildArcs() {
		ListOfLists<Arc<TreeToken>> childArcs = new ListOfLists<Arc<TreeToken>>();
		childArcs.addAll(mLeftChildArcs);
		childArcs.addAll(mRightChildArcs);
		return Collections.unmodifiableList(childArcs);
	}

	@Override
	public Arc<TreeToken> getHeadArc() {
		return mHeadArc;
	}

	@Override
	public int getCoreLength() {
		return mText.length();
	}

	@Override
	public int getFullLength() {
		return getText().length();
	}

	@Override
	public TreeToken getCore() {
		return this;
	}

	@Override
	public String getLexRoot() {
		return mRoot;
	}

	@Override
	public String getPos() {
		return mPos;
	}
	
	@Override
	public String getPos2() {
		return mPos2;
	}

	@Override
	public String getGloss() {
		return mGloss;
	}

	@Override
	public String getLemma() {
		return mLemma;
	}

	@Override
	public String getCoreText() {
		return mText;
	}

	@Override
	public String getText() {
		StringBuilder buf = new StringBuilder();
		for(Affix pref : mPrefixes) buf.append(pref.getText());
		buf.append(mText);
		for(Affix suff : mSuffixes) buf.append(suff.getText());
		return buf.toString();
	}

	@Override
	public void setPos(String pos) {
		mPos = pos;
	}
	
	@Override
	public void setPos2(String pos2) {
		mPos2 = pos2;
	}

	@Override
	public void setLemma(String lemma) {
		mLemma = lemma;
	}

	@Override
	public void setGloss(String gloss) {
		mGloss = gloss;
	}

	@Override
	public void setRoot(String root) {
		mRoot = root;
	}

	@Override
	public void addPrefix(Affix prefix) {
		mPrefixes.add((DefaultAffix)prefix);
	}
	
	public void revise(String newText) {
		assert(newText.length() > 0);
		mText = newText;
	}
	
	public void removePrefix(DefaultAffix prefix) {
		boolean removed = mPrefixes.remove(prefix);
		if(removed == false) throw new RuntimeException("Failed to remove prefix.");
	}
	
	public void removeSuffix(DefaultAffix suffix) {
		boolean removed = mSuffixes.remove(suffix);
		if(removed == false) throw new RuntimeException("Failed to remove suffix.");
	}
	
	public void addPrefixToFront(DefaultAffix prefix) {
		mPrefixes.add(0, prefix);
	}

	@Override
	public void addSuffix(Affix suffix) {
		mSuffixes.add((DefaultAffix)suffix);
	}
	
	public void addSuffix(int index, Affix suffix) {
		mSuffixes.add(index, (DefaultAffix)suffix);
	}

	@Override
	public void setHeadArc(Arc<TreeToken> headArc) {
		mHeadArc = headArc;
	}

	@Override
	public void setNextClitic(TreeToken next) {
		mNextClitic = (DefaultTreeToken)next;
	}

	@Override
	public void setPrevClitic(TreeToken prev) {
		mPrevClitic = (DefaultTreeToken)prev;
	}

	@Override
	public void setNextSurfaceCore(TreeToken next) {
		mSurfaceNext = (DefaultTreeToken)next;
	}

	@Override
	public void setPrevSurfaceCore(TreeToken prev) {
		mSurfacePrev = (DefaultTreeToken)prev;
	}

	public void shrinkFromEnd(int amount) {
		mText = mText.substring(0, mText.length()-amount);
	}
	
	public void shrinkFromStart(int amount) {
		mText = mText.substring(amount);
	}
	
	@Override
	public String toString() {
		return getText();
	}

	@Override
	public List<Arc<TreeToken>> getPreChildArcs() {
		return Collections.unmodifiableList(mLeftChildArcs);
	}

	@Override
	public List<Arc<TreeToken>> getPostChildArcs() {
		return Collections.unmodifiableList(mRightChildArcs);
	}

	public void addChildArc(Arc<TreeToken> child, boolean preChild) {
		if(child.getHead() != this) throw new RuntimeException("Inserted arc didn't have its parent property set correctly; must refer to <code>this</code> object");
		(preChild?mLeftChildArcs:mRightChildArcs).add(child);
	}
	
	@Override
	public void addPreChildArc(Arc<TreeToken> child) {
		if(child.getHead() != this) throw new RuntimeException("Inserted arc didn't have its parent property set correctly; must refer to <code>this</code> object");
		mLeftChildArcs.add(child);
	}

	@Override
	public void addPostChildArc(Arc<TreeToken> child) {
		if(child.getHead() != this) throw new RuntimeException("Inserted arc didn't have its parent property set correctly; must refer to <code>this</code> object");
		mRightChildArcs.add(child);
	}

	@Override
	public void removeChildArc(Arc<TreeToken> mta) {
		mLeftChildArcs.remove(mta);
		mRightChildArcs.remove(mta);
	}

	@Override
	public void setCoreText(String text) {
		mText = text;
	}

	
	@Override
	public Object getProperty(String key) {
		return mProperties.get(key);
	}

	@Override
	public void setProperty(String key, Object value) {
		mProperties.put(key, value);
	}

	@Override
	public Set<String> getPropertyKeys() {
		return Collections.unmodifiableSet(mProperties.keySet());
	}

	@Override
	public List<KeyValuePair<String, String>> getMorphologyFeatures() {
		return Collections.unmodifiableList(mMorphologyFeatures);
	}

	@Override
	public List<Arc<TreeToken>> getEnhancedDeps() {
		return Collections.unmodifiableList(mEnhancedDeps);
	}
	
	public void setMorphologyFeature(String key, String value) {
		mMorphologyFeatures.add(new KeyValuePair<String, String>(key, value));
	}
	
	public boolean removeMorphologyFeature(String key) {
		return mMorphologyFeatures.remove(key);
	}
	
	public void addEnhancedDep(Arc<TreeToken> enhancedDep) {
		if(enhancedDep.getChild() != this) throw new RuntimeException("Enhanced dep must have <code>this</code> as child");
		mEnhancedDeps.add(enhancedDep);
	}
	
	public boolean removeEnhancedDep(Arc<TreeToken> enhancedDep) {
		return mEnhancedDeps.remove(enhancedDep);
	}

	@Override
	public boolean isEmptyNode() {
		return mIsEmptyNode;
	}
}
