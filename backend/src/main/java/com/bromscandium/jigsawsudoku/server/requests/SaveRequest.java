package com.bromscandium.jigsawsudoku.server.requests;

public class SaveRequest {
    private String login;
    private String boardJson;
    private int attempts;
    private long timeSeconds;
    private String difficulty;
    private int size;


    public SaveRequest(String login, String boardJson, int attempts, long timeSeconds, String difficulty, int size) {
        this.login = login;
        this.boardJson = boardJson;
        this.attempts = attempts;
        this.timeSeconds = timeSeconds;
        this.difficulty = difficulty;
        this.size = size;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
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
