package com.codelog.fitch;

import com.codelog.fitch.game.Player;
import com.codelog.fitch.graphics.Drawable;
import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;
import com.jogamp.newt.event.WindowAdapter;
import com.jogamp.newt.event.WindowEvent;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.*;
import com.jogamp.opengl.math.Matrix4;
import com.jogamp.opengl.util.Animator;
import glm_.vec2.Vec2;

import java.util.ArrayList;
import java.util.List;

class Main implements GLDebugListener, GLEventListener, KeyListener {

    private GLWindow window;
    private Animator animator;

    private Player player;

    private List<Drawable> drawList;

    public static void main(String[] args) {
        new Main().setup();
    }

    private void setup() {

        GLProfile glProfile = GLProfile.get(GLProfile.GL4);
        GLCapabilities glCap = new GLCapabilities(glProfile);
        window = GLWindow.create(glCap);
        window.setTitle("Fitch");
        window.setSize(800, 600);
        window.setVisible(true);

        window.addGLEventListener(this);
        window.addKeyListener(this);

        animator = new Animator(window);
        window.setAnimator(animator);
        animator.start();

        window.setAutoSwapBufferMode(false);

        window.addWindowListener(new WindowAdapter() {
            @Override
            public void windowDestroyed(WindowEvent e) {
                animator.stop();
                System.exit(0);
            }
        });

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void init(GLAutoDrawable drawable) {

        var gl = drawable.getGL().getGL4();

        gl.glEnable(gl.GL_DEPTH_TEST);
        gl.glEnable(gl.GL_BLEND);

        gl.glBlendFunc(gl.GL_SRC_ALPHA, gl.GL_DST_ALPHA);
        gl.glDepthFunc(gl.GL_LEQUAL);

        drawList = new ArrayList<>();

        player = new Player(new Vec2(50, 50), 50, 100);
        player.setDrawDepth(0.0f);
        drawList.add(player);

        for (Drawable d : drawList)
            d.init(gl);

    }

    @Override
    public void dispose(GLAutoDrawable drawable) {

    }

    @Override
    public void display(GLAutoDrawable drawable) {

        var gl = drawable.getGL().getGL4();

        gl.glClearColor(128, 128, 0, 255);
        gl.glClearDepth(1.0f);
        gl.glClear(gl.GL_COLOR_BUFFER_BIT | gl.GL_DEPTH_BUFFER_BIT);

        update(gl);
        render(gl);

        window.swapBuffers();

    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {

    }

    @Override
    public void messageSent(GLDebugMessage event) {

        System.out.println("GL Debug[" + event.getDbgId() + "]: " + event.getDbgMsg());

    }

    private void update(GL4 gl) {

        for (Drawable d : drawList)
            d.update(gl);

    }

    private void render(GL4 gl) {

        for (Drawable d : drawList)
            d.draw(gl);

    }

}