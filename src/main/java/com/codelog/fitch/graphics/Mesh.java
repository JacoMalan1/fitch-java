package com.codelog.fitch.graphics;

import com.codelog.fitch.Main;
import com.jogamp.opengl.GL4;

import java.io.IOException;

public class Mesh implements Drawable {

    private VertexBufferObject vertexBuffer;
    private VertexBufferObject indexBuffer;
    private VertexArrayObject vao;
    private ShaderProgram shader;

    private float[] vertices;
    private int[] indices;

    private String fragmentShader;
    private String vertexShader;

    Mesh(float[] vertices, int[] indices, String vertexShader, String fragmentShader) {
        this.vertices = vertices;
        this.indices = indices;
        this.vertexShader = vertexShader;
        this.fragmentShader = fragmentShader;
    }

    @Override
    public void init(GL4 gl) {

        vao = new VertexArrayObject(gl);
        vao.bind(gl);

        vertexBuffer = new VertexBufferObject(gl, gl.GL_ARRAY_BUFFER);
        indexBuffer = new VertexBufferObject(gl, gl.GL_ELEMENT_ARRAY_BUFFER);

        vertexBuffer.bind(gl);
        vertexBuffer.sendFloatData(gl, vertices, gl.GL_STATIC_DRAW);

        indexBuffer.bind(gl);
        indexBuffer.sendIntData(gl, indices);

        shader = new ShaderProgram(gl);
        shader.addVertexShader(vertexShader);
        shader.addFragmentShader(fragmentShader);

        try {
            shader.compile(gl);
        } catch (ShaderCompilationException | IOException e) {
            Main.getLogger().log(this, e);
        }

    }

    @Override
    public void update(GL4 gl) {

        // Cannot update mesh at this stage.

    }

    @Override
    @SuppressWarnings("Duplicates")
    public void draw(GL4 gl) {

        vao.bind(gl);
        shader.bind(gl);

        vertexBuffer.bind(gl);
        indexBuffer.bind(gl);

        gl.glEnableVertexArrayAttrib(vao.getID(), 0);
        gl.glEnableVertexArrayAttrib(vao.getID(), 1);

        gl.glVertexAttribPointer(0, 3, gl.GL_FLOAT, false, 5 * Float.BYTES, 0);
        gl.glVertexAttribPointer(1, 2, gl.GL_FLOAT, false, 5 * Float.BYTES, 3 * Float.BYTES);

        gl.glDrawElements(gl.GL_QUADS, indices.length, gl.GL_INT, 0);

        gl.glDisableVertexArrayAttrib(vao.getID(), 0);
        gl.glDisableVertexArrayAttrib(vao.getID(), 1);

    }
}
