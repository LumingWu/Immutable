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

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.RandomAccess;
import java.util.function.UnaryOperator;

public class ImmutableArrayList<E> implements ImmutableList<E>, RandomAccess {

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
        return false;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public E get(int index) {
        return null;
    }

    @Override
    public int indexOf(Object object) {
        return 0;
    }

    @Override
    public boolean contains(Object object) {
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        return false;
    }

    @Override
    public boolean containsAll(ImmutableList<?> immutableList) {
        return false;
    }

    @Override
    public ImmutableListResult<E> set(int index, E element) {
        return null;
    }

    @Override
    public ImmutableListBatchResult<E> set(int index, Collection<E> collection) {
        return null;
    }

    @Override
    public ImmutableListBatchResult<E> set(int index, ImmutableList<E> immutableList) {
        return null;
    }

    @Override
    public ImmutableList<E> replaceAll(UnaryOperator<E> operator) {
        return null;
    }

    @Override
    public ImmutableListStatusResult retainAll(Collection<?> collection) {
        return null;
    }

    @Override
    public ImmutableListStatusResult retainAll(ImmutableList<?> immutableList) {
        return null;
    }

    @Override
    public ImmutableList<E> sort(Comparator<? super E> comparator) {
        return null;
    }

    @Override
    public ImmutableListResult<E> remove(int index) {
        return null;
    }

    @Override
    public ImmutableListBatchResult<E> removeAll(int start, int end) {
        return null;
    }

    @Override
    public ImmutableListStatusResult<E> remove(Object object) {
        return null;
    }

    @Override
    public ImmutableListStatusResult<E> removeAll(Collection<?> collection) {
        return null;
    }

    @Override
    public ImmutableListStatusResult<E> removeAll(ImmutableList<? extends E> immutableList) {
        return null;
    }

    @Override
    public Iterator<E> iterator() {
        return null;
    }

