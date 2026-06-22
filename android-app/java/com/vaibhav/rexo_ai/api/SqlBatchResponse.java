package com.vaibhav.quantumcore3.api;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class SqlBatchResponse {

    @SerializedName("status")
    private String status;

    @SerializedName("total_queries")
    private int totalQueries;

    @SerializedName("vulnerable_count")
    private int vulnerableCount;

    @SerializedName("safe_count")
    private int safeCount;

    @SerializedName("results")
    private List<QueryResult> results;

    // Getters
    public String getStatus() {
        return status;
    }

    public int getTotalQueries() {
        return totalQueries;
    }

    public int getVulnerableCount() {
        return vulnerableCount;
    }

    public int getSafeCount() {
        return safeCount;
    }

    public List<QueryResult> getResults() {
        return results;
    }

    public static class QueryResult {
        @SerializedName("query")
        private String query;

        @SerializedName("is_vulnerable")
        private boolean isVulnerable;

        @SerializedName("matched_patterns")
        private List<String> matchedPatterns;

        public String getQuery() {
            return query;
        }

        public boolean isVulnerable() {
            return isVulnerable;
        }

        public List<String> getMatchedPatterns() {
            return matchedPatterns;
        }
    }
}