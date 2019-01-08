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
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import arl.common.meta.ArgMapOptions.Option;
import arl.common.types.IntHolder;
import arl.common.util.IOUtils;

public class ArgMapParser {
	
	public final static String PARAM_ASSIGNMENT_OPERATOR = "=";
	
	public ArgMap parseArgMap(String argMapFilePath) throws IOException, ArgMapParsingException {
		return parseArgMap(null, argMapFilePath, new HashMap<>());
	}
	public ArgMap parseArgMap(ArgMapOptions options, String argMapFilePath, Map<String, Object> variablesMap) throws IOException, ArgMapParsingException {
		List<String> lines = IOUtils.readUTF8FileAsLines(new File(argMapFilePath));
		// Ignore lines with comments
		for(ListIterator<String> li = lines.listIterator(); li.hasNext();) {
			String nextString = li.next();
			if(nextString.trim().startsWith("#")) {
				li.remove();
			}
		}
		String[] stringArgs = String.join(" ", lines).replaceAll("\\s+",  " ").trim().split(" ");
		return parseOptions(options, stringArgs, variablesMap);
	}

	public ArgMap parseOptions(String[] args) throws ArgMapParsingException {
		return parseOptions(null, args, new HashMap<>());
	}
	
	public ArgMap parseOptions(ArgMapOptions options, 
			   String[] args) throws ArgMapParsingException {
		return parseOptions(options, args, new HashMap<>());
	}
	
	public ArgMap parseOptions(ArgMapOptions options, 
							   String[] args,
							   Map<String, Object> variables) throws ArgMapParsingException {
		List<Option> optionsList = options == null ? null : options.getOptions();
		ArgMap argMap = null;
		try {
			argMap = getArgMap(null, options, optionsList, args, new IntHolder(0), 1, variables);
		}
		catch(Exception e) {
			throw new ArgMapParsingException(e);
		}
		return argMap;
	}
	
