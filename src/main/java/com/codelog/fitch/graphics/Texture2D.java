package com.codelog.fitch.graphics;

import com.jogamp.opengl.GL4;
import com.jogamp.opengl.util.GLBuffers;

import javax.imageio.ImageIO;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

@SuppressWarnings("WeakerAccess")
public class Texture2D {

    private int id;
    private int width;
    private int height;

    public Texture2D(int id, int width, int height) {

        this.id = id;
        this.width = width;
        this.height = height;

    }

    public void bind(GL4 gl) {

        gl.glBindTexture(gl.GL_TEXTURE_2D, this.id);

    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public static Texture2D loadTexture(GL4 gl, String filePath) throws IOException {

        filePath = "content/" + filePath;
        var image = ImageIO.read(new File(filePath));
        int width = image.getWidth();
        int height = image.getHeight();
        Raster data = image.getData();
        float[] pixelData = new float[width * height * 4];
        data.getPixels(0, 0, width, height, pixelData);

        IntBuffer buff = IntBuffer.allocate(1);
        gl.glGenTextures(1, buff);
        int id = buff.get(0);

        FloatBuffer pixelBuffer = GLBuffers.newDirectFloatBuffer(pixelData);
        gl.glBindTexture(gl.GL_TEXTURE_2D, id);
        gl.glTexImage2D(gl.GL_TEXTURE_2D, 0, gl.GL_RGBA, width, height, 0, gl.GL_RGBA, gl.GL_FLOAT, pixelBuffer);
        gl.glTexParameteri(gl.GL_TEXTURE_2D, gl.GL_TEXTURE_MAG_FILTER, gl.GL_NEAREST);
        gl.glTexParameteri(gl.GL_TEXTURE_2D, gl.GL_TEXTURE_MIN_FILTER, gl.GL_NEAREST);

        return new Texture2D(id, width, height);

    }

}
