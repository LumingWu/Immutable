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

import com.lumingwu.immutable.list.arraylist.ImmutableArrayList;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ImmutableArrayListTest {

    private ImmutableArrayList<Integer> list;

    @Before
    public void before() {
        list = new ImmutableArrayList<Integer>(1);
    }

    @Test
    public void createNewList_passedNoArguments_argumentsMatch() {
        list = new ImmutableArrayList<Integer>();

        assertEquals(6, list.getBranchFactor());
        assertEquals(64, list.getCapacity());
        assertEquals(0, list.size());
    }

    @Test
    public void createNewList_passedBranchFactor_argumentsMatch() {
        assertEquals(1, list.getBranchFactor());
        assertEquals(2, list.getCapacity());
        assertEquals(0, list.size());
    }

    @Test
    public void createNewList_passedLowerBoundBranchFactor_noException() {
        new ImmutableArrayList<Integer>(1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createNewList_passedZeroBranchFactor_illegalArgumentException() {
        new ImmutableArrayList<Integer>(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createNewList_passedNegativeBranchFactor_illegalArgumentException() {
        new ImmutableArrayList<Integer>(-1);
    }

    @Test
    public void createNewList_passedUpperBoundBranchFactor_noException() {
        new ImmutableArrayList<Integer>(30);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createNewList_passedHigherThanUpperBoundBranchFactor_illegalArgumentException() {
        new ImmutableArrayList<Integer>(31);
    }

    @Test
    public void createNewList_getSize_isEmpty() {
        assertEquals(0, list.size());
    }

    @Test
    public void createNewList_isEmpty_isEmpty() {
        assertTrue(list.isEmpty());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void createNewList_getItem_indexOutOfBoundException() {
        list.get(0);
    }

    @Test
    public void addItem_checkReference_newReferenceCreated() {
        ImmutableArrayList<Integer> newList = list.add(1);

        assertNotSame(list, newList);
    }

    @Test
    public void addItem_oldListGetProperties_remainedSame() {
        list.add(1);

        createNewList_passedBranchFactor_argumentsMatch();
    }

    @Test
    public void addItem_oldListGetSize_remainedSame() {
        list.add(1);

        assertEquals(0, list.size());
    }

    @Test
    public void addItem_getSize_sizeIncremented() {
        list = list.add(1);

        assertEquals(1, list.size());
    }

}
