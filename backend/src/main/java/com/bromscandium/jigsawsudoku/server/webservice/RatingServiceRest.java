package com.bromscandium.jigsawsudoku.server.webservice;

import com.bromscandium.jigsawsudoku.entity.Rating;
import com.bromscandium.jigsawsudoku.interfaces.RatingInterface;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/rating")
public class RatingServiceRest {

    @Autowired
    private RatingInterface ratingInterface;

    @PostMapping
    public ResponseEntity<String> setRating(@RequestBody Rating rating) {
        ratingInterface.setRating(rating);
        return ResponseEntity.ok("Rating added successfully!");
    }

    @GetMapping("/average/{game}")
    public ResponseEntity<Integer> getAverageRating(@PathVariable String game) {
        int averageRating = ratingInterface.getAverageRating(game);
        return ResponseEntity.ok(averageRating);
    }

    @GetMapping("/{game}/{player}")
    public ResponseEntity<Integer> getRating(@PathVariable String game, @PathVariable String player) {
        int rating = ratingInterface.getRating(game, player);
        return ResponseEntity.ok(rating);
    }

    @PutMapping("/rating/update")
    public ResponseEntity<?> updateRating(@RequestBody Rating rating) {
        ratingInterface.updateRating(rating);
        return ResponseEntity.ok(Map.of("message", "Rating updated successfully"));
    }

    @GetMapping("/all")
    public ResponseEntity<List<Rating>> getAllRatings() {
        List<Rating> ratings = ratingInterface.getAllRatings();
        return ResponseEntity.ok(ratings);
    }

    @DeleteMapping("/rating/delete/{id}")
    public void deleteRating(@PathVariable int id) {
        ratingInterface.deleteRating(id);
    }
}
