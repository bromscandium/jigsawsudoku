package com.bromscandium.jigsawsudoku.server.webservice;

import com.bromscandium.jigsawsudoku.entity.Score;
import com.bromscandium.jigsawsudoku.interfaces.ScoreInterface;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/score")
public class ScoreServiceRest {

    @Autowired
    private ScoreInterface scoreInterface;

    @PostMapping
    public ResponseEntity<String> addScore(@RequestBody Score score) {
        scoreInterface.addScore(score);
        return ResponseEntity.ok("Score added successfully!");
    }

    @GetMapping("/{game}")
    public ResponseEntity<List<Score>> getTopScores(@PathVariable String game) {
        List<Score> scores = scoreInterface.getTopScores(game);
        if (scores.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(scores);
    }

    @PutMapping("/score/update")
    public ResponseEntity<?> updateScore(@RequestBody Score score) {
        scoreInterface.updateScore(score);
        return ResponseEntity.ok(Map.of("message", "Score updated successfully"));
    }

    @GetMapping("/player/{nickname}")
    public ResponseEntity<List<Score>> getScoresByPlayer(@PathVariable String nickname) {
        List<Score> scores = scoreInterface.getScoresByPlayer(nickname);
        if (scores.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(scores);
    }
}
