package com.vaibhav.quantumcore3.api;

import com.google.gson.annotations.SerializedName;

/**
 * Generic API Response Model for all API responses
 */
public class ApiResponse {

    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private String message;

    @SerializedName("answer")
    private String answer;

    @SerializedName("question")
    private String question;

    @SerializedName("error")
    private String error;

    @SerializedName("action")
    private String action;

    @SerializedName("url")
    private String url;

    @SerializedName("query")
    private String query;

    // Constructors
    public ApiResponse() {
    }

    public ApiResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }

    // Getters and Setters
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    // Helper methods
    public boolean isSuccess() {
        return "success".equalsIgnoreCase(status);
    }

    public boolean hasError() {
        return error != null && !error.isEmpty();
    }

    @Override
    public String toString() {
        return "ApiResponse{" +
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", answer='" + answer + '\'' +
                ", error='" + error + '\'' +
                '}';
    }
}