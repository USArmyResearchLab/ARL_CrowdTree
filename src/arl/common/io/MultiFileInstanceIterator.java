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

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;

import arl.common.InitializableIterator;
import arl.common.meta.ArgMap;
import arl.common.meta.Initializable;
import arl.common.meta.InitializationException;

public class MultiFileInstanceIterator<I extends Object> implements InitializableIterator<I>{

	//public final static String DEFAULT_ENCODING = Encodings.UTF8;
	
	public final static String INSTANCE_READER = "InstanceReader";
	public final static String FILE_LIST = "FileList";
	
	private InstanceReader<? extends I> mInstanceReader;
	private InputStream mInputStream;
	private I mNextInstance;
	private String[] mFiles;
	private int mCurrentFileIndex;
	
	//private String mEncoding;
	
	public MultiFileInstanceIterator() {
		
	}
	
	public MultiFileInstanceIterator(InstanceReader<? extends I> ireader,
			 String[] inputFiles) throws InitializationException {
		ArgMap argMap = new ArgMap();
		argMap.put(FILE_LIST, Arrays.asList(inputFiles));
		argMap.put(INSTANCE_READER, ireader);
		initialize(argMap);
		//mEncoding = encoding;
	}
	
	@Override
	public void initialize(ArgMap argMap) throws InitializationException {
		Initializable.checkArgNames(argMap, new HashSet<>(Arrays.asList(INSTANCE_READER, FILE_LIST)));
		mInstanceReader = (InstanceReader<? extends I>)argMap.get(INSTANCE_READER);
		mFiles = ((List<String>)argMap.get(FILE_LIST)).toArray(new String[0]);
	}
	
	@Override
	public boolean hasNext() {
		if(mNextInstance != null) {
			return true;
		}
		else {
			try {
				boolean result = advanceIfNecessary();
				if(result == false) return false;
				
			}
			catch(Exception e) {
				throw new RuntimeException(e);
			}
			return mNextInstance != null;
		}
	}
	
	private boolean advanceIfNecessary() throws Exception {
		boolean result = true;
		if(mInputStream == null) {
			if(mCurrentFileIndex == mFiles.length) {
				return false;
			}
			else {
				//mBufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(mFiles[mCurrentFileIndex]), mEncoding));
				mInputStream = new FileInputStream(mFiles[mCurrentFileIndex]);
				mInstanceReader.setSource(mInputStream);
			}
		}
		while(mNextInstance == null) {
			mNextInstance = mInstanceReader.readInstance();
			if(mNextInstance == null) {
				mInputStream.close();
				mInputStream = null;
				mCurrentFileIndex++;
				if(mCurrentFileIndex == mFiles.length) {
					return result;
				}
				else {
					mInputStream = new FileInputStream(mFiles[mCurrentFileIndex]);//, mEncoding))
				}	
			}
		}
		return result;
	}

	@Override
	public I next() {
		try {
			advanceIfNecessary();
		} 
		catch (Exception e) {
			throw new RuntimeException(e);
		}
		I result = mNextInstance;
		mNextInstance = null;
		if(result == null) {
			throw new NoSuchElementException();
		}
		return result;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}
	
	public void close() throws IOException {
		//if(mBufferedReader != null) mBufferedReader.close();
		if(mInputStream != null) mInputStream.close();
	}

}
