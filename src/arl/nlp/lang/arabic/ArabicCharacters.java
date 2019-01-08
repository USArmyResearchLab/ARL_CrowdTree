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

public class ArabicCharacters {
	
	public static char	
				AR_FULL_STOP = '\u06D4',
				AR_EMPTY_CENTRE_LOW_STOP = '\u06EA',
				AR_START_OF_RUB_EL_HIZB = '\u06DE',
				AR_PLACE_OF_SAJDAH = '\u06E9',
				
				// PUNCTUATION
				AR_NUMBER_SIGN = '\u0600',
				AR_SIGN_SANAH = '\u0601',
				AR_FOOTNOTE_MARKER = '\u0602',
				AR_SIGN_SAFHA = '\u0603',
				// nothing for \u0604-060a
				AFGHANI_SIGN = '\u060b',
				AR_COMMA = '\u060c',
				AR_DATE_SEPARATOR = '\u060d',
				AR_POETIC_VERSE_SIGN = '\u060e',
				AR_SIGN_MISRA = '\u060f',
				
				AR_SIGN_SALLALLAHOU_ALAYHE_WASSALLAM = '\u0610',
				AR_SIGN_ALAYHE_ASSALLAM = '\u0611',
				AR_SIGN_RAHMATULLAH_ALAYHE = '\u0612',
				AR_SIGN_RADI_ALLAHOU_ANHU = '\u0613',
				AR_SIGN_TAKHALLUS = '\u0614',
				AR_SMALL_HIGH_TAH = '\u0615',
				// nothing for \u0616-u061a
				AR_SEMICOLON = '\u061b',
				AR_TRIPLE_DOT_PUNC = '\u061d',
				AR_QMARK = '\u061f', 
	
				// ARABIC CORE (1ST BLOCK)
				HAMZA = '\u0621',
				ALEF_W_MADDA_ABOVE = '\u0622',
				ALEF_W_HAMZA_ABOVE = '\u0623',
				WAW_W_HAMZA_ABOVE = '\u0624',
				ALEF_W_HAMZA_BELOW = '\u0625',
				YEH_W_HAMZA_ABOVE = '\u0626',
				ALEF = '\u0627',
				BEH =  '\u0628',
				TEH_MARBUTA = '\u0629',
				TEH =  '\u062a',
				THEH = '\u062b',
				JEEM = '\u062c',
				HAH =  '\u062d',
				KHAH = '\u062e',
				DAL =  '\u062f',
				THAL = '\u0630',
				REH =  '\u0631',
				ZAIN = '\u0632',
				SEEN = '\u0633',
				SHEEN = '\u0634',
				SAD =  '\u0635',
				DAD =  '\u0636',
				TAH =  '\u0637',
				ZAH =  '\u0638',
				AIN =  '\u0639',
				GHAIN = '\u063a',
				
				// TATWEEL (KASHIDA)
				TATWEEL = '\u0640',
				
				// ARABIC CORE, SECOND BLOCK
				FEH =  '\u0641',
				QAF =  '\u0642',
				KAF =  '\u0643',
				LAM =  '\u0644',
				MEEM = '\u0645',
				NOON = '\u0646',
				HEH =  '\u0647',
				WAW =  '\u0648',
				MAKSURA = '\u0649',
				YEH =  '\u064a',
				
				// CORE DIACRITICS
				FATHATAN = '\u064b',
				DAMMATAN = '\u064c',
				KASRATAN = '\u064d',
				FATHA = '\u064e',
				DAMMA = '\u064f',
				KASRA = '\u0650',
				SHADDA = '\u0651',
				SUKUN = '\u0652',
				
				MADDA_ABOVE = '\u0653',
				HAMZA_ABOVE = '\u0654',
				HAMZA_BELOW	= '\u0655',
				ALEF_SUBSCRIPT = '\u0656',
				DAMMA_INVERTED = '\u0657',
				MARK_NOON_GHUNNA = '\u0658',
				ZWARAKAY = '\u0659', // schwa sound diacritic (used in Pashto)
				VOWEL_SIGN_SMALL_V_ABOVE = '\u065a',
				VOWEL_SIGN_INVERTED_SMALL_V_ABOVE = '\u065b',
				VOWEL_SIGN_DOT_BELOW = '\u065c',
				VOWEL_SIGN_REVERSED_DAMMA = '\u065d',
				VOWEL_SIGN_FATHA_WITH_TWO_DOTS = '\u065e',
				
