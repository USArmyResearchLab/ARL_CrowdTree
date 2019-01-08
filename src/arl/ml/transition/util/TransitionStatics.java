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

package arl.ml.transition.util;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import arl.common.io.InstanceReader;
import arl.common.io.MultiFileInstanceIterator;
import arl.common.meta.InitializationException;

public class TransitionStatics {
	
	public static <I extends Object> List<I> readInstances(InstanceReader<I> instanceReader, String trainingFiles) throws InitializationException {
		List<I> instances = new LinkedList<>();
		int numInstances = 0;
		for(MultiFileInstanceIterator<I> mfir = new MultiFileInstanceIterator<>(instanceReader, 
				trainingFiles.split(File.pathSeparator)); mfir.hasNext();) {
			instances.add(mfir.next());
			numInstances++;
			if(numInstances % 10000 == 0) {
				System.err.println(numInstances + " instances read");
			}
		}
		return instances;
	}
	
}
