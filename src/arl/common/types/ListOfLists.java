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

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/**
 * Collection that enables multiple lists to be iterated over as a single list without copying
 * the elements of each sublist into a master array or other structure.
 */
public class ListOfLists<E> implements List<E> {

	private List<List<? extends E>> mLists;
	
	public ListOfLists() {
		mLists = new LinkedList<List<? extends E>>();
	}
	
	public ListOfLists(List<List<? extends E>> lists) {
		mLists = lists;
	}
	
	/*public void addList(List<? extends E> list) {
		if(list != null) mLists.add(list);
	}*/

	@Override
	public int size() {
		int totalSize = 0;
		for(ListIterator<List<? extends E>> listIt = mLists.listIterator(); listIt.hasNext();) {
			totalSize += listIt.next().size();
		}
		return totalSize;
	}

	@Override
	public boolean isEmpty() {
		return size()==0;
	}

	@Override
	public boolean contains(Object o) {
		boolean found = false;
		for(ListIterator<List<? extends E>> listIt = mLists.listIterator(); listIt.hasNext();) {
			if(listIt.next()==o) {
				found = true;
				break;
			}
		}
		return found;
	}

	@Override
	public Iterator<E> iterator() {
		return listIterator();
	}

	@Override
	public Object[] toArray() {
		LinkedList<Object> list = new LinkedList<>();
		for(List<? extends E> x : mLists) list.addAll(x);
		return list.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		throw new UnsupportedOperationException("Not supported");
	}

	@Override
	public boolean add(E e) {
		throw new UnsupportedOperationException("Not supported");
	}

	@Override
	public boolean remove(Object o) {
		throw new UnsupportedOperationException("Not supported");
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		throw new UnsupportedOperationException("Not supported");
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		if(c instanceof List) {
			mLists.add((List<? extends E>)c);
			return true;
		}
		else {
			throw new IllegalArgumentException(ListOfLists.class.getName() + " only accepts non-null <code>List</code>s.");
		}
	}

	@Override
	public boolean addAll(int index, Collection<? extends E> c) {
		throw new UnsupportedOperationException("Not supported");
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		throw new UnsupportedOperationException("Not supported");
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		throw new UnsupportedOperationException("Not supported");
	}

	@Override
	public void clear() {
		mLists.clear();
	}

	@Override
	public E get(int index) {
		throw new UnsupportedOperationException("Not supported");
	}

	@Override
	public E set(int index, E element) {
		throw new UnsupportedOperationException("Not supported");
	}

	@Override
	public void add(int index, E element) {
		throw new UnsupportedOperationException("Not supported");
	}

	@Override
	public E remove(int index) {
		throw new UnsupportedOperationException("Not supported");
	}

	@Override
	public int indexOf(Object o) {
		throw new UnsupportedOperationException("Not supported");
	}

	@Override
	public int lastIndexOf(Object o) {
		throw new UnsupportedOperationException("Not supported");
	}

	@Override
	public ListIterator<E> listIterator() {
		return new ListIterator<E>() {

			ListIterator<List<? extends E>> mListIt = mLists.listIterator();
			ListIterator<? extends E> mCurrentIterator = null;
			
			@Override
			public boolean hasNext() {
				while(mListIt.hasNext() && (mCurrentIterator == null || !mCurrentIterator.hasNext())) {
					mCurrentIterator = mListIt.next().listIterator();
				}
				return (mCurrentIterator != null && mCurrentIterator.hasNext());
			}

			@Override
			public E next() {
				return mCurrentIterator.next();
			}

			@Override
			public boolean hasPrevious() {
				throw new UnsupportedOperationException("Not implemented");
			}

			@Override
			public E previous() {
				throw new UnsupportedOperationException("Not implemented");
			}

			@Override
			public int nextIndex() {
				throw new UnsupportedOperationException("Not implemented");
			}

			@Override
			public int previousIndex() {
				throw new UnsupportedOperationException("Not implemented");
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException("Not implemented");
			}

			@Override
			public void set(E e) {
				throw new UnsupportedOperationException("Not implemented");
				
			}

			@Override
			public void add(E e) {
				throw new UnsupportedOperationException("Not implemented");
			}
		};
	}

	@Override
	public ListIterator<E> listIterator(int index) {
		throw new UnsupportedOperationException("Not supported");
	}

	@Override
	public List<E> subList(int fromIndex, int toIndex) {
		throw new UnsupportedOperationException("Not supported");
	}

}
