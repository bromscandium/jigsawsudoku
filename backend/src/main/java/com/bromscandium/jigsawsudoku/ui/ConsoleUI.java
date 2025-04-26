package com.bromscandium.jigsawsudoku.ui;

import com.bromscandium.jigsawsudoku.game.Board;

public class ConsoleUI {
    public static void printBoard(Board board) {
        int size = board.getSize();
        System.out.println("Current board:");
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                int value = board.getCells()[row][col].getValue();
                int regionId = board.getRegionMap()[row][col];
                char regionChar = (char) ('a' + regionId - 1);
                String cell = (value == 0 ? "_" : String.valueOf(value)) + regionChar;
                System.out.print(cell + (col < size - 1 ? " " : ""));
            }
            System.out.println();
        }
    }

    public static void printHelp() {
        System.out.println("-------------------------------------");
        System.out.println("help - Check all commands");
        System.out.println("a <row> <col> <number> - Add number to the board");
        System.out.println("d <row> <col> - Delete number from the board");
        System.out.println("score - Show top scores");
        System.out.println("rate - Add rating");
        System.out.println("comment - Comment game");
        System.out.println("exit - Exit game");
    }
}
