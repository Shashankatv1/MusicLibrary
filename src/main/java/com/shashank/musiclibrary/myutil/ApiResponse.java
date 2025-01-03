package com.shashank.musiclibrary.myutil;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiResponse<T> {
    private int status;
    private T data;
    private String message;
    private String error;

    // Constructors
    public ApiResponse(int status, T data, String message) {
        this.status = status;
        this.data = data;
        this.message = message;
        this.error = null; // error is null when the request is successful
    }

    public ApiResponse(int status, String message, String error) {
        this.status = status;
        this.message = message;
        this.error = error;
        this.data = null; // no data in error responses
    }
}
