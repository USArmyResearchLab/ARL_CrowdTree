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

package arl.nlp.lang.arabic;

import java.util.HashMap;
import java.util.Map;

import arl.nlp.lang.UnicodeConstants;

import static arl.nlp.lang.arabic.ArabicCharacters.*;

public class Buckwalter {

	private static char[][] BOTH_WAYS_MAPPINGS =
			new char[][]{
		     // u0621-u063A ARABIC CORE LETTER (FIRST SECTION)
		     {'\'', HAMZA}, // hamza
		     {'|',  ALEF_W_MADDA_ABOVE}, // alef with madda above (ALEF VARIANT)
		     {'>',  ALEF_W_HAMZA_ABOVE}, // alef with hamza above (ALEF VARIANT)
		     {'&', WAW_W_HAMZA_ABOVE}, // waw with hamza above (HAMZA VARIANT)
		     {'<', ALEF_W_HAMZA_BELOW}, // alef with hamza below (ALEF VARIANT)
		     {'}', YEH_W_HAMZA_ABOVE}, // yeh with hamza above (HAMZA VARIANT)
		     {'A', ALEF}, // alef (ALEF VARIANT)
		     {'b', BEH}, // beh
		     {'p', TEH_MARBUTA}, // teh marbuta
		     {'t', TEH}, // teh
		     {'v', THEH}, // theh
		     {'j', JEEM}, // jeem
		     {'H', HAH}, // hah
		     {'x', KHAH}, // khah
		     {'d', DAL}, // dal
		     {'*', THAL}, // thal
		     {'r', REH}, // reh
		     {'z', ZAIN}, // zain
		     {'s', SEEN}, // seen
		     {'$', SHEEN}, // sheen
		     {'S', SAD}, // sad
		     {'D', DAD}, // dad
		     {'T', TAH}, // tah
		     {'Z', ZAH}, // zah
		     {'E', AIN}, // ain
		     {'g', GHAIN}, // ghain
		     // tatweel / kashida
		     {'_', TATWEEL}, // Tatweel/kashida
		     // u0641-u064A (ARABIC CORE LETTER; SECOND SECTION)
		     {'f', FEH}, // feh
		     {'q', QAF}, // qaf
		     {'k', KAF}, // kaf
		     {'l', LAM}, // lam
		     {'m', MEEM}, // meem
		     {'n', NOON}, // noon
		     {'h', HEH}, // heh
		     {'w', WAW}, // waw
		     {'Y', MAKSURA}, // maksura
		     {'y', YEH}, // yeh
		     // u064b-u0652 ARABIC CORE DIACRITICS
		     {'F', FATHATAN}, // fathatan
		     {'N', DAMMATAN}, // dammatan
		     {'K', KASRATAN}, // kasratan
		     
		     {'a', FATHA}, // fatha
		     {'u', DAMMA}, // damma
		     {'i', KASRA}, // kasra
		     
		     {'~', SHADDA}, // shadda
		     {'o', SUKUN},  // sukun
		     // ARABIC EXTENDED (a lot more possibilities here...)
		     {'`', ALEF_SUPERSCRIPT}, // superscript alef
		     {'{', ALEF_WASLA}, // alef wasla (ALEF VARIANT)
		     
		     
		     {'P', PEH}, // peh (non-MSA)
		     {'J', TCHEH}, // tcheh (non-MSA)
		     {'R', JEH}, // jeh (non-MSA)
		     {'V', VEH}, // veh (non-MSA)
		     {'G', GAF},  // gaf (non-MSA)
		     
		     // Deviation from standard Buckwalter transliteration; consider deleting this line
		     {',', AR_COMMA},
		     // More additions (not standard Buckwalter)
		     {'e', ZWARAKAY}, // schwa diacritic for Pashto
		     {'^', VOWEL_SIGN_INVERTED_SMALL_V_ABOVE}, // 
		     {'ķ', KEHEH},
		     {'ÿ', FARSI_YEH},
		     {'‡', UnicodeConstants.ZERO_WIDTH_NON_JOINER},
		     {'ɷ', HAMZA_ABOVE}
		    };
	
		private static char[][] TO_BUCK_ONLY = new char[][]{
			{',', AR_COMMA}, // Arabic comma
			{';', AR_SEMICOLON}, // Arabic semicolon
			{'?', AR_QMARK}, // Arabic question mark
			// Indo-Arabic digits
			{'0', INDO_AR_ZERO},
			{'1', INDO_AR_ONE},
			{'2', INDO_AR_TWO},
			{'3', INDO_AR_THREE},
			{'4', INDO_AR_FOUR},
			{'5', INDO_AR_FIVE},
			{'6', INDO_AR_SIX},
			{'7', INDO_AR_SEVEN},
			{'8', INDO_AR_EIGHT},
			{'9', INDO_AR_NINE},
			
			// Indo-Persian-Arabic digits; may not be core Buckwalter transliteration
			{'0', INDO_FA_AR_ZERO},
			{'1', INDO_FA_AR_ONE},
			{'2', INDO_FA_AR_TWO},
			{'3', INDO_FA_AR_THREE},
			{'4', INDO_FA_AR_FOUR},
			{'5', INDO_FA_AR_FIVE},
			{'6', INDO_FA_AR_SIX},
			{'7', INDO_FA_AR_SEVEN},
			{'8', INDO_FA_AR_EIGHT},
			{'9', INDO_FA_AR_NINE},
			
			// Non-core Buckwalter transliteration
			{'%', AR_PERCENT}
		};
	
	private final static Map<Character, Character> ARABIC_TO_BUCK = new HashMap<>();
	private final static Map<Character, Character> BUCK_TO_ARABIC = new HashMap<>();
	static {
		for(char[] pair : BOTH_WAYS_MAPPINGS) {
			ARABIC_TO_BUCK.put(pair[1], pair[0]);
			BUCK_TO_ARABIC.put(pair[0], pair[1]);
		}
		for(char[] pair : TO_BUCK_ONLY) {
			ARABIC_TO_BUCK.put(pair[1], pair[0]);
		}
	}
		    
	public final static char buckToArabic(char input) {
		Character result = BUCK_TO_ARABIC.get(input);
		if(result == null) result = input;
		return result;
	}
	
	public final static String buckToArabic(String input) {
		return replaceChars(input, BUCK_TO_ARABIC);
	}
	
	public final static char arabicToBuck(char input) {
		Character result = ARABIC_TO_BUCK.get(input);
		if(result == null) result = input;
		return result;
	}
	
	public final static String arabicToBuck(String input) {
		return replaceChars(input, ARABIC_TO_BUCK);
	}
	
	private final static String replaceChars(String input, Map<Character, Character> charMap) {
		char[] chars = input.toCharArray();
		for(int i = 0; i < chars.length; i++) {
			Character newChar = charMap.get(chars[i]);
			if(newChar != null) chars[i] = newChar.charValue();
		}
		return new String(chars);
	}
	
}
