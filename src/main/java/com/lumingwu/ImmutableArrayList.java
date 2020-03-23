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

package main.java.com.lumingwu;

public class ImmutableArrayList<E> {

    private int multiplier;
    private int capacity;
    private int size;
    private Object[] list;

    public ImmutableArrayList(int multiplier) {
        this(multiplier,multiplier,0);
        this.multiplier = multiplier;
    }

    private ImmutableArrayList(ImmutableArrayList<E> immutableArrayList, int newSize) {
        this(immutableArrayList.multiplier, immutableArrayList.capacity, newSize);
        list = immutableArrayList.list.clone();
    }

    private ImmutableArrayList(int multiplier, int capacity, int size) {
        if(multiplier < 2) {
            throw new IllegalArgumentException("Multiplier must be at least 2");
        }
        this.multiplier = multiplier;
        this.capacity = capacity;
        this.size = size;

        list = new Object[multiplier];
    }

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
                while(index >= multiplier) {
                    int nextSearchListCapacity = searchList.capacity / multiplier;
                    int nextSearchListIndex = index / nextSearchListCapacity;
                    int padding = nextSearchListIndex * nextSearchListCapacity;
                    ImmutableArrayList<E> nextSearchList = (ImmutableArrayList<E>) searchList.list[nextSearchListIndex];
                    if (nextSearchList == null) {
                        nextSearchList = new ImmutableArrayList<E>(multiplier, nextSearchListCapacity, Math.min(toAddSize, nextSearchListCapacity));
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
                int expandListCapacity = searchList.capacity * multiplier;
                ImmutableArrayList<E> expandList = new ImmutableArrayList<E>(multiplier, expandListCapacity, Math.min(searchList.size + toAddSize, expandListCapacity));
                expandList.list[0] = searchList;
                searchList = expandList;
            }
        }
        while(toAddSize > 0) {
            if(searchList.size < searchList.capacity) {
                while(index >= multiplier) {
                    int nextSearchListCapacity = searchList.capacity / multiplier;
                    int nextSearchListIndex = index / nextSearchListCapacity;
                    int padding = nextSearchListIndex * nextSearchListCapacity;
                    ImmutableArrayList<E> nextSearchList = (ImmutableArrayList<E>) searchList.list[nextSearchListIndex];
                    if (nextSearchList == null) {
                        nextSearchList = new ImmutableArrayList<E>(multiplier, nextSearchListCapacity, Math.min(toAddSize, nextSearchListCapacity));
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
                int expandListCapacity = searchList.capacity * multiplier;
                ImmutableArrayList<E> expandList = new ImmutableArrayList<E>(multiplier, expandListCapacity, Math.min(searchList.size + toAddSize, expandListCapacity));
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
        while(index > multiplier) {
            int nextSearchListCapacity = searchList.capacity / multiplier;
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

}
