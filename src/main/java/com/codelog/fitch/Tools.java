package com.codelog.fitch;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Tools {

    public static String loadFile(String filePath) throws IOException {

        FileReader fr = new FileReader(filePath);
        BufferedReader reader = new BufferedReader(fr);

        String contents = "";
        String line;

        while ((line = reader.readLine()) != null) {

            contents += line + "\n";

        }

        reader.close();

        return contents;

    }

}
