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

public class ExecuteExecutable {

	public final static String OPT_EXECUTABLE = "Executable";
	
	private static ArgMapOptions createCommandLineOptions() {
		return new ArgMapOptions(new String[][] {
						{OPT_EXECUTABLE,		"instance", 	"instance of a class that implements " + Executable.class.getName()},
					});
	}
	
	public static void main(String[] args) throws Exception {
		// Print the arguments (sanity check)
		for(String arg : args) {
			System.err.println(arg);
		}
		ArgMap params = new ArgMapParser().parseOptions(createCommandLineOptions(), args);	
		Executable executable = (Executable)params.get(OPT_EXECUTABLE);
		executable.execute();
	}
	
}
