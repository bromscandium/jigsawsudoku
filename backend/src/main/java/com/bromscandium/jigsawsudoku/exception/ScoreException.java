package com.bromscandium.jigsawsudoku.exception;

public class ScoreException extends RuntimeException {
    private String errorCode;

    public ScoreException(String message) {
        super(message);
    }

    public ScoreException(String message, Throwable cause) {
        super(message, cause);
    }

    public ScoreException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
