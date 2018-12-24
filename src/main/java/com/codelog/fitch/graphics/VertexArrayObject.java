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

import com.jogamp.opengl.GL4;
import com.jogamp.opengl.util.GLBuffers;

import java.nio.IntBuffer;

public class VertexArrayObject {

    private int id;

    public VertexArrayObject(GL4 gl) {

        IntBuffer buff = IntBuffer.allocate(1);
        gl.glGenVertexArrays(1, buff);
        this.id = buff.get(0);
        gl.glBindVertexArray(this.id);

    }

    public void bind(GL4 gl) {
        gl.glBindVertexArray(this.id);
    }

    public void dispose(GL4 gl) {

        int[] arr = { this.id };
        IntBuffer buff = GLBuffers.newDirectIntBuffer(arr);
        gl.glDeleteVertexArrays(1, buff);

    }

    public int getID() { return this.id; }

}
