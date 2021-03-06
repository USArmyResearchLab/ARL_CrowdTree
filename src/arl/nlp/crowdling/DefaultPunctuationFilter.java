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

package arl.nlp.crowdling;

import static arl.nlp.lang.UnicodeConstants.ELLIPSIS;
import static arl.nlp.lang.UnicodeConstants.EM_DASH;
import static arl.nlp.lang.UnicodeConstants.EN_DASH;

public class DefaultPunctuationFilter implements PunctuationFilter {
	
	@Override
	public boolean isPunctuation(String text, String pos) {
		return text.matches("[,\\.\\!\\?:;\\-`'\\(\\)\\\"\\]\\[\\{\\}"+ELLIPSIS+EM_DASH+EN_DASH+"]+")
				// Don't treat ' in possessive constructions as punctuation
				&& !pos.equals("POS");
	}

}
