package com.example.newsfeed.exception;

import com.example.newsfeed.exception.ErrorCode;

public class CustomException extends BaseException {

    public CustomException(ErrorCode errorCode) {
        super(errorCode.getMessage(), errorCode.getStatus());
    }
}
