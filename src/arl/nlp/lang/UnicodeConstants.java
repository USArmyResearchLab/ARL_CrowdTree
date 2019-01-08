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

package arl.nlp.lang;

import java.util.HashMap;
import java.util.Map;

import arl.nlp.lang.arabic.ArabicCharacters;

/**
 * Interface for naming various unicode characters
 */
public class UnicodeConstants {
	
	private UnicodeConstants() {
		
	}

	public final static char BLOCK_ARABIC_START = '\u0600',
							 BLOCK_ARABIC_END = '\u06FF',
							 BLOCK_ARABIC_SUPPLEMENT_START = '\u0750',
							 BLOCK_ARABIC_SUPPLEMENT_END = '\u077F',
							 BLOCK_ARABIC_EXTENDED_A_START = '\u08A0',
							 BLOCK_ARABIC_EXTENDED_A_END = '\u08FF',
							 BLOCK_ARABIC_PRESENTATION_FORMS_A_START = '\uFB50',
							 BLOCK_ARABIC_PRESENTATION_FORMS_A_END = '\uFDFF',
							 BLOCK_ARABIC_PRESENTATION_FORMS_B_START = '\uFE70',
							 BLOCK_ARABIC_PRESENTATION_FORMS_B_END = '\uFEFF';
	
	public final static char 
							AR_QMARK = ArabicCharacters.AR_QMARK,
							AR_FULL_STOP = ArabicCharacters.AR_FULL_STOP,
							AR_COMMA = ArabicCharacters.AR_COMMA,
							AR_SEMICOLON = ArabicCharacters.AR_SEMICOLON,
							
							AR_PERCENT = ArabicCharacters.AR_PERCENT,
							AR_DECIMEL_SEPARATOR = ArabicCharacters.AR_DECIMEL_SEPARATOR,
							AR_THOUSANDS_SEPARATOR = ArabicCharacters.AR_THOUSANDS_SEPARATOR,
							AR_FIVE_POINT_START = ArabicCharacters.AR_FIVE_POINT_START,
							
							AR_ORNATE_LEFT_PAREN = ArabicCharacters.AR_ORNATE_LEFT_PAREN,
							AR_ORNATE_RIGHT_PAREN = ArabicCharacters.AR_ORNATE_RIGHT_PAREN,
	
							FULL_WIDTH_DIGIT_ZERO = '\uFF10',
							FULL_WIDTH_DIGIT_ONE = '\uFF11',
							FULL_WIDTH_DIGIT_TWO = '\uFF12',
							FULL_WIDTH_DIGIT_THREE = '\uFF13',
							FULL_WIDTH_DIGIT_FOUR = '\uFF14',
							FULL_WIDTH_DIGIT_FIVE = '\uFF15',
							FULL_WIDTH_DIGIT_SIX = '\uFF16',
							FULL_WIDTH_DIGIT_SEVEN = '\uFF17',
							FULL_WIDTH_DIGIT_EIGHT = '\uFF18',
							FULL_WIDTH_DIGIT_NINE = '\uFF19',
							
							EXCLAMATION_MARK = '\u0021', // !
							FULL_STOP = '\u002E',// .
							QUESTION_MARK = '\u003F', // ? 
							INVERTED_QUESTION_MARK = '\u00BF', // 191 ¿
							DOUBLE_EXCLAMATION_POINT = '\u203C', // 203C ‼
							INTERROBANG = '\u203D', // 203D ‽
							DOUBLE_QUESTION_MARK = '\u2047', // ⁇
							QUESTION_EXCLAMATION_MARK = '\u2048', // ⁈
							EXCLAMATION_QUESTION_MARK = '\u2049', // ⁉
							REVERSE_QUESTION_MARK = '\u2E2E', // ⸮ (different codepoint from Arabic question mark)
							SMALL_FULL_STOP = '\uFE52', // ﹒
							SMALL_QUESTION_MARK = '\uFE56', // ﹖
							SMALL_EXCLAMATION_MARK = '\uFE57', //﹗
							FULLWIDTH_EXCLAMATION_MARK = '\uFF01', // ！
							FULLWIDTH_FULL_STOP = '\uFF0E', // ．
							FULL_WIDTH_QUESTION_MARK = '\uFF1F',
							
							ELLIPSIS = '\u2026', // 8230
							
