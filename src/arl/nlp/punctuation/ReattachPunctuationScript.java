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

import java.io.BufferedInputStream;
import java.io.FileInputStream;

import arl.common.io.InstanceReader;
import arl.common.io.InstanceWriter;
import arl.common.meta.ArgMap;
import arl.common.meta.ArgMapParser;
import arl.nlp.parse.types.DefaultParse;
import arl.nlp.parse.types.DefaultTreeToken;
import arl.nlp.parse.types.Parse;
import arl.nlp.parse.types.TreeToken;

public class ReattachPunctuationScript {

	public final static String PARAM_IN_FILE = "Input",
							   PARAM_INSTANCE_READER = "InstanceReader",
							   PARAM_INSTANCE_WRITER = "InstanceWriter";

	public static void main(String[] args) throws Exception {
		ArgMap argMap = new ArgMapParser().parseOptions(args);
		
		InstanceReader<Parse> reader = (InstanceReader<Parse>)argMap.get(PARAM_INSTANCE_READER);
		InstanceWriter<Parse> writer = (InstanceWriter<Parse>)argMap.get(PARAM_INSTANCE_WRITER);
		
		BufferedInputStream bis = new BufferedInputStream(new FileInputStream(argMap.getFile(PARAM_IN_FILE)));
		DefaultParse instance = null;
		DefaultPunctuationAttachmentHeuristic punctAttacher = new DefaultPunctuationAttachmentHeuristic();
		reader.setSource(bis);
		while((instance = (DefaultParse)reader.readInstance()) != null) {
			DefaultTreeToken root = null;
			for(TreeToken core : instance.getAllTreeTokens()) {
				if(core.getHeadArc() == null) {
					root = (DefaultTreeToken)core;
					break;
				}
			}
			punctAttacher.reattachPunctuation(instance, root);
			writer.appendInstance(instance);
		}
		bis.close();
		writer.close();
	}
	
}
