package com.vaibhav.quantumcore3.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import java.util.Map;

/**
 * Retrofit Interface for Jarvis AI API
 *
 * This interface defines the API endpoint for Jarvis backend
 * We only define what we actually use to keep it simple
 */
public interface JarvisApiService {

    // ========== SMART COMMAND PROCESSING (Main endpoint) ==========

    /**
     * Process natural language commands
     * Endpoint: POST /jarvis/command
     *
     * Request body example:
     * {
     *   "command": "What's the time?"
     * }
     *
     * Response example:
     * {
     *   "status": "success",
     *   "command": "What's the time?",
     *   "response": "The current time is 02:30 PM",
     *   "data": {
     *     "time": "02:30 PM"
     *   }
     * }
     */
    @POST("jarvis/command")
    Call<JarvisCommandResponse> processCommand(@Body Map<String, String> request);

    // NOTE: You can add more endpoints here as needed in the future
    // For now, we only need the command endpoint since it handles everything
}