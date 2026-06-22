package com.vaibhav.quantumcore3.api;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class SqlResponse {

    @SerializedName("status")
    private String status;

    @SerializedName("query")
    private String query;

    @SerializedName("is_vulnerable")
    private boolean isVulnerable;

    @SerializedName("vulnerability_level")
    private String vulnerabilityLevel;

    @SerializedName("matched_patterns")
    private List<String> matchedPatterns;

    @SerializedName("pattern_count")
    private int patternCount;

    @SerializedName("recommendation")
    private String recommendation;

    @SerializedName("error")
    private String error;

    @SerializedName("results")
    private SqlFileResults results;

    // Getters
    public String getStatus() {
        return status;
    }

    public String getQuery() {
        return query;
    }

    public boolean isVulnerable() {
        return isVulnerable;
    }

    public String getVulnerabilityLevel() {
        return vulnerabilityLevel;
    }

    public List<String> getMatchedPatterns() {
        return matchedPatterns;
    }

    public int getPatternCount() {
        return patternCount;
    }

    public String getRecommendation() {
        return recommendation;
    }

    public String getError() {
        return error;
    }

    public SqlFileResults getResults() {
        return results;
    }

    public boolean isSuccess() {
        return "success".equalsIgnoreCase(status);
    }

    // Inner class for file scan results
    public static class SqlFileResults {
        @SerializedName("attacker_ip")
        private String attackerIp;

        @SerializedName("attempt_count")
        private int attemptCount;

        @SerializedName("first_payload")
        private String firstPayload;

        @SerializedName("last_payload")
        private String lastPayload;

        @SerializedName("formatted_symbol_count")
        private int formattedSymbolCount;

        // Getters
        public String getAttackerIp() {
            return attackerIp;
        }

        public int getAttemptCount() {
            return attemptCount;
        }

        public String getFirstPayload() {
            return firstPayload;
        }

        public String getLastPayload() {
            return lastPayload;
        }

        public int getFormattedSymbolCount() {
            return formattedSymbolCount;
        }
    }
}