							WHITE_HEART = '\u2661', // ♡
							BLACK_HEART = '\u2665', // ♥
							QUARTER_NOTE = '\u2669', // ♩
							EIGHTH_NOTE = '\u266A', // ♪
							BEAMED_EIGHTH_NOTES = '\u266B', // ♫
							BEAMED_SIXTEENTH_NOTES = '\u266C', // ♬
							MUSIC_FLAT_SIGN = '\u266D', // ♭
							MUSIC_NATURAL_SIGN = '\u266E', // ♮
							MUSIC_SHARP_SIGN = '\u266F', // ♯
							WHITE_SMILEY_FACE = '\u263A', // ☺
							BLACK_SMILEY_FACE = '\u263B', // ☻
							PEACE_SYMBOL = '\u262E', // ☮
							SPARKLES = '\u2728', // ✨
							VICTORY_HAND = '\u270C', // ✌️
							
							COPYRIGHT = '\u00A9', //©
							SECTION = '\u00A7', //§
							
							ACUTE_ACCENT = '\u00B4', // ´
							PILCROW = '\u00B6', //¶
							MIDDLE_DOT = '\u00B7', //·
	
							FRACTION_SLASH = '\u2044', // ⁄
							DIVISION_SLASH = '\u2215', // ∕
							
							// Hyphens (SEE HYPHENS below)
							// Don't forget HYPHEN_BULLET = '\u2043'
							SOFT_HYPHEN = '\u00AD', // ­
							HYPHEN_MINUS = '\u002D', // - 	
							ARMENIAN_HYPHEN = '\u058A', // ֊
							HEBREW_PUNCTUATION_MAQAF = '\u058B', // ־
							HYPHEN = '\u2010', // -
							NON_BREAKING_HYPHEN = '\u2011', // ‑
							FIGURE_DASH = '\u2012', // ‒
							EN_DASH = '\u2013', // –
							EM_DASH = '\u2014', // —
							HORIZONTAL_BAR = '\u2015', // ―
							SWUNG_DASH = '\u2053', // ⁓  // NOTE: there are a few more of this stype of thing
							TWO_EM_DASH = '\u2E3A',
							THREE_EM_DASH = '\u2E3B',
							SMALL_EM_DASH = '\uFE58',
							SMALL_HYPHEN_MINUS = '\uFE63',
							FULLWIDTH_HYPHEN_MINUS = '\uFF0D',
							HEAVY_MINUS_SIGN = '\u2796',
							
							// SEE SINGLE_QUOTES, DOUBLE_QUOTES below
							CONTROL_LOW_DOUBLE_RIGHT_QUOTE = '\u0084', // <control> (double right quotes) 132
							CONTROL_SINGLE_LEFT_QUOTE = '\u0091', // 145 <control>
							CONTROL_SINGLE_RIGHT_QUOTE = '\u0092', // 146 <control>
							CONTROL_DOUBLE_LEFT_QUOTE = '\u0093', // 147 <control>
							CONTROL_DOUBLE_RIGHT_QUOTE = '\u0094', // 148 <control>
							// Regular quotes (SEE SINGLE_QUOTES, DOUBLE_QUOTES below) 
							GRAVE_ACCENT = '\u0060', // 96 `
							APOSTROPHE = '\'', // 39 ' 
							LEFT_SINGLE_QUOTE = '\u2018', // 8216 ‘ 
							RIGHT_SINGLE_QUOTE = '\u2019', // 8217 ’
							LEFT_DOUBLE_QUOTE = '\u201C', // 8220 “
							RIGHT_DOUBLE_QUOTE = '\u201D', // 8221 ”
							DOUBLE_QUOTE = '\u0022', // "
							// French quotes (SEE SINGLE_QUOTES, DOUBLE_QUOTES below)
							LEFT_SINGLE_FRENCH_QUOTE = '\u2039', // ‹
							RIGHT_SINGLE_FRENCH_QUOTE = '\u203A', // ›
							LEFT_FRENCH_QUOTE = '\u00AB', // 171 «
							RIGHT_FRENCH_QUOTE = '\u00BB', // 187 »							 
							// German quotes (SEE SINGLE_QUOTES, DOUBLE_QUOTES below)
							SINGLE_LOW9_QUOTE = '\u201A', // 8218 ‚
							SINGLE_HIGH_REVERSED_NINE_QUOTE = '\u201B', //8219 ‛
							DOUBLE_LOW9_QUOTE = '\u201E', // 8222 „
							DOUBLE_HIGH_REVERSED9_QUOTE = '\u201F', // 8223 ‟
						    // SEE SINGLE_QUOTES, DOUBLE_QUOTES below
							PRIME = '\u2032', // 8242 ′
							DOUBLE_PRIME = '\u2033', // 8243 ″
							TRIPLE_PRIME = '\u2034', // 8244 ‴
						    // SEE SINGLE_QUOTES, DOUBLE_QUOTES below
							REVERSED_PRIME = '\u2035', // 8245 ‵ 
							REVERSED_DOUBLE_PRIME = '\u2036', // 8246 ‶ 
							REVERSED_TRIPLE_PRIME = '\u2037', // 8247 ‷
							FULL_WIDTH_QUOTATION_MARK = '\uFF02', // ＂
							FULL_WIDTH_APOSTROPHE = '\uFF07',
							
							
							// bullet characters
							BULLET = '\u2022', // •
							TRIANGULAR_BULLET = '\u2023', // ‣
							WHITE_BULLET = '\u25E6', // ◦
							HYPHEN_BULLET = '\u2043', // ⁃
							BLACK_LEFTWARDS_BULLET = '\u204c', // ⁌
							BLACK_RIGHTWARDS_BULLET = '\u204d', // ⁍
							BULLET_OPERATOR = '\u2219', // ∙ (dot product)
							BLACK_CIRCLE = '\u25CF',
							BLACK_CIRCLE_FOR_RECORD = '\u23FA',
							MEDIUM_BLACK_CIRCLE = '\u26AB',
							LARGE_BLACK_CIRCLE = '\u2B24',
							BLACK_RIGHT_POINTING_POINTER = '\u25BA',
							
