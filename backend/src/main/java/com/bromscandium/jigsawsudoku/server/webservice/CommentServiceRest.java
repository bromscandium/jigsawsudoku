package com.bromscandium.jigsawsudoku.server.webservice;

import com.bromscandium.jigsawsudoku.entity.Comment;
import com.bromscandium.jigsawsudoku.interfaces.CommentInterface;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/comment")
public class CommentServiceRest {

    @Autowired
    private CommentInterface commentInterface;

    @PostMapping
    public ResponseEntity<String> addComment(@RequestBody Comment comment) {
        commentInterface.addComment(comment);
        return ResponseEntity.ok("Comment added successfully!");
    }

    @GetMapping("/{game}")
    public ResponseEntity<List<Comment>> getComments(@PathVariable String game) {
        List<Comment> comments = commentInterface.getComments(game);
        if (comments.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(comments);
    }

    @GetMapping("/players")
    public ResponseEntity<List<String>> getPlayers() {
        List<String> players = commentInterface.getAllPlayers();
        return ResponseEntity.ok(players);
    }

    @PutMapping("/comment/update")
    public ResponseEntity<?> updateComment(@RequestBody Comment comment) {
        commentInterface.updateComment(comment);
        return ResponseEntity.ok(Map.of("message", "Comment updated successfully"));
    }

    @GetMapping("/all")
    public ResponseEntity<List<Comment>> getAllComments() {
        List<Comment> comments = commentInterface.getAllComments();
        if (comments.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(comments);
    }

    @DeleteMapping("/comment/delete/{id}")
    public void deleteComment(@PathVariable int id) {
        commentInterface.deleteComment(id);
    }
}
