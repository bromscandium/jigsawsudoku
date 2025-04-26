package com.bromscandium.jigsawsudoku.local.rest;

import com.bromscandium.jigsawsudoku.entity.Score;
import com.bromscandium.jigsawsudoku.exception.ScoreException;
import com.bromscandium.jigsawsudoku.interfaces.ScoreInterface;

import io.github.cdimascio.dotenv.Dotenv;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class ScoreServiceRestClient implements ScoreInterface {
    Dotenv dotenv = Dotenv.configure().directory("../").load();
    String url = dotenv.get("SCORE_SERVICE_URL");

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public void addScore(Score score) {
        restTemplate.postForEntity(url, score, Score.class);
    }

    @Override
    public List<Score> getTopScores(String game) {
        return Arrays.asList(restTemplate.getForEntity(url + "/" + game, Score[].class).getBody());
    }

    @Override
    public void reset() {
        throw new UnsupportedOperationException("Not supported via web service");
    }

    @Override
    public void updateScore(Score score) throws ScoreException {
        try {
            restTemplate.put(url + "/scores/" + score.getIdent(), score);
        } catch (Exception e) {
            throw new ScoreException("Error updating the score: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Score> getScoresByPlayer(String player) {
        return Arrays.asList(
                restTemplate.getForEntity(url + "/player/" + player, Score[].class).getBody()
        );
    }
}