				DOTLESS_BEH = '\u066e',
				DOTLESS_QAF = '\u066f',
				ALEF_SUPERSCRIPT = '\u0670',
				ALEF_WASLA ='\u0671',
				
				// NON-MSA
				PEH = '\u067E',
				TCHEH = '\u0686',
				JEH = '\u0698',
				VEH = '\u06A4',
				KEHEH = '\u06A9',
			    KAF_WITH_RING = '\u06AB',
				GAF = '\u06AF',
				FARSI_YEH = '\u06CC',
				VE = '\u06CB',
				AR_E = '\u06D0',
				AR_YEH_BARREE = '\u06D2',
				YEH_WITH_TAIL = '\u06CD',
				
				//DIGITS
				INDO_AR_ZERO = '\u0660',
				INDO_AR_ONE = '\u0661',
				INDO_AR_TWO = '\u0662',
				INDO_AR_THREE = '\u0663',
				INDO_AR_FOUR = '\u0664',
				INDO_AR_FIVE = '\u0665',
				INDO_AR_SIX = '\u0666',
				INDO_AR_SEVEN = '\u0667',
				INDO_AR_EIGHT = '\u0668',
				INDO_AR_NINE = '\u0669',
				
				AR_PERCENT = '\u066A',
				AR_DECIMEL_SEPARATOR = '\u066B',
				AR_THOUSANDS_SEPARATOR = '\u066C',
				AR_FIVE_POINT_START = '\u066D',
				
				//DIGITS2
				INDO_FA_AR_ZERO = '\u06F0',
				INDO_FA_AR_ONE = '\u06F1',
				INDO_FA_AR_TWO = '\u06F2',
				INDO_FA_AR_THREE = '\u06F3',
				INDO_FA_AR_FOUR = '\u06F4',
				INDO_FA_AR_FIVE = '\u06F5',
				INDO_FA_AR_SIX = '\u06F6',
				INDO_FA_AR_SEVEN = '\u06F7',
				INDO_FA_AR_EIGHT = '\u06F8',
				INDO_FA_AR_NINE = '\u06F9',
				
