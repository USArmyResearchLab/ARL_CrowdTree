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
 * Java bean sentence representation.
 * For JSON I/O using Jackson or similar library
 */
public class Sentence implements Serializable {
	
		private static final long serialVersionUID = 1L;

		/** Sentence as a String */		 
		private String fulltext;
		
		/** Sentence ID */
		private String sid;
		
		/** Text direction (e.g., left-to-right (ltr); right-to-left (rtl)) */
		private String dir; 
		
		/** The root node of the sentence */
		private TreeNode root;
		
		/** CoNLL-U comments */
		private List<String> comments;
	
		public void setRoot(TreeNode root) {
			this.root = root;
		}
		public TreeNode getRoot() {
			return this.root;
		}
		
		public String getSid() {
			return sid;
		}
		public void setSid(String sid) {
			this.sid = sid;
		}
		
		public String getFulltext() {
			return fulltext;
		}
		public void setFulltext(String fulltext) {
			this.fulltext = fulltext;
		}
		
		public String getDir() {
			return dir;
		}
		public void setDir(String dir) {
			this.dir = dir;
		}
		
		public List<String> getComments() {
			return this.comments;
		}
		public void setComments(List<String> comments) {
			this.comments = comments;
		}
	
}
