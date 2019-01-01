package com.codelog.fitch.game;

import com.codelog.fitch.graphics.Rectangle;
import com.codelog.fitch.math.Vector2;
import com.codelog.syphen.Body;
import com.codelog.syphen.math.Vec2;

public class Block {

    static final int BLOCK_SIZE = 50;
    private Rectangle drawRect;
    private Body body;
    private BlockType type;

    private Vector2 pos;

    public Block(int x, int y, BlockType type) {

        this.pos = new Vector2(x, y);
        drawRect = new Rectangle((float)pos.x * BLOCK_SIZE, (float)pos.y * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
        Vector2 cpos = getScreenPos();
        Vec2 pos = new Vec2(cpos.x, cpos.y);
        body = new Body(pos, 1, new com.codelog.syphen.shapes.Rectangle(pos, BLOCK_SIZE, BLOCK_SIZE));
        body.setStatic(true);
        this.type = type;

    }

    Vector2 getPos() { return pos; }
    private Vector2 getScreenPos() { return pos.mult(BLOCK_SIZE); }
    public Body getBody() { return body; }
    public Rectangle getDrawRect() { return drawRect; }

}