				// Arabic Presentation Forms-A (FB50–FDFF)
				AR_ALEF_WASLA_ISOLATED = '\uFB50',
				AR_ALEF_WASLA_FINAL = '\uFB51',
				AR_BEEH_ISOLATED = '\uFB52',
				AR_BEEH_FINAL = '\uFB53',
				AR_BEEH_INITIAL = '\uFB54',
				AR_BEEH_MEDIAL = '\uFB55',
				AR_PEH_ISOLATED = '\uFB56',
				AR_PEH_FINAL = '\uFB57',
				AR_PEH_INITIAL = '\uFB58',
				AR_PEH_MEDIAL = '\uFB59',
				AR_BEHEH_ISOLATED = '\uFB5A',
				AR_BEHEH_FINAL = '\uFB5B',
				AR_BEHEH_INITIAL = '\uFB5C',
				AR_BEHEH_MEDIAL = '\uFB5D',
				AR_TTEHEH_ISOLATED = '\uFB5E',
				AR_TTEHEH_FINAL = '\uFB3F',
				AR_TTEHEH_INITIAL = '\uFB60',
				AR_TTEHEH_MEDIAL = '\uFB61',
				AR_TEHEH_ISOLATED = '\uFB62',
				AR_TEHEH_FINAL = '\uFB63',
				AR_TEHEH_INITIAL = '\uFB64',
				AR_TEHEH_MEDIAL = '\uFB65',
				AR_TTEH_ISOLATED = '\uFB66',
				AR_TTEH_FINAL = '\uFB67',
				AR_TTEH_INITIAL = '\uFB68',
				AR_TTEH_MEDIAL = '\uFB69',
				AR_VEH_ISOLATED = '\uFB6A',
				AR_VEH_FINAL = '\uFB6B',
				AR_VEH_INITIAL = '\uFB6C',
				AR_VEH_MEDIAL = '\uFB6D',
				AR_PEHEH_ISOLATED = '\uFB6E',
				AR_PEHEH_FINAL = '\uFB6F',
				AR_PEHEH_INITIAL = '\uFB70',
				AR_PEHEH_MEDIAL = '\uFB71',
				AR_DYEH_ISOLATED = '\uFB72',
				AR_DYEH_FINAL = '\uFB73',
				AR_DYEH_INITIAL = '\uFB74',
				AR_DYEH_MEDIAL = '\uFB75',
				AR_NYEH_ISOLATED = '\uFB76',
				AR_NYEH_FINAL = '\uFB77',
				AR_NYEH_INITIAL = '\uFB78',
				AR_NYEH_MEDIAL = '\uFB79',
				AR_TCHEH_ISOLATED = '\uFB7A',
				AR_TCHEH_FINAL = '\uFB7B',
				AR_TCHEH_INITIAL = '\uFB7C',
				AR_TCHEH_MEDIAL = '\uFB7D',
				AR_TCHEHEH_ISOLATED = '\uFB7E',
				AR_TCHEHEH_FINAL = '\uFB7F',
				AR_TCHEHEH_INITIAL = '\uFB80',
				AR_TCHEHEH_MEDIAL = '\uFB81',
				AR_DDAHAL_ISOLATED = '\uFB82',
				AR_DDAHAL_FINAL = '\uFB83',
				AR_DAHAL_ISOLATED = '\uFB84',
				AR_DAHAL_FINAL = '\uFB85',
				AR_DUL_ISOLATED = '\uFB86',
				AR_DUL_FINAL = '\uFB87',
				AR_DDAL_ISOLATED = '\uFB88',
				AR_DDAL_FINAL = '\uFB89',
				AR_JEH_ISOLATED = '\uFB8A',
				AR_JEH_FINAL = '\uFB8B',
				AR_RREH_ISOLATED = '\uFB8C',
				AR_RREH_FINAL = '\uFB8D',
				AR_KEHEH_ISOLATED = '\uFB8E',
				AR_KEHEH_FINAL = '\uFB8F',
				AR_KEHEH_INITIAL = '\uFB90',
				AR_KEHEH_MEDIAL = '\uFB91',
				AR_GAF_ISOLATED = '\uFB92',
				AR_GAF_FINAL = '\uFB93',
				AR_GAF_INITIAL = '\uFB94',
				AR_GAF_MEDIAL = '\uFB95',
				AR_GUEH_ISOLATED = '\uFB96',
				AR_GUEH_FINAL = '\uFB97',
				AR_GUEH_INITIAL = '\uFB98',
				AR_GUEH_MEDIAL = '\uFB99',
				AR_NGOEH_ISOLATED = '\uFB9A',
				AR_NGOEH_FINAL = '\uFB9B',
				AR_NGOEH_INITIAL = '\uFB9C',
				AR_NGOEH_MEDIAL = '\uFB9D',
				AR_GHUNNA_ISOLATED = '\uFB9E',
				AR_GHUNNA_FINAL = '\uFB9F',
				AR_RNOON_ISOLATED = '\uFBA0',
				AR_RNOON_FINAL = '\uFBA1',
				AR_RNOON_INITIAL = '\uFBA2',
				AR_RNOON_MEDIAL = '\uFBA3',
				AR_HEH_W_YEH_ABOVE_ISOLATED = '\uFBA4',
				AR_HEH_W_YEH_ABOVE_FINAL = '\uFBA5',
				AR_HEH_GOAL_ISOLATED = '\uFBA6',
				AR_HEH_GOAL_FINAL = '\uFBA7',
				AR_HEH_GOAL_INITIAL = '\uFBA8',
				AR_HEH_GOAL_MEDIAL = '\uFBA9',
				AR_HEH_DOACHASHMEE_ISOLATED = '\uFBAA',
				AR_HEH_DOACHASHMEE_FINAL = '\uFBAB',
				AR_HEH_DOACHASHMEE_INITIAL = '\uFBAC',
				AR_HEH_DOACHASMEE_MEDIAL = '\uFBAD',
				AR_YEH_BARREE_ISOLATED = '\uFBAE',
				AR_YEH_BARREE_FINAL = '\uFBAF',
				AR_YEH_BARREE_W_HAMZA_ABOVE_ISOLATED = '\uFBB0',
				AR_YEH_BARREE_W_HAMZA_ABOVE_FINAL = '\uFBB1',
				AR_NG_ISOLATED = '\uFBD3',
				AR_NG_FINAL = '\uFBD4',
				AR_NG_INITIAL = '\uFBD5',
				AR_NG_MEDIAL = '\uFBD6',
				AR_U_ISOLATED = '\uFBD7',
				AR_U_FINAL = '\uFBD8',
				AR_OE_ISOLATED = '\uFBD9',
				AR_OE_FINAL = '\uFBDA',
				AR_YU_ISOLATED = '\uFBDB',
				AR_YU_FINAL = '\uFBDC',
				AR_U_W_HAMZA_ABOVE_ISOLATED = '\uFBDD',
				AR_VE_ISOLATED = '\uFBDE',
				AR_VE_FINAL = '\uFBDF',
				AR_KIRGHIZ_OE_ISOLATED = '\uFBE0',
				AR_KIRGHIZ_OE_FINAL = '\uFBE1',
				AR_KIRGHIZ_YU_ISOLATED = '\uFBE2',
				AR_KIRGHIZ_YU_FINAL = '\uFBE3',
				AR_E_ISOLATED = '\uFBE4',
				AR_E_FINAL = '\uFBE5',
				AR_E_INITIAL = '\uFBE6',
				AR_E_MEDIAL = '\uFBE7',
				AR_UIGHUR_KAZAKH_KIRGHIZ_ALEF_MAKSURA_INITIAL = '\uFBE8',
				AR_UIGHUR_KAZAKH_KIRGHIZ_ALEF_MAKSURA_MEDIAL = '\uFBE9',
				AR_FARSI_YEH_ISOLATED = '\uFBFC',
				AR_FARSI_YEH_FINAL = '\uFBFD',
				AR_FARSI_YEH_INITIAL = '\uFBFE',
				AR_FARSI_YEH_MEDIAL = '\uFBFF',
				// gap
				AR_LIGATURE_SHADDA_W_DAMMATAN_ISOLATED = '\uFC5E',
				AR_LIGATURE_SHADDA_W_KASRATAN_ISOLATED = '\uFC5F',
				AR_LIGATURE_SHADDA_W_FATHA_ISOLATED = '\uFC60',
				AR_LIGATURE_SHADDA_W_DAMMA_ISOLATED = '\uFC61',
				AR_LIGATURE_SHADDA_W_KASRA_ISOLATED = '\uFC62',
				AR_ORNATE_LEFT_PAREN = '\uFD3E',
				AR_ORNATE_RIGHT_PAREN = '\uFD3F',
				AR_LIGATURE_ALLAH_ISOLATED = '\uFDF2',
				AR_RIAL_SIGN = '\uFDFC',
				//gap to FDFF
						
