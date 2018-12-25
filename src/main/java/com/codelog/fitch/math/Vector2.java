package com.codelog.fitch.math;

public class Vector2 {

    public double x;
    public double y;

    public Vector2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector2(Vector2 other) {
        this.x = other.x;
        this.y = other.y;
    }

    public Vector2 mult(double scalar) {
        return new Vector2(this.x * scalar, this.y * scalar);
    }

    public Vector2 div(double scalar) {
        return new Vector2(this.x / scalar, this.y / scalar);
    }

    public Vector2 add(Vector2 vec) {
        return new Vector2(this.x + vec.x, this.y + vec.y);
    }

    public Vector2 sub(Vector2 vec) {
        return new Vector2(this.x - vec.x, this.y - vec.y);
    }

}
