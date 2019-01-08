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
import java.util.Map;

/**
 * Java bean sentence representation.
 * For JSON I/O using Jackson or similar library
 */
public class TreeNode implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/** Node text */
	private String  text;
	/** Node ID */
	private String  oid;
	/** Part-of-speech */
	private String  pos;
	/** Part-of-speech 2 */
	private String  pos2;
	/** Lemma */
	private String  lemma;
	/** Lexical root (e.g., Arabic triconsonantal root) */
	private String  lroot;
	/** Gloss */
	private String  gloss;
	/** Dependency arc label */
	private String  dep; 
	/** For use with chains of clitics (as with Arabic) */
	private String  prevCliticId; 
	/** For chains of clitics (as with Arabic) */
	private String  nextCliticId; 
	
	/** Marker to treat as punctuation */
	private Boolean punct;
	/** Indicator for whether it is followed by whitespace */
	private Boolean noSpaceAfter;
	
	/** Original parent node ID */
	private String pid; 
	
	/** Intended to hold CoNLL-U FEATS morphology features */
	private List<KeyValuePair> feats;
	/** Intended to hold CoNLL-U MISC features */
	private List<KeyValuePair> misc;
	
	// NOTE: Currently only used to send data *to* the JS client... see also D3TreeNode.children
	private List<TreeNode> children;
	
	// Specifies the SVG shape type (e.g., circle, rect, polygon, path...)
	private String shapeType;
	// Specifies the shape CSS class
	private String shapeClass;
	// Specified the SVG shape attributes (e.g., r (radius of circle), points (of polygon), ...)
	private Map<String, String> shapeAttrs;
	
	public void setPrevCliticId(String id) {
		prevCliticId = id;
	}
	public String getPrevCliticId() {
		return prevCliticId;
	}
	public void setNextCliticId(String id) {
		nextCliticId = id;
	}
	public String getNextCliticId() {
		return nextCliticId;
	}
	
	public void setPunct(Boolean punct) {
		this.punct = punct;
	}
	public Boolean getPunct() {
		return punct;
	}
	
	public void setNoSpaceAfter(Boolean noSpaceAfter) {
		this.noSpaceAfter = noSpaceAfter;
	}
	public Boolean getNoSpaceAfter() {
		return this.noSpaceAfter;
	}
	
	public String getPos() {
		return pos;
	}
	public void setPos(String pos) {
		this.pos = pos;
	}
	
	public String getPos2() {
		return pos2;
	}
	public void setPos2(String pos2) {
		this.pos2 = pos2;
	}
	
	public String getLemma() {
		return lemma;
	}
	public void setLemma(String lemma) {
		this.lemma = lemma;
	}
	
	public String getLroot() {
		return lroot;
	}
	public void setLroot(String lroot) {
		this.lroot = lroot;
	}
	
	public String getGloss() {
		return gloss;
	}
	public void setGloss(String g) {
		this.gloss = g;
	}
	
	public String getDep() {
		return dep;
	}
	public void setDep(String dep) {
		this.dep = dep;
	}
	
	public List<TreeNode> getChildren() {
		return children;
	}
	public void setChildren(List<TreeNode> children) {
		this.children = children;
	}
	
	public List<KeyValuePair> getFeats() {
		return feats;
	}
	public void setFeats(List<KeyValuePair> feats) {
		this.feats = feats;
	}
	
	public List<KeyValuePair> getMisc() {
		return misc;
	}
	public void setMisc(List<KeyValuePair> misc) {
		this.misc = misc;
	}
	
	public String getOid() {
		return oid;
	}
	public void setOid(String oid) {
		this.oid = oid;
	}
	
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	
	
	public String getShapeType() {
		return shapeType;
	}
	public void setShapeType(String shapeType) {
		this.shapeType = shapeType;
	}
	
	public String getShapeClass() {
		return shapeClass;
	}
	public void setShapeClass(String shapeClass) {
		this.shapeClass = shapeClass;
	}
	
	public Map<String,String> getShapeAttrs() {
		return this.shapeAttrs;
	}
	public void setShapeAttrs(Map<String, String> shapeAttrs) {
		this.shapeAttrs = shapeAttrs;
	}
	
}
