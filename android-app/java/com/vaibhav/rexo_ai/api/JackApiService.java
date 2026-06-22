package com.vaibhav.quantumcore3.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import java.util.Map;

/**
 * Retrofit Interface for Jack AI API
 */
public interface JackApiService {

    // Health Check
    @GET("jack/health")
    Call<ApiResponse> healthCheck();

    // Ask Question
    @POST("jack/ask")
    Call<ApiResponse> askQuestion(@Body Map<String, String> request);

    // Process Command
    @POST("jack/command")
    Call<ApiResponse> processCommand(@Body Map<String, String> request);

    // Open Camera
    @POST("jack/open_camera")
    Call<ApiResponse> openCamera();

    // Open Browser
    @POST("jack/open_browser")
    Call<ApiResponse> openBrowser(@Body Map<String, String> request);

    // Search Google
    @POST("jack/search_google")
    Call<ApiResponse> searchGoogle(@Body Map<String, String> request);

    // Open Application
    @POST("jack/open_application")
    Call<ApiResponse> openApplication(@Body Map<String, String> request);

    // Close Application
    @POST("jack/close_application")
    Call<ApiResponse> closeApplication(@Body Map<String, String> request);

    // Close Website
    @POST("jack/close_website")
    Call<ApiResponse> closeWebsite();

    // Close Browser
    @POST("jack/close_browser")
    Call<ApiResponse> closeBrowser();

    // Add FAQ
    @POST("jack/add_faq")
    Call<ApiResponse> addFaq(@Body Map<String, String> request);

    // Get All FAQs
    @GET("jack/get_all_faqs")
    Call<ApiResponse> getAllFaqs();
}