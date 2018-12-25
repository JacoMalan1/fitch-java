package com.codelog.fitch.graphics;

import glm_.vec4.Vec4;

@SuppressWarnings({ "WeakerAccess", "unused" })
public class Colour {

    public static final Colour CornFlowerBlue = new Colour(new Vec4(100f / 255f, 149f / 255f, 237f / 255f, 1f));
    public static final Colour Black = new Colour(0, 0, 0, 255);
    public static final Colour White = new Colour(255, 255, 255, 255);
    public static final Colour Blue = new Colour(0, 0, 255, 255);
    public static final Colour Red = new Colour(255, 0, 0, 255);
    public static final Colour Green = new Colour(0, 255, 0, 255);
    public static final Colour Yellow = new Colour(255, 255, 0, 255);
    public static final Colour Purple = new Colour(255, 0, 255, 255);

    Vec4 rgba;

    public Colour(Vec4 rgba) {
        this.rgba = rgba;
    }

    public Colour(int red, int green, int blue, int alpha) {
        new Colour(new Vec4(red / 255f, green / 255f, blue / 255f, alpha / 255f));
    }

    public Colour(int r, int g, int b) {
        new Colour(new Vec4(r / 255f, g / 255f, b / 255f, 0f));
    }

    public Colour(float r, float g, float b, float a) {
        rgba = new Vec4(r, g, b, a);
    }

    public int[] getRGBA() {

        return new int[] {
                rgba.getX().intValue() * 255, rgba.getY().intValue() * 255, rgba.getZ().intValue() * 255, rgba.getW().intValue() * 255
        };

    }

    public int getR() { return rgba.getX().intValue() * 255; }
    public int getG() { return rgba.getY().intValue() * 255; }
    public int getB() { return rgba.getZ().intValue() * 255; }
    public int getA() { return rgba.getW().intValue() * 255; }

    public static Colour fromFloats(float r, float g, float b, float a) {
        return new Colour(r * 255, g * 255, b * 255, a * 255);
    }

    public float[] getFloats() {

        return new float[] {
                rgba.getX(), rgba.getY(), rgba.getZ(), rgba.getW()
        };

    }

}
