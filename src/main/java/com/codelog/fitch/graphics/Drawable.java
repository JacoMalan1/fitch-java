package com.codelog.fitch.graphics;

import com.jogamp.opengl.GL4;

public interface Drawable {

    void init(GL4 gl);
    void update(GL4 gl);
    void draw(GL4 gl);

}
