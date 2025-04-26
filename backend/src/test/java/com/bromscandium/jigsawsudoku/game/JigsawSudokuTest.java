package com.bromscandium.jigsawsudoku.game;

import com.bromscandium.jigsawsudoku.TestSudokuGameConfig;
import com.bromscandium.jigsawsudoku.enums.Difficulty;
import com.bromscandium.jigsawsudoku.local.SpringClient;
import com.bromscandium.jigsawsudoku.interfaces.CommentInterface;
import com.bromscandium.jigsawsudoku.interfaces.RatingInterface;
import com.bromscandium.jigsawsudoku.interfaces.ScoreInterface;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = "spring.main.web-application-type=none",
        classes = {SpringClient.class, TestSudokuGameConfig.class})
class JigsawSudokuTest {

    @Autowired
    private ScoreInterface scoreInterface;

    @Autowired
    private RatingInterface ratingInterface;

    @Autowired
    private CommentInterface commentService;

    private SudokuGame sudokuGame;
    private Board board;

    @BeforeEach
    void setUp() {
        sudokuGame = new SudokuGame(scoreInterface, ratingInterface, commentService);
        SudokuGenerator generator = new SudokuGenerator();
        board = generator.generateSudoku(3, Difficulty.EASY);
    }

    @Test
    void testValidSolution() {
        board.getCells()[0][0].setValue(1);
        board.getCells()[0][1].setValue(2);
        board.getCells()[0][2].setValue(3);
        board.getCells()[1][0].setValue(3);
        board.getCells()[1][1].setValue(1);
        board.getCells()[1][2].setValue(2);
        board.getCells()[2][0].setValue(2);
        board.getCells()[2][1].setValue(3);
        board.getCells()[2][2].setValue(1);

        assertTrue(SudokuGenerator.isValidSudokuSolution(board), "Board should be solved correctly");
    }

    @Test
    void testInvalidSolution() {
        board.getCells()[0][0].setValue(1);
        board.getCells()[0][1].setValue(1);
        board.getCells()[0][2].setValue(1);

        assertFalse(SudokuGenerator.isValidSudokuSolution(board), "Board should be incorrect");
    }

    @Test
    void testEmptyBoard() {
        assertFalse(SudokuGenerator.isValidSudokuSolution(board), "Sudoku board doesn't contain any solution");
    }

    @Test
    void testDifferentDifficulties() {
        SudokuGenerator generator = new SudokuGenerator();
        Board easyBoard = generator.generateSudoku(3, Difficulty.EASY);
        Board hardBoard = generator.generateSudoku(3, Difficulty.HARD);

        assertNotEquals(countFilledCells(easyBoard), countFilledCells(hardBoard),
                "Different difficulties should have different number of pre-filled cells");
    }

    private int countFilledCells(Board board) {
        int count = 0;
        for (int i = 0; i < board.getCells().length; i++) {
            for (int j = 0; j < board.getCells()[i].length; j++) {
                if (board.getCells()[i][j].getValue() != 0) {
                    count++;
                }
            }
        }
        return count;
    }
}
