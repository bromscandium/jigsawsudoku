package com.bromscandium.jigsawsudoku.local.rest;

import com.bromscandium.jigsawsudoku.entity.Rating;
import com.bromscandium.jigsawsudoku.exception.RatingException;
import com.bromscandium.jigsawsudoku.interfaces.RatingInterface;

import io.github.cdimascio.dotenv.Dotenv;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class RatingServiceRestClient implements RatingInterface {
    Dotenv dotenv = Dotenv.configure().directory("../").load();
    String url = dotenv.get("RATING_SERVICE_URL");

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public void setRating(Rating rating) {
        restTemplate.postForEntity(url, rating, Rating.class);
    }

    @Override
    public int getAverageRating(String game) {
        return restTemplate.getForObject(url + "/" + game, Integer.class);
    }

    @Override
    public int getRating(String game, String player) {
        return restTemplate.getForObject(url + "/" + game + "/" + player, Integer.class);
    }

    @Override
    public void reset() {
        throw new UnsupportedOperationException("Not supported via web service");
    }

    @Override
    public void updateRating(Rating rating) throws RatingException {
        try {
            restTemplate.put(url + "/ratings/" + rating.getIdent(), rating);
        } catch (Exception e) {
            throw new RatingException("Error updating the rating: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Rating> getAllRatings() {
        return Arrays.asList(restTemplate.getForObject(url + "/all", Rating[].class));
    }

    @Override
    public void deleteRating(int id) throws RatingException {

    }
}
