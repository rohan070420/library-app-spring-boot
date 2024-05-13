package com.demo.springbootlibrary.errorResponse;

import lombok.Data;

@Data
public class CustomErrorResponse {
    private String message;
    private int errorCode; // Optionally, you can include an error code
    private Object additionalData; // Additional data related to the error, such as a stack trace

    // Getters and setters
}

