package com.bromscandium.jigsawsudoku.server.dto;

public class SaveDTO {
    private Long id;
    private UserDTO user;
    private String boardJson;
    private int attempts;
    private long timeSeconds;
    private String difficulty;
    private int size;

    public SaveDTO(Long id, UserDTO user, String boardJson, int attempts, long timeSeconds, String difficulty, int size) {
        this.id = id;
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

    public UserDTO getUser() {
        return user;
    }

    public String getBoardJson() {
        return boardJson;
    }

    public int getAttempts() {
        return attempts;
    }

    public long getTimeSeconds() {
        return timeSeconds;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public int getSize() {
        return size;
    }
}
