package com.commuteflow.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;
import java.util.Map;

@Getter
@AllArgsConstructor
public class ApiError {

    private boolean success;
    private String message;
    private int status;
    private Instant timestamp;
    private String path;
    private Map<String, String> errors;
}