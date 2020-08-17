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

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.function.UnaryOperator;

public interface ImmutableList<E> extends Iterable<E> {

    public ImmutableList<E> add(E item);

    public ImmutableList<E> add(int index, E item);

    public ImmutableList<E> addAll(Iterable<? extends E> collection);

    public ImmutableList<E> addAll(int index, Iterable<? extends E> collection);

    public boolean isEmpty();

    public int size();

    public E get(int index);

    public int indexOf(Object object);

    /*
    public boolean contains(Object object);

    public boolean containsAll(Collection<?> collection);

    public boolean containsAll(ImmutableList<?> immutableList);

    public ImmutableListResult<E> set(int index, E element);

    public ImmutableListBatchResult<E> set(int index, Collection<E> collection);

    public ImmutableListBatchResult<E> set(int index, ImmutableList<E> immutableList);

    public ImmutableList<E> replaceAll(UnaryOperator<E> operator);

    public ImmutableListStatusResult retainAll(Collection<?> collection);

    public ImmutableListStatusResult retainAll(ImmutableList<?> immutableList);

    public ImmutableList<E> sort(Comparator<? super E> comparator);

    public ImmutableListResult<E> remove(int index);

    public ImmutableListBatchResult<E> removeAll(int start, int end);

    public ImmutableListStatusResult<E> remove(Object object);

    public ImmutableListStatusResult<E> removeAll(Collection<?> collection);

    public ImmutableListStatusResult<E> removeAll(ImmutableList<? extends E> immutableList);

    public Iterator<E> iterator();

    public ImmutableList<E> subList(int fromIndex, int toIndex);

    public Object[] toArray();

    public <T> T[] toArray(T[] array);

     */

    public boolean equals(Object o);

    public int hashCode();

}
