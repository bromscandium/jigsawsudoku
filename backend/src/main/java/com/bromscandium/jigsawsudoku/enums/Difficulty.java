package com.bromscandium.jigsawsudoku.enums;

public enum Difficulty {
    EASY(1.9F, 1),
    MEDIUM(1.7F, 2),
    HARD(1.4F, 3);

    private final float number;
    private final int multiplier;

    Difficulty(float number, int multiplier) {
        this.number = number;
        this.multiplier = multiplier;
    }

    public float number() {
        return number;
    }

    public int multiplier() {
        return multiplier;
    }
}
