package com.bromscandium.jigsawsudoku.exception;

public class CommentException extends RuntimeException {
    private String errorCode;

    public CommentException(String message) {
        super(message);
    }

    public CommentException(String message, Throwable cause) {
        super(message, cause);
    }

    public CommentException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
