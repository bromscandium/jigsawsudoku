package com.bromscandium.jigsawsudoku.services;

import com.bromscandium.jigsawsudoku.entity.Score;
import com.bromscandium.jigsawsudoku.server.jpa.ScoreServiceJPA;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ScoreInterfaceJPATest {

    @PersistenceContext
    private EntityManager entityManager;

    private ScoreServiceJPA scoreService;

    @BeforeEach
    void setup() {
        scoreService = new ScoreServiceJPA();
        injectEntityManager(scoreService, entityManager);
    }

    private void injectEntityManager(ScoreServiceJPA service, EntityManager em) {
        try {
            var field = ScoreServiceJPA.class.getDeclaredField("entityManager");
            field.setAccessible(true);
            field.set(service, em);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(1)
    void testAddScore() {
        scoreService.addScore(new Score("game1", "player1", 150, new Date()));
        List<Score> scores = scoreService.getTopScores("game1");
        assertFalse(scores.isEmpty());
        assertEquals(150, scores.get(0).getPoints());
    }

    @Test
    @Order(2)
    void testTopScoresLimit() {
        for (int i = 0; i < 15; i++) {
            scoreService.addScore(new Score("game2", "player" + i, 100 + i, new Date()));
        }
        List<Score> scores = scoreService.getTopScores("game2");
        assertEquals(10, scores.size(), "Should return top 10 scores");
    }

    @Test
    @Order(3)
    void testResetScores() {
        scoreService.addScore(new Score("game3", "player", 200, new Date()));
        scoreService.reset();
        List<Score> scores = scoreService.getTopScores("game3");
        assertTrue(scores.isEmpty(), "Scores should be empty after reset");
    }
}
