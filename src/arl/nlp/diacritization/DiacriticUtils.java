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

package arl.nlp.diacritization;

import static arl.nlp.lang.arabic.ArabicCharacters.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import arl.common.types.CharTuple3;
import arl.nlp.lang.arabic.Buckwalter;

public class DiacriticUtils {
	
	public final static Set<Character> VOWEL_DIACRITIC_SET = new HashSet<>(Arrays.asList(new Character[]{FATHA, KASRA, DAMMA, FATHATAN, KASRATAN, DAMMATAN, SUKUN}));
	public final static Set<Character> FINAL_ONLY_DIACRITIC_SET = new HashSet<>(Arrays.asList(new Character[]{FATHATAN, KASRATAN, DAMMATAN}));
	
	public static String filterDiacritics(String input) {
		StringBuilder buf = new StringBuilder();
		int len = input.length();
		for(int i = 0; i < len; i++) {
			Character c = input.charAt(i);
			// What about superscript alef?
			if(c.charValue() != SHADDA && !VOWEL_DIACRITIC_SET.contains(c) && c.charValue() != ALEF_SUPERSCRIPT) {
				buf.append(c.charValue());
			}
		}
		return buf.toString();
	}
	
	public static String tuplesToString(List<CharTuple3> tuples) {
		StringBuilder solutionBuf = new StringBuilder();
		for(CharTuple3 ct : tuples) {
			solutionBuf.append(ct.x);
			if(ct.y != ' ') solutionBuf.append(ct.y);
			if(ct.z != ' ') solutionBuf.append(ct.z);
		}
		return solutionBuf.toString();
	}
	
	public static List<CharTuple3> createDiacriticList(String text) {
		int textLen = text.length();
		List<CharTuple3> diacritics = new ArrayList<>(textLen);
		CharTuple3 lastTuple = null;
		for(int i = 0; i < textLen; i++) {
			Character c = text.charAt(i);
			if(lastTuple == null && (c == SHADDA || DiacriticUtils.VOWEL_DIACRITIC_SET.contains(c))) {
				System.err.println("TEXT: " + Buckwalter.arabicToBuck(text) + " i:" + i);
			}
			if(c != SHADDA && !DiacriticUtils.VOWEL_DIACRITIC_SET.contains(c) && c != ALEF_SUPERSCRIPT) {
				CharTuple3 tuple = new CharTuple3(c, ' ', ' ');
				lastTuple = tuple;
				diacritics.add(tuple);
			}
			else if(c == SHADDA) {
				if(lastTuple != null) lastTuple.y = c;
			}
			else if(c != ALEF_SUPERSCRIPT){
				if(lastTuple != null) lastTuple.z = c;
			}
		}
		return diacritics;
	}
}
