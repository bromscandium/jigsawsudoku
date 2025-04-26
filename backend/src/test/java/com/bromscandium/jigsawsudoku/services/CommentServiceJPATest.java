package com.bromscandium.jigsawsudoku.services;

import com.bromscandium.jigsawsudoku.entity.Comment;
import com.bromscandium.jigsawsudoku.server.jpa.CommentServiceJPA;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CommentServiceJPATest {

    @PersistenceContext
    private EntityManager entityManager;

    private CommentServiceJPA commentService;

    @BeforeEach
    void setup() {
        commentService = new CommentServiceJPA();
        injectEntityManager(commentService, entityManager);
    }

    private void injectEntityManager(CommentServiceJPA service, EntityManager em) {
        try {
            var field = CommentServiceJPA.class.getDeclaredField("entityManager");
            field.setAccessible(true);
            field.set(service, em);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(1)
    void testAddComment() {
        Comment comment = new Comment("game1", "player1", "Nice!", new Date());
        commentService.addComment(comment);

        List<Comment> comments = commentService.getComments("game1");
        assertFalse(comments.isEmpty());
        assertEquals("Nice!", comments.get(0).getComment());
    }

    @Test
    @Order(2)
    void testGetCommentsLimit() {
        for (int i = 0; i < 15; i++) {
            commentService.addComment(new Comment("game2", "player" + i, "Good!", new Date()));
        }

        List<Comment> comments = commentService.getComments("game2");
        assertEquals(10, comments.size(), "Should return only 10 comments");
    }

    @Test
    @Order(3)
    void testResetComments() {
        commentService.addComment(new Comment("game3", "player", "Reset me!", new Date()));

        commentService.reset();
        List<Comment> comments = commentService.getComments("game3");
        assertTrue(comments.isEmpty(), "Comments should be empty after reset");
    }
}
