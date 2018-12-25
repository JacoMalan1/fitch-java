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

package com.codelog.fitch;

import com.codelog.fitch.game.Player;
import com.codelog.fitch.graphics.*;
import com.codelog.fitch.graphics.Rectangle;
import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;
import com.jogamp.newt.event.WindowAdapter;
import com.jogamp.newt.event.WindowEvent;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.*;
import com.jogamp.opengl.math.Matrix4;
import com.jogamp.opengl.util.Animator;
import glm_.vec2.Vec2;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.World;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Vector2;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main implements KeyListener, GLDebugListener, GLEventListener {

    private GLWindow window;
    private Animator animator;
    private Player player;
    private List<Drawable> drawList;
    private static Logger logger;
    private static Rectangle background;
    private World world;
    private Body floorBody;

    private Map<String, Texture2D> textureMap;

    private String[] propsToLog = new String[] {
            "os.name", "os.arch", "os.version", "java.vendor", "java.vm.name", "java.version"
    };

    public static Logger getLogger() { return logger; }

    public static void main(String[] args) {
        logger = new Logger();
        new Main().setup();
    }
    private void setup() {

        GLProfile glProfile = GLProfile.get(GLProfile.GL4);
        GLCapabilities glCap = new GLCapabilities(glProfile);
        glCap.setDepthBits(16);
        window = GLWindow.create(glCap);
        window.setTitle("Fitch");

        int width = Toolkit.getDefaultToolkit().getScreenSize().width;
        int height = Toolkit.getDefaultToolkit().getScreenSize().height;
        window.setSize(width, height);
        window.setFullscreen(true);
        window.setPointerVisible(false);
        window.setVisible(true);
        window.setContextCreationFlags(GLContext.CTX_OPTION_DEBUG);
        window.getContext().enableGLDebugMessage(true);
        window.getContext().addGLDebugListener(this);
        window.addKeyListener(this);

        window.addGLEventListener(this);

        animator = new Animator(window);
        window.setAnimator(animator);
        animator.start();

        window.setAutoSwapBufferMode(false);

        window.addWindowListener(new WindowAdapter() {
            @Override
            public void windowDestroyed(WindowEvent e) {
                animator.stop();
                logger.write();
                System.exit(0);
            }
        });

    }

    @Override
    public void init(GLAutoDrawable drawable) {

        logSystemInfo();

        var gl = drawable.getGL().getGL4();

        logger.log(this, LogSeverity.INFO, String.format("OpenGL version: %s\n", window.getContext().getGLVersion()));

        gl.glDebugMessageControl(gl.GL_DONT_CARE, gl.GL_DONT_CARE, gl.GL_DONT_CARE, 0, null, 0, true);

        gl.glEnable(gl.GL_DEPTH_TEST);
        gl.glEnable(gl.GL_DEBUG_OUTPUT_SYNCHRONOUS);
        gl.glEnable(gl.GL_BLEND);
        gl.glEnable(gl.GL_TEXTURE_2D);

        gl.glBlendFunc(gl.GL_SRC_ALPHA, gl.GL_ONE_MINUS_SRC_ALPHA);
        gl.glDepthFunc(gl.GL_LEQUAL);

        drawList = new ArrayList<>();
        textureMap = new HashMap<>();

        try {
            textureMap.put("player", Texture2D.loadTexture(gl, "player.png"));
            textureMap.put("background", Texture2D.loadTexture(gl, "background.png"));
        } catch (IOException e) {
            logger.log(this, e);
        }

        world = new World();

        background = new Rectangle(0, 0, window.getWidth(), window.getHeight());
        background.setDrawDepth(0.9f);
        background.setUseTexture(true);
        drawList.add(background);

        player = new Player(new Vec2(0, 0), 50, 100);
        player.setDrawDepth(0.0f);
        drawList.add(player);

        floorBody = new Body();
        floorBody.translateToOrigin();
        floorBody.translate(0f, 350);
        floorBody.addFixture(new org.dyn4j.geometry.Rectangle((9f / 16f * 10f), 1));
        floorBody.setMass(MassType.INFINITE);
        world.addBody(floorBody);

        for (Drawable d : drawList)
            d.init(gl);

        player.setTexture(textureMap.get("player"), true);
        background.setTexture(textureMap.get("background"), false);

        logger.log(this, LogSeverity.INFO, "Initialising...");

        world.addBody(player.getPhysicsBody());
        world.setGravity(World.EARTH_GRAVITY.multiply(-1));

    }

    private void logSystemInfo() {

        StringBuilder msgBuilder = new StringBuilder();
        msgBuilder.append("System information:");
        msgBuilder.append('\n');

        for (String prop : propsToLog) {
            msgBuilder.append('\t');
            msgBuilder.append(prop);
            msgBuilder.append('=');
            msgBuilder.append(System.getProperty(prop));
            msgBuilder.append('\n');
        }

        logger.log(this, LogSeverity.INFO, msgBuilder.toString());

    }

    @Override
    public void dispose(GLAutoDrawable drawable) {

    }

    @Override
    public void display(GLAutoDrawable drawable) {

        var gl = drawable.getGL().getGL4();

        float[] cfb = Colour.CornFlowerBlue.getFloats();
        gl.glClearColor(cfb[0], cfb[1], cfb[2], cfb[3]);
        gl.glClearDepth(1f);
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

        world.step(2);

    }

    private void render(GL4 gl) {

        Matrix4 mat = new Matrix4();
        mat.makeOrtho(0, window.getWidth(), window.getHeight(), 0, 0f, 1f);
        MatrixStack<Matrix4> stack = new MatrixStack<>();
        stack.push(mat);

        background.loadMatrixStack(stack.cloneStack());
        player.loadMatrixStack(stack.cloneStack());

        for (Drawable d : drawList)
            d.draw(gl);

    }

    @Override
    public void keyPressed(KeyEvent e) {

        switch (e.getKeyCode()) {
            case KeyEvent.VK_ESCAPE:
                window.destroy();
                break;
            case KeyEvent.VK_F11:
                window.setFullscreen(!window.isFullscreen());
                break;
            case KeyEvent.VK_SPACE:
                player.getPhysicsBody().applyImpulse(new Vector2(0, -500));
                break;
            case KeyEvent.VK_D:
                player.getPhysicsBody().applyForce(new Vector2(2000, 0));
            default:
                break;
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}