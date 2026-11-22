package com.staysphere.companymgmt.dto;

import java.time.Instant;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.List;

public class ApiErrorResponse {

    private final Instant timeStamp;

    private final int status;

    private final String error;

    private final String message;

    private final Map<String, List<String>> fieldErrors;

    public ApiErrorResponse(Instant timeStamp, int status, String error, String message, Map<String, List<String>> fieldErrors){
        this.timeStamp = timeStamp;
        this.status = status;
        this.error = error;
        this.message = message;
        this.fieldErrors = fieldErrors == null
                ? Map.of()
                : fieldErrors.entrySet().stream()
                    .collect(Collectors.toUnmodifiableMap(
                        Map.Entry::getKey,
                        entry -> List.copyOf(entry.getValue())
                    ));
    }

    public Instant getTimeStamp() {
        return timeStamp;
    }

    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public Map<String, List<String>> getFieldErrors() {
        return fieldErrors;
    }
    

}
