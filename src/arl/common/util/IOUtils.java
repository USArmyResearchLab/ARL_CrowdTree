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

package arl.common.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;

import arl.common.io.UTF8BufferedFileReader;

// TODO: Consider moving to new Java I/O
public class IOUtils {

	public static int countLines(File file) throws Exception {
		BufferedReader reader = new UTF8BufferedFileReader(file);
		int count = 0;
		while((reader.readLine()) != null) {
			count++;
		}
		reader.close();
		return count;
	}
	
	public static List<String> readUTF8FileAsLines(InputStream is) throws IOException {
		return readFileAsLines(new BufferedReader(new InputStreamReader(is, "UTF-8")));
	}
	
	public static List<String> readFileAsLines(BufferedReader reader) throws IOException {
		List<String> lines = new ArrayList<>();
		String line = null;
		while((line = reader.readLine()) != null) {
			lines.add(line);
		}
		reader.close();
		return lines;
	}
	
	public static List<String> readUTF8FileAsLines(File file) throws IOException {
		UTF8BufferedFileReader reader = new UTF8BufferedFileReader(file);
		List<String> lines = new ArrayList<>();
		String line = null;
		while((line = reader.readLine()) != null) {
			lines.add(line);
		}
		reader.close();
		return lines;
	}
	
	public static String readUTF8FileAsString(File file, String lineEndToInsert) throws IOException {
		List<String> lines = readUTF8FileAsLines(file);
		StringBuilder buf = new StringBuilder();
		for(String line : lines) {
			buf.append(line).append(lineEndToInsert);
		}
		return buf.toString();
	}
	
	public static String readUTF8StreamAsString(InputStream iStream, String lineEndToInsert) throws IOException {
		List<String> lines = readFileAsLines(new BufferedReader(new InputStreamReader(iStream, "UTF-8")));
		StringBuilder buf = new StringBuilder();
		for(String line : lines) {
			buf.append(line).append(lineEndToInsert);
		}
		return buf.toString();
	}
	
	public static String readUTF8FileAsString(File file) throws IOException {
		StringBuilder buf = new StringBuilder();
		BufferedReader reader = new UTF8BufferedFileReader(file);
		char[] cbuf = new char[4096];
		int read = 0;
		while((read = reader.read(cbuf)) != -1) {
			buf.append(cbuf, 0, read);
		}
		reader.close();
		return buf.toString();
	}
	
	public static String readUTF8StreamAsString(InputStream istream) throws IOException {
		StringBuilder buf = new StringBuilder();
		BufferedReader reader = new BufferedReader(new InputStreamReader(istream, "UTF-8"));
		char[] cbuf = new char[4096];
		int read = 0;
		while((read = reader.read(cbuf)) != -1) {
			buf.append(cbuf, 0, read);
		}
		reader.close();
		return buf.toString();
	}

	public static void collectAllFiles(File baseDir, Set<File> fileSet) {
		for(File f : baseDir.listFiles()) {
			if(f.isDirectory()) {
				collectAllFiles(f, fileSet);
			}
			else {
				fileSet.add(f);
			}
		}
	}
	
	public static void collectAllFilePaths(File baseDir, Set<String> filePathSet) {
		for(String path : baseDir.list()) {
			File f = new File(baseDir, path);
			if(f.isDirectory()) {
				collectAllFilePaths(f, filePathSet);
			}
			else {
				filePathSet.add(f.getAbsolutePath());
			}
		}
	}
	
	public static void addFiles(File directory, List<File> fileList, boolean includeSubdirs, Matcher fnameFilter) {
		File[] files = directory.listFiles();
		for(File file : files) {
			if(file.isDirectory()) {
				if(includeSubdirs) {
					addFiles(file, fileList, includeSubdirs, fnameFilter);
				}
			}
			else {
				if(fnameFilter == null || fnameFilter.reset(file.getName()).matches()) {
					fileList.add(file);
				}
			}
		}
	}

	public static void copyFile(File in, File out) throws IOException {
		DataInputStream dis = new DataInputStream(new BufferedInputStream(new FileInputStream(in)));
		DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(out)));
		byte[] buf = new byte[1000000];
		int read = -1;
		while((read = dis.read(buf)) != -1) {
			dos.write(buf, 0, read);
		}
		dos.close();
		dis.close();
	}
	
}
