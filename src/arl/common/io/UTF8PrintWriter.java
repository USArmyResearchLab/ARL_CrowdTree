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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class UTF8PrintWriter extends PrintWriter {
	
	
	public UTF8PrintWriter(String outFile) throws FileNotFoundException, UnsupportedEncodingException {
		this(new File(outFile));
	}

	public UTF8PrintWriter(File outFile) throws FileNotFoundException, UnsupportedEncodingException {
		this(outFile, false);
	}
	
	public UTF8PrintWriter(String outFile, boolean append) throws FileNotFoundException, UnsupportedEncodingException {
		this(new File(outFile), append);
	}
	
	public UTF8PrintWriter(File outFile, boolean append) throws FileNotFoundException, UnsupportedEncodingException {
		super(new OutputStreamWriter(new FileOutputStream(outFile, append), "UTF-8"));
	}
	
}
