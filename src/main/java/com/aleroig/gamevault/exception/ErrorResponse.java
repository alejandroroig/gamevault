package com.aleroig.gamevault.exception;

import java.util.Map;

public record ErrorResponse(
        String timestamp,
        int status,
        String error,
        String message,
        String path,
        Map<String, String> fields
) {
    public ErrorResponse(String timestamp, int status, String error, String message, String path) {
        this(timestamp, status, error, message, path, Map.of());
    }
}
