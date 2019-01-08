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

package arl.common.meta;

import java.util.HashSet;
import java.util.Set;

public interface Initializable {

	void initialize(ArgMap argMap) throws InitializationException;
	
	public static void checkArgNames(ArgMap argMap, Set<String> validArgNames) throws InitializationException {
		if(argMap != null) {
			for(String argName : argMap.keySet()) {
				if(!validArgNames.contains(argName)) {
					throw new InitializationException("Unexpected argument name: " + argName);
				}
			}
		}
	}
	
	public static void requireArgs(ArgMap argMap, Set<String> requiredArgNames) throws InitializationException {
		if(argMap == null && requiredArgNames.size() > 0) {
			throw new InitializationException("Missing arguments: " + String.join(", ", requiredArgNames));
		}
		else if(argMap != null) {
			Set<String> reqArgsCopy = new HashSet<>(requiredArgNames);
			reqArgsCopy.removeAll(argMap.keySet());
			if(reqArgsCopy.size() > 0) {
				throw new InitializationException("Missing arguments: " + String.join(", ", reqArgsCopy));
			}
		}
	}
	
}