				// Arabic Presentation Forms-B (FE70–FEFF)
				AR_COMBINING_LIGATURE_LEFT_HALF = '\uFE20',
				AR_COMBINING_LIGATURE_RIGHT_HALF = '\uFE21',
				AR_COMBINING_DOUBLE_TILDE_LEFT_HALF = '\uFE22',
				AR_COMBINING_DOUBLE_TILDE_RIGHT_HALF = '\uFE23',
				
				AR_HAMZA_ISOLATED = '\uFE80',
				AR_ALEF_W_MADDA_ABOVE_ISOLATED = '\uFE81',
				AR_ALEF_W_MADDA_ABOVE_FINAL = '\uFE82',
				AR_ALEF_W_HAMZA_ABOVE_ISOLATED = '\uFE83',
				AR_ALEF_W_HAMZA_ABOVE_FINAL = '\uFE84',
				AR_WAW_W_HAMZA_ABOVE_ISOLATED = '\uFE85',
				AR_WAW_W_HAMZA_ABOVE_FINAL = '\uFE86',
				AR_ALEF_W_HAMZA_BELOW_ISOLATED = '\uFE87',
				AR_ALEF_W_HAMZA_BELOW_FINAL = '\uFE88',
				AR_YEH_W_HAMZA_ABOVE_ISOLATED = '\uFE89',
				AR_YEH_W_HAMZA_ABOVE_FINAL = '\uFE8A',
				AR_YEH_W_HAMZA_ABOVE_INITIAL = '\uFE8B',
				AR_YEH_W_HAMZA_ABOVE_MEDIAL = '\uFE8C',
				AR_ALEF_ISOLATED = '\uFE8D',
				AR_ALEF_FINAL = '\uFE8E',
				AR_BEH_ISOLATED = '\uFE8F',
				AR_BEH_FINAL = '\uFE90',
				AR_BEH_INITIAL = '\uFE91',
				AR_BEH_MEDIAL = '\uFE92',
				AR_TEH_MARBUTA_ISOLATED = '\uFE93',
				AR_TEH_MARBUTA_FINAL = '\uFE94',
				AR_TEH_ISOLATED = '\uFE95',
				AR_TEH_FINAL = '\uFE96',
				AR_TEH_INITIAL = '\uFE97',
				AR_TEH_MEDIAL = '\uFE98',
				AR_THEH_ISOLATED = '\uFE99',
				AR_THEH_FINAL = '\uFE9A',
				AR_THEH_INITIAL = '\uFE9B',
				AR_THEH_MEDIAL = '\uFE9C',
				AR_JEEM_ISOLATED = '\uFE9D',
				AR_JEEM_FINAL = '\uFE9E',
				AR_JEEM_INITIAL = '\uFE9F',
				AR_JEEM_MEDIAL = '\uFEA0',
				AR_HAH_ISOLATED = '\uFEA1',
				AR_HAH_FINAL = '\uFEA2',
				AR_HAH_INITIAL = '\uFEA3',
				AR_HAH_MEDIAL = '\uFEA4',
				AR_KHAH_ISOLATED = '\uFEA5',
				AR_KHAH_FINAL = '\uFEA6',
				AR_KHAH_INITIAL = '\uFEA7',
				AR_KHAH_MEDIAL = '\uFEA8',
				AR_DAL_ISOLATED = '\uFEA9',
				AR_DAL_FINAL = '\uFEAA',
				AR_THAL_ISOLATED = '\uFEAB',
				AR_THAL_FINAL = '\uFEAC',
				AR_REH_ISOLATED = '\uFEAD',
				AR_REH_FINAL = '\uFEAE',
				AR_ZAIN_ISOLATED = '\uFEAF',
				AR_ZAIN_FINAL = '\uFEB0',
				AR_SEEN_ISOLATED = '\uFEB1',
				AR_SEEN_FINAL = '\uFEB2',
				AR_SEEN_INITIAL = '\uFEB3',
				AR_SEEN_MEDIAL = '\uFEB4',
				AR_SHEEN_ISOLATED = '\uFEB5',
				AR_SHEEN_FINAL = '\uFEB6',
				AR_SHEEN_INITIAL = '\uFEB7',
				AR_SHEEN_MEDIAL = '\uFEB8',
				AR_SAD_ISOLATED = '\uFEB9',
				AR_SAD_FINAL = '\uFEBA',
				AR_SAD_INITIAL = '\uFEBB',
				AR_SAD_MEDIAL = '\uFEBC',
				AR_DAD_ISOLATED = '\uFEBD',
				AR_DAD_FINAL = '\uFEBE',
				AR_DAD_INITIAL = '\uFEBF',
				AR_DAD_MEDIAL = '\uFEC0',
				AR_TAH_ISOLATED = '\uFEC1',
				AR_TAH_FINAL = '\uFEC2',
				AR_TAH_INITIAL = '\uFEC3',
				AR_TAH_MEDIAL = '\uFEC4',
				AR_ZAH_ISOLATED = '\uFEC5',
				AR_ZAH_FINAL = '\uFEC6',
				AR_ZAH_INITIAL = '\uFEC7',
				AR_ZAH_MEDIAL = '\uFEC8',
				AR_AIN_ISOLATED = '\uFEC9',
				AR_AIN_FINAL = '\uFECA',
				AR_AIN_INITIAL = '\uFECB',
				AR_AIN_MEDIAL = '\uFECC',
				AR_GHAIN_ISOLATED = '\uFECD',
				AR_GHAIN_FINAL = '\uFECE',
				AR_GHAIN_INITIAL = '\uFECF',
				AR_GHAIN_MEDIAL = '\uFED0',
				AR_FEH_ISOLATED = '\uFED1',
				AR_FEH_FINAL = '\uFED2',
				AR_FEH_INITIAL = '\uFED3',
				AR_FEH_MEDIAL = '\uFED4',
				AR_QAF_ISOLATED = '\uFED5',
				AR_QAF_FINAL = '\uFED6',
				AR_QAF_INITIAL = '\uFED7',
				AR_QAF_MEDIAL = '\uFED8',
				AR_KAF_ISOLATED = '\uFED9',
				AR_KAF_FINAL = '\uFEDA',
				AR_KAF_INITIAL = '\uFEDB',
				AR_KAF_MEDIAL = '\uFEDC',
				AR_LAM_ISOLATED = '\uFEDD',
				AR_LAM_FINAL = '\uFEDE',
				AR_LAM_INITIAL = '\uFEDF',
				AR_LAM_MEDIAL = '\uFEE0',
				AR_MEEM_ISOLATED = '\uFEE1',
				AR_MEEM_FINAL = '\uFEE2',
				AR_MEEM_INITIAL = '\uFEE3',
				AR_MEEM_MEDIAL = '\uFEE4',
				AR_NOON_ISOLATED = '\uFEE5',
				AR_NOON_FINAL = '\uFEE6',
				AR_NOON_INITIAL = '\uFEE7',
				AR_NOON_MEDIAL = '\uFEE8',
				AR_HEH_ISOLATED = '\uFEE9',
				AR_HEH_FINAL = '\uFEEA',
				AR_HEH_INITIAL = '\uFEEB',
				AR_HEH_MEDIAL = '\uFEEC',
				AR_WAW_ISOLATED = '\uFEED',
				AR_WAW_FINAL = '\uFEEE',
				AR_ALEF_MAKSURA_ISOLATED = '\uFEEF',
				AR_ALEF_MAKSURA_FINAL = '\uFEF0',
				AR_YEH_ISOLATED = '\uFEF1',
				AR_YEH_FINAL = '\uFEF2',
				AR_YEH_INITIAL = '\uFEF3',
				AR_YEH_MEDIAL = '\uFEF4',
				
