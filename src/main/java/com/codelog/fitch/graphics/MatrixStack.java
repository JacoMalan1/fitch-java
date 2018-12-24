package com.codelog.fitch.graphics;

import java.util.List;
import java.util.Stack;

public class MatrixStack<T> {

    private List<T> matrices;
    private int numElements;

    public MatrixStack() {

        matrices = new Stack<>();
        numElements = 0;

    }

    public void push(T mat) {
        matrices.add(mat);
        numElements++;
    }

    public T pop() {

        T result = null;
        if (numElements > 0) {
            result = matrices.get(matrices.size() - 1);
            matrices.remove(matrices.size() - 1);
            numElements--;
        }

        return result;
    }

}
