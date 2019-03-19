package com.codelog.fitch.game;

import com.codelog.fitch.Main;
import com.codelog.fitch.graphics.Rectangle;
import com.codelog.fitch.math.Vector2;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;

public class Block {

    static final int BLOCK_SIZE = 50;
    private Rectangle drawRect;
    private BlockType type;
    private Body body;

    private Vector2 pos;

    public Block(int x, int y, BlockType type) {

        this.pos = new Vector2(x, y);
        drawRect = new Rectangle((float)pos.x * BLOCK_SIZE, (float)pos.y * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
        this.type = type;

        // Init Box2D physics
        var bodyDef = new BodyDef();
        bodyDef.position = Main.pixelsToWorld(getScreenPos());
        bodyDef.type = BodyType.STATIC;

        var shape = new PolygonShape();
        shape.setAsBox((float)Main.scalarPToW(BLOCK_SIZE / 2f), (float)Main.scalarPToW(BLOCK_SIZE / 2f));

        var fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.restitution = 0f;
        fixtureDef.density = 1f;
        fixtureDef.friction = 0.1f;

        body = Main.world.createBody(bodyDef);
        body.createFixture(fixtureDef);

    }

    Vector2 getPos() { return pos; }
    private Vector2 getScreenPos() { return pos.mult(BLOCK_SIZE); }
    public Rectangle getDrawRect() { return drawRect; }

}
