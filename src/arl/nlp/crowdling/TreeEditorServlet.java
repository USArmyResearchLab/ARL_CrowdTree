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

package arl.nlp.crowdling;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import javax.servlet.*;
import javax.servlet.http.*;

import arl.common.meta.ArgMap;
import arl.common.meta.ArgMapParser;
import arl.common.meta.Initializable;

public class TreeEditorServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	/** Variable name to be passed in for reading in the ArgMap */
	public final static String SERVLET_REF_VARIABLE_NAME = "___SERVLET_REF___";
	
	/** File with <code>ArgMap</code> for servlet */
	public final static String INIT_PARAM_CONFIG_FILE = "SERVLET_ARGMAP_FILE";
	
	public final static String  APP_COMMON_MODULE = "CommonModule",
								APP_GET_MODULE = "GetModule",
								APP_POST_MODULE = "PostModule",
										
								// Intended to prevent multiple failed attempts to create the servlet, etc.						
								APP_LOAD_FAILURE = "___LoadFailure";
	
	@Override
	public void init() throws ServletException {
		System.err.println(this.getClass().getName() + ": init() called");
		ServletContext servletContext = getServletContext();
		try {
			Boolean loadFailure = (Boolean)getServletContext().getAttribute(APP_LOAD_FAILURE);
			if(loadFailure==null||!loadFailure.booleanValue()) {
				// Load the configuration properties
				String servletArgMapFile = getInitParameter(INIT_PARAM_CONFIG_FILE);
				ArgMapParser amp = new ArgMapParser();
				Map<String, Object> variablesMap = new HashMap<String, Object>();
				variablesMap.put(SERVLET_REF_VARIABLE_NAME, this);
				ArgMap args = amp.parseArgMap(null, servletArgMapFile, variablesMap);
				
				Initializable.checkArgNames(args, new HashSet<>(Arrays.asList(APP_COMMON_MODULE, APP_GET_MODULE, APP_POST_MODULE)));
				
				// Reusing the variable name in the ArgMap file as the name of the attribute in the servletcontext
				servletContext.setAttribute(APP_COMMON_MODULE, args.get(APP_COMMON_MODULE));
				servletContext.setAttribute(APP_GET_MODULE, (GetModule)args.get(APP_GET_MODULE));
				servletContext.setAttribute(APP_POST_MODULE, (PostModule)args.get(APP_POST_MODULE));
				
				System.err.println(this.getClass().getName() + " : init() completed successfully");
			}
		} 
		catch(Exception e) {
			System.err.println(this.getClass().getName() + " : init() failure!");
			e.printStackTrace();
			servletContext.setAttribute(APP_LOAD_FAILURE, Boolean.TRUE);
		}
	}
	 
	
	@Override
	public void doGet(HttpServletRequest request,
					  HttpServletResponse response)
							  throws ServletException, IOException {
		GetModule getModule = (GetModule)getServletContext().getAttribute(APP_GET_MODULE);
		getModule.doGet(request, response, this);
	}
	
	@Override
	public void doPost(HttpServletRequest request, 
					   HttpServletResponse response)
			throws ServletException, IOException {
		PostModule postModule = ((PostModule)getServletContext().getAttribute(APP_POST_MODULE));
		postModule.doPost(request, response, this);
	}
	
	@Override
	public void destroy() {
		System.err.println(this.getClass().getName() + " : destroy() called");
		// TODO: may wish to implement something akin to this
		// common_module.destroyModule();
		// get_module.destroyModule();
		// post_module.destroyModule();
		
		// NOTE: Currently, the training thread never gets killed
		//((TreeEditorCommonModule)getServletContext().getAttribute(APP_COMMON_MODULE)).getTrainerThread().interrupt();
	}

}
