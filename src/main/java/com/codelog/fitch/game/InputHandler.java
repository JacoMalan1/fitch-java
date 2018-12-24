package com.codelog.fitch.game;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;
import com.jogamp.newt.opengl.GLWindow;

import java.util.ArrayList;
import java.util.List;

public class InputHandler implements KeyListener {

    private List<Short> keysDown;
    private List<Short> keysDownLast;

    public void init(GLWindow window) {

        keysDown = new ArrayList<>();
        window.addKeyListener(this);

    }

    @Override
    public void keyPressed(KeyEvent e) {

        keysDown.add(e.getKeyCode());

    }

    @Override
    public void keyReleased(KeyEvent e) {

        List<Short> remove = new ArrayList<>();
        remove.add(e.getKeyCode());
        keysDown.removeAll(remove);

    }

    public void update() {
        keysDownLast = keysDown;
    }

    public boolean keyDown(short key) {
        return keysDown.contains(key);
    }

    public boolean keyUp(short key) {
        return !keysDown.contains(key);
    }

    public boolean keyPress(short key) {
        return (keysDown.contains(key)) && !(keysDownLast.contains(key));
    }

    public boolean keyRelease(short key) {
        return !(keysDown.contains(key)) && (keysDownLast.contains(key));
    }

}
