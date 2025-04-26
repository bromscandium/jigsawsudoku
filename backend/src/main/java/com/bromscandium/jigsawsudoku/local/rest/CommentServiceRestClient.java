package com.bromscandium.jigsawsudoku.local.rest;

import com.bromscandium.jigsawsudoku.entity.Comment;
import com.bromscandium.jigsawsudoku.exception.CommentException;
import com.bromscandium.jigsawsudoku.interfaces.CommentInterface;

import io.github.cdimascio.dotenv.Dotenv;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class CommentServiceRestClient implements CommentInterface {
    Dotenv dotenv = Dotenv.configure().directory("../").load();
    String url = dotenv.get("COMMENT_SERVICE_URL");

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public void addComment(Comment comment) {
        restTemplate.postForEntity(url, comment, Comment.class);
    }

    @Override
    public List<Comment> getComments(String game) {
        return Arrays.asList(restTemplate.getForEntity(url + "/" + game, Comment[].class).getBody());
    }

    @Override
    public void reset() {
        throw new UnsupportedOperationException("Not supported via web service");
    }

    @Override
    public List<String> getAllPlayers() {
        return Arrays.asList(restTemplate.getForEntity(url + "/players", String[].class).getBody());
    }

    @Override
    public void updateComment(Comment comment) throws CommentException {
        try {
            restTemplate.put(url + "/comments/" + comment.getIdent(), comment);
        } catch (Exception e) {
            throw new CommentException("Error updating the comment: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Comment> getAllComments() {
        return Arrays.asList(restTemplate.getForEntity(url + "/all", Comment[].class).getBody());
    }

    @Override
    public void deleteComment(int id) throws CommentException {

    }
}
