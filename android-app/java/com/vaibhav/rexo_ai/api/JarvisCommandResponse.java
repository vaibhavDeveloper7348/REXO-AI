package com.vaibhav.quantumcore3.api;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import java.util.Map;

/**
 * Jarvis AI Response Models
 * This file contains all response classes for Jarvis API
 *
 * IMPORTANT: Only ONE class can be public per file in Java.
 * All other classes are package-private (no modifier) so they can be used within the api package.
 */

// ========== BASE RESPONSE (Package-private) ==========
class BaseJarvisResponse {
    @SerializedName("status")
    protected String status;

    @SerializedName("error")
    protected String error;

    public String getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public boolean isSuccess() {
        return "success".equalsIgnoreCase(status);
    }

    public boolean hasError() {
        return error != null && !error.isEmpty();
    }
}

// ========== COMMAND RESPONSE (PUBLIC - Main one used in JarvisActivity) ==========
public class JarvisCommandResponse extends BaseJarvisResponse {
    @SerializedName("command")
    private String command;

    @SerializedName("response")
    private String response;

    @SerializedName("data")
    private Map<String, Object> data;

    public String getCommand() {
        return command;
    }

    public String getResponse() {
        return response;
    }

    public Map<String, Object> getData() {
        return data;
    }
}

// ========== HEALTH CHECK RESPONSE ==========
class JarvisHealthResponse extends BaseJarvisResponse {
    @SerializedName("message")
    private String message;

    @SerializedName("version")
    private String version;

    @SerializedName("features")
    private List<String> features;

    public String getMessage() {
        return message;
    }

    public String getVersion() {
        return version;
    }

    public List<String> getFeatures() {
        return features;
    }
}

// ========== TIME RESPONSE ==========
class JarvisTimeResponse extends BaseJarvisResponse {
    @SerializedName("time")
    private String time;

    public String getTime() {
        return time;
    }
}

// ========== DATE RESPONSE ==========
class JarvisDateResponse extends BaseJarvisResponse {
    @SerializedName("date")
    private String date;

    public String getDate() {
        return date;
    }
}

// ========== DATETIME RESPONSE ==========
class JarvisDateTimeResponse extends BaseJarvisResponse {
    @SerializedName("datetime")
    private DateTimeData datetime;

    public DateTimeData getDatetime() {
        return datetime;
    }

    static class DateTimeData {
        @SerializedName("date")
        private String date;

        @SerializedName("time")
        private String time;

        @SerializedName("day")
        private String day;

        @SerializedName("timestamp")
        private String timestamp;

        public String getDate() { return date; }
        public String getTime() { return time; }
        public String getDay() { return day; }
        public String getTimestamp() { return timestamp; }
    }
}

// ========== WEATHER RESPONSE ==========
class JarvisWeatherResponse extends BaseJarvisResponse {
    @SerializedName("city")
    private String city;

    @SerializedName("weather")
    private String weather;

    public String getCity() {
        return city;
    }

    public String getWeather() {
        return weather;
    }
}

// ========== WIKIPEDIA RESPONSE ==========
class JarvisWikipediaResponse extends BaseJarvisResponse {
    @SerializedName("topic")
    private String topic;

    @SerializedName("summary")
    private String summary;

    public String getTopic() {
        return topic;
    }

    public String getSummary() {
        return summary;
    }
}

// ========== JOKE RESPONSE ==========
class JarvisJokeResponse extends BaseJarvisResponse {
    @SerializedName("joke")
    private String joke;

    public String getJoke() {
        return joke;
    }
}

// ========== NEWS RESPONSE ==========
class JarvisNewsResponse extends BaseJarvisResponse {
    @SerializedName("headlines")
    private List<String> headlines;

    @SerializedName("count")
    private int count;

    public List<String> getHeadlines() {
        return headlines;
    }

    public int getCount() {
        return count;
    }
}

// ========== YOUTUBE RESPONSE ==========
class JarvisYoutubeResponse extends BaseJarvisResponse {
    @SerializedName("query")
    private String query;

