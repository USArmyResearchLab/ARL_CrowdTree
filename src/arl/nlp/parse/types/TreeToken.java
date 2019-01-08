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

import java.util.List;

import arl.common.types.KeyValuePair;
import arl.graph.types.Arc;

public interface TreeToken extends MorphoAnchor {
	/** Returns the previous clitic, if it exists; null otherwise */
	TreeToken getPrevClitic();
	/** Returns the next clitic, if it exists; null otherwise */
	TreeToken getNextClitic();
	/** 
	 * Returns the next core, if it exists; null otherwise 
	 * (NOTE: if there is a previous clitic, it will be the same as <code>getNextClitic</code>) 
	 */
	TreeToken getNextSurfaceTreeToken();
	/** 
	 * Returns the previous core, if it exists; null otherwise 
	 * (NOTE: if there is a previous clitic, it will be the same as <code>getPrevClitic</code>) 
	 */
	TreeToken getPrevSurfaceTreeToken();
	
	int getCoreLength();
	String getCoreText();
	
	List<? extends Affix> getPrefixes();
	List<? extends Affix> getSuffixes();
	List<? extends Affix> getAffixes();
	boolean hasPrefix();
	boolean hasSuffix();
	
	
	
	/** Returns a <code>List<code> of <code>Arc</code>s to child <code>Core</code>s preceding this one. */
	List<Arc<TreeToken>> getPreChildArcs();
	/** Returns a <code>List<code> of <code>Arc</code>s to child <code>Core</code>s following this one. */
	List<Arc<TreeToken>> getPostChildArcs();
	/** Returns a <code>List<code> of <code>Arc</code>s to all child <code>Core</code>s of this one */
	List<Arc<TreeToken>> getChildArcs();
	
	/** Returns an <code>Arc</code> to the governing <code>Core</code> (if it exists). */
	Arc<TreeToken> getHeadArc();
	
	/** Support for CoNLL-U empty nodes */
	boolean isEmptyNode();
	/** Returns enhanced dependencies*/
	List<Arc<TreeToken>> getEnhancedDeps();
	/** Returns morphology features */
	List<KeyValuePair<String, String>> getMorphologyFeatures();
}
