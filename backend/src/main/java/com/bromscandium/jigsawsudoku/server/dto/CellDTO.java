package com.bromscandium.jigsawsudoku.server.dto;

public class CellDTO {
    private int value;
    private boolean fixed;

    public CellDTO() {
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