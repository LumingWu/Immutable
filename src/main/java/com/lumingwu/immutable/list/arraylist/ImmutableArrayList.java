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

import java.util.*;

public class ImmutableArrayList<E> implements ImmutableList<E>, RandomAccess {

    private int branchFactor;
    private int capacity;
    private int size;
    private Object[] list;

    /**
     * Create a new empty ImmutableArrayList that has the capacity of 2^6. With the default {@code branchFactor} of 6.0
     */
    public ImmutableArrayList() {
        this(6);
    }

    /**
     * Create a new empty ImmutableArrayList that has the capacity of 2^{@code branchFactor}.
     * @param branchFactor - The branch factor is an integer between 1 and 29 inclusively that represents the number of
     *                     positions for bitwise shifting. It is used for determining the size of all template lists of
     *                     the {@code ImmutableArrayList<E>}, which is 2^{@code branchFactor}.
     */
    public ImmutableArrayList(int branchFactor) {
        this(branchFactor, 1 << branchFactor, 0);
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
    public ImmutableArrayList<E> add(E item) {
        return addAll(this.size, Arrays.asList(item));
    }

    @Override
    public ImmutableArrayList<E> add(int index, E item) {
        return addAll(index, Arrays.asList(item));
    }

    @Override
    public ImmutableArrayList<E> addAll(Iterable<? extends E> collection) {
        return addAll(this.size, collection);
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
     * @throws IndexOutOfBoundsException if the index is out of range index < 0 or index > size
     */
    @Override
    public ImmutableArrayList<E> addAll(int index, Iterable<? extends E> collection) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }

        Iterator<?> collectionIterator = collection.iterator();
        Deque<E> shiftItemStack = new LinkedList<E>();

        ImmutableArrayList<E> searchList = new ImmutableArrayList(this, size);
        Deque<ImmutableArrayList<E>> searchListStack = new LinkedList<ImmutableArrayList<E>>();
        searchListStack.push(searchList);
        Deque<Integer> paddingStack = new LinkedList<Integer>();
        paddingStack.push(0);
        Deque<Integer> sizeIncrementStack = new LinkedList<Integer>();
        sizeIncrementStack.push(0);
        while (collectionIterator.hasNext()) {
            if(index <= searchList.size) {
                while (index >= searchList.capacity) {
                    int nextSearchListCapacity = searchList.capacity >> branchFactor;
                    int nextSearchListIndex = index / nextSearchListCapacity;

                    ImmutableArrayList<E> nextSearchList = (ImmutableArrayList<E>) searchList.list[nextSearchListIndex];
                    if (nextSearchList == null) {
                        nextSearchList = new ImmutableArrayList<E>(branchFactor, nextSearchListCapacity, 0);
                    } else {
                        nextSearchList = new ImmutableArrayList<E>(nextSearchList, nextSearchList.size);
                    }

                    searchList = nextSearchList;
                    searchListStack.push(searchList);
                    int padding = nextSearchListIndex * nextSearchListCapacity;
                    index -= padding;
                    paddingStack.push(padding);
                    sizeIncrementStack.push(0);
                }
                while (collectionIterator.hasNext() && index < searchList.size) {
                    shiftItemStack.addFirst((E) searchList.list[index]);
                    searchList.list[index] = collectionIterator.next();
                    index++;
                }
                while (collectionIterator.hasNext() && index < searchList.capacity) {
                    searchList.list[index] = collectionIterator.next();
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
                searchList = backtrackSizeIncrement(searchListStack, sizeIncrementStack);
                index += paddingStack.pop();
            }

            if (searchList.size == searchList.capacity) {
                ImmutableArrayList<E> expandList = new ImmutableArrayList<E>(branchFactor, searchList.capacity << branchFactor, searchList.size);
                expandList.list[0] = searchList;
                searchList = expandList;
            }
        }

        while(!searchListStack.isEmpty()) {
            searchList = backtrackSizeIncrement(searchListStack, sizeIncrementStack);
        }

        return searchList;
    }

    private ImmutableArrayList<E> backtrackSizeIncrement(Deque<ImmutableArrayList<E>> listStack, Deque<Integer> sizeIncrementStack) {
        ImmutableArrayList<E> previousList = listStack.pop();

        int sizeIncrement = sizeIncrementStack.pop();
        if(!sizeIncrementStack.isEmpty()) {
            sizeIncrementStack.push(sizeIncrementStack.pop() + sizeIncrement);
        }
        previousList.size += sizeIncrement;

        return previousList;
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

    @Override
    public Iterator<E> iterator() {
        return null;
    }

    public int getBranchFactor() {
        return branchFactor;
    }

    public int getCapacity() {
        return capacity;
    }

}
