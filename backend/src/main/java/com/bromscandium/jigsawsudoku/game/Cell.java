package com.bromscandium.jigsawsudoku.game;

public class Cell {
    private int value;
    private boolean fixed;

    public Cell(boolean fixed, int value) {
        this.fixed = fixed;
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public boolean isFixed() {
        return fixed;
    }

    public void setFixed(boolean fixed) {
        this.fixed = fixed;
    }
}
