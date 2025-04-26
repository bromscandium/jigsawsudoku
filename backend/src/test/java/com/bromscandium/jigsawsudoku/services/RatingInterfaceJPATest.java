package com.bromscandium.jigsawsudoku.services;

import com.bromscandium.jigsawsudoku.entity.Rating;
import com.bromscandium.jigsawsudoku.server.jpa.RatingServiceJPA;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RatingInterfaceJPATest {

    @PersistenceContext
    private EntityManager entityManager;

    private RatingServiceJPA ratingService;

    @BeforeEach
    void setup() {
        ratingService = new RatingServiceJPA();
        injectEntityManager(ratingService, entityManager);
    }

    private void injectEntityManager(RatingServiceJPA service, EntityManager em) {
        try {
            var field = RatingServiceJPA.class.getDeclaredField("entityManager");
            field.setAccessible(true);
            field.set(service, em);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(1)
    void testSetAndGetRating() {
        ratingService.setRating(new Rating("game1", "player1", 4, new Date()));
        int rating = ratingService.getRating("game1", "player1");
        assertEquals(4, rating);
    }

    @Test
    @Order(2)
    void testGetAverageRating() {
        ratingService.setRating(new Rating("game2", "player1", 4, new Date()));
        ratingService.setRating(new Rating("game2", "player2", 2, new Date()));
        int average = ratingService.getAverageRating("game2");
        assertEquals(3, average);
    }

    @Test
    @Order(3)
    void testResetRatings() {
        ratingService.setRating(new Rating("game3", "player", 5, new Date()));
        ratingService.reset();
        int ratingAfterReset = ratingService.getRating("game3", "player");
        assertEquals(0, ratingAfterReset, "Rating should be reset to 0");
    }
}
