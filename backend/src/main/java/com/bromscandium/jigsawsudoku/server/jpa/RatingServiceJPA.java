package com.bromscandium.jigsawsudoku.server.jpa;

import com.bromscandium.jigsawsudoku.entity.Rating;
import com.bromscandium.jigsawsudoku.exception.RatingException;
import com.bromscandium.jigsawsudoku.interfaces.RatingInterface;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;

@Service("ratingService")
@Transactional
public class RatingServiceJPA implements RatingInterface {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void setRating(Rating rating) {
        if (rating != null) {
            entityManager.persist(rating);
        } else {
            throw new IllegalArgumentException("Rating cannot be null");
        }
    }

    @Override
    public int getAverageRating(String game) {
        if (game == null || game.trim().isEmpty()) {
            throw new IllegalArgumentException("Game name cannot be null or empty");
        }

        Double result = (Double) entityManager
                .createNamedQuery("Rating.getAverageRating")
                .setParameter("game", game)
                .getSingleResult();
        return result != null ? result.intValue() : 0;
    }

    @Override
    public void reset() {
        entityManager.createNamedQuery("Rating.resetRatings").executeUpdate();
    }

    @Override
    public int getRating(String game, String player) {
        if (game == null || game.trim().isEmpty() || player == null || player.trim().isEmpty()) {
            throw new IllegalArgumentException("Game and player cannot be null or empty");
        }

        try {
            Integer result = (Integer) entityManager.createQuery(
                            "SELECT r.rating FROM Rating r WHERE r.game = :game AND r.player = :player ORDER BY r.ratedOn DESC")
                    .setParameter("game", game)
                    .setParameter("player", player)
                    .setMaxResults(1)
                    .getSingleResult();

            return result != null ? result : 0;
        } catch (Exception e) {
            // Log the exception here if necessary
            return 0;
        }
    }

    @Override
    public void updateRating(Rating rating) throws RatingException {
        if (rating == null || rating.getPlayer() == null || rating.getGame() == null) {
            throw new IllegalArgumentException("Rating, player, and game cannot be null");
        }

        Rating existingRating = entityManager.find(Rating.class, rating.getIdent());

        if (existingRating == null) {
            entityManager.persist(rating);
        } else {
            existingRating.setRating(rating.getRating());
            existingRating.setRatedOn(rating.getRatedOn());
            entityManager.merge(existingRating);
        }
    }

    @Override
    public List<Rating> getAllRatings() {
        return entityManager.createQuery("SELECT c FROM Rating c ORDER BY c.ratedOn DESC", Rating.class)
                .getResultList();
    }

    @Override
    @Transactional
    public void deleteRating(int id) throws RatingException {
        Rating rating = entityManager.find(Rating.class, id);
        if (rating == null) {
            throw new RatingException("Rating not found");
        }
        entityManager.remove(rating);
    }
}
