package com.bromscandium.jigsawsudoku.server.jpa;

import com.bromscandium.jigsawsudoku.entity.Comment;
import com.bromscandium.jigsawsudoku.exception.CommentException;
import com.bromscandium.jigsawsudoku.interfaces.CommentInterface;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import java.util.List;

@Service("commentService")
@Transactional
public class CommentServiceJPA implements CommentInterface {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void addComment(Comment comment) {
        if (comment == null || comment.getPlayer() == null || comment.getGame() == null) {
            throw new IllegalArgumentException("Comment, player, and game cannot be null");
        }
        entityManager.persist(comment);
    }

    @Override
    public List<Comment> getComments(String game) {
        if (game == null || game.trim().isEmpty()) {
            throw new IllegalArgumentException("Game name cannot be null or empty");
        }

        TypedQuery<Comment> query = entityManager.createNamedQuery("Comment.getComments", Comment.class);
        query.setParameter("game", game);
        query.setMaxResults(10);
        return query.getResultList();
    }

    @Override
    public List<String> getAllPlayers() {
        return entityManager.createQuery("SELECT DISTINCT c.player FROM Comment c", String.class)
                .getResultList();
    }

    @Override
    public void reset() {
        entityManager.createNamedQuery("Comment.resetComments").executeUpdate();
    }

    @Override
    public void updateComment(Comment comment) throws CommentException {
        if (comment == null || comment.getPlayer() == null || comment.getGame() == null) {
            throw new IllegalArgumentException("Comment, player, and game cannot be null");
        }

        Comment existingComment = entityManager.find(Comment.class, comment.getIdent());

        if (existingComment == null) {
            entityManager.persist(comment);
        } else {
            existingComment.setComment(comment.getComment());
            existingComment.setGame(comment.getGame());
            existingComment.setPlayer(comment.getPlayer());
            entityManager.merge(existingComment);
        }
    }

    @Override
    public List<Comment> getAllComments() {
        return entityManager.createQuery("SELECT c FROM Comment c ORDER BY c.commentedOn DESC", Comment.class)
                .getResultList();
    }

    @Override
    @Transactional
    public void deleteComment(int id) throws CommentException {
        Comment comment = entityManager.find(Comment.class, id);
        if (comment == null) {
            throw new CommentException("Comment not found");
        }
        entityManager.remove(comment);
    }
}
