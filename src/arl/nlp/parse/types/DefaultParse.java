/*
 * 
 * This file is part of ARL CrowdTree and is subject to the following:
 * 
 * Copyright 2018 U.S. Government and Nhien Phan
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

package arl.nlp.parse.types;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import arl.graph.types.Arc;
import arl.nlp.parse.ParseChangeListener;
import arl.nlp.parse.ParseChangedEvent;

public class DefaultParse implements Parse {

	private static final long serialVersionUID = 1L;

	private Map<String, Object> mProperties;
	private List<ParseChangeListener> mListeners = new ArrayList<>();
	private List<String> mComments = new ArrayList<>(0);
	private DefaultTreeToken mFirstCore;
	
	public DefaultParse(List<? extends MorphoAnchor> anchors) {
		for(MorphoAnchor anchor : anchors) {
			if(anchor instanceof TreeToken && ((TreeToken)anchor).getPrevSurfaceTreeToken() == null) {
				if(mFirstCore != null) throw new RuntimeException("UNEXPECTED CONDITION... CAN'T HAVE TWO FIRST CORES! UNDERLYING CODE BUG");
				mFirstCore = (DefaultTreeToken)anchor;
			}
			addAnchor(anchor);
		}
	}
	
	@Override
	public void addParseChangeListener(ParseChangeListener listener) {
		mListeners.add(listener);
	}

	@Override
	public void removeParseChangeListener(ParseChangeListener listener) {
		mListeners.remove(listener);
	}
	
	public void addAnchor(MorphoAnchor newAnchor) {
		/*if(newAnchor instanceof Affix)
			mAllAffixes.add((StandardAffix)newAnchor);
		mAllAnchors.add(newAnchor);*/
		if(newAnchor instanceof TreeToken) {
			DefaultTreeToken core = (DefaultTreeToken)newAnchor;
			if(core.getNextSurfaceTreeToken() == mFirstCore) {
				mFirstCore = core;
			}
		}
		fireNodeAdded(newAnchor);
	}

	@Override
	public List<DefaultTreeToken> getAllTreeTokens() {
		List<DefaultTreeToken> allCores = new ArrayList<>();
		for(DefaultTreeToken core = mFirstCore; core != null; core = core.getNextSurfaceTreeToken()) {
			allCores.add(core);
		}
		return allCores;
	}

	@Override
	public Affix factorOutSuffix(TreeToken core, int suffixLength) {
		// should check that core belongs to this object
		DefaultTreeToken c = (DefaultTreeToken)core;
		
		String text = c.getCoreText();
		String affixText = text.substring(text.length()-suffixLength);
		
		DefaultAffix suffixAnchor = new DefaultAffix(c, affixText, false);
		c.addSuffix(0, suffixAnchor);
		c.shrinkFromEnd(suffixLength);
		
		addAnchor(suffixAnchor);	
		// fire events
		fireAnchorChanged(c);
		return suffixAnchor;
	}

	@Override
	public Affix factorOutPrefix(TreeToken core, int prefixLength) {
		// should check that core belongs to this object
		DefaultTreeToken c = (DefaultTreeToken)core;
		String text = c.getCoreText();
		String affixText = text.substring(0, prefixLength);
				
		DefaultAffix prefixAnchor = new DefaultAffix(c, affixText, true);
		c.addPrefix(prefixAnchor);
				
		c.shrinkFromStart(prefixLength);		
		addAnchor(prefixAnchor);	
		// fire events
		fireAnchorChanged(c);
		return prefixAnchor;
	}

	@Override
	public void detachEnclitic(TreeToken anchor, int encliticLength) {
		DefaultTreeToken core = (DefaultTreeToken)anchor;
		
		String normText = core.getText();
		
		MutableTreeToken cliticAnchor = new DefaultTreeToken(normText.substring(normText.length()-encliticLength));//core.getIndex()+1
		
		core.shrinkFromEnd(encliticLength);
		
		cliticAnchor.setPrevSurfaceCore(core);
		
		cliticAnchor.setPrevClitic(core);
				
		MutableTreeToken nextSurfaceCore = (MutableTreeToken)core.getNextSurfaceTreeToken();
		MutableTreeToken nextClitic = (MutableTreeToken)core.getNextClitic();
				
		core.setNextSurfaceCore(cliticAnchor);
		core.setNextClitic(cliticAnchor);
				
		cliticAnchor.setNextSurfaceCore(nextSurfaceCore);
		cliticAnchor.setNextClitic(nextClitic);
		
		if(nextSurfaceCore != null) nextSurfaceCore.setPrevSurfaceCore(cliticAnchor);
		if(nextClitic != null) nextClitic.setPrevClitic(cliticAnchor);
				
		addAnchor(cliticAnchor);
		// Set label of anchor to null in case label had been set before (alternatively, one could refuse to label until after clitics are separated)
		if(core.getPos() != null) setLabel(core, null);
		
		// fire events
		fireCliticDetachedChanged(core, false);;
		fireAnchorChanged(core);
		if(nextSurfaceCore != null) fireAnchorChanged(nextSurfaceCore);
		//if(nextCore != null) fireAnchorChanged(nextCore);
		if(nextClitic != null) fireAnchorChanged(nextClitic);
	}
	
	public void removeSuffix(DefaultTreeToken core, DefaultAffix suffix) {
		core.removeSuffix(suffix);
	}
	
	public void removePrefix(DefaultTreeToken core, DefaultAffix prefix) {
		core.removePrefix(prefix);
	}
	
	public void deleteCore(DefaultTreeToken core) {
		if(core.getChildArcs().size() > 0) System.err.println("Error: Removal of non-leaves not yet fully supported!");
		if(mFirstCore == core) mFirstCore = core.getNextSurfaceTreeToken();
		Arc<TreeToken> headArc = core.getHeadArc();
		// invalid way to remove an arc
		if(headArc != null) ((MutableTreeToken)headArc.getHead()).removeChildArc(headArc);
		DefaultTreeToken prevClitic = core.getPrevClitic();
		DefaultTreeToken nextClitic = core.getNextClitic();
		DefaultTreeToken prevSurf = core.getPrevSurfaceTreeToken();
		DefaultTreeToken nextSurf = core.getNextSurfaceTreeToken();
		
		if(prevClitic != null) prevClitic.setNextClitic(nextClitic);
		if(nextClitic != null) nextClitic.setPrevClitic(prevClitic);
		if(prevSurf != null) prevSurf.setNextSurfaceCore(nextSurf);
		if(nextSurf != null) nextSurf.setPrevSurfaceCore(prevSurf);
	}
	
	public void appendPrefix(DefaultTreeToken core, DefaultAffix prefix) {
		addAnchor(prefix);
		core.addPrefixToFront(prefix);
	}
	
	public void appendFinalPrefix(DefaultTreeToken core, DefaultAffix prefix) {
		addAnchor(prefix);
		core.addPrefix(prefix);
	}
	
	
	public void appendInitialSuffix(DefaultTreeToken core, DefaultAffix suffix) {
		addAnchor(suffix);
		core.addSuffix(0, suffix);
	}
	
	public void appendSuffix(DefaultTreeToken core, DefaultAffix suffix) {
		addAnchor(suffix);
		core.addSuffix(suffix);
	}
	
	public void replaceText(DefaultTreeToken core, String newText) {
		core.revise(newText);
	}
	
	public void replaceText(DefaultAffix affix, String newText) {
		affix.revise(newText);
	}
	
	public void deletePrefix(DefaultAffix prefix, DefaultTreeToken core) {
		core.removePrefix(prefix);
	}
	
	@Override
	public void detachProclitic(TreeToken anchor, int procliticLength) {
		DefaultTreeToken core = (DefaultTreeToken)anchor;
		
		String normText = core.getText();
	
		DefaultTreeToken cliticAnchor = new DefaultTreeToken(normText.substring(0, procliticLength));//, core.getIndex()
		core.shrinkFromStart(procliticLength);
		//System.err.println("Post: " + core.getText()+ " " + core.getCoreStart() + "+" + core.getCoreEnd()+"+"+core.getNormMap()[core.getCoreStart()]+"+"+core.getNormMap()[core.getCoreEnd()]);
		
		cliticAnchor.setNextSurfaceCore(core);
		
		cliticAnchor.setNextClitic(core);
		
		MutableTreeToken prevSurfaceCore = (MutableTreeToken)core.getPrevSurfaceTreeToken();
		MutableTreeToken prevClitic = (MutableTreeToken)core.getPrevClitic();
		
		cliticAnchor.setPrevSurfaceCore(prevSurfaceCore);
		cliticAnchor.setPrevClitic(prevClitic);
		
		core.setPrevSurfaceCore(cliticAnchor);
		
		core.setPrevClitic(cliticAnchor);
		
		if(prevSurfaceCore != null) prevSurfaceCore.setNextSurfaceCore(cliticAnchor);
		if(prevClitic!=null) prevClitic.setNextClitic(cliticAnchor);
		
		MutableTreeToken coreToUpdate = core;
		while(coreToUpdate != null) {
			//coreToUpdate.setIndex(coreToUpdate.getIndex()+1);
			coreToUpdate = (MutableTreeToken)coreToUpdate.getNextSurfaceTreeToken();
		}
		if(core == mFirstCore) mFirstCore = cliticAnchor;
		
		addAnchor(cliticAnchor);
		// Set label of anchor to null in case label had been set before (alternatively, one could refuse to label until after clitics are separated)
		if(core.getPos() != null) setLabel(anchor, null);
		
		// fire events
		fireCliticDetachedChanged(core, true);
		fireAnchorChanged(core);
		if(prevSurfaceCore != null) fireAnchorChanged(prevSurfaceCore);
		//if(prevCore != null) fireAnchorChanged(prevCore);
		if(prevClitic!=null) fireAnchorChanged(prevClitic);
	}

	@Override
	public void attach(TreeToken c, TreeToken h, String label, boolean isPreChild) {
		MutableTreeToken child = (MutableTreeToken)c;
		MutableTreeToken head = (MutableTreeToken)h;
		Arc<TreeToken> newArc = new Arc<TreeToken>(child, head, label); 
		if(isPreChild)head.addPreChildArc(newArc);
		else head.addPostChildArc(newArc);
		child.setHeadArc(newArc);
		
		// fire events
		fireAnchorChanged(c);
		fireNodeAttached(c);
	}

	@Override
	public void setLabel(MorphoAnchor anchor, String label) {
		((MutableMorphoAnchor)anchor).setPos(label);
		fireAnchorChanged(anchor);
	}

	@Override
	public void setResumptiveness(MorphoAnchor anchor, int value) {
		((MutableMorphoAnchor)anchor).setProperty(RESUMPTIVE_FLAG, value);
		fireAnchorChanged(anchor);		
	}
	
	protected void fireCliticDetachedChanged(MorphoAnchor node, boolean proclitic) {
		ParseChangedEvent mse = new ParseChangedEvent(this, node);
		for(ParseChangeListener listener : mListeners) {
			listener.cliticDetached(mse, proclitic);
		}
	}
	
	protected void fireAnchorChanged(MorphoAnchor node) {
		ParseChangedEvent mse = new ParseChangedEvent(this, node);
		for(ParseChangeListener listener : mListeners) {
			listener.nodeChanged(mse);
		}
	}
	
	protected void fireNodeAdded(MorphoAnchor node) {
		ParseChangedEvent mse = new ParseChangedEvent(this, node);
		for(ParseChangeListener listener : mListeners) {
			listener.nodeAdded(mse);
		}
	}
	
	protected void fireNodeAttached(MorphoAnchor node) {
		ParseChangedEvent mse = new ParseChangedEvent(this, node);
		for(ParseChangeListener listener : mListeners) {
			listener.nodeAttached(mse);
		}
	}

	@Override
	/**
	 * resolveAffixes indicates whether to work with the 'core text' or the text of the whole tree token
	 */
	public void insertText(TreeToken core, String insertionText, int insertionPoint, boolean resolveAffixes) {
		MorphoAnchor modifiedAnchor = null;
		MorphoAnchor lastAnchor = null;
		DefaultTreeToken score = (DefaultTreeToken)core;
		if(!resolveAffixes) {
			score.setCoreText(new StringBuilder(score.getText()).insert(insertionPoint, insertionText).toString());
			modifiedAnchor = score;
		}
		else {
			int index = 0;
			for(Affix prefix : score.getPrefixes()) {
				String text = prefix.getText();
				int len = text.length();
				for(int i = 0; i < len; i++) {
					index++;
					if(index == insertionPoint) {
						DefaultAffix affix = (DefaultAffix)prefix;
						//System.err.println("Anchor (prefix) text is currently: " + affix.getText());
						affix.revise(new StringBuilder(affix.getText()).insert(i+1, insertionText).toString());
						modifiedAnchor = affix;
						break;
					}
				}
				if(modifiedAnchor != null) break;
			}
			
			
			if(modifiedAnchor == null) {
				lastAnchor = score;
				String text = score.getCoreText();
				int len = text.length();
				for(int i = 0; i < len; i++) {
					index++;
					if(index == insertionPoint) {
						//System.err.println("Anchor (core) text is currently: " + Buckwalter.arabicToBuck(score.getCoreText()) + " of length " + score.getText().length());
						//System.err.println("Adding: " + insertionText + " at i="+i);
						String newText = new StringBuilder(score.getCoreText()).insert(i+1, insertionText).toString();
						//System.err.println("New text: " + Buckwalter.arabicToBuck(newText) + " of length: " + newText.length());
						score.revise(newText);
						modifiedAnchor = score;
						break;
					}
				}
			}
			
			if(modifiedAnchor == null) {
				for(Affix suffix : score.getSuffixes()) {
					lastAnchor = suffix;
					String text = suffix.getText();
					int len = text.length();
					for(int i = 0; i < len; i++) {
						index++;
						if(index == insertionPoint) {
							DefaultAffix affix = (DefaultAffix)suffix;
							//System.err.println("Anchor (suffix) text is currently: " + affix.getText());
							affix.revise(new StringBuilder(affix.getText()).insert(i+1, insertionText).toString());
							modifiedAnchor = affix;
							break;
						}
					}
					if(modifiedAnchor != null) break;
				}
			}
		}
		/*if(modifiedAnchor == null && insertionPoint == score.getFullLength()){
			if(lastAnchor instanceof Affix) {
				StandardAffix affix = (StandardAffix)suffix;
				System.err.println("Anchor text is currently: " + affix.getText());
				affix.revise(new StringBuilder(affix.getText()).insert(i, insertionText).toString());
			}
		}*/
		//System.err.println("Anchor text is now: " + modifiedAnchor.getText());
		fireAnchorChanged(modifiedAnchor);
	}

	
	@Override
	public Object getProperty(String key) {
		return mProperties==null?null:mProperties.get(key);
	}

	@Override
	public void setProperty(String key, Object value) {
		if(mProperties == null) {
			mProperties = new LinkedHashMap<>();
		}
		mProperties.put(key, value);
	}
	
	@Override
	public Set<String> getPropertyKeys() {
		return mProperties==null?new HashSet<>():mProperties.keySet();
	}

	@Override
	public void addComment(String comment) {
		mComments.add(comment);
	}

	@Override
	public void deleteComment(String comment) {
		mComments.remove(comment);
	}

	@Override
	public Collection<String> getComments() {
		return mComments;
	}
}
