package com.bromscandium.jigsawsudoku.game;

import static java.lang.Math.round;

import org.springframework.stereotype.Service;

import com.bromscandium.jigsawsudoku.enums.*;

import java.util.*;

@Service
public class SudokuGenerator {
    public Board generateSudoku(int size, Difficulty difficulty) {
        Board board = null;
        boolean correctSolution = false;

        while (!correctSolution) {
            board = new Board(size);
            initializeRegionMap(board);
            generateNumbersForBoard(board);
            correctSolution = isBoardFilled(board);
        }

        removeNumbers(board, difficulty);
        return board;
    }

    private boolean isBoardFilled(Board board) {
        for (int i = 0; i < board.getSize(); i++) {
            for (int j = 0; j < board.getSize(); j++) {
                if (board.getCells()[i][j].getValue() == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    private void initializeRegionMap(Board board) {
        int size = board.getSize();
        int[][] regionMap = new int[0][];
        boolean generatedCorrectly = false;

        while (!generatedCorrectly) {
            regionMap = new int[size][size];
            for (int i = 0; i < size; i++) {
                Arrays.fill(regionMap[i], -1);
            }

            int regionIndex = 1;
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    if (regionMap[i][j] == -1) {
                        fillRegion(regionMap, i, j, regionIndex, size);
                        regionIndex++;
                    }
                }
            }

            generatedCorrectly = isValidRegionMap(regionMap, size, board);
        }

        board.setRegionMap(regionMap);
    }

    private boolean isValidRegionMap(int[][] regionMap, int size, Board board) {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (regionMap[i][j] > board.getSize()) {
                    return false;
                }
            }
        }
        return true;
    }

    private void fillRegion(int[][] regionMap, int startX, int startY, int regionIndex, int size) {
        int cellsCount = 1;
        Random random = new Random();

        Queue<int[]> queue = new LinkedList<>();
        queue.add(new int[]{startX, startY});
        regionMap[startX][startY] = regionIndex;

        List<Direction> directionsList = Arrays.asList(Direction.values());

        while (!queue.isEmpty() && cellsCount < size) {
            int[] cell = queue.poll();
            int startIdx = random.nextInt(directionsList.size());

            for (int i = 0; i < directionsList.size(); i++) {
                Direction dir = directionsList.get((startIdx + i) % directionsList.size());
                int newX = cell[0] + dir.getDx();
                int newY = cell[1] + dir.getDy();

                if (newX >= 0 && newX < size && newY >= 0 && newY < size && regionMap[newX][newY] == -1) {
                    regionMap[newX][newY] = regionIndex;
                    queue.add(new int[]{newX, newY});
                    cellsCount++;
                    if (cellsCount >= size) break;
                }
            }
        }
    }

    private void generateNumbersForBoard(Board board) {
        solveBoard(board, 0, 0);
    }

    private boolean solveBoard(Board board, int row, int col) {
        int size = board.getSize();
        if (row == size) return true;

        int nextRow = (col == size - 1) ? row + 1 : row;
        int nextCol = (col == size - 1) ? 0 : col + 1;

        List<Integer> numbers = new ArrayList<>();
        for (int i = 1; i <= size; i++) {
            numbers.add(i);
        }
        Collections.shuffle(numbers);

        for (int num : numbers) {
            if (!isValidPlacement(board, row, col, num)) continue;

            board.setCellValue(row, col, num, true);
            if (solveBoard(board, nextRow, nextCol)) {
                return true;
            }
            board.setCellValue(row, col, 0, false);
        }
        return false;
    }

    private boolean isValidPlacement(Board board, int row, int col, int num) {
        int size = board.getSize();
        for (int i = 0; i < size; i++) {
            if (board.getCells()[row][i].getValue() == num || board.getCells()[i][col].getValue() == num) {
                return false;
            }
        }

        int regionId = board.getRegionMap()[row][col];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (i == row && j == col) continue;
                if (board.getRegionMap()[i][j] == regionId && board.getCells()[i][j].getValue() == num) {
                    return false;
                }
            }
        }
        return true;
    }

    private void removeNumbers(Board board, Difficulty difficulty) {
        int size = board.getSize();
        int totalCells = size * size;
        int cellsToRemove = round(totalCells / difficulty.number());
        Random random = new Random();
        for (int i = 0; i < cellsToRemove; i++) {
            int row, col;
            do {
                row = random.nextInt(size);
                col = random.nextInt(size);
            } while (board.getCells()[row][col].getValue() == 0);
            board.setCellValue(row, col, 0, false);
        }
    }

    public static boolean isValidSudokuSolution(Board board) {
        int size = board.getSize();

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                int value = board.getCells()[i][j].getValue();
                if (value != 0) {
                    for (int k = 0; k < size; k++) {
                        if (k != j && board.getCells()[i][k].getValue() == value) {
                            return false;
                        }
                    }

                    for (int k = 0; k < size; k++) {
                        if (k != i && board.getCells()[k][j].getValue() == value) {
                            return false;
                        }
                    }

                    int regionId = board.getRegionMap()[i][j];
                    for (int x = 0; x < size; x++) {
                        for (int y = 0; y < size; y++) {
                            if (board.getRegionMap()[x][y] == regionId && (x != i || y != j)) {
                                if (board.getCells()[x][y].getValue() == value) {
                                    return false;
                                }
                            }
                        }
                    }
                } else {
                    return false;
                }
            }
        }

        return true;
    }
}