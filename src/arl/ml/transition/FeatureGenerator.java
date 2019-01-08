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

package arl.ml.transition;

import java.util.Iterator;

import arl.common.meta.Initializable;
import arl.ml.transition.metastate.MetaState;


public interface FeatureGenerator<A extends Object, 
								  S extends Object,
								  MS extends MetaState<S>,
								  FR extends Object> extends Initializable {
		public FR getFeats(A anchor, S state, MS metastate);
		
		// TODO: probably should change this to MS at some point...
		// TODO: describe the purpose of this method
		public void reset(S currentInstance, MetaState<S> metastate);
		
		// TODO: describe the purpose of this method
		public void prime(Iterator<S> trainingInstances);
}