				AR_LIGATURE_LAM_W_ALEF_W_MADDA_ABOVE_ISOLATED = '\uFEF5',
				AR_LIGATURE_LAM_W_ALEF_W_MADDA_ABOVE_FINAL = '\uFEF6',
				AR_LIGATURE_LAM_W_ALEF_W_HAMZA_ABOVE_ISOLATED = '\uFEF7',
				AR_LIGATURE_LAM_W_ALEF_W_HAMZA_ABOVE_FINAL = '\uFEF8',
				AR_LIGATURE_LAM_W_ALEF_W_HAMZA_BELOW_ISOLATED = '\uFEF9',
				AR_LIGATURE_LAM_W_ALEF_W_HAMZA_BELOW_FINAL = '\uFEFA',
				AR_LIGATURE_LAM_W_ALEF_ISOLATED = '\uFEFB',
				AR_LIGATURE_LAM_W_ALEF_FINAL = '\uFEFC'
				;
	
	private static final Character[][] presentationForms = new Character[][] {
		{ALEF,	AR_ALEF_ISOLATED,	null,	null, 					AR_ALEF_FINAL},
		{BEH,	AR_BEH_ISOLATED,	AR_BEH_INITIAL,	AR_BEH_MEDIAL,	AR_BEH_FINAL},
		{TEH,	AR_TEH_ISOLATED,	AR_TEH_INITIAL, AR_TEH_MEDIAL,	AR_TEH_FINAL},
		{THEH,	AR_THEH_ISOLATED,	AR_THEH_INITIAL,	AR_THEH_MEDIAL,	AR_THEH_FINAL},
		{JEEM,	AR_JEEM_ISOLATED,	AR_JEEM_INITIAL,	AR_JEEM_MEDIAL,		AR_JEEM_FINAL},
		{HAH,	AR_HAH_ISOLATED,	AR_HAH_INITIAL,		AR_HAH_MEDIAL,	AR_HAH_MEDIAL},
		{KHAH,	AR_KHAH_ISOLATED,	AR_KHAH_INITIAL,	AR_KHAH_MEDIAL,	AR_KHAH_FINAL},
		{DAL,	AR_DAL_ISOLATED,	null,				null,				AR_DAL_FINAL},
		{THAL,	AR_THAL_ISOLATED,	null,				null,				AR_THAL_FINAL},
		{REH,	AR_REH_ISOLATED,	null,				null,				AR_REH_FINAL},
		{ZAIN,	AR_ZAIN_ISOLATED,	null,				null,				AR_ZAIN_FINAL},
		{SEEN,	AR_SEEN_ISOLATED,	AR_SEEN_INITIAL,	AR_SEEN_MEDIAL,		AR_SEEN_FINAL},
		{SHEEN,	AR_SHEEN_ISOLATED,	AR_SHEEN_INITIAL,	AR_SHEEN_MEDIAL,	AR_SHEEN_FINAL},
		{SAD,	AR_SAD_ISOLATED,	AR_SAD_INITIAL,		AR_SAD_MEDIAL,		AR_SAD_FINAL},
		{DAD,	AR_DAD_ISOLATED,	AR_DAD_INITIAL,		AR_DAD_MEDIAL,		AR_DAD_FINAL},
		{TAH,	AR_TAH_ISOLATED,	AR_TAH_INITIAL, 	AR_TAH_MEDIAL,		AR_TAH_FINAL},
		{ZAH,	AR_ZAH_ISOLATED,	AR_ZAH_INITIAL,		AR_ZAH_MEDIAL,		AR_ZAH_FINAL},
		{AIN,	AR_AIN_ISOLATED,	AR_AIN_INITIAL,		AR_AIN_MEDIAL,		AR_AIN_FINAL},
		{GHAIN,	AR_GHAIN_ISOLATED,	AR_GHAIN_INITIAL,	AR_GHAIN_MEDIAL,	AR_GHAIN_FINAL},
		{FEH,	AR_FEH_ISOLATED,	AR_FEH_INITIAL,		AR_FEH_MEDIAL,		AR_FEH_FINAL},
		{QAF,	AR_QAF_ISOLATED,	AR_QAF_INITIAL,		AR_QAF_MEDIAL,		AR_QAF_FINAL},
		{KAF,	AR_KAF_ISOLATED,	AR_KAF_INITIAL,		AR_KAF_MEDIAL,		AR_KAF_FINAL},
		{LAM,	AR_LAM_ISOLATED,	AR_LAM_INITIAL,		AR_LAM_MEDIAL,		AR_LAM_FINAL},
		{MEEM,	AR_MEEM_ISOLATED,	AR_MEEM_INITIAL,	AR_MEEM_MEDIAL,		AR_MEEM_FINAL},
		{NOON,	AR_NOON_ISOLATED,	AR_NOON_INITIAL,	AR_NOON_MEDIAL,		AR_NOON_FINAL},
		{HEH,	AR_HEH_ISOLATED,	AR_HEH_INITIAL,		AR_HEH_MEDIAL,		AR_HEH_FINAL},
		{WAW,	AR_WAW_ISOLATED,	null,				null,				AR_WAW_FINAL},
		{YEH,	AR_YEH_ISOLATED,	AR_YEH_INITIAL,		AR_YEH_MEDIAL,		AR_YEH_FINAL},
		{TEH_MARBUTA,	AR_TEH_MARBUTA_ISOLATED,	null,		null,		AR_TEH_MARBUTA_FINAL},
		
				
		{PEH,	AR_PEH_ISOLATED,	AR_PEH_INITIAL,		AR_PEH_MEDIAL,		AR_PEH_FINAL},
		{JEH,	AR_JEH_ISOLATED,	null,		null,		AR_JEH_FINAL},
		{VE,	AR_VE_ISOLATED,	null,		null,		AR_VE_FINAL},
		{KEHEH,	AR_KEHEH_ISOLATED,	AR_KEHEH_INITIAL,		AR_KEHEH_MEDIAL,		AR_KEHEH_FINAL},
		{TCHEH,	AR_TCHEH_ISOLATED,	AR_TCHEH_INITIAL,		AR_TCHEH_MEDIAL,		AR_TCHEH_FINAL},
		{AR_E,	AR_E_ISOLATED,	AR_E_INITIAL,		AR_E_MEDIAL,		AR_E_FINAL},
		{GAF, AR_GAF_ISOLATED, AR_GAF_INITIAL, AR_GAF_MEDIAL, AR_GAF_FINAL},
		{FARSI_YEH,	AR_FARSI_YEH_ISOLATED,	AR_FARSI_YEH_INITIAL,		AR_FARSI_YEH_MEDIAL,		AR_FARSI_YEH_FINAL},
		
		{HAMZA,	AR_HAMZA_ISOLATED,	null,				null,				null},
		{MAKSURA,	AR_ALEF_MAKSURA_ISOLATED,	null,	null,			AR_ALEF_MAKSURA_FINAL},
		{YEH_W_HAMZA_ABOVE,		AR_YEH_W_HAMZA_ABOVE_ISOLATED,	AR_YEH_W_HAMZA_ABOVE_INITIAL, AR_YEH_W_HAMZA_ABOVE_MEDIAL, AR_YEH_W_HAMZA_ABOVE_FINAL},
		{WAW_W_HAMZA_ABOVE, 	AR_WAW_W_HAMZA_ABOVE_ISOLATED,	null,	null,	AR_WAW_W_HAMZA_ABOVE_FINAL},
		{ALEF_W_HAMZA_ABOVE,	AR_ALEF_W_HAMZA_ABOVE_ISOLATED,	null,	null,	AR_ALEF_W_HAMZA_ABOVE_FINAL},
		{ALEF_W_HAMZA_BELOW,	AR_ALEF_W_HAMZA_BELOW_ISOLATED,	null,	null,	AR_ALEF_W_HAMZA_BELOW_FINAL},
		{ALEF_W_MADDA_ABOVE,	AR_ALEF_W_MADDA_ABOVE_ISOLATED,	null,	null,	AR_ALEF_W_MADDA_ABOVE_FINAL},
		{ALEF_WASLA,			AR_ALEF_WASLA_ISOLATED,			null,	null,	AR_ALEF_WASLA_FINAL}
	};
	
