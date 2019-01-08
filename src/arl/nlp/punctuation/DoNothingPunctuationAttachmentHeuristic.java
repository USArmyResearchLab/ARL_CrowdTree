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

import java.util.HashSet;

import arl.common.meta.ArgMap;
import arl.common.meta.Initializable;
import arl.common.meta.InitializationException;
import arl.nlp.parse.types.DefaultParse;
import arl.nlp.parse.types.DefaultTreeToken;

public class DoNothingPunctuationAttachmentHeuristic implements PunctuationAttacher<DefaultParse, DefaultTreeToken>, Initializable {
	
	@Override
	public void initialize(ArgMap argMap) throws InitializationException {
		Initializable.checkArgNames(argMap, new HashSet<>());
	}
	
	@Override
	public void reattachPunctuation(DefaultParse ms, DefaultTreeToken root) {
		
	}
	
}
