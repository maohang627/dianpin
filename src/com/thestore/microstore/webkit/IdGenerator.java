package com.thestore.microstore.webkit;

public class IdGenerator {

    private int                i        = 0;

    private static IdGenerator INSTANCE = null;

    private IdGenerator() {
    }

    public static IdGenerator getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new IdGenerator();
        }
        return INSTANCE;
    }

    public int nextId() {
        return i++;
    }

}
