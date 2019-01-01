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
import com.codelog.fitch.game.BlockType;
import com.codelog.fitch.game.Level;
import com.codelog.fitch.game.LevelParseException;
import com.codelog.fitch.math.Vector2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Tools {

    public static String loadFile(String filePath) throws IOException {

        FileReader fr = new FileReader(filePath);
        BufferedReader reader = new BufferedReader(fr);

        StringBuilder sb = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {

            sb.append(line);
            sb.append("\n");

        }

        reader.close();

        return sb.toString();

    }

    @SuppressWarnings("WeakerAccess")
    public static Level loadLevel(String filePath) throws IOException, LevelParseException {

        String[] lines = loadFile(filePath).split("\n");
        List<Block> blocks = new ArrayList<>();
        Vector2 startPos = new Vector2(0, 0);

        for (String line : lines) {

            String[] fields = line.split(",");
            if (fields[0].equalsIgnoreCase(""))
                throw new LevelParseException("No such block type.");

            BlockType type;

            switch (fields[0]) {
                case "solid":
                    type = BlockType.Solid;
                    break;
                case "start":
                    type = BlockType.Start;
                    break;
                default:
                    type = BlockType.Solid;
                    break;
            }

            int x = Integer.parseInt(fields[1]);
            int y = Integer.parseInt(fields[2]);

            if (type == BlockType.Start) {
                startPos = new Vector2(x, y);
                continue;
            }

            blocks.add(new Block(x, y, type));

        }

        return new Level(startPos, blocks);

    }

}
