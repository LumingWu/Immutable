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

/**
 * BacktrackValues -    A simple wrapper data structure that contains backtrack values for ImmutableArrayList search operation.
 *
 * @param <E>
 */
class BacktrackValues<E> {

    public ImmutableArrayList<E> list;
    public int padding;

    public BacktrackValues(ImmutableArrayList<E> list, int padding) {
        this.list = list;
        this.padding = padding;
    }

}