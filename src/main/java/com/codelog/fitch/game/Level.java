package com.codelog.fitch.game;

import com.codelog.fitch.Main;
import com.codelog.fitch.math.Vector2;

import java.util.ArrayList;
import java.util.List;

import static com.codelog.fitch.game.Block.BLOCK_SIZE;

@SuppressWarnings({ "WeakerAccess", "unused" })
public class Level {

    private List<Block> blockList;
    private Vector2 levelSize;
    private Vector2 startPos;

    /**
     * Creates a new instance of the Level object.
     * Copies the blockList.
     * @param levelSize The size of the level in blocks.
     * @param blockList A list of blocks in the level. (Gets copied)
     */
    public Level(Vector2 startPos, Vector2 levelSize, List<Block> blockList) {
        this.blockList = new ArrayList<>(blockList);
        this.levelSize = levelSize;
        this.startPos = startPos.mult(BLOCK_SIZE);
    }

    public Level(Vector2 startPos, List<Block> blockList) {

        Vector2 size = new Vector2(0, 0);

        for (Block b : blockList) {

            Vector2 pos = b.getPos();

            size.x = (pos.x > size.x) ? pos.x : size.x;
            size.y = (pos.y > size.y) ? pos.y : size.y;

        }

        this.startPos = startPos.mult(BLOCK_SIZE);
        this.blockList = blockList;
        this.levelSize = size;

    }

    Level(Level other) {
        this.blockList = new ArrayList<>(other.blockList);
        this.levelSize = other.levelSize;
        this.startPos = other.startPos;
    }

    /**
     * Copies the level into a two-dimensional
     * array. (Warning: this is very memory-consuming!)
     * @return Array of blocks.
     */
    public Block[][] toArray() {

        Block[][] blocks = new Block[BLOCK_SIZE][BLOCK_SIZE];
        for (Block b : blockList) {
            blocks[(int)b.getPos().x][(int)b.getPos().y] = b;
        }

        return blocks;

    }

    public List<Block> getBlocks() { return this.blockList; }
    public Vector2 getStartPos() { return startPos; }

    public void addBlock(Block block) {
        blockList.add(block);
    }

    @Override
    public Level clone(){

        try {
            super.clone();
        } catch (CloneNotSupportedException e) {
            Main.getLogger().log(this, e);
        }

        return new Level(this);

    }
}
