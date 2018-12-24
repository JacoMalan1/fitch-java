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

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class VertexBufferObject {

    private int id;
    private int type;
    private boolean dataSent = false;

    public VertexBufferObject(GL4 gl, int type) {

        IntBuffer buff = IntBuffer.allocate(1);
        gl.glGenBuffers(1, buff);
        this.id = buff.get(0);
        this.type = type;
        bind(gl);

    }

    public void bind(GL4 gl) {
        gl.glBindBuffer(this.type, this.id);
    }

    public void unbind(GL4 gl) {
        gl.glBindBuffer(this.type, 0);
    }

    public void sendFloatData(GL4 gl, float[] data, int usage) {

        FloatBuffer buffer = GLBuffers.newDirectFloatBuffer(data);
        bind(gl);

        if (!dataSent) {
            gl.glBufferData(this.type, data.length * Float.BYTES, buffer, usage);
            dataSent = true;
        } else {
            gl.glBufferSubData(this.type, 0, data.length * Float.BYTES, buffer);
        }

        unbind(gl);

    }

    public void sendFloatData(GL4 gl, float[] data) {
        sendFloatData(gl, data, gl.GL_DYNAMIC_DRAW);
    }

}
