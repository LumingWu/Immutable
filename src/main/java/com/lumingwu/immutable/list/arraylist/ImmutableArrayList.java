/*
 * Copyright 2020 Luming Wu. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file except in compliance with the License. A copy of the License is located at
 *
 * https://github.com/LumingWu/ImmutableList/blob/master/LICENSE
 *
 * or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions
 * and limitations under the License.
 */

package com.lumingwu.immutable.list.arraylist;

import com.lumingwu.immutable.list.ImmutableList;
import com.lumingwu.immutable.list.ImmutableListBatchResult;
import com.lumingwu.immutable.list.ImmutableListResult;
import com.lumingwu.immutable.list.ImmutableListStatusResult;

import java.util.*;
import java.util.function.UnaryOperator;

public class ImmutableArrayList<E> implements ImmutableList<E>, RandomAccess {

    private int branchFactor;
    private int capacity;
    private int size;
    private Object[] list;

    public ImmutableArrayList(int branchFactor) {
        this(branchFactor, (int)Math.pow(2, branchFactor), 0);
        this.branchFactor = branchFactor;
    }

    private ImmutableArrayList(ImmutableArrayList<E> immutableArrayList, int newSize) {
        this(immutableArrayList.branchFactor, immutableArrayList.capacity, newSize);
        list = immutableArrayList.list.clone();
    }

    private ImmutableArrayList(int branchFactor, int capacity, int size) {
        if(branchFactor < 1) {
            throw new IllegalArgumentException("Branch factor is less than 1.");
        }
        if(branchFactor > 30) {
            throw new IllegalArgumentException("Branch factor is larger than 30.");
        }

        this.branchFactor = branchFactor;
        this.capacity = capacity;
        this.size = size;

        list = new Object[branchFactor];
    }

    @Override
    public ImmutableList<E> add(E item) {
        return null;
    }

    @Override
    public ImmutableList<E> add(int index, E item) {
        return null;
    }

    @Override
    public ImmutableList<E> addAll(Collection<? extends E> collection) {
        return null;
    }

    @Override
    public ImmutableList<E> addAll(int index, Collection<? extends E> collection) {
        return null;
    }

    @Override
    public ImmutableList<E> addAll(ImmutableList<? extends E> immutableList) {
        return null;
    }

    @Override
    public ImmutableList<E> addAll(int index, ImmutableList<? extends E> immutableList) {
        return null;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public E get(int index) {
        if(index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        ImmutableArrayList<E> searchList = this;
        while(index > capacity) {
            int nextSearchListCapacity = searchList.capacity >> branchFactor;
            int nextSearchListIndex = index / nextSearchListCapacity;
            searchList = (ImmutableArrayList<E>)searchList.list[nextSearchListIndex];
            index = index - nextSearchListIndex * nextSearchListCapacity;
        }
        return (E)searchList.list[index];
    }

    @Override
    public int indexOf(Object object) {
        return 0;
    }

    /**
     * Create a new ImmutableArrayList that added zero or more {@items} starting at {@index}. All original items starting at
     * {@index} will shift right to make space.
     *
     * @param index - index at which the specified element is to be inserted
     * @param collection - a collection of items to be added at the index
     *
     * @return a reference of the new ImmutableArrayList represents the new state after the operation
     *
     * @throws IndexOutOfBoundsException if the index is out of range (index < 0 || index > size())
     */
    public ImmutableArrayList<E> add(int index, Collection<? extends E> collection) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }

        Iterator<?> collectionIterator = collection.iterator();
        Deque<E> shiftItemStack = new LinkedList<E>();

        ImmutableArrayList<E> searchList = new ImmutableArrayList(this, size);
        Deque<ImmutableArrayList<E>> searchListStack = new LinkedList<ImmutableArrayList<E>>();
        Deque<Integer> paddingStack = new LinkedList<Integer>();
        Deque<Integer> sizeIncrementStack = new LinkedList<Integer>();
        while (collectionIterator.hasNext()) {
            if(index < searchList.size) {
                while (index >= searchList.capacity) {
                    int nextSearchListCapacity = searchList.capacity >> branchFactor;
                    int nextSearchListIndex = index / nextSearchListCapacity;

                    ImmutableArrayList<E> nextSearchList = (ImmutableArrayList<E>) searchList.list[nextSearchListIndex];
                    if (nextSearchList == null) {
                        nextSearchList = new ImmutableArrayList<E>(branchFactor, nextSearchListCapacity, 0);
                    } else {
                        nextSearchList = new ImmutableArrayList<E>(nextSearchList, nextSearchList.size);
                    }

                    int padding = nextSearchListIndex * nextSearchListCapacity;
                    index -= padding;
                    paddingStack.push(padding);
                    searchListStack.push(searchList);
                    sizeIncrementStack.push(0);

                    searchList = nextSearchList;
                }
                while (collectionIterator.hasNext() && index < searchList.size) {
                    shiftItemStack.addFirst((E) searchList.list[index]);
                    searchList.list[index] = collectionIterator.next();
                    index++;
                }
                while (collectionIterator.hasNext() && index < searchList.capacity) {
                    searchList.list[index] = collectionIterator.next();
                    size++;
                    sizeIncrementStack.push(sizeIncrementStack.pop() + 1);
                    index++;
                }
            }

            if (!collectionIterator.hasNext()) {
                if(shiftItemStack.isEmpty()) {
                    break;
                }
                collectionIterator = shiftItemStack.iterator();
                shiftItemStack.clear();
            }

            while (!searchListStack.isEmpty() && searchList.size == searchList.capacity) {
                searchList = searchListStack.pop();
                index += paddingStack.pop();
                int sizeIncrement = sizeIncrementStack.pop();
                sizeIncrementStack.push(sizeIncrementStack.pop() + sizeIncrement);
                searchList.size += sizeIncrement;
            }

            if (searchList.size == searchList.capacity) {
                ImmutableArrayList<E> expandList = new ImmutableArrayList<E>(branchFactor, searchList.capacity << branchFactor, searchList.size);
                expandList.list[0] = searchList;
                searchList = expandList;
            }
        }

        // TODO: Pop the search stack to update size.

        return searchList;
    }

    private void addAllIterable(Iterator<? extends E> toAddIterable) {

    }

    private void getToSublist(Stack<BacktrackValues<E>> searchStack, ImmutableArrayList<E> searchList) {
        while(index >= searchList.capacity) {
            int nextSearchListCapacity = searchList.capacity >> branchFactor;
            int nextSearchListIndex = index / nextSearchListCapacity;
            int backtrackPadding = nextSearchListIndex * nextSearchListCapacity;
            index -= backtrackPadding;
            searchStack.push(new BacktrackValues<E>(searchList, backtrackPadding));

            ImmutableArrayList<E> nextSearchList = (ImmutableArrayList<E>) searchList.list[nextSearchListIndex];
            if (nextSearchList == null) {
                searchList = new ImmutableArrayList<E>(branchFactor, nextSearchListCapacity, Math.min(toAddSize, nextSearchListCapacity));
            } else {
                searchList = new ImmutableArrayList<E>(nextSearchList, Math.min(nextSearchList.size + toAddSize, nextSearchList.capacity));
            }
        }
    }

    @Override
    public Iterator<E> iterator() {
        return null;
    }

}