    @Override
    public ImmutableList<E> subList(int fromIndex, int toIndex) {
        return null;
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T> T[] toArray(T[] array) {
        return null;
    }

    /*
    private int branchFactor;
    private int capacity;
    private int size;
    private Object[] list;

    public ImmutableArrayList(int multiplier) {
        this(multiplier,multiplier,0);
        this.branchFactor = multiplier;
    }

    private ImmutableArrayList(ImmutableArrayList<E> immutableArrayList, int newSize) {
        this(immutableArrayList.branchFactor, immutableArrayList.capacity, newSize);
        list = immutableArrayList.list.clone();
    }

    private ImmutableArrayList(int multiplier, int capacity, int size) {
        if(multiplier < 2) {
            throw new IllegalArgumentException("Multiplier must be at least 2");
        }
        this.branchFactor = multiplier;
        this.capacity = capacity;
        this.size = size;

        list = new Object[multiplier];
    }
    */

    /**
     * Create a new ImmutableArrayList that added zero or more {@items} starting at {@index}. All original items starting at
     * {@index} will shift right to make space.
     *
     * @param index - index at which the specified element is to be inserted
     * @param items
     *
     * @return a reference of the new ImmutableArrayList represents the new state after the operation
     *
     * @throws IndexOutOfBoundsException if the index is out of range (index < 0 || index > size())
     */
    /*
    public ImmutableArrayList<E> add(int index, E... items) {
        if(index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }

        int itemsLength = items.length;
        int itemIndex = 0;
        int toAddSize = itemsLength;
        Stack<E> shiftItemStack = new Stack<E>();

        Stack<BacktrackValues<E>> searchStack = new Stack<BacktrackValues<E>>();
        ImmutableArrayList<E> searchList = new ImmutableArrayList(this, Math.min(size + toAddSize, capacity));

        while(itemIndex < itemsLength) {
            if(searchList.size < searchList.capacity) {
                while(index >= branchFactor) {
                    int nextSearchListCapacity = searchList.capacity / branchFactor;
                    int nextSearchListIndex = index / nextSearchListCapacity;
                    int padding = nextSearchListIndex * nextSearchListCapacity;
                    ImmutableArrayList<E> nextSearchList = (ImmutableArrayList<E>) searchList.list[nextSearchListIndex];
                    if (nextSearchList == null) {
                        nextSearchList = new ImmutableArrayList<E>(branchFactor, nextSearchListCapacity, Math.min(toAddSize, nextSearchListCapacity));
                    } else {
                        nextSearchList = new ImmutableArrayList<E>(nextSearchList, Math.min(nextSearchList.size + toAddSize, nextSearchList.capacity));
                    }
                    searchStack.push(new BacktrackValues<E>(searchList, padding));
                    searchList = nextSearchList;
                    index -= padding;
                }
                while(index < searchList.size && itemIndex < itemsLength){
                    shiftItemStack.addFirst((E)searchList.list[index]);
                    searchList.list[index] = items[itemIndex];
                    itemIndex++;
                    index++;
                }
                while(index < searchList.capacity && itemIndex < itemsLength) {
                    searchList.list[index] = items[itemIndex];
                    toAddSize--;
                    itemIndex++;
                    index++;
                }
            }
            if(itemIndex == itemsLength) {
                break;
            }
            while(!searchStack.isEmpty() && searchList.size == searchList.capacity) {
                BacktrackValues<E> iterateValue = searchStack.pop();
                searchList = iterateValue.list;
                index += iterateValue.padding;
            }
            if(searchList.size == searchList.capacity) {
                int expandListCapacity = searchList.capacity * branchFactor;
                ImmutableArrayList<E> expandList = new ImmutableArrayList<E>(branchFactor, expandListCapacity, Math.min(searchList.size + toAddSize, expandListCapacity));
                expandList.list[0] = searchList;
                searchList = expandList;
            }
        }
        while(toAddSize > 0) {
            if(searchList.size < searchList.capacity) {
                while(index >= branchFactor) {
                    int nextSearchListCapacity = searchList.capacity / branchFactor;
                    int nextSearchListIndex = index / nextSearchListCapacity;
                    int padding = nextSearchListIndex * nextSearchListCapacity;
                    ImmutableArrayList<E> nextSearchList = (ImmutableArrayList<E>) searchList.list[nextSearchListIndex];
                    if (nextSearchList == null) {
                        nextSearchList = new ImmutableArrayList<E>(branchFactor, nextSearchListCapacity, Math.min(toAddSize, nextSearchListCapacity));
                    } else {
                        nextSearchList = new ImmutableArrayList<E>(nextSearchList, Math.min(nextSearchList.size + toAddSize, nextSearchList.capacity));
                    }
                    searchStack.push(new BacktrackValues<E>(searchList, padding));
                    searchList = nextSearchList;
                    index -= padding;
                }
                while(index < searchList.size && toAddSize > 0){
                    shiftItemStack.addFirst((E)searchList.list[index]);
                    searchList.list[index] = shiftItemStack.pop();
                    index++;
                }
                while(index < searchList.capacity && toAddSize > 0) {
                    searchList.list[index] = shiftItemStack.pop();
                    toAddSize--;
                    index++;
                }
            }
            if(toAddSize == 0) {
                break;
            }
            while(!searchStack.isEmpty() && searchList.size == searchList.capacity) {
                BacktrackValues<E> iterateValue = searchStack.pop();
                searchList = iterateValue.list;
                index += iterateValue.padding;
            }
            if(searchList.size == searchList.capacity) {
                int expandListCapacity = searchList.capacity * branchFactor;
                ImmutableArrayList<E> expandList = new ImmutableArrayList<E>(branchFactor, expandListCapacity, Math.min(searchList.size + toAddSize, expandListCapacity));
                expandList.list[0] = searchList;
                searchList = expandList;
            }
        }
        if(searchStack.isEmpty()) {
            return searchList;
        }
        return searchStack.getFirst().list;
    }

    public E get(int index) {
        if(index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        ImmutableArrayList<E> searchList = this;
        while(index > branchFactor) {
            int nextSearchListCapacity = searchList.capacity / branchFactor;
            int nextSearchListIndex = index / nextSearchListCapacity;
            searchList = (ImmutableArrayList<E>)searchList.list[nextSearchListIndex];
            index = index - nextSearchListIndex * nextSearchListCapacity;
        }
        return (E)searchList.list[index];
    }

    public int size() {
        return size;
    }

    public E set(int index, E item) {
        return null;
    }

    public E remove(int index) {
        return null;
    }
    */

}
