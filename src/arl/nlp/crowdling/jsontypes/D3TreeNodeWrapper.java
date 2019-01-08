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

package arl.nlp.crowdling.jsontypes;

import java.io.Serializable;
import java.util.List;

/**
 * Version 4 of D3 wraps the provided tree structure in new objects. This 
 * represents a node wrapper. 
 * 
 * For JSON I/O using Jackson or similar library
 */
public class D3TreeNodeWrapper implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private TreeNode data;
	private List<D3TreeNodeWrapper> children;
	
	public List<D3TreeNodeWrapper> getChildren() {
		return children;
	}
	public void setChildren(List<D3TreeNodeWrapper> children) {
		this.children = children;
	}
	
	public void setData(TreeNode data) {
		this.data = data;
	}
	public TreeNode getData() {
		return this.data;
	}
	
}
