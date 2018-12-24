/*

A platformer game written using OpenGL.
    Copyright (C) 2017-2018  Jaco Malan

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.

*/

package com.codelog.fitch.graphics;

import java.util.List;
import java.util.Stack;

public class MatrixStack<T> {

    private List<T> matrices;
    private int numElements;

    public MatrixStack() {

        matrices = new Stack<>();
        numElements = 0;

    }

    public void push(T mat) {
        matrices.add(mat);
        numElements++;
    }

    public T pop() {

        T result = null;
        if (numElements > 0) {
            result = matrices.get(matrices.size() - 1);
            matrices.remove(matrices.size() - 1);
            numElements--;
        }

        return result;
    }

}
