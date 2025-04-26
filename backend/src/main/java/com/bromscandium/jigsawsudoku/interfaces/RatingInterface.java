package com.bromscandium.jigsawsudoku.interfaces;

import com.bromscandium.jigsawsudoku.entity.Rating;
import com.bromscandium.jigsawsudoku.exception.RatingException;

import java.util.List;

public interface RatingInterface {
    void setRating(Rating rating) throws RatingException;

    int getAverageRating(String game) throws RatingException;

    int getRating(String game, String player) throws RatingException;

    void reset() throws RatingException;

    void updateRating(Rating rating) throws RatingException;

    List<Rating> getAllRatings();

    void deleteRating(int id) throws RatingException;
}
