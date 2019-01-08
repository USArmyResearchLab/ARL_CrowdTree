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

import java.util.ArrayList;
import java.util.List;

public class ArgMapOptions {
	
	public static class Option {
		private String mName;
		private String mType;
		private String mDescription;
		
		public Option(String name, String type, String description) {
			mName = name;
			mType = type;
			mDescription = description;
		}
		
		public String getName() {
			return mName;
		}
		
		public String getType() {
			return mType;
		}
		
		public String getDescription() {
			return mDescription;
		}
	}
	
	private List<Option> mOptions = new ArrayList<>();
	
	public ArgMapOptions() {
		
	}
	
	public ArgMapOptions(String[][] options) {
		add(options);
	}
	
	public ArgMapOptions add(String name, String type, String description) {
		mOptions.add(new Option(name, type, description));
		return this;
	}
	
	public void add(String[][] options) {
		for(String[] option : options) {
			if(option.length != 3) {
				throw new IllegalArgumentException("Option array must contain three Strings: name, type, description");
			}
			add(option[0], option[1], option[2]);
		}
	}
	
	public List<Option> getOptions() {
		return new ArrayList<>(mOptions);
	}
	
	public String getArgumentsDescriptionString() {
		StringBuilder buf = new StringBuilder();
		for(Option opt : mOptions) {
			buf.append(opt.getName() + "\t<" + opt.getType() + ">\t" + opt.getDescription()).append("\n");
		}
		return buf.toString();
	}
	
}
