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

import com.codelog.fitch.game.Block;
import com.codelog.fitch.game.Level;
import com.codelog.fitch.game.LevelParseException;
import com.codelog.fitch.game.Player;
import com.codelog.fitch.graphics.*;
import com.codelog.fitch.math.Vector2;
import com.codelog.syphen.World;
import com.codelog.syphen.WorldBuilder;
import com.codelog.syphen.math.Vec2;
import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.math.Matrix4;
import com.jogamp.opengl.util.Animator;

import javax.annotation.Nullable;
import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main extends JFrame implements KeyListener, GLEventListener {

    // Constants
    public static final Vec2 GRAVITY = new Vec2(0, 0.8);

    // Instance variables
    private GLCanvas canvas;
    private Animator animator;
    private Player player;
    private List<Drawable> drawList;
    private World world;

    // Static variables
    private static boolean write_log = true;
    private static Logger logger;
    private static Rectangle background;
    private static Level level;

    private static Map<String, Texture2D> textureMap;

    private static String[] propsToLog = new String[] {
            "os.name", "os.arch", "os.version", "java.vendor", "java.vm.name", "java.version"
    };

    public static Logger getLogger() { return logger; }

    private Main() throws IllegalArgumentException {
        super("Fitch");
    }

    private static void sendHelp() {

        // Shows commandline help message.
        StringBuilder sb = new StringBuilder();

        sb.append("Usage: fitch <arguments>\n\n");
        sb.append("\t--no-log\tDo not write log info to a file.\n");

    }

    public static void main(@Nullable String[] args) throws IllegalArgumentException {

        // Parse commandline arguments.
        if (args != null) {
            for (int i = 0; i < args.length; i++) {
                switch (args[i]) {

                    case "--no-log":
                        write_log = false;
                        break;

                    default:
                        sendHelp();
                        break;

                } // endswitch
            } // endfor
        } // endif

        // Start game.
        logger = new Logger();
        new Main().setup();

    }
    private void setup() {

        // Setup GL Canvas and capabilities.
        GLProfile glProfile = GLProfile.get(GLProfile.GL4);
        GLCapabilities glCap = new GLCapabilities(glProfile);
        glCap.setDepthBits(16);

        canvas = new GLCanvas(glCap);
        animator = new Animator(canvas);

        add(canvas);
        canvas.addGLEventListener(this);
        setSize(800, 600);
        setVisible(true);
        addKeyListener(this);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setFocusable(true);

        canvas.setAutoSwapBufferMode(false);
        animator.start();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                animator.stop();
                if (write_log)
                    logger.write();
            }
        });

    }

    @Override
    public void init(GLAutoDrawable drawable) {

        logSystemInfo();

        var gl = drawable.getGL().getGL4();

        logger.log(this, LogSeverity.INFO, String.format("OpenGL version: %s\n", canvas.getContext().getGLVersion()));

        gl.glDebugMessageControl(gl.GL_DONT_CARE, gl.GL_DONT_CARE, gl.GL_DONT_CARE, 0, null, 0, true);

        // Enable OpenGL features
        gl.glEnable(gl.GL_DEPTH_TEST);
        gl.glEnable(gl.GL_DEBUG_OUTPUT_SYNCHRONOUS);
        gl.glEnable(gl.GL_BLEND);
        gl.glEnable(gl.GL_TEXTURE_2D);

        // Set GL functions
        gl.glBlendFunc(gl.GL_SRC_ALPHA, gl.GL_ONE_MINUS_SRC_ALPHA);
        gl.glDepthFunc(gl.GL_LEQUAL);

        drawList = new ArrayList<>();
        textureMap = new HashMap<>();

        // Initialize textures
        try {
            textureMap.put("player", Texture2D.loadTexture(gl, "player.png"));
            textureMap.put("background", Texture2D.loadTexture(gl, "background.png"));
            textureMap.put("solid", Texture2D.loadTexture(gl, "solid.png"));
        } catch (IOException e) {
            logger.log(this, e);
        }

        // Build world
        WorldBuilder wb = new WorldBuilder();
        wb.setGravity(new Vec2(0, 1));
        wb.setAirResistance(1.1);
        wb.setTerminalVelocity(10);
        world = wb.toWorld();

        // Setup background
        background = new Rectangle(-50, -50, canvas.getWidth() + 50, canvas.getHeight() + 50);
        background.setDrawDepth(0.9f);
        background.setUseTexture(true);
        drawList.add(background);

        // TODO: Implement level loading.

        try {
            level = Tools.loadLevel("content/level1.fl");
        } catch (IOException | LevelParseException e) {
            // We can't recover from level-load failure, so exit.
            logger.log(this, e);
            this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        }

        for (Block b : level.getBlocks()) {
            b.getDrawRect().setUseTexture(true);
            b.getDrawRect().setDrawDepth(0.2f);
            drawList.add(b.getDrawRect());
        }

        // Setup player
        var pwidth = 50;
        var pheight = 100;
        var pos = level.getStartPos().sub(new Vector2(0, pheight * 2));
        player = new Player(pos, pwidth, pheight);
        player.setDrawDepth(0.0f);
        drawList.add(player);

        // Initialize all the drawables
        for (Drawable d : drawList)
            d.init(gl);

        // Set initial textures
        player.setTexture(textureMap.get("player"), true);
        background.setTexture(textureMap.get("background"), false);

        for (Block b : level.getBlocks()) {
            b.getDrawRect().setTexture(textureMap.get("solid"), false);
            world.addBody(b.getBody());
        }

        // Add every physics body
        world.addBody(player.getPhysicsBody());

        logger.log(this, LogSeverity.INFO, "Initialising...");

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
        // Nothing to dispose of :)
    }

    @Override
    public void display(GLAutoDrawable drawable) { // Called on render

        var gl = drawable.getGL().getGL4();

        // Pre-update
        float[] cfb = Colour.CornFlowerBlue.getFloats();
        gl.glClearColor(cfb[0], cfb[1], cfb[2], cfb[3]);
        gl.glClearDepth(1f);
        gl.glClear(gl.GL_COLOR_BUFFER_BIT | gl.GL_DEPTH_BUFFER_BIT);

        update(gl); // Object updates
        render(gl); // Render everything

        // Post-render
        // TODO: Add post-render cleanup.

        canvas.swapBuffers();

    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {

        var gl = drawable.getGL().getGL4();

        // Resize the viewport to correspond
        // to the new canvas size.
        gl.glViewport(x, y, width, height);

    }

    private void update(GL4 gl) {

        for (Drawable d : drawList)
            d.update(gl);

        world.step(1);

    }

    private void render(GL4 gl) {

        // TODO: Optimize rendering.

        Matrix4 mat = new Matrix4();
        mat.makeOrtho(0, canvas.getWidth(), canvas.getHeight(), 0, 0f, 1f);

        Vector2 pos = player.getPos();
        float translateX = -((float)pos.x - canvas.getWidth() / 2) - player.getWidth() * 2;
        float translateY = -((float)pos.y - canvas.getHeight() / 2);

        mat.translate(translateX, translateY, 0f);

        MatrixStack<Matrix4> stack = new MatrixStack<>();
        stack.push(mat);

        background.setPos(new Vector2(-translateX, -translateY));

        background.loadMatrixStack(stack.cloneStack());
        player.loadMatrixStack(stack.cloneStack());

        for (Block b : level.getBlocks()) {
            b.getDrawRect().loadMatrixStack(stack);
        }

        for (Drawable d : drawList)
            d.draw(gl);

    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {
    }

    @Override
    public synchronized void keyPressed(KeyEvent keyEvent) {

        // TODO: Optimize keyboard handling.

        switch (keyEvent.getKeyCode()) {
            case KeyEvent.VK_ESCAPE:
                dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
                break;
            case KeyEvent.VK_F11:
                break;
            case KeyEvent.VK_SPACE:
                break;
            case KeyEvent.VK_D:
                player.getPhysicsBody().applyForce(new Vec2(3, 0));
                break;
            case KeyEvent.VK_A:
                player.getPhysicsBody().applyForce(new Vec2(-3, 0));
                break;
            default:
                break;
        }

    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
    }
}
