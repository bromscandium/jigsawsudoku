package com.bromscandium.jigsawsudoku.game;

public class Board {
    private final int size;
    private Cell[][] cells;
    private int[][] regionMap;

    public Board(int size) {
        this.size = size;
        this.cells = new Cell[size][size];
        this.regionMap = new int[size][size];
        initializeCells();
    }

    public int getSize() {
        return size;
    }

    public Cell[][] getCells() {
        return cells;
    }

    public int[][] getRegionMap() {
        return regionMap;
    }

    public void setRegionMap(int[][] regionMap) {
        this.regionMap = regionMap;
    }

    private boolean isValidCoordinate(int row, int col) {
        return row >= 0 && row < size && col >= 0 && col < size;
    }

    private void initializeCells() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                cells[i][j] = new Cell(false, 0);
            }
        }
    }

    public void setCellValue(int row, int col, int value, boolean fixed) {
        if (isValidCoordinate(row, col)) {
            Cell cell = cells[row][col];
            cell.setValue(value);
            cell.setFixed(fixed);
        }
    }
}
