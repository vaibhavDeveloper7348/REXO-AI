package com.vaibhav.quantumcore3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.vaibhav.quantumcore3.api.ApiClient;
import com.vaibhav.quantumcore3.api.ApiResponse;
import com.vaibhav.quantumcore3.api.JackApiService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * JackAiActivity - Fixed Mobile-First AI Assistant
 * All issues resolved: null queries, YouTube opening, website handling
 */
public class JackAiActivity extends AppCompatActivity {

    private static final String TAG = "JackAiActivity";

    private RecyclerView recyclerMessages;
    private MessageAdapter messageAdapter;
    private ArrayList<Message> messageList;
    private CardView btnMicrophone, btnCamera, btnGlobe, btnSettings;
    private ImageView btnVolume, btnSettingsTop;

    // API Service
    private JackApiService jackApiService;

    // Text-to-Speech
    private TextToSpeech textToSpeech;
    private boolean isTTSReady = false;

    // Constants
    private static final int REQUEST_VOICE_INPUT = 100;
    private static final int REQUEST_AUDIO_PERMISSION = 102;
    private static final int REQUEST_CAMERA_CAPTURE = 200;
    private static final int REQUEST_CAMERA_PERMISSION = 201;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jack_ai);

        // Initialize API Service
        jackApiService = ApiClient.getJackApiService();

        // Initialize views
        initializeViews();

        // Setup RecyclerView
        setupRecyclerView();

        // Initialize Text-to-Speech
        initializeTextToSpeech();

        // Check API health
        checkApiHealth();

        // Request permissions
        requestPermissions();

        // Setup button listeners
        setupButtonListeners();
    }

    private void initializeViews() {
        recyclerMessages = findViewById(R.id.recyclerMessages);
        btnMicrophone = findViewById(R.id.btnMicrophone);
        btnCamera = findViewById(R.id.btnCamera);
        btnGlobe = findViewById(R.id.btnGlobe);
        btnSettings = findViewById(R.id.btnSettings);
        btnVolume = findViewById(R.id.btnVolume);
        btnSettingsTop = findViewById(R.id.btnSettingsTop);
    }

    private void setupRecyclerView() {
        messageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(messageList);
        recyclerMessages.setLayoutManager(new LinearLayoutManager(this));
        recyclerMessages.setAdapter(messageAdapter);
    }

    private void initializeTextToSpeech() {
        textToSpeech = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                int result = textToSpeech.setLanguage(Locale.US);
                if (result != TextToSpeech.LANG_MISSING_DATA &&
                        result != TextToSpeech.LANG_NOT_SUPPORTED) {
                    isTTSReady = true;
                    textToSpeech.setSpeechRate(1.0f);
                    textToSpeech.setPitch(1.0f);
                }
            }
        });
    }

    private void speak(String text) {
        if (isTTSReady && textToSpeech != null && text != null && !text.isEmpty()) {
            textToSpeech.stop();
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        }
    }

    private void stopSpeaking() {
        if (textToSpeech != null) {
            textToSpeech.stop();
        }
    }

    private void checkApiHealth() {
        Call<ApiResponse> call = jackApiService.healthCheck();
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse> call,
                                   @NonNull Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String message = "Hello! I'm Jack AI, your personal assistant.\n\n" +
                            "✅ Backend Connected!\n\n" +
                            "📱 All actions will be performed on YOUR MOBILE:\n\n" +
                            "🎤 Tap microphone to speak\n" +
                            "📷 I can open/close camera\n" +
                            "🌐 I can open any website\n" +
                            "🔍 I can search anything\n" +
                            "📱 I can open any app\n\n" +
                            "Try: 'Open YouTube' or 'Search Python'";
                    addMessage("Jack AI", message);
                    speak("Hello! I'm Jack AI. Ready to help!");
                } else {
                    showOfflineMessage();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                showOfflineMessage();
            }
        });
    }

    private void showOfflineMessage() {
        String message = "❌ Backend offline. Using local mode.\n\n" +
                "Basic functions still work!\n" +
                "Try: 'Open camera', 'Open browser'";
        addMessage("Jack AI", message);
        speak("Backend offline. Using local mode.");
    }

    private void setupButtonListeners() {
        btnMicrophone.setOnClickListener(v -> startVoiceInput());

        btnCamera.setOnClickListener(v -> {
            addUserMessage("Open camera");
            executeAction("OPEN_CAMERA", new HashMap<>());
        });

        btnGlobe.setOnClickListener(v -> {
            addUserMessage("Open browser");
            Map<String, Object> data = new HashMap<>();
            data.put("url", "https://www.google.com");
            executeAction("OPEN_BROWSER", data);
        });

        btnSettings.setOnClickListener(v -> showSettings());

        btnVolume.setOnClickListener(v -> {
            if (textToSpeech != null && textToSpeech.isSpeaking()) {
                stopSpeaking();
                Toast.makeText(this, "Speech stopped", Toast.LENGTH_SHORT).show();
            }
        });

        btnSettingsTop.setOnClickListener(v -> showSettings());
    }

    private void requestPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA},
                    REQUEST_AUDIO_PERMISSION);
        }
    }

    private void startVoiceInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to Jack AI...");

        try {
            startActivityForResult(intent, REQUEST_VOICE_INPUT);
        } catch (Exception e) {
            Toast.makeText(this, "Voice input not supported", Toast.LENGTH_SHORT).show();
        }
    }

    private void processUserInput(String input) {
        if (input == null || input.trim().isEmpty()) {
            return;
        }

        addUserMessage(input);

        // Send to backend for processing
        Map<String, String> request = new HashMap<>();
        request.put("command", input);

        Call<ApiResponse> call = jackApiService.processCommand(request);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse> call,
                                   @NonNull Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse apiResponse = response.body();
                    handleBackendResponse(apiResponse);
                } else {
                    Log.e(TAG, "Backend error: " + response.code());
                    handleLocalCommand(input);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                Log.e(TAG, "Backend failure: " + t.getMessage());
                handleLocalCommand(input);
            }
        });
    }

    /**
     * Handle response from backend and execute action on mobile
     * FIXED: Proper null checks and data extraction
     */
    private void handleBackendResponse(ApiResponse response) {
        try {
            String action = response.getAction();
            String message = response.getMessage();
            String answer = response.getAnswer();
            String url = response.getUrl();
            String query = response.getQuery();

            Log.d(TAG, "Action: " + action);
            Log.d(TAG, "Message: " + message);
            Log.d(TAG, "URL: " + url);
            Log.d(TAG, "Query: " + query);

            // Display message
            if (message != null && !message.isEmpty()) {
                addMessage("Jack AI", message);
                speak(message);
            } else if (answer != null && !answer.isEmpty()) {
                addMessage("Jack AI", answer);
                speak(answer);
            }

            // Execute action on mobile with proper data
            if (action != null && !action.isEmpty()) {
                Map<String, Object> actionData = new HashMap<>();

                // Add URL if present
                if (url != null && !url.isEmpty()) {
                    actionData.put("url", url);
                }

                // Add query if present
                if (query != null && !query.isEmpty()) {
                    actionData.put("query", query);
                }

                executeAction(action, actionData);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error handling response: " + e.getMessage());
            addMessage("Jack AI", "Error processing command");
        }
    }

    /**
     * Execute action on mobile device
     * FIXED: Proper null checks and string handling
     */
    private void executeAction(String action, Map<String, Object> data) {
        if (action == null || action.isEmpty()) {
            return;
        }

        try {
            Log.d(TAG, "Executing action: " + action);

            switch (action) {
                case "OPEN_CAMERA":
                    openCamera();
                    break;

                case "CLOSE_CAMERA":
                    closeCamera();
                    break;

                case "OPEN_BROWSER":
                    String browserUrl = (data != null && data.containsKey("url")) ?
                            String.valueOf(data.get("url")) : "https://www.google.com";
                    openBrowser(browserUrl);
                    break;

                case "CLOSE_BROWSER":
                    closeBrowser();
                    break;

                case "OPEN_WEBSITE":
                    String websiteUrl = (data != null && data.containsKey("url")) ?
                            String.valueOf(data.get("url")) : "https://www.google.com";
                    openBrowser(websiteUrl);
                    break;

                case "SEARCH_GOOGLE":
                    String searchQuery = (data != null && data.containsKey("query")) ?
                            String.valueOf(data.get("query")) : "";
                    if (!searchQuery.isEmpty() && !searchQuery.equals("null")) {
                        searchGoogle(searchQuery);
                    } else {
                        addMessage("Jack AI", "❌ No search query provided");
                    }
                    break;

                case "OPEN_APP":
                    String appName = (data != null && data.containsKey("app")) ?
                            String.valueOf(data.get("app")) : "";
                    if (!appName.isEmpty()) {
                        openApp(appName);
                    }
                    break;

                case "CLOSE_APP":
                    String closeAppName = (data != null && data.containsKey("app")) ?
                            String.valueOf(data.get("app")) : "";
                    if (!closeAppName.isEmpty()) {
                        closeApp(closeAppName);
                    }
                    break;

                case "FAQ_ANSWER":
                    // Already handled in response message
                    break;

                default:
                    Log.w(TAG, "Unknown action: " + action);
                    break;
            }
        } catch (Exception e) {
            Log.e(TAG, "Error executing action: " + e.getMessage());
            addMessage("Jack AI", "❌ Error: " + e.getMessage());
        }
    }

    /**
     * Open mobile camera
     */
    private void openCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    REQUEST_CAMERA_PERMISSION);
            return;
        }

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(cameraIntent, REQUEST_CAMERA_CAPTURE);
            addMessage("Jack AI", "📷 Opening camera on your mobile!");
            speak("Opening camera");
        } else {
            addMessage("Jack AI", "❌ Camera not available");
        }
    }

    /**
     * Close camera
     */
    private void closeCamera() {
        onBackPressed();
        addMessage("Jack AI", "📷 Camera closed");
        speak("Closing camera");
    }

    /**
     * Open browser with URL
     * FIXED: Proper URL validation and null checks
     */
    private void openBrowser(String url) {
        try {
            if (url == null || url.isEmpty() || url.equals("null")) {
                url = "https://www.google.com";
            }

            // Clean and validate URL
            url = url.trim();
            if (!url.startsWith("http://") && !url.startsWith("https://")) {
                url = "https://" + url;
            }

            Log.d(TAG, "Opening URL: " + url);

            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(browserIntent);

            addMessage("Jack AI", "🌐 Opening browser!");
            speak("Opening browser");
        } catch (Exception e) {
            Log.e(TAG, "Browser error: " + e.getMessage());
            addMessage("Jack AI", "❌ Cannot open browser");
        }
    }

    /**
     * Close browser
     */
    private void closeBrowser() {
        Intent intent = new Intent(this, JackAiActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);

        addMessage("Jack AI", "🌐 Returning to Jack AI");
        speak("Closing browser");
    }

    /**
     * Search Google
     * FIXED: Proper query handling and null checks
     */
    private void searchGoogle(String query) {
        try {
            if (query == null || query.isEmpty() || query.equals("null")) {
                addMessage("Jack AI", "❌ No search query");
                return;
            }

            query = query.trim();
            Log.d(TAG, "Searching for: " + query);

            String searchUrl = "https://www.google.com/search?q=" + Uri.encode(query);

            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(searchUrl));
            startActivity(browserIntent);

            addMessage("Jack AI", "🔍 Searching for: " + query);
            speak("Searching for " + query);
        } catch (Exception e) {
            Log.e(TAG, "Search error: " + e.getMessage());
            addMessage("Jack AI", "❌ Search failed");
        }
    }

    /**
     * Open mobile app
     */
    private void openApp(String appName) {
        try {
            Intent intent = null;

            switch (appName.toLowerCase()) {
                case "calculator":
                    intent = getPackageManager().getLaunchIntentForPackage(
                            "com.android.calculator2");
                    if (intent == null) {
                        intent = new Intent();
                        intent.setAction(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_APP_CALCULATOR);
                    }
                    break;

                case "contacts":
                    intent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse("content://contacts/people/"));
                    break;

                case "settings":
                    intent = new Intent(android.provider.Settings.ACTION_SETTINGS);
                    break;

                case "gallery":
                case "photos":
                    intent = new Intent(Intent.ACTION_VIEW);
                    intent.setType("image/*");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    break;

                default:
                    addMessage("Jack AI", "❌ App not supported: " + appName);
                    return;
            }

            if (intent != null) {
                startActivity(intent);
                addMessage("Jack AI", "✅ Opening " + appName);
                speak("Opening " + appName);
            }
        } catch (ActivityNotFoundException e) {
            addMessage("Jack AI", "❌ " + appName + " not found");
        } catch (Exception e) {
            addMessage("Jack AI", "❌ Error: " + e.getMessage());
        }
    }

    /**
     * Close app
     */
    private void closeApp(String appName) {
        Intent intent = new Intent(this, JackAiActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);

        addMessage("Jack AI", "✅ Returning to Jack AI");
        speak("Closing " + appName);
    }

    /**
     * Handle local commands when backend unavailable
     */
    private void handleLocalCommand(String command) {
        String cmd = command.toLowerCase();

        if (cmd.contains("camera") && cmd.contains("open")) {
            executeAction("OPEN_CAMERA", new HashMap<>());
        } else if (cmd.contains("browser") || cmd.contains("google")) {
            Map<String, Object> data = new HashMap<>();
            data.put("url", "https://www.google.com");
            executeAction("OPEN_BROWSER", data);
        } else if (cmd.contains("youtube")) {
            Map<String, Object> data = new HashMap<>();
            data.put("url", "https://www.youtube.com");
            executeAction("OPEN_WEBSITE", data);
        } else if (cmd.contains("search")) {
            String query = cmd.replace("search", "").trim();
            if (!query.isEmpty()) {
                Map<String, Object> data = new HashMap<>();
                data.put("query", query);
                executeAction("SEARCH_GOOGLE", data);
            }
        } else {
            String message = "Try: 'open camera', 'open YouTube', 'search Python'";
            addMessage("Jack AI", message);
            speak(message);
        }
    }

    private void showSettings() {
        String message = "⚙️ Jack AI Settings\n\n" +
                "Mode: Mobile-First\n" +
                "All actions on mobile ✅";
        addMessage("Jack AI", message);
    }

    private void addMessage(String sender, String text) {
        if (text == null || text.isEmpty()) {
            return;
        }
        String time = new SimpleDateFormat("h:mm:ss a", Locale.getDefault())
                .format(new Date());
        Message message = new Message(sender, text, time, sender.equals("Jack AI"));
        messageList.add(message);
        messageAdapter.notifyItemInserted(messageList.size() - 1);
        recyclerMessages.smoothScrollToPosition(messageList.size() - 1);
    }

    private void addUserMessage(String text) {
        if (text == null || text.isEmpty()) {
            return;
        }
        String time = new SimpleDateFormat("h:mm:ss a", Locale.getDefault())
                .format(new Date());
        Message message = new Message("You", text, time, false);
        messageList.add(message);
        messageAdapter.notifyItemInserted(messageList.size() - 1);
        recyclerMessages.smoothScrollToPosition(messageList.size() - 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_VOICE_INPUT && resultCode == RESULT_OK && data != null) {
            ArrayList<String> result = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            if (result != null && !result.isEmpty()) {
                String spokenText = result.get(0);
                processUserInput(spokenText);
            }
        }

        if (requestCode == REQUEST_CAMERA_CAPTURE) {
            if (resultCode == RESULT_OK) {
                addMessage("Jack AI", "📷 Photo captured!");
                speak("Photo captured");
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "✅ Permission granted", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopSpeaking();
    }
}