							// funny spaces
							NON_BREAKING_SPACE = '\u00a0', // &nbsp;
							HAIR_SPACE = '\u0002',
							ZERO_WIDTH_NON_JOINER = '\u200C',
							
							// funny formatting
							POP_DIRECTIONAL_FORMATTING = '\u202C',
							LEFT_TO_RIGHT_EMBEDDING = '\u202A',
							RIGHT_TO_LEFT_EMBEDDING = '\u202B',
							INHIBIT_ARABIC_FORM_SHAPING = '\u206C',
							ACTIVATE_ARABIC_FORM_SHAPING = '\u206D'
							;
							 
	
	public final static String DOUBLE_QUOTES = "["+
												LEFT_FRENCH_QUOTE+
												RIGHT_FRENCH_QUOTE+
												CONTROL_DOUBLE_LEFT_QUOTE+
												CONTROL_DOUBLE_RIGHT_QUOTE+
												LEFT_DOUBLE_QUOTE+
												RIGHT_DOUBLE_QUOTE+
												DOUBLE_QUOTE+
												DOUBLE_LOW9_QUOTE+
												DOUBLE_HIGH_REVERSED9_QUOTE+
												DOUBLE_PRIME+
												TRIPLE_PRIME+
												REVERSED_DOUBLE_PRIME+
												REVERSED_TRIPLE_PRIME+
												"]";
	
	public final static String SINGLE_QUOTES = "["+
												GRAVE_ACCENT+
												ACUTE_ACCENT+
												APOSTROPHE+
												LEFT_SINGLE_QUOTE+
												RIGHT_SINGLE_QUOTE+
												LEFT_SINGLE_FRENCH_QUOTE+
												RIGHT_SINGLE_FRENCH_QUOTE+
												CONTROL_SINGLE_LEFT_QUOTE+
												CONTROL_SINGLE_RIGHT_QUOTE+
												SINGLE_LOW9_QUOTE+
												SINGLE_HIGH_REVERSED_NINE_QUOTE+
												PRIME+
												REVERSED_PRIME+
												"]";
	
	public final static String LEFT_QUOTES = "["+
							   // Single
							   GRAVE_ACCENT+
							   LEFT_SINGLE_QUOTE+
							   LEFT_SINGLE_FRENCH_QUOTE+
							   CONTROL_SINGLE_LEFT_QUOTE+
							   SINGLE_LOW9_QUOTE+
							   PRIME+
							   // Double
							   LEFT_FRENCH_QUOTE+
							   CONTROL_DOUBLE_LEFT_QUOTE+
							   LEFT_DOUBLE_QUOTE+
							   DOUBLE_LOW9_QUOTE+
							   DOUBLE_PRIME+
							   TRIPLE_PRIME+
							   "]";
	
	public final static String RIGHT_QUOTES = "["+
							   // Single
							   ACUTE_ACCENT+
							   RIGHT_SINGLE_QUOTE+
							   RIGHT_SINGLE_FRENCH_QUOTE+
							   CONTROL_SINGLE_RIGHT_QUOTE+
							   SINGLE_HIGH_REVERSED_NINE_QUOTE+
							   REVERSED_PRIME+
							   // Double
							   RIGHT_FRENCH_QUOTE+
							   CONTROL_DOUBLE_RIGHT_QUOTE+
							   RIGHT_DOUBLE_QUOTE+
							   DOUBLE_HIGH_REVERSED9_QUOTE+
							   REVERSED_DOUBLE_PRIME+
							   REVERSED_TRIPLE_PRIME+
							   "]";
	
