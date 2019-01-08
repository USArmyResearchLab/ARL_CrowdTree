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
import java.util.Set;

// TODO: Get rid of this thing somehow
public interface MorphoAnchor extends Serializable {
	
	Set<String> getPropertyKeys();
	Object getProperty(String key);
	void setProperty(String key, Object value);
	
	int getFullLength();
	
	TreeToken getCore();
	
	String getLexRoot();
	String getText();
	String getPos();
	
	/**
	 * A second Part-of-Speech field. Introduced for holding Universal POS tags
	 */
	String getPos2();
	String getGloss();
	String getLemma();
	
}
