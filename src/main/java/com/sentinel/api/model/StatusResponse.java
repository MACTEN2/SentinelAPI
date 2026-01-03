package com.sentinel.api.model;

public class StatusResponse {
    public String status;
    public String message;
    public String version;

    public StatusResponse(String status, String message, String version) {
        this.status = status;
        this.message = message;
        this.version = version;
    }

    // A simple method to turn this into a JSON string manually
    public String toJson() {
        return String.format("{\"status\": \"%s\", \"message\": \"%s\", \"version\": \"%s\"}", 
                             status, message, version);
    }
}