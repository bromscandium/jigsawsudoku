package com.bromscandium.jigsawsudoku.server.dto;

public class BoardDTO {
    private int size;
    private int[][] regionMap;
    private CellDTO[][] cells;

    public BoardDTO() {
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int[][] getRegionMap() {
        return regionMap;
    }

    public void setRegionMap(int[][] regionMap) {
        this.regionMap = regionMap;
    }

    public CellDTO[][] getCells() {
        return cells;
    }

    public void setCells(CellDTO[][] cells) {
        this.cells = cells;
    }
}