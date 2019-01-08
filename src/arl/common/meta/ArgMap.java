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

package arl.common.meta;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ArgMap extends HashMap<String, Object> implements Initializable {
	
	private static final long serialVersionUID = 1L;
	
	public ArgMap() {
		
	}
	
	@Override
	public void initialize(ArgMap argMap) throws InitializationException {
		putAll(argMap);
	}
	
	public ArgMap(Object[][] argToValue) {
		for(Object[] pair : argToValue) {
			if(pair.length != 2) {
				throw new IllegalArgumentException("All Object[][] arrays must have 2 as the second dimension");
			}
			if(!(pair[0] instanceof String)) {
				throw new IllegalArgumentException("First entry in each Object[] pair must be a String");
			}
			put((String)pair[0], pair[1]);
		}
	}
	
	public ArgMap(Map<String, Object> argToValue) {
		super(argToValue);
	}
	
	public String getStringValue(String argument, String defaultValue) {
		String value = (String)get(argument);
		return value == null ? defaultValue : value;
	}
	
	public String getStringValue(String argument) {
		return getStringValue(argument, null);
	}
	
	public File getFile(String argument) {
		return new File(getStringValue(argument));
	}
	
	public File getFile(String argument, File defaultValue) {
		String fileString = getStringValue(argument);
		return fileString==null?defaultValue:new File(fileString);
	}
	
	public double getDoubleValue(String argument, double defaultValue) {
		String value = (String)get(argument);
		return value == null ? defaultValue : Double.parseDouble(value);
	}
	
	public double getDoubleValue(String argument) {
		return getDoubleValue(argument, 0);
	}
	
	public Integer getIntegerValue(String argument, Integer defaultValue) {
		String value = (String)get(argument);
		return value == null ? defaultValue : Integer.parseInt(value);
	}
	
	public Integer getIntegerValue(String argument) {
		return getIntegerValue(argument, null);
	}
	
	public Boolean getBooleanValue(String argument) {
		return getBooleanValue(argument, null);
	}
	
	public Boolean getBooleanValue(String argument, Boolean defaultValue) {
		String value = (String)get(argument);
		return value == null ? defaultValue : Boolean.parseBoolean(value);
	}

	
	
}