    @SerializedName("youtube_url")
    private String youtubeUrl;

    public String getQuery() {
        return query;
    }

    public String getYoutubeUrl() {
        return youtubeUrl;
    }
}

// ========== IP RESPONSE ==========
class JarvisIpResponse extends BaseJarvisResponse {
    @SerializedName("ip_address")
    private String ipAddress;

    public String getIpAddress() {
        return ipAddress;
    }
}

// ========== LOCATION RESPONSE ==========
class JarvisLocationResponse extends BaseJarvisResponse {
    @SerializedName("location")
    private LocationData location;

    public LocationData getLocation() {
        return location;
    }

    static class LocationData {
        @SerializedName("city")
        private String city;

        @SerializedName("region")
        private String region;

        @SerializedName("country")
        private String country;

        @SerializedName("latitude")
        private double latitude;

        @SerializedName("longitude")
        private double longitude;

        @SerializedName("timezone")
        private String timezone;

        public String getCity() { return city; }
        public String getRegion() { return region; }
        public String getCountry() { return country; }
        public double getLatitude() { return latitude; }
        public double getLongitude() { return longitude; }
        public String getTimezone() { return timezone; }
    }
}

// ========== CALCULATOR RESPONSE ==========
class JarvisCalculatorResponse extends BaseJarvisResponse {
    @SerializedName("expression")
    private String expression;

    @SerializedName("result")
    private double result;

    public String getExpression() {
        return expression;
    }

    public double getResult() {
        return result;
    }
}

// ========== SYSTEM STATS RESPONSE ==========
class JarvisSystemStatsResponse extends BaseJarvisResponse {
    @SerializedName("stats")
    private SystemStats stats;

    public SystemStats getStats() {
        return stats;
    }

    static class SystemStats {
        @SerializedName("cpu_percent")
        private double cpuPercent;

        @SerializedName("memory")
        private MemoryInfo memory;

        @SerializedName("disk")
        private DiskInfo disk;

        @SerializedName("battery")
        private BatteryInfo battery;

        public double getCpuPercent() { return cpuPercent; }
        public MemoryInfo getMemory() { return memory; }
        public DiskInfo getDisk() { return disk; }
        public BatteryInfo getBattery() { return battery; }
    }

    static class MemoryInfo {
        @SerializedName("used")
        private String used;

        @SerializedName("total")
        private String total;

        @SerializedName("percent")
        private double percent;

        public String getUsed() { return used; }
        public String getTotal() { return total; }
        public double getPercent() { return percent; }
    }

    static class DiskInfo {
        @SerializedName("used")
        private String used;

        @SerializedName("total")
        private String total;

        @SerializedName("percent")
        private double percent;

        public String getUsed() { return used; }
        public String getTotal() { return total; }
        public double getPercent() { return percent; }
    }

    static class BatteryInfo {
        @SerializedName("percent")
        private double percent;

        @SerializedName("plugged")
        private boolean plugged;

        @SerializedName("time_left")
        private Object timeLeft;

        public double getPercent() { return percent; }
        public boolean isPlugged() { return plugged; }
        public Object getTimeLeft() { return timeLeft; }
    }
}

// ========== USER INFO RESPONSE ==========
class JarvisUserInfoResponse extends BaseJarvisResponse {
    @SerializedName("user_info")
    private Map<String, String> userInfo;

    public Map<String, String> getUserInfo() {
        return userInfo;
    }
}

// ========== APP CONTROL RESPONSE ==========
class JarvisAppControlResponse extends BaseJarvisResponse {
    @SerializedName("message")
    private String message;

    @SerializedName("app_name")
    private String appName;

    public String getMessage() {
        return message;
    }

    public String getAppName() {
        return appName;
    }
}

// ========== WEBSITE RESPONSE ==========
class JarvisWebsiteResponse extends BaseJarvisResponse {
    @SerializedName("message")
    private String message;

    @SerializedName("url")
    private String url;

    public String getMessage() {
        return message;
    }

    public String getUrl() {
        return url;
    }
}