package com.bromscandium.jigsawsudoku;

import com.bromscandium.jigsawsudoku.game.SudokuGame;
import com.bromscandium.jigsawsudoku.interfaces.CommentInterface;
import com.bromscandium.jigsawsudoku.interfaces.RatingInterface;
import com.bromscandium.jigsawsudoku.interfaces.ScoreInterface;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestSudokuGameConfig {
    @Bean
    public SudokuGame sudokuGame(ScoreInterface scoreInterface,
                                 RatingInterface ratingInterface,
                                 CommentInterface commentService) {
        return new SudokuGame(scoreInterface, ratingInterface, commentService) {
            @Override
            public boolean startGame() {
                System.out.println("[TEST] startGame() is stubbed.");
                return false;
            }
        };
    }
}