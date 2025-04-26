package com.bromscandium.jigsawsudoku.exception;

public class RatingException extends RuntimeException {
    private String errorCode;

    public RatingException(String message) {
        super(message);
    }

    public RatingException(String message, Throwable cause) {
        super(message, cause);
    }

    public RatingException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
