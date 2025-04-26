package com.bromscandium.jigsawsudoku.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "saves")
public class Save {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @Lob
    @Column(nullable = false, columnDefinition = "TEXT")
    private String boardJson;

    @Column(nullable = false)
    private int attempts;

    @Column(nullable = false)
    private long timeSeconds;

    @Column(nullable = false)
    private String difficulty;

    @Column(nullable = false)
    private int size;

    public Save() {
    }

    public Save(User user, String boardJson, int attempts, long timeSeconds, String difficulty, int size) {
        this.user = user;
        this.boardJson = boardJson;
        this.attempts = attempts;
        this.timeSeconds = timeSeconds;
        this.difficulty = difficulty;
        this.size = size;
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getBoardJson() {
        return boardJson;
    }

    public void setBoardJson(String boardJson) {
        this.boardJson = boardJson;
    }

    public int getAttempts() {
        return attempts;
    }

    public void setAttempts(int attempts) {
        this.attempts = attempts;
    }

    public long getTimeSeconds() {
        return timeSeconds;
    }

    public void setTimeSeconds(long timeSeconds) {
        this.timeSeconds = timeSeconds;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
