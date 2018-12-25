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

import com.jogamp.opengl.math.Matrix4;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

import static java.util.stream.Collectors.toList;

@SuppressWarnings("WeakerAccess")
public class MatrixStack<T> {

    private List<T> matrices;
    private int numElements;

    public MatrixStack() {

        matrices = new ArrayList<>();
        numElements = 0;

    }

    @SuppressWarnings("unchecked")
    public MatrixStack(MatrixStack other) {
        this.matrices = new ArrayList<>(other.matrices);
        this.numElements = other.numElements;
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

    public static Matrix4 flattenStack(MatrixStack<Matrix4> stack) {

        Matrix4 result = new Matrix4();
        result.loadIdentity();
        Matrix4 tempMat;
        while ((tempMat = stack.pop()) != null)
            result.multMatrix(tempMat);

        return result;

    }

    public MatrixStack<T> cloneStack() {
        return new MatrixStack<>(this);
    }

}
