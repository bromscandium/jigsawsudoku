package com.bromscandium.jigsawsudoku.server.jpa;

import com.bromscandium.jigsawsudoku.entity.Score;
import com.bromscandium.jigsawsudoku.exception.ScoreException;
import com.bromscandium.jigsawsudoku.interfaces.ScoreInterface;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import java.util.List;

@Service("scoreService")
@Transactional
public class ScoreServiceJPA implements ScoreInterface {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void addScore(Score score) {
        if (score != null) {
            entityManager.persist(score);
        } else {
            throw new IllegalArgumentException("Score cannot be null");
        }
    }

    @Override
    public List<Score> getTopScores(String game) {
        if (game == null || game.trim().isEmpty()) {
            throw new IllegalArgumentException("Game name cannot be null or empty");
        }

        TypedQuery<Score> query = entityManager.createNamedQuery("Score.getTopScores", Score.class);
        query.setParameter("game", game);
        query.setMaxResults(10);
        return query.getResultList();
    }

    @Override
    public void reset() {
        entityManager.createNamedQuery("Score.resetScores").executeUpdate();
    }

    @Override
    public void updateScore(Score score) throws ScoreException {
        if (score == null || score.getPlayer() == null || score.getGame() == null) {
            throw new IllegalArgumentException("Score, player, and game cannot be null");
        }

        Score existingScore = entityManager.find(Score.class, score.getIdent());

        if (existingScore == null) {
            entityManager.persist(score);
        } else {
            existingScore.setPoints(score.getPoints());
            existingScore.setPlayedOn(score.getPlayedOn());
            entityManager.merge(existingScore);
        }
    }


    @Override
    public List<Score> getScoresByPlayer(String player) {
        if (player == null || player.trim().isEmpty()) {
            throw new IllegalArgumentException("Player name cannot be null or empty");
        }

        return entityManager.createQuery("SELECT s FROM Score s WHERE s.player = :player", Score.class)
                .setParameter("player", player)
                .getResultList();
    }
}