	// reverse order
	private static final Character[][] digits = new Character[][]{
		{INDO_FA_AR_ZERO, INDO_AR_ZERO},
		{INDO_FA_AR_ONE, INDO_AR_ONE},
		{INDO_FA_AR_TWO, INDO_AR_TWO},
		{INDO_FA_AR_THREE, INDO_AR_THREE},
		{INDO_FA_AR_FOUR, INDO_AR_FOUR},
		{INDO_FA_AR_FIVE, INDO_AR_FIVE},
		{INDO_FA_AR_SIX, INDO_AR_SIX},
		{INDO_FA_AR_SEVEN, INDO_AR_SEVEN},
		{INDO_FA_AR_EIGHT, INDO_AR_EIGHT},
		{INDO_FA_AR_NINE, INDO_AR_NINE},
	};
	
	private static Map<Character, Character> INDO_DIGIT_MAP = new HashMap<>();
	private static Map<Character, Character> PRESENTATION_FORM_TO_CANONICAL_FORM = new HashMap<>();
	private static Map<Character, Character[]> CHARACTER_TO_PRESENTATION_FORMS = new HashMap<>();
	static {
		for(Character[] chars : presentationForms) {
			Character canonicalForm = chars[0];
			CHARACTER_TO_PRESENTATION_FORMS.put(canonicalForm, chars);
			for(Character c : chars) {
				if(c != null) {
					PRESENTATION_FORM_TO_CANONICAL_FORM.put(c, canonicalForm);
				}
			}
		}
		// In reverse order
		for(Character[] digi : digits) {
			INDO_DIGIT_MAP.put(digi[1], digi[0]);
		}
	}
	
	public final static String getStandardForm(String arText) {
		StringBuilder buf = new StringBuilder();
		int len = arText.length();
		for(int i = 0; i < len; i++) buf.append(getStandardForm(arText.charAt(i)));
		return buf.toString();
	}
	
	public final static Character getStandardForm(Character c) {
		return replace(c, PRESENTATION_FORM_TO_CANONICAL_FORM);
	}
	
	private final static Character replace(Character c, Map<Character, Character> charMap) {
		Character result = charMap.get(c);
		if(result == null) result = c;
		return result;
	}
	
	public final static String mergeDigits(String arText) {
		StringBuilder buf = new StringBuilder();
		int len = arText.length();
		for(int i = 0; i < len; i++) {
			buf.append(replace(arText.charAt(i), INDO_DIGIT_MAP));
		}
		return buf.toString();
	}
	
	// Probably belongs in a different file
	public final static boolean isInterDental(char c) {
		return c == THEH || c == THAL || c == ZAH;
	}

	// Probably belongs in a different file
	public final static boolean isEmphatic(char c) {
		return c == SAD || c == DAD || c == ZAH || c == TAH || c==HAH; 
	}
				
}
