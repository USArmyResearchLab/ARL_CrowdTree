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

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DefaultAffix implements MutableAffix {

	private static final long serialVersionUID = 1L;

	private Map<String, Object> mProperties = new HashMap<>();
	
	private String mRoot, mLemma, mPos, mPos2, mGloss;
	private String mText;
	private DefaultTreeToken mCore;
	private boolean mIsPrefix;
	
	public DefaultAffix(DefaultTreeToken core, boolean isPrefix) {
		mCore = core;
		mIsPrefix = isPrefix;
	}
	
	public DefaultAffix(DefaultTreeToken c, String affixText, boolean isPrefix) {
		mCore = c;
		mText = affixText;
		mIsPrefix = isPrefix;
	}

	@Override
	public int getFullLength() {
		return mText.length();
	}

	@Override
	public DefaultTreeToken getCore() {
		return mCore;
	}

	@Override
	public String getLexRoot() {
		return mRoot;
	}

	@Override
	public String getText() {
		return mText;
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
	public boolean isPrefix() {
		return mIsPrefix;
	}

	@Override
	public boolean isSuffix() {
		return !mIsPrefix;
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
	public void setCore(TreeToken core) {
		mCore = (DefaultTreeToken)core;
	}
	
	@Override
	public String toString() {
		return getText();
	}

	public void revise(String newText) {
		mText = newText;
	}
	
	@Override
	public Object getProperty(String key) {
		return mProperties==null?null:mProperties.get(key);
	}

	@Override
	public void setProperty(String key, Object value) {
		if(mProperties == null) {
			mProperties = new HashMap<>();
		}
		mProperties.put(key, value);
	}
	
	@Override
	public Set<String> getPropertyKeys() {
		return mProperties==null?new HashSet<>():Collections.unmodifiableSet(mProperties.keySet());
	}
}
