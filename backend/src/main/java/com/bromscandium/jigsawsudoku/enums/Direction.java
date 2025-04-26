package com.bromscandium.jigsawsudoku.enums;

public enum Direction {
    DOWN(1, 0),
    UP(-1, 0),
    RIGHT(0, 1),
    LEFT(0, -1);

    private final int dx;
    private final int dy;

    Direction(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public int getDx() {
        return dx;
    }

    public int getDy() {
        return dy;
    }
}
