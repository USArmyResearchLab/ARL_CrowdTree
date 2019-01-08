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

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import arl.nlp.parse.ParseChangeListener;
import arl.nlp.parse.types.TreeToken;

public interface Parse extends Serializable {

	final static String ANNOTATOR_ID_PROPERTY_KEY = "miac.annotator_id",
			            SENTENCE_ID_PROPERTY_KEY = "miac.sentence_id",
			            RESUMPTIVE_FLAG = "miac.resumptive";
	
	void addComment(String comment);
	void deleteComment(String comment);
	Collection<String> getComments();
	
	Set<String> getPropertyKeys();
	Object getProperty(String key);
	void setProperty(String key, Object value);
	
	List<? extends TreeToken> getAllTreeTokens();

	void addParseChangeListener(ParseChangeListener listener);
	void removeParseChangeListener(ParseChangeListener listener);
	// This deserves eventual revisiting... most of these actions seem to 
	// be at a different level than what would warrant a method on this interface
	Affix factorOutSuffix(TreeToken core, int suffixLength);
	Affix factorOutPrefix(TreeToken core, int prefixLength);
	
	void insertText(TreeToken anchor, String text, int insertionPoint, boolean resolveAffixes);
	void detachEnclitic(TreeToken core, int encliticLength);
	void detachProclitic(TreeToken core, int procliticLength);
	void attach(TreeToken child, TreeToken head, String label, boolean isPreChild);
	void setLabel(MorphoAnchor anchor, String label);
	void setResumptiveness(MorphoAnchor anchor, int value);
	
}
