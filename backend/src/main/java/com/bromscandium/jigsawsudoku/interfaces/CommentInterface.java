package com.bromscandium.jigsawsudoku.interfaces;

import com.bromscandium.jigsawsudoku.entity.Comment;
import com.bromscandium.jigsawsudoku.exception.CommentException;

import java.util.List;

public interface CommentInterface {
    void addComment(Comment comment) throws CommentException;

    List<Comment> getComments(String game) throws CommentException;

    List<String> getAllPlayers();

    void reset() throws CommentException;

    void updateComment(Comment comment) throws CommentException;

    List<Comment> getAllComments();

    void deleteComment(int id) throws CommentException;
}
