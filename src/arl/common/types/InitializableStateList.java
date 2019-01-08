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

package arl.common.types;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import arl.common.io.InstanceReader;
import arl.common.meta.ArgMap;
import arl.common.meta.Initializable;
import arl.common.meta.InitializationException;
import arl.ml.transition.TrainingInstanceFilter;
import arl.ml.transition.state.StateFactory;
import arl.ml.transition.util.TransitionStatics;

public class InitializableStateList<I,S> extends ArrayList<S> implements Initializable {

	private static final long serialVersionUID = 1L;
	
	public final static String PARAM_DATA_FILES = "DataFiles",
			   					PARAM_INSTANCE_READER = "InstanceReader",
			   					PARAM_STATE_FACTORY = "StateFactory",
			   					PARAM_INSTANCE_FILTER = "Filter";
	
	@Override
	public void initialize(ArgMap argMap) throws InitializationException {
		Initializable.checkArgNames(argMap, new HashSet<>(Arrays.asList(PARAM_DATA_FILES, 
																		PARAM_INSTANCE_READER, 
																		PARAM_STATE_FACTORY,
																		PARAM_INSTANCE_FILTER)));
		
		String instancesFiles = argMap.getStringValue(PARAM_DATA_FILES);
		if(instancesFiles != null) {
			StateFactory<I, S> stateFactory	= (StateFactory<I, S>)argMap.get(PARAM_STATE_FACTORY);
			InstanceReader<I> instanceReader = (InstanceReader<I>)argMap.get(PARAM_INSTANCE_READER);
			TrainingInstanceFilter<S> filter = (TrainingInstanceFilter<S>)argMap.get(PARAM_INSTANCE_FILTER);
		
			List<I> instances = TransitionStatics.readInstances(instanceReader, instancesFiles);
			for(I instance : instances) {
				add(stateFactory.createState(instance));
			}
		
			// Filter out instances as necessary
			if(filter != null) {
				filter.filter(listIterator());
			}
		}
	}

}
