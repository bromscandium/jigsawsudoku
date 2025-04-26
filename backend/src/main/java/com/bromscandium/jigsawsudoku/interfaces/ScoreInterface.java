package com.bromscandium.jigsawsudoku.interfaces;

import com.bromscandium.jigsawsudoku.entity.Score;
import com.bromscandium.jigsawsudoku.exception.ScoreException;

import java.util.List;

public interface ScoreInterface {
    void addScore(Score score) throws ScoreException;

    List<Score> getTopScores(String game) throws ScoreException;

    void reset() throws ScoreException;

    void updateScore(Score score) throws ScoreException;

    List<Score> getScoresByPlayer(String player);
}
