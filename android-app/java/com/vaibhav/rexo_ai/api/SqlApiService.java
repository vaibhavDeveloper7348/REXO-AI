package com.vaibhav.quantumcore3.api;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import java.util.Map;

/**
 * Retrofit Interface for SQL Injection Detection API
 */
public interface SqlApiService {

    // Health Check
    @GET("sql/health")
    Call<ApiResponse> healthCheck();

    // Scan single query
    @POST("sql/scan_query")
    Call<SqlResponse> scanQuery(@Body Map<String, String> request);

    // Scan CSV file
    @Multipart
    @POST("sql/scan_file")
    Call<SqlResponse> scanFile(@Part MultipartBody.Part file);

    // Get test examples
    @GET("sql/test_examples")
    Call<SqlExamplesResponse> getTestExamples();

    // Batch scan
    @POST("sql/batch_scan")
    Call<SqlBatchResponse> batchScan(@Body Map<String, Object> request);
}