	public final static String HYPHENS = "[" + 
										 SOFT_HYPHEN+
										 "\\"+HYPHEN_MINUS+  // must escape this one 	
										 ARMENIAN_HYPHEN+
										 HEBREW_PUNCTUATION_MAQAF+
										 HYPHEN+
										 NON_BREAKING_HYPHEN+
										 FIGURE_DASH+
										 EN_DASH+
										 EM_DASH+
										 TWO_EM_DASH+
										 THREE_EM_DASH+
										 SMALL_EM_DASH+
										 SMALL_HYPHEN_MINUS+
										 FULLWIDTH_HYPHEN_MINUS+
										 HORIZONTAL_BAR+
										 SWUNG_DASH+
										 
										"]";
	
	public final static String LONG_DASH = "["+
							EN_DASH + 
							EM_DASH + 
							TWO_EM_DASH + 
							THREE_EM_DASH + 
							SMALL_EM_DASH + 
						"]";
	
	public final static String SENTENCE_ENDERS = "[" +
								AR_FULL_STOP+
								AR_QMARK+
								"\\"+EXCLAMATION_MARK+
								"\\"+FULL_STOP+
								"\\"+QUESTION_MARK+ 
								INVERTED_QUESTION_MARK+
								DOUBLE_EXCLAMATION_POINT+
								INTERROBANG+
								DOUBLE_QUESTION_MARK+
								QUESTION_EXCLAMATION_MARK+
								EXCLAMATION_QUESTION_MARK+
								REVERSE_QUESTION_MARK+
								SMALL_FULL_STOP+
								SMALL_QUESTION_MARK+
								SMALL_EXCLAMATION_MARK+
								FULLWIDTH_EXCLAMATION_MARK+
								FULLWIDTH_FULL_STOP+
								FULL_WIDTH_QUESTION_MARK + "]";
	
	// likely incomplete
	private static final Character[][] fullwidthmappings = new Character[][] {
		{FULL_WIDTH_DIGIT_ZERO, '0'},
		{FULL_WIDTH_DIGIT_ONE, '1'},
		{FULL_WIDTH_DIGIT_TWO, '2'},
		{FULL_WIDTH_DIGIT_THREE, '3'},
		{FULL_WIDTH_DIGIT_FOUR, '4'},
		{FULL_WIDTH_DIGIT_FIVE, '5'},
		{FULL_WIDTH_DIGIT_SIX, '6'},
		{FULL_WIDTH_DIGIT_SEVEN, '7'},
		{FULL_WIDTH_DIGIT_EIGHT, '8'},
		{FULL_WIDTH_DIGIT_NINE, '9'},
		{FULL_WIDTH_QUESTION_MARK, '?'},
		{FULLWIDTH_EXCLAMATION_MARK, '!'},
		{FULLWIDTH_FULL_STOP, '.'},
		{FULLWIDTH_HYPHEN_MINUS, '-'},
		{FULL_WIDTH_APOSTROPHE, '\''},
		{FULL_WIDTH_QUOTATION_MARK, '"'},
	};
	
	private static Map<Character, Character> FULLWIDTH_TO_CANONICAL_FORM = new HashMap<>();
	static {
		for(Character[] mapping : fullwidthmappings) {
			FULLWIDTH_TO_CANONICAL_FORM.put(mapping[0], mapping[1]);
		}
	}
	
	public final static String mapFullWidthChars(String text) {
		StringBuilder buf = new StringBuilder();
		int len = text.length();
		for(int i = 0; i < len; i++) {
			char c = text.charAt(i);
			Character result = FULLWIDTH_TO_CANONICAL_FORM.get(c);
			if(result == null) result = c;
			buf.append(result);
		}
		return buf.toString();
	}
	
	
	public static String convertUnicode(char[] little, String text) {
		StringBuilder buf = new StringBuilder();
		final int length = text.length();
		for(int i = 0; i < 6; i++) little[i] = '\0';
		for(int i = 0; i < length; i++) {
			char c = text.charAt(i);
			
			little[0] = little[1];
			little[1] = little[2];
			little[2] = little[3];
			little[3] = little[4];
			little[4] = little[5];
			little[5] = c;
			
			buf.append(c);
			if(little[0] == '\\' &&
			   (little[1] == 'u' || little[1] == 'U') &&
			   isPossibleUnicode(little[2]) &&
			   isPossibleUnicode(little[3]) &&
			   isPossibleUnicode(little[4]) &&
			   isPossibleUnicode(little[5]) 
					) {
				String last4 = buf.substring(buf.length()-4, buf.length());
				char unicode = (char)Integer.parseInt(last4, 16);
				buf.setLength(buf.length()-6);
				buf.append(unicode);
			}
		}
		return buf.toString();
	}
	
	private static boolean isPossibleUnicode(char c) {
		return (c >= 'a' && c <= 'f') || (c >= 'A' && c <= 'F') || (c >= '0' && c <= '9');
	}
}
