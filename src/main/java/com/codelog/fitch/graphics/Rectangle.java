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

import com.codelog.fitch.Main;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.math.Matrix4;
import glm_.vec2.Vec2;

import java.io.IOException;

public class Rectangle implements Drawable {

    private Vec2 pos;
    private float height;
    private float width;

    private VertexArrayObject vao;
    private VertexBufferObject vbo;
    private ShaderProgram shaderProgram;
    private Texture2D texture;

    private MatrixStack<Matrix4> matrixStack;

    private boolean useTexture = false;
    private float drawDepth = 0.0f;

    public Rectangle(float x, float y, float width, float height) {

        this.pos = new Vec2(x, y);
        this.height = height;
        this.width = width;

    }

    public float getWidth() { return width; }
    public float getHeight() { return height; }
    public float getX() { return pos.getX(); }
    public float getY() { return pos.getY(); }

    @Override
    public void init(GL4 gl) {
        vao = new VertexArrayObject(gl);
        vbo = new VertexBufferObject(gl, gl.GL_ARRAY_BUFFER);
        shaderProgram = new ShaderProgram(gl);

        if (!useTexture) {
            shaderProgram.addVertexShader("shaders/bvshader.glsl");
            shaderProgram.addFragmentShader("shaders/bfshader.glsl");
        } else {
            shaderProgram.addVertexShader("shaders/bvshader_tex.glsl");
            shaderProgram.addFragmentShader("shaders/bfshader_tex.glsl");
        }

        try {
            shaderProgram.compile(gl);
        } catch (IOException | ShaderCompilationException e) {
            Main.getLogger().log(this, e);
        }

        matrixStack = new MatrixStack<>();

    }

    @Override
    public void update(GL4 gl) {

        vao.bind(gl);
        vbo.bind(gl);

        float x = pos.getX();
        float y = pos.getY();

        float[] vertices;
        if (!useTexture) {
            vertices = new float[] {
                    x,          y,          drawDepth,
                    x + width,  y,          drawDepth,
                    x,          y + width,  drawDepth,
                    x + width,  y + width,  drawDepth
            };
        } else {
            vertices = new float[] {
                    x,          y,            drawDepth, 0, 0,
                    x,          y + height,   drawDepth, 0, 1,
                    x + width,  y,            drawDepth, 1, 0,
                    x + width,  y + height,   drawDepth, 1, 1,
            };
        }

        vbo.sendFloatData(gl, vertices, gl.GL_DYNAMIC_DRAW);

    }

    @Override
    public void draw(GL4 gl) {

        vao.bind(gl);
        vbo.bind(gl);
        shaderProgram.bind(gl);
        if (useTexture) {
            texture.bind(gl);
        }

        Matrix4 projMat = MatrixStack.flattenStack(matrixStack);

        int handle = gl.glGetUniformLocation(shaderProgram.getID(), "projMat");
        gl.glUniformMatrix4fv(handle, 1, false, projMat.getMatrix(), 0);

        if (!useTexture) {

            gl.glEnableVertexAttribArray(0);

            gl.glVertexAttribPointer(0, 3, gl.GL_FLOAT, false, 3 * Float.BYTES, 0);

            gl.glDrawArrays(gl.GL_TRIANGLE_STRIP, 0, 4);
            gl.glDisableVertexAttribArray(0);

        } else {

            gl.glEnableVertexAttribArray(0);
            gl.glEnableVertexAttribArray(1);

            gl.glVertexAttribPointer(0, 3, gl.GL_FLOAT, false, 5 * Float.BYTES, 0);
            gl.glVertexAttribPointer(1, 2, gl.GL_FLOAT, false, 5 * Float.BYTES, 3 * Float.BYTES);

            gl.glDrawArrays(gl.GL_TRIANGLE_STRIP, 0, 4);
            gl.glDisableVertexAttribArray(0);
            gl.glDisableVertexAttribArray(1);

        }

    }

    public void loadMatrixStack(MatrixStack<Matrix4> _mstack) { matrixStack = _mstack; }
    public void setTexture(Texture2D texture, boolean changeDims) {
        this.texture = texture;
        if (changeDims) {
            this.width = texture.getWidth();
            this.height = texture.getHeight();
        }
    }

    public void setDrawDepth(float drawDepth) { this.drawDepth = drawDepth; }
    public void setUseTexture(boolean useTexture) { this.useTexture = useTexture; }

}
