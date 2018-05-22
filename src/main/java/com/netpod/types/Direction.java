package com.netpod.types;

public enum Direction {
    NORTH('N'), SOUTH('S'), EAST('E'), WEST('W');

    private final char code;

    Direction(final char pCode) {
        this.code = pCode;
    }

    public char getCode() {
        return code;
    }

    public boolean is(final char code) {
        return this.code == code;
    }
}
