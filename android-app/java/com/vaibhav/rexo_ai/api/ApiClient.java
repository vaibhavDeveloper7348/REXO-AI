package com.vaibhav.quantumcore3.api;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import java.util.concurrent.TimeUnit;

/**
 * Retrofit API Client for all backend services
 * Updated with Jarvis AI Service
 */
public class ApiClient {

    // Base URLs for different APIs
    // Use 10.0.2.2 for Android Emulator (localhost equivalent)
    // Use your computer's IP address for real device (e.g., "http://192.168.1.5")

    private static final String JACK_BASE_URL = "http://192.168.43.90:5001/";
    private static final String JARVIS_BASE_URL = "http://192.168.43.90:5002/";
    private static final String SQL_BASE_URL = "http://192.168.43.90:5003/";

    private static Retrofit jackRetrofit = null;
    private static Retrofit jarvisRetrofit = null;
    private static Retrofit sqlRetrofit = null;

    /**
     * Create OkHttpClient with logging and timeouts
     */
    private static OkHttpClient createOkHttpClient() {
        // Add logging interceptor for debugging
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        return new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();
    }

    /**
     * Get Retrofit instance for Jack AI API
     */
    public static Retrofit getJackClient() {
        if (jackRetrofit == null) {
            jackRetrofit = new Retrofit.Builder()
                    .baseUrl(JACK_BASE_URL)
                    .client(createOkHttpClient())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return jackRetrofit;
    }

    /**
     * Get Retrofit instance for Jarvis AI API
     */
    public static Retrofit getJarvisClient() {
        if (jarvisRetrofit == null) {
            jarvisRetrofit = new Retrofit.Builder()
                    .baseUrl(JARVIS_BASE_URL)
                    .client(createOkHttpClient())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return jarvisRetrofit;
    }

    /**
     * Get Retrofit instance for SQL Detection API
     */
    public static Retrofit getSqlClient() {
        if (sqlRetrofit == null) {
            sqlRetrofit = new Retrofit.Builder()
                    .baseUrl(SQL_BASE_URL)
                    .client(createOkHttpClient())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return sqlRetrofit;
    }

    /**
     * Get Jack API Service
     */
    public static JackApiService getJackApiService() {
        return getJackClient().create(JackApiService.class);
    }

    /**
     * Get Jarvis API Service
     */
    public static JarvisApiService getJarvisApiService() {
        return getJarvisClient().create(JarvisApiService.class);
    }

    /**
     * Get SQL API Service
     */
    public static SqlApiService getSqlApiService() {
        return getSqlClient().create(SqlApiService.class);
    }

    /**
     * Reset all clients (useful for changing base URLs)
     */
    public static void resetClients() {
        jackRetrofit = null;
        jarvisRetrofit = null;
        sqlRetrofit = null;
    }

    /**
     * Update base URL for real device (use your computer's IP)
     * Example: setBaseUrlForRealDevice("192.168.1.100")
     */
    public static void setBaseUrlForRealDevice(String ipAddress) {
        // This would require rebuilding the clients with new URLs
        // For now, just log the instruction
        android.util.Log.i("ApiClient",
                "To use real device, change base URLs to: http://" + ipAddress + ":500X/");
    }
}

/*
 * ============================================================================
 * CONFIGURATION GUIDE
 * ============================================================================
 *
 * FOR ANDROID EMULATOR:
 * Change all base URLs to use 10.0.2.2 instead of 192.168.43.90:
 *
 *   private static final String JACK_BASE_URL = "http://10.0.2.2:5001/";
 *   private static final String JARVIS_BASE_URL = "http://10.0.2.2:5002/";
 *   private static final String SQL_BASE_URL = "http://10.0.2.2:5003/";
 *
 * ============================================================================
 *
 * FOR REAL ANDROID DEVICE:
 * 1. Find your computer's IP address:
 *    - Windows: ipconfig → IPv4 Address
 *    - Mac/Linux: ifconfig → inet address
 *
 * 2. Replace 192.168.43.90 with your computer's IP:
 *    Example: "http://192.168.1.100:5001/"
 *
 * 3. Make sure:
 *    - Phone and computer on same WiFi
 *    - Firewall allows connections on ports 5001, 5002, 5003
 *    - Python backend servers are running
 *
 * ============================================================================
 *
 * TESTING BACKEND:
 * Test if backends are accessible using browser or curl:
 *
 *   http://192.168.43.90:5001/jack/health
 *   http://192.168.43.90:5002/jarvis/health
 *   http://192.168.43.90:5003/sql/health
 *
 * ============================================================================
 */