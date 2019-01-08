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

/*
 * ============================================================================
 *  Per the Apache 2.0 license redistribution requirements, the 
 *  University of Southern California copyright notice below is retained 
 *  in this file. This file includes code from one or more source files 
 *  of the following project (which is licensed under Apache 2.0).
 *     https://www.isi.edu/publications/licensed-sw/fanseparser/index.html
 *  This file contains modifications and is not identical to the original code.
 * ============================================================================    
 * 
 * Copyright 2011 University of Southern California 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0 
 *      
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 */

package arl.nlp.parse.io;

import static arl.nlp.lang.arabic.ArabicCharacters.TATWEEL;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import arl.common.io.InstanceReadException;
import arl.common.io.InstanceReader;
import arl.common.meta.ArgMap;
import arl.common.meta.Initializable;
import arl.common.meta.InitializationException;
import arl.common.meta.transform.Transform;
import arl.common.meta.transform.TransformException;
import arl.nlp.parse.types.DefaultParse;
import arl.nlp.parse.types.DefaultTreeToken;
import arl.nlp.parse.types.MorphoAnchor;
import arl.nlp.parse.types.MutableTreeToken;
import arl.nlp.parse.types.Parse;
import arl.nlp.parse.types.TreeToken;

public class OneSentencePerLineParseReader implements InstanceReader<Parse> { 

	public final static String PARAM_CANONICALIZE = "Canonicalize",
							   PARAM_CHAR_TRANSFORMS = "CharTransforms",
							   PARAM_DEFAULT_POS_TAG = "DefaultPosTag";
	
	private boolean mCanonicalize = true;
	private List<Transform<Character, Character>> mCharTransforms;
	private String mDefaultPos = "NN";
	
	private BufferedReader mReader;
	
	@Override
	public void initialize(ArgMap params) throws InitializationException {
		Initializable.checkArgNames(params, new HashSet<>(Arrays.asList(PARAM_CANONICALIZE, PARAM_CHAR_TRANSFORMS, PARAM_DEFAULT_POS_TAG)));
		mCanonicalize = params.getBooleanValue(PARAM_CANONICALIZE, mCanonicalize);
		mCharTransforms = (List<Transform<Character, Character>>)params.get(PARAM_CHAR_TRANSFORMS);
		mDefaultPos = params.getStringValue(PARAM_DEFAULT_POS_TAG, mDefaultPos);
	}
	
	@Override
	public void setSource(InputStream is) {
		try {
			mReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
		}
		catch(UnsupportedEncodingException uee) {
			throw new RuntimeException(uee);
		}
	}
	
	@Override
	public Parse readInstance() throws InstanceReadException {
		List<MorphoAnchor> allAnchors = new ArrayList<>();
		
		try {
			String line = null;
			while((line = mReader.readLine()) != null) {
				if(!line.trim().equals("")) {
					line = line.trim();
					MutableTreeToken prevCoreAnchor = null;
		
					int chunk = 0;
					String[] tokens = line.split("\\s+");
					for(String t : tokens) {
						StringBuilder textBuf = new StringBuilder();

						int len = t.length();
						for(int i = 0; i < len; i++) {
							char c = t.charAt(i);
							if(c != TATWEEL) {
								try {
									if(mCharTransforms != null) {
										for(Transform<Character, Character> charTransform : mCharTransforms) {
											c = charTransform.transform(c);
										}
									}
								}
								catch(TransformException te) {
									throw new RuntimeException(te);
								}
								textBuf.append(c);
							}
						}
				
						MutableTreeToken core = new DefaultTreeToken(textBuf.toString(), mDefaultPos);
						
						if(prevCoreAnchor != null) {
							prevCoreAnchor.setNextSurfaceCore((TreeToken)core);
							core.setPrevSurfaceCore(prevCoreAnchor);
						}
						prevCoreAnchor = core;		
				
						if(core != null) {
							allAnchors.add(core);
						}
						chunk++;
					}
					break;
				}
			}
		}
		catch(IOException ioe) {
			throw new InstanceReadException(ioe);
		}
		return allAnchors.size() == 0 ? null : new DefaultParse(allAnchors);
	}
	
}
