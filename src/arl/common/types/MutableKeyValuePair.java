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

public class MutableKeyValuePair<K, V> extends KeyValuePair<K, V>{

	private static final long serialVersionUID = 1L;
	

	public MutableKeyValuePair(K key, V value) {
		super(key, value);
	}
	
	@Override
	public void setKey(K newKey) {
		super.setKey(newKey);
	}
	
	@Override
	public void setValue(V newValue) {
		super.setValue(newValue);
	}
	
}
