package com.bromscandium.jigsawsudoku.local.jdbc;

import com.bromscandium.jigsawsudoku.entity.Comment;
import com.bromscandium.jigsawsudoku.interfaces.CommentInterface;
import com.bromscandium.jigsawsudoku.exception.CommentException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static com.bromscandium.jigsawsudoku.server.config.DbConfig.*;

public class CommentServiceJDBC implements CommentInterface {
    private static final String URL = getUrl();
    private static final String USER = getUser();
    private static final String PASSWORD = getPassword();
    public static final String INSERT = "INSERT INTO comment (player, game, comment, commentedon) VALUES (?, ?, ?, ?)";

    public static final String SELECT_DISTINCT_PLAYERS = "SELECT DISTINCT player FROM comment";

    public static final String SELECT = "SELECT player, game, comment, commentedon FROM comment WHERE game = ? ORDER BY commenton DESC";

    public static final String DELETE = "DELETE FROM comment";

    @Override
    public void addComment(Comment comment) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(INSERT)) {
            statement.setString(1, comment.getPlayer());
            statement.setString(2, comment.getGame());
            statement.setString(3, comment.getComment());
            statement.setTimestamp(4, new Timestamp(comment.getCommentedOn().getTime()));
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new CommentException("Error inserting comment", e);
        }
    }

    @Override
    public List<Comment> getComments(String game) {
        List<Comment> comments = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(SELECT)) {
            statement.setString(1, game);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    comments.add(new Comment(
                            rs.getString("game"),
                            rs.getString("player"),
                            rs.getString("comment"),
                            rs.getTimestamp("commentedOn")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new CommentException("Problem selecting comments", e);
        }
        return comments;
    }

    @Override
    public void reset() {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement st = connection.createStatement()) {
            st.executeUpdate(DELETE);
        } catch (SQLException e) {
            throw new CommentException("Problem deleting comments", e);
        }
    }

    @Override
    public List<String> getAllPlayers() {
        List<String> players = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(SELECT_DISTINCT_PLAYERS);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                players.add(rs.getString("player"));
            }
        } catch (SQLException e) {
            throw new CommentException("Problem selecting players", e);
        }
        return players;
    }

    @Override
    public void updateComment(Comment comment) throws CommentException {
        throw new CommentException("JDBC method for updating comment not implemented yet.");
    }

    @Override
    public List<Comment> getAllComments() {
        throw new CommentException("JDBC method for comment not implemented yet.");
    }

    @Override
    public void deleteComment(int id) throws CommentException {

    }
}
