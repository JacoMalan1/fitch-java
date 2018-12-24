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
