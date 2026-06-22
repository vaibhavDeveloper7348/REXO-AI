package com.vaibhav.quantumcore3;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.vaibhav.quantumcore3.api.ApiClient;
import com.vaibhav.quantumcore3.api.ApiResponse;
import com.vaibhav.quantumcore3.api.SqlApiService;
import com.vaibhav.quantumcore3.api.SqlResponse;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SqlDetectionActivity extends AppCompatActivity {

    private EditText etSqlCode;
    private CardView btnScanVulnerabilities, btnUploadFile;
    private CardView cardVulnerableExample, cardSecureExample;

    // API Service
    private SqlApiService sqlApiService;

    // File picker request code
    private static final int REQUEST_FILE_PICKER = 300;
    private static final int REQUEST_STORAGE_PERMISSION = 301;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sql_detection);

        // Initialize API Service
        sqlApiService = ApiClient.getSqlApiService();

        // Initialize views
        initializeViews();

        // Setup button listeners
        setupButtonListeners();

        // Check API health
        checkApiHealth();
    }

    private void initializeViews() {
        etSqlCode = findViewById(R.id.etSqlCode);
        btnScanVulnerabilities = findViewById(R.id.btnScanVulnerabilities);
        btnUploadFile = findViewById(R.id.btnUploadFile);
        cardVulnerableExample = findViewById(R.id.cardVulnerableExample);
        cardSecureExample = findViewById(R.id.cardSecureExample);
    }

    /**
     * Check if SQL API is online
     */
    private void checkApiHealth() {
        Call<ApiResponse> call = sqlApiService.healthCheck();
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse> call,
                                   @NonNull Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(SqlDetectionActivity.this,
                            "✅ SQL Guard Backend Connected",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SqlDetectionActivity.this,
                            "⚠️ Backend connection issue",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse> call,
                                  @NonNull Throwable t) {
                Toast.makeText(SqlDetectionActivity.this,
                        "❌ Backend offline. Start: python sql_api.py",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setupButtonListeners() {
        // Scan for Vulnerabilities button
        btnScanVulnerabilities.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanSqlQuery();
            }
        });

        // Upload File button
        btnUploadFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFilePicker();
            }
        });

        // Vulnerable Example Card
        cardVulnerableExample.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadVulnerableExample();
            }
        });

        // Secure Example Card
        cardSecureExample.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadSecureExample();
            }
        });
    }

    /**
     * Scan SQL query for vulnerabilities
     */
    private void scanSqlQuery() {
        String sqlCode = etSqlCode.getText().toString().trim();

        if (sqlCode.isEmpty()) {
            Toast.makeText(this, "Please enter SQL code to analyze", Toast.LENGTH_SHORT).show();
            return;
        }

        // Show loading
        Toast.makeText(this, "Scanning for vulnerabilities...", Toast.LENGTH_SHORT).show();

        // Prepare request
        Map<String, String> request = new HashMap<>();
        request.put("query", sqlCode);

        // Call API
        Call<SqlResponse> call = sqlApiService.scanQuery(request);
        call.enqueue(new Callback<SqlResponse>() {
            @Override
            public void onResponse(@NonNull Call<SqlResponse> call,
                                   @NonNull Response<SqlResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    SqlResponse result = response.body();
                    showScanResult(result);
                } else {
                    Toast.makeText(SqlDetectionActivity.this,
                            "❌ Scan failed. Status: " + response.code(),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<SqlResponse> call, @NonNull Throwable t) {
                Toast.makeText(SqlDetectionActivity.this,
                        "❌ Connection error: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Show scan results in dialog
     */
    private void showScanResult(SqlResponse result) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        if (result.isVulnerable()) {
            // Vulnerable query
            builder.setTitle("⚠️ VULNERABILITY DETECTED!");
            builder.setIcon(android.R.drawable.ic_dialog_alert);

            StringBuilder message = new StringBuilder();
            message.append("Vulnerability Level: ").append(result.getVulnerabilityLevel()).append("\n\n");
            message.append("Detected Patterns:\n");

            List<String> patterns = result.getMatchedPatterns();
            if (patterns != null) {
                for (String pattern : patterns) {
                    message.append("• ").append(pattern).append("\n");
                }
            }

            message.append("\nRecommendation:\n");
            message.append(result.getRecommendation());

            builder.setMessage(message.toString());
            builder.setPositiveButton("OK", null);

            // Show toast
            Toast.makeText(this, "⚠️ SQL INJECTION DETECTED!", Toast.LENGTH_LONG).show();

        } else {
            // Safe query
            builder.setTitle("✅ QUERY IS SAFE");
            builder.setIcon(android.R.drawable.ic_dialog_info);
            builder.setMessage(result.getRecommendation());
            builder.setPositiveButton("OK", null);

            // Show toast
            Toast.makeText(this, "✅ No vulnerabilities found!", Toast.LENGTH_SHORT).show();
        }

        builder.show();
    }

    /**
     * Open file picker for CSV upload
     * Supports all Android versions:
     * - Android 9 and below: Requires READ_EXTERNAL_STORAGE permission
     * - Android 10+ (API 29+): No permission needed, uses scoped storage
     */
    private void openFilePicker() {
        // Build.VERSION_CODES.Q = API 29 = Android 10
        // For Android 10+, ACTION_GET_CONTENT doesn't require storage permission (scoped storage)
        // Only check permission for Android 9 (API 28) and below
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            // Android 9 (Pie) and below - need storage permission
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_STORAGE_PERMISSION);
                return;
            }
        }
        // Android 10+ (including QP1A): No permission needed, proceed directly to file picker

        // Open file picker (works without permission on Android 10+)
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*"); // Allow all file types, user can filter
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        // Also accept CSV specifically
        String[] mimeTypes = {"text/csv", "application/vnd.ms-excel", "text/comma-separated-values"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);

        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select CSV File"),
                    REQUEST_FILE_PICKER);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "Please install a file manager", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Upload CSV file to API
     */
    private void uploadCsvFile(Uri fileUri) {
        try {
            // Show loading
            Toast.makeText(this, "Uploading and scanning file...", Toast.LENGTH_SHORT).show();

            // Get file name
            String fileName = getFileName(fileUri);

            // Create temp file
            File tempFile = createTempFileFromUri(fileUri, fileName);

            // Create request body
            RequestBody requestFile = RequestBody.create(
                    MediaType.parse("text/csv"),
                    tempFile
            );

            MultipartBody.Part filePart = MultipartBody.Part.createFormData(
                    "file",
                    fileName,
                    requestFile
            );

            // Call API
            Call<SqlResponse> call = sqlApiService.scanFile(filePart);
            call.enqueue(new Callback<SqlResponse>() {
                @Override
                public void onResponse(@NonNull Call<SqlResponse> call,
                                       @NonNull Response<SqlResponse> response) {
                    // Clean up temp file
                    if (tempFile.exists()) {
                        tempFile.delete();
                    }

                    if (response.isSuccessful() && response.body() != null) {
                        SqlResponse result = response.body();
                        showFileScanResult(result);
                    } else {
                        Toast.makeText(SqlDetectionActivity.this,
                                "❌ File scan failed. Status: " + response.code(),
                                Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<SqlResponse> call, @NonNull Throwable t) {
                    // Clean up temp file
                    if (tempFile.exists()) {
                        tempFile.delete();
                    }

                    Toast.makeText(SqlDetectionActivity.this,
                            "❌ Connection error: " + t.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            });

        } catch (Exception e) {
            Toast.makeText(this, "❌ File upload error: " + e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Show file scan results
     */
    private void showFileScanResult(SqlResponse result) {
        if (result.getResults() != null) {
            SqlResponse.SqlFileResults fileResults = result.getResults();

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("📊 File Scan Results");

            StringBuilder message = new StringBuilder();
            message.append("Attacker IP: ").append(fileResults.getAttackerIp()).append("\n\n");
            message.append("Total Attempts: ").append(fileResults.getAttemptCount()).append("\n\n");
            message.append("First Payload:\n").append(fileResults.getFirstPayload()).append("\n\n");
            message.append("Last Payload:\n").append(fileResults.getLastPayload());

            builder.setMessage(message.toString());
            builder.setPositiveButton("OK", null);
            builder.show();

            Toast.makeText(this,
                    "Found " + fileResults.getAttemptCount() + " SQL injection attempts!",
                    Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Load vulnerable example query
     */
    private void loadVulnerableExample() {
        String vulnerableQuery = "SELECT * FROM users WHERE username = 'admin' OR '1'='1'";
        etSqlCode.setText(vulnerableQuery);
        Toast.makeText(this,
                "Loaded vulnerable query - Uses SQL injection!",
                Toast.LENGTH_SHORT).show();
    }

    /**
     * Load secure example query
     */
    private void loadSecureExample() {
        String secureQuery = "SELECT * FROM users WHERE username = ? AND password = ?";
        etSqlCode.setText(secureQuery);
        Toast.makeText(this,
                "Loaded secure query - Uses parameterized queries!",
                Toast.LENGTH_SHORT).show();
    }

    /**
     * Get file name from Uri
     */
    private String getFileName(Uri uri) {
        String fileName = "file.csv";
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        if (cursor != null) {
            int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            cursor.moveToFirst();
            fileName = cursor.getString(nameIndex);
            cursor.close();
        }
        return fileName;
    }

    /**
     * Create temp file from Uri
     */
    private File createTempFileFromUri(Uri uri, String fileName) throws Exception {
        InputStream inputStream = getContentResolver().openInputStream(uri);
        File tempFile = new File(getCacheDir(), fileName);

        FileOutputStream outputStream = new FileOutputStream(tempFile);
        byte[] buffer = new byte[1024];
        int length;

        while ((length = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, length);
        }

        outputStream.close();
        inputStream.close();

        return tempFile;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_FILE_PICKER && resultCode == RESULT_OK) {
            if (data != null && data.getData() != null) {
                Uri fileUri = data.getData();
                uploadCsvFile(fileUri);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_STORAGE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "✅ Storage permission granted", Toast.LENGTH_SHORT).show();
                openFilePicker();
            } else {
                Toast.makeText(this, "❌ Storage permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}