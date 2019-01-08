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

package arl.nlp.parse.io;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashSet;

import arl.common.io.InstanceWriter;
import arl.common.meta.ArgMap;
import arl.common.meta.Initializable;
import arl.common.meta.InitializationException;
import arl.nlp.parse.types.Parse;

abstract public class AbstractParseWriter implements InstanceWriter<Parse> {

	public final static String PARAM_OUTPUT_FILE = "Output",
								PARAM_AUTO_FLUSH = "Autoflush",
								PARAM_ENCODING = "Encoding",
								PARAM_APPEND = "Append";
	
	protected PrintWriter mWriter;
	protected boolean mAutoFlush = true;
	
	@Override
	public void initialize(ArgMap args) throws InitializationException {
		Initializable.checkArgNames(args, new HashSet<>(Arrays.asList(PARAM_APPEND, PARAM_ENCODING, PARAM_OUTPUT_FILE, PARAM_AUTO_FLUSH)));
		String encoding = args.containsKey(PARAM_ENCODING)?args.getStringValue(PARAM_ENCODING):"UTF-8";
		String outputFile = args.getStringValue(PARAM_OUTPUT_FILE);
		boolean append = args.getBooleanValue(PARAM_APPEND, Boolean.FALSE);
		try {
			if(outputFile != null) {
				mWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(outputFile, append), encoding));
			}
			else {
				mWriter = new PrintWriter(new OutputStreamWriter(System.out, encoding));
			}
		}
		catch(IOException ioe) {
			throw new InitializationException(ioe);
		}
		mAutoFlush = args.getBooleanValue(PARAM_AUTO_FLUSH, mAutoFlush);
	}

	@Override
	public void flush() {
		mWriter.flush();
	}


	@Override
	public void close() {
		mWriter.close();
	}

}
