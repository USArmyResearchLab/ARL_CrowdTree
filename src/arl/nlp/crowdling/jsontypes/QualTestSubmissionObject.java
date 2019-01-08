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

public class QualTestSubmissionObject implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/** Assignment ID */
	private String assignmentId;
	/** HIT ID */
	private String hitId;
	/** Worker ID */
	private String workerId;
	/** Notes */
	private String notes;
	/** The list of submitted sentences */
	private List<QualTestSentence> sentences;
	
	public List<QualTestSentence> getSentences() {
		return sentences;
	}
	public void setSentences(List<QualTestSentence> sentences) {
		this.sentences = sentences;
	}

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
	
}
