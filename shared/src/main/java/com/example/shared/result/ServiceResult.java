package com.example.shared.result;

public class ServiceResult {
    /**
     * Default Message for Result Objects, used to store errors
     */
    public String message;
    /**
     * Default boolean for success of Service Requests
     */
    public boolean success;

    public ServiceResult(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

}
