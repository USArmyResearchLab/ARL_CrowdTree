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

package arl.common.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class UTF8BufferedFileReader extends BufferedReader {
	
	
	public UTF8BufferedFileReader(String inputFile) throws FileNotFoundException, UnsupportedEncodingException {
		this(new File(inputFile));
	}
	
	public UTF8BufferedFileReader(File inputFile) throws FileNotFoundException, UnsupportedEncodingException {
		this(inputFile, (int)Math.pow(2, 16));
	}

	public UTF8BufferedFileReader(File inputFile, int buffSize) throws FileNotFoundException, UnsupportedEncodingException {
		super(new InputStreamReader(new FileInputStream(inputFile), "UTF-8"), buffSize);
	}
	
}
