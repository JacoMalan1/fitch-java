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

package com.codelog.fitch.game;

import com.codelog.fitch.Main;
import com.codelog.fitch.graphics.*;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.math.Matrix4;
import glm_.vec2.Vec2;

import java.io.IOException;

import static com.codelog.fitch.graphics.Texture2D.loadTexture;

@SuppressWarnings({"unused", "FieldCanBeLocal"})
public class Player implements Drawable {

    private Vec2 pos;
    private float width;
    private float height;
    private boolean isStanding = false;
    private boolean isRunning = false;
    private float drawDepth = 0f;
    private Texture2D texture;

    private ShaderProgram shaderProgram;
    private VertexArrayObject vao;
    private VertexBufferObject vbo;
    private MatrixStack<Matrix4> matrixStack;

    public Player(Vec2 pos, float width, float height) {
        this.pos = pos;
        this.width = width;
        this.height = height;
    }

    public Player(Rectangle rect) {
        new Player(new Vec2(rect.getX(), rect.getY()), rect.getWidth(), rect.getHeight());
    }

    private void setupBuffers(GL4 gl) {

        float x = pos.getX();
        float y = pos.getY();

        float[] vertices = {
                x,          y,            drawDepth, 0, 0,
                x + width,  y,            drawDepth, 1, 0,
                x,          y + height,   drawDepth, 0, 1,
                x + width,  y + height,   drawDepth, 0, 0
        };

        vbo.sendFloatData(gl, vertices);

    }

    @Override
    public void update(GL4 gl) {
        setupBuffers(gl);

        Matrix4 ident = new Matrix4();
        matrixStack.push(ident);

    }

    @Override
    public void init(GL4 gl) {

        vao = new VertexArrayObject(gl);
        vao.bind(gl);
        vbo = new VertexBufferObject(gl, gl.GL_ARRAY_BUFFER);
        vbo.bind(gl);
        shaderProgram = new ShaderProgram(gl);
        shaderProgram.addVertexShader("pvshader.glsl");
        shaderProgram.addFragmentShader("pfshader.glsl");

        try {
            shaderProgram.compile(gl);
        } catch (ShaderCompilationException | IOException e) {
            Main.getLogger().log(this, e);
        }

        setupBuffers(gl);

        matrixStack = new MatrixStack<>();

        try {
            texture = loadTexture(gl, "player.png");
        } catch (IOException e) {
            Main.getLogger().log(this, e);
        }
    }

    @Override
    public void draw(GL4 gl) {

        vao.bind(gl);
        vbo.bind(gl);
        shaderProgram.bind(gl);
        texture.bind(gl);

        Matrix4 projMat = new Matrix4();
        projMat.loadIdentity();

        // Unroll the matrix stack
        Matrix4 temp;
        while ((temp = matrixStack.pop()) != null)
            projMat.multMatrix(temp);

        int handle = gl.glGetUniformLocation(shaderProgram.getID(), "projMat");
        gl.glUniformMatrix4fv(handle, 1, false, projMat.getMatrix(), 0);

        gl.glEnableVertexArrayAttrib(vao.getID(), 0);
        gl.glEnableVertexArrayAttrib(vao.getID(), 1);

        gl.glVertexAttribPointer(0, 3, gl.GL_FLOAT, false, 5 * Float.BYTES, 0);
        gl.glVertexAttribPointer(1, 2, gl.GL_FLOAT, false, 5 * Float.BYTES, 3 * Float.BYTES);

        gl.glDrawArrays(gl.GL_QUADS, 0, 4);

        gl.glDisableVertexArrayAttrib(vao.getID(), 0);
        gl.glDisableVertexArrayAttrib(vao.getID(), 1);

    }

    public Vec2 getPos() { return pos; }
    public void setPos(Vec2 pos) { this.pos = pos; }

    public MatrixStack<Matrix4> getMatrixStack() { return matrixStack; }
    public void loadMatrixStack(MatrixStack<Matrix4> _mstack) { matrixStack = _mstack; }

    public float getWidth() { return width; }
    public float getHeight() { return height; }

    public boolean getRunning() { return isRunning; }
    public boolean getStanding() { return isStanding; }

    public float getDrawDepth() { return drawDepth; }
    public void setDrawDepth(float _dd) { drawDepth = _dd; }

}
