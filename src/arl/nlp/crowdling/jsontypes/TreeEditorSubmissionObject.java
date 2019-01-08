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
public class TreeEditorSubmissionObject implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/** Assignment ID */
	private String assignmentId;
	/** HIT ID */
	private String hitId;
	/** Worker ID */
	private String workerId;
	
	/** Browser (e.g., firefox) */
	private String browser;
	/** Screen height */
	private String screenHeight;
	/** Screen width */
	private String screenWidth;
	
	/** Root of the user annotated version of the parse tree */
	private D3TreeNodeWrapper root;
	/** CoNLL-U comment lines */
	private List<String> comments;
	/** Copy of the original sentence sent by the servlet to the client*/
	private Sentence originalSentence;
	/** User-provided notes */
	private String notes;
	
	
	public String getAssignmentId() {
		return assignmentId;
	}
	public void setAssignmentId(String id) {
		this.assignmentId = id;
	}
	
	public String getWorkerId() {
		return workerId;
	}
	public void setWorkerId(String id) {
		this.workerId = id;
	}
	
	public String getHitId() {
		return hitId;
	}
	public void setHitId(String id) {
		this.hitId = id;
	}
	
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	
	public String getBrowser() {
		return browser;
	}
	public void setBrowser(String browser) {
		this.browser = browser;
	}
	
	public String getScreenHeight() {
		return screenHeight;
	}
	public void setScreenHeight(String screenHeight) {
		this.screenHeight = screenHeight;
	}
	
	public String getScreenWidth() {
		return screenWidth;
	}
	public void setScreenWidth(String screenWidth) {
		this.screenWidth = screenWidth;
	}
	
	public D3TreeNodeWrapper getRoot() {
		return this.root;
	}
	public void setRoot(D3TreeNodeWrapper root) {
		this.root = root;
	}
	
	public Sentence getOriginalSentence() {
		return this.originalSentence;
	}
	public void setOriginalSentence(Sentence sentence) {
		this.originalSentence = sentence;
	}
	
	public List<String> getComments() {
		return this.comments;
	}
	public void setComments(List<String> comments) {
		this.comments = comments;
	}
	
}
