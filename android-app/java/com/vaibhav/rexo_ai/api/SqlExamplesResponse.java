package com.vaibhav.quantumcore3.api;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class SqlExamplesResponse {

    @SerializedName("status")
    private String status;

    @SerializedName("examples")
    private Examples examples;

    public String getStatus() {
        return status;
    }

    public Examples getExamples() {
        return examples;
    }

    public static class Examples {
        @SerializedName("vulnerable_queries")
        private List<String> vulnerableQueries;

        @SerializedName("safe_queries")
        private List<String> safeQueries;

        public List<String> getVulnerableQueries() {
            return vulnerableQueries;
        }

        public List<String> getSafeQueries() {
            return safeQueries;
        }
    }
}