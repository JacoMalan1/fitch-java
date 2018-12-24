package com.codelog.fitch.graphics;

import glm_.vec2.Vec2;

public class Rectangle {

    private Vec2 pos;
    private float height;
    private float width;

    public Rectangle(float x, float y, float width, float height) {

        this.pos = new Vec2(x, y);
        this.height = height;
        this.width = width;

    }

    public float getWidth() { return width; }
    public float getHeight() { return height; }
    public float getX() { return pos.getX(); }
    public float getY() { return pos.getY(); }

}
