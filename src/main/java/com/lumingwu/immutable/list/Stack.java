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

package com.lumingwu.immutable.list;

/**
 * Stack -  A simple dedicated singly-linked Stack for ImmutableArrayList search operation.
 *
 * @param <E>
 */
class Stack<E> {

    private IterateNode<E> head;
    private IterateNode<E> tail;

    public void addFirst(E value) {
        IterateNode<E> newNode = new IterateNode<E>();
        newNode.value = value;
        if(head != null) {
            head.previous = newNode;
        }
        head = newNode;
    }

    public E getFirst() {
        return head.value;
    }

    public void push(E value) {
        if(isEmpty()) {
            head = new IterateNode<E>();
            head.value = value;
            tail = head;
            return;
        }
        IterateNode<E> newNode = new IterateNode<E>();
        newNode.previous = tail;
        newNode.value = value;
        tail = newNode;
    }

    public E pop() {
        E value = tail.value;
        tail = tail.previous;
        return value;
    }

    public boolean isEmpty() {
        return head == null;
    }

    private class IterateNode<E> {

        public IterateNode<E> previous;
        public E value;

    }

}