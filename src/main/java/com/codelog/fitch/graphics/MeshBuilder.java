package com.codelog.fitch.graphics;

import java.util.ArrayList;
import java.util.List;

public class MeshBuilder {

    List<Integer> indices;
    List<Float> vertices;
    int currentIndex = 0;
    String fragmentShader;
    String vertexShader;

    public MeshBuilder() {

        indices = new ArrayList<>();
        vertices = new ArrayList<>();

    }

    public void addVertexShader(String vshader) { vertexShader = vshader; }
    public void addFragmentShader(String fshader) { fragmentShader = fshader; }

    public void addMeshElement(float[] elementData) {

        for (float d : elementData) {
            vertices.add(d);
            indices.add(currentIndex);
            currentIndex++;
        }

    }

    public Mesh toMesh() {

        float[] vertexData = new float[vertices.size()];
        int[] indexData = new int[indices.size()];

        for (int i : indices) {

            vertexData[i] = vertices.get(i);
            indexData[i] = indices.get(i);

        }

        return new Mesh(vertexData, indexData, vertexShader, fragmentShader);

    }

}
