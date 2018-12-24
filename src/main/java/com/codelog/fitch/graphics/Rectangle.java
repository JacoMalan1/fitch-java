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