	/**
	 * 
	 * @param targetInstance -- the instance for which the ArgMap is being created; this is pass in solely so that members of collections
	 * 					  may be added (may be null)
	 * @param clo
	 * @param optionsList
	 * @param args
	 * @param index
	 * @param currentIndentationLevel
	 * @return
	 * @throws Exception
	 */
	public ArgMap getArgMap(Object targetInstance,
							ArgMapOptions clo, 
							List<Option> optionsList, 
							String[] args, 
							IntHolder index, 
							int currentIndentationLevel,
							Map<String, Object> variables) throws Exception {
		ArgMap argMap = new ArgMap();
		for(; index.value < args.length; index.value+=2) {
			String argumentIdentifier = args[index.value];
			int indentationLevel = 0;
			for(int c = 0; c < argumentIdentifier.length(); c++) {
				if(argumentIdentifier.charAt(c) == '-') {
					indentationLevel++;
				}
				else {
					break;
				}
			}
			if(indentationLevel < currentIndentationLevel) {
				// overshot end of argument list for current indentation level, move cursor back 2 places and go back
				index.value-=2; 
				break;
			}
			if(indentationLevel - currentIndentationLevel > 1) {
				throw new ArgMapParsingException("unexpected jump in indentation level: " + argumentIdentifier);
			}
			if(!argumentIdentifier.startsWith("-") || argumentIdentifier.length() <= 1) {
				throw new ArgMapParsingException("unexpected argument name: " + argumentIdentifier);
			}
			else {
				String argIdName = argumentIdentifier.substring(indentationLevel);

				boolean isInstantiation = false,				
						isListElement = false,
						isVariableDefinition = false,
						isVariableLookup = false;
				
				// Variable definitions begin with >
				if(argIdName.startsWith(">")) {
					if(currentIndentationLevel > 1) {
						// Note: there is no indentation level 0
						throw new ArgMapParsingException("Variable definitions currently only supported at indentation level 1: " + argumentIdentifier);
					}
					isVariableDefinition = true;
					argIdName = argIdName.substring(1);
				}
				else {
					// List elements begin with _
					// Note: list elements may also be variable lookups OR instantiations
					// Note: variable definitions may NOT also be list elements
					if(argIdName.startsWith("_")) {
						isListElement = true;
						argIdName = argIdName.substring(1);
					}
				}
				
				// Variable lookups begin with ^
				if(argIdName.startsWith("^")) {
					isVariableLookup = true;
					argIdName = argIdName.substring(1);
				}
				// Object instantiations begin with +
				// Note: a variable lookup may not also be an instantiation (the variable is just being looked up)
				else if(argIdName.startsWith("+")) {
					isInstantiation = true;
					argIdName = argIdName.substring(1);
				}
				
				// Check against the list of accepted arguments (if provided)
				// No checking for list elements
				if(optionsList != null && !isListElement && !isVariableDefinition) {
					boolean matched = false;
					for(Option opt : optionsList) {
						if(opt.getName().equals(argIdName)) {
							matched = true;
							break;
						}
					}
					if(!matched) {
						throw new ArgMapParsingException("unexpected argument name: " + argumentIdentifier + "\n" + clo.getArgumentsDescriptionString());
					}
				}
				
				Object argValue = args[index.value+1];
				// Variable definition lookup
				if(isVariableLookup) {
					if(isInstantiation) {
						throw new ArgMapParsingException("Variable references (i.e., " + args[index.value+1] + " ) may not be marked as instantiations (+)");
					}
					argValue = variables.get(args[index.value+1]);
				}
				// (May not be both a lookup and an object)
				else if(isInstantiation) {
					Object newObject = null;
					try {
						newObject = Class.forName((String)argValue).newInstance();
					}
					catch(Exception e) {
						System.err.println("Failed to instantiate class: " + argValue);
						throw new RuntimeException(e);
					}
					if(newObject instanceof Initializable) {
						Initializable instance = (Initializable)newObject;
						index.value+=2; // move forward for next set of arguments
						ArgMap subArgMap = getArgMap(instance, clo, null, args, index, currentIndentationLevel+1, variables);
						instance.initialize(subArgMap);
					}
					else if (newObject instanceof Collection) {
						Collection instance = (Collection)newObject;
						index.value+=2; // move forward for next set of arguments
						getArgMap(instance, clo, null, args, index, currentIndentationLevel+1, variables);
					}
					argValue = newObject;
				}
				if(isListElement) {
					if(!(targetInstance instanceof Collection)) {
						System.err.println("TYPE:" + targetInstance.getClass());
						throw new ArgMapParsingException("Prior element isn't a java.util.Collection. Can't add an element.");
					}
					else {
						((Collection)targetInstance).add(argValue);
					}
				}
				else {
					if(targetInstance != null && !(targetInstance instanceof Initializable)) {
						throw new ArgMapParsingException(targetInstance.getClass().getName() + " isn't an Initializable; therefore, can't take arguments or have its own ArgMap.");
					}
					else {
						if(isVariableDefinition) {
							//System.err.println("Setting variable definition for : " + argIdName);
							variables.put(argIdName, argValue);
						}
						else {
							argMap.put(argIdName, argValue);
						}
					}
				}
			}
		}
		return argMap;
	}
	
	public static ArgMap parseStandardOptionsInput(String optionsInput) {
		return parseStandardOptionsInput(optionsInput, ":");//File.pathSeparator.equals(":")?";":":");
	}
	
	public static ArgMap parseStandardOptionsInput(String optionsInput, String delimiter) {
		ArgMap optionsMap = new ArgMap();
		if(optionsInput != null && !optionsInput.trim().equals("")) {
			String[] optionsArray = optionsInput.split(delimiter);
			for(int i = 0; i < optionsArray.length; i++) {
				String option = optionsArray[i];
				int equalsSign = option.indexOf(PARAM_ASSIGNMENT_OPERATOR);
				optionsMap.put(option.substring(0, equalsSign), 
								option.substring(equalsSign+1));
			}
		}
		return optionsMap;
	}
	
}
