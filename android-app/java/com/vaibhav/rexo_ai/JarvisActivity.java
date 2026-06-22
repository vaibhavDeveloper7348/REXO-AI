package com.vaibhav.quantumcore3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;
import android.widget.ProgressBar;

import com.vaibhav.quantumcore3.api.ApiClient;
import com.vaibhav.quantumcore3.api.JarvisApiService;
import com.vaibhav.quantumcore3.api.JarvisCommandResponse;

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
 * JarvisActivity - FAST AI Assistant with Smart NLP
 *
 * KEY IMPROVEMENTS:
 * - Instant local responses (no waiting for backend)
 * - Smart NLP - understands variations in commands
 * - Calculator searches automatically
 * - Backend used only when needed
 * - Much faster response time
 */
public class JarvisActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private static final String TAG = "JarvisActivity";
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private static final int REQUEST_SPEECH_RECOGNIZER = 100;
    private static final int BACKEND_TIMEOUT = 3000; // 3 seconds timeout

    // UI Components
    private FrameLayout micCircle;
    private ImageView ivMicrophone;
    private CardView btnSettings, btnHelp;
    private EditText etCommand;
    private Button btnSendCommand;
    private TextView tvResponse, tvStatus;
    private ProgressBar progressBar;

    // Animation
    private ObjectAnimator pulseAnimator, pulseAnimatorY;

    // Voice & Speech
    private TextToSpeech textToSpeech;
    private boolean isTTSReady = false;

    // API Service
    private JarvisApiService jarvisApiService;

    // Backend timeout handler
    private Handler timeoutHandler = new Handler(Looper.getMainLooper());
    private Runnable timeoutRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jarvis);

        jarvisApiService = ApiClient.getJarvisApiService();

        initializeViews();
        initializeTextToSpeech();
        setupPulseAnimation();
        setupClickListeners();
        checkMicrophonePermission();
        showWelcomeMessage();
    }

    private void initializeViews() {
        micCircle = findViewById(R.id.micCircle);
        ivMicrophone = findViewById(R.id.ivMicrophone);
        btnSettings = findViewById(R.id.btnSettings);
        btnHelp = findViewById(R.id.btnHelp);
        etCommand = findViewById(R.id.etCommand);
        btnSendCommand = findViewById(R.id.btnSendCommand);
        tvResponse = findViewById(R.id.tvResponse);
        tvStatus = findViewById(R.id.tvStatus);
        progressBar = findViewById(R.id.progressBar);

        if (progressBar != null) progressBar.setVisibility(View.GONE);
    }

    private void initializeTextToSpeech() {
        textToSpeech = new TextToSpeech(this, this);
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = textToSpeech.setLanguage(Locale.US);
            isTTSReady = (result != TextToSpeech.LANG_MISSING_DATA &&
                    result != TextToSpeech.LANG_NOT_SUPPORTED);

            if (isTTSReady) {
                speak("Hello! I'm Jarvis. Ready to assist you.");
            }
        }
    }

    private void speak(String text) {
        if (isTTSReady && textToSpeech != null && text != null && !text.isEmpty()) {
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        }
    }

    private void stopSpeaking() {
        if (textToSpeech != null && textToSpeech.isSpeaking()) {
            textToSpeech.stop();
        }
    }

    private void setupClickListeners() {
        if (micCircle != null) {
            micCircle.setOnClickListener(v -> startVoiceRecognition());
        }

        if (btnSendCommand != null) {
            btnSendCommand.setOnClickListener(v -> {
                String command = etCommand.getText().toString().trim();
                if (!command.isEmpty()) {
                    processCommand(command);
                } else {
                    Toast.makeText(this, "Please enter a command", Toast.LENGTH_SHORT).show();
                }
            });
        }

        if (btnSettings != null) {
            btnSettings.setOnClickListener(v -> showSettingsInfo());
        }

        if (btnHelp != null) {
            btnHelp.setOnClickListener(v -> showHelpDialog());
        }
    }

    private void setupPulseAnimation() {
        if (micCircle == null) return;

        pulseAnimator = ObjectAnimator.ofFloat(micCircle, "scaleX", 1f, 1.1f, 1f);
        pulseAnimator.setDuration(2000);
        pulseAnimator.setRepeatCount(ValueAnimator.INFINITE);
        pulseAnimator.setRepeatMode(ValueAnimator.RESTART);
        pulseAnimator.setInterpolator(new LinearInterpolator());

        pulseAnimatorY = ObjectAnimator.ofFloat(micCircle, "scaleY", 1f, 1.1f, 1f);
        pulseAnimatorY.setDuration(2000);
        pulseAnimatorY.setRepeatCount(ValueAnimator.INFINITE);
        pulseAnimatorY.setRepeatMode(ValueAnimator.RESTART);
        pulseAnimatorY.setInterpolator(new LinearInterpolator());

        pulseAnimator.start();
        pulseAnimatorY.start();
    }

    private void checkMicrophonePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    REQUEST_RECORD_AUDIO_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "✅ Microphone ready", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showWelcomeMessage() {
        String welcome = "✅ Jarvis AI Ready!\n\n" +
                "I can help you with:\n" +
                "🔢 Calculations - Just say any math!\n" +
                "🔍 Search anything instantly\n" +
                "⏰ Time & Date\n" +
                "🌤️ Weather info\n" +
                "😄 Jokes & News\n" +
                "📺 YouTube & Web\n\n" +
                "🎤 Tap mic or type!";

        if (tvResponse != null) tvResponse.setText(welcome);
    }

    private void startVoiceRecognition() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            checkMicrophonePermission();
            return;
        }

        stopSpeaking();

        if (pulseAnimator != null) pulseAnimator.setDuration(800);
        if (pulseAnimatorY != null) pulseAnimatorY.setDuration(800);
        if (tvStatus != null) tvStatus.setText("Listening...");

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak now...");

        try {
            startActivityForResult(intent, REQUEST_SPEECH_RECOGNIZER);
        } catch (Exception e) {
            Toast.makeText(this, "Speech recognition not supported", Toast.LENGTH_SHORT).show();
            resetListeningState();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_SPEECH_RECOGNIZER && resultCode == RESULT_OK && data != null) {
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (result != null && result.size() > 0) {
                String spokenText = result.get(0);
                if (etCommand != null) etCommand.setText(spokenText);
                processCommand(spokenText);
            }
            resetListeningState();
        }
    }

    private void resetListeningState() {
        if (pulseAnimator != null) pulseAnimator.setDuration(2000);
        if (pulseAnimatorY != null) pulseAnimatorY.setDuration(2000);
        if (tvStatus != null) tvStatus.setText("Tap to speak");
    }

    /**
     * MAIN COMMAND PROCESSOR - Smart NLP with instant responses
     */
    private void processCommand(String command) {
        if (command == null || command.trim().isEmpty()) return;

        command = command.trim();
        String cmd = command.toLowerCase();

        Log.d(TAG, "Processing: " + command);

        // Show processing indicator
        showLoading(true);
        if (tvResponse != null) {
            tvResponse.setText("🤔 Processing...");
        }

        // === INSTANT LOCAL RESPONSES (No backend delay) ===

        // 1. TIME
        if (matchesPattern(cmd, new String[]{"time", "what time", "whats the time", "tell time"})) {
            respondInstantly(getCurrentTime(), "The current time is " + getCurrentTime());
            return;
        }

        // 2. DATE
        if (matchesPattern(cmd, new String[]{"date", "what date", "whats the date", "today date", "todays date"})) {
            respondInstantly(getCurrentDate(), "Today's date is " + getCurrentDate());
            return;
        }

        // 3. DAY
        if (matchesPattern(cmd, new String[]{"what day", "which day", "day today"})) {
            respondInstantly(getCurrentDay(), "Today is " + getCurrentDay());
            return;
        }

        // 4. CALCULATOR - Detect math expressions
        if (containsCalculation(cmd)) {
            handleCalculation(command);
            return;
        }

        // 5. SEARCH (YouTube)
        if (matchesPattern(cmd, new String[]{"youtube", "search youtube", "find on youtube", "open youtube"})) {
            handleYouTubeSearch(command);
            return;
        }

        // 6. SEARCH (Google/Web)
        if (matchesPattern(cmd, new String[]{"search", "google", "find", "look up", "search for"})) {
            handleGoogleSearch(command);
            return;
        }

        // 7. JOKES
        if (matchesPattern(cmd, new String[]{"joke", "tell joke", "make me laugh", "funny"})) {
            String joke = getRandomJoke();
            respondInstantly(joke, joke);
            return;
        }

        // 8. OPEN BROWSER/WEBSITE
        if (matchesPattern(cmd, new String[]{"open browser", "open google", "browser"})) {
            openBrowser("https://www.google.com");
            respondInstantly("Opening browser", "Opening Google");
            return;
        }

        // === BACKEND CALLS (With timeout for speed) ===

        // For weather, news, Wikipedia - use backend with timeout
        if (matchesPattern(cmd, new String[]{"weather", "temperature", "forecast"}) ||
                matchesPattern(cmd, new String[]{"news", "headlines", "latest news"}) ||
                matchesPattern(cmd, new String[]{"tell me about", "who is", "what is", "wikipedia"})) {

            tryBackendWithTimeout(command);
            return;
        }

        // === DEFAULT: Try backend, fallback to local ===
        tryBackendWithTimeout(command);
    }

    /**
     * Smart pattern matching - understands variations
     */
    private boolean matchesPattern(String input, String[] patterns) {
        for (String pattern : patterns) {
            if (input.contains(pattern)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check if command contains calculation
     */
    private boolean containsCalculation(String cmd) {
        // Remove common words
        String cleaned = cmd.toLowerCase()
                .replace("calculate", "")
                .replace("what is", "")
                .replace("what's", "")
                .replace("whats", "")
                .replace("solve", "")
                .replace("compute", "")
                .trim();

        // Check for math operators and numbers
        return cleaned.matches(".*\\d+.*[+\\-*/x÷×].*\\d+.*");
    }

    /**
     * Handle calculation with auto-search
     */
    private void handleCalculation(String command) {
        try {
            // Extract expression
            String expression = command.toLowerCase()
                    .replace("calculate", "")
                    .replace("what is", "")
                    .replace("what's", "")
                    .replace("whats", "")
                    .replace("solve", "")
                    .replace("compute", "")
                    .trim();

            // Try to evaluate locally
            double result = evaluateExpression(expression);
            String resultStr = formatResult(result);

            String response = "📊 " + expression + " = " + resultStr;

            // Also search it on Google
            String searchQuery = expression + " calculation";
            openGoogleSearch(searchQuery);

            respondInstantly(response, "The answer is " + resultStr);

        } catch (Exception e) {
            // If calculation fails, just search it
            String searchQuery = command.replace("calculate", "").trim();
            openGoogleSearch(searchQuery);
            respondInstantly("Searching: " + searchQuery, "Searching for " + searchQuery);
        }
    }

    /**
     * Evaluate math expression - FIXED VERSION
     */
    private double evaluateExpression(String mathExpr) throws Exception {
        mathExpr = mathExpr.replaceAll("\\s+", "");
        mathExpr = mathExpr.replace("x", "*").replace("X", "*");
        mathExpr = mathExpr.replace("÷", "/").replace("×", "*");

        ExpressionEvaluator evaluator = new ExpressionEvaluator(mathExpr);
        return evaluator.parse();
    }

    /**
     * Expression Evaluator Class - Avoids inner class variable issues
     */
    private static class ExpressionEvaluator {
        private String expression;
        private int pos = -1;
        private int ch;

        ExpressionEvaluator(String expr) {
            this.expression = expr;
        }

        void nextChar() {
            ch = (++pos < expression.length()) ? expression.charAt(pos) : -1;
        }

        boolean eat(int charToEat) {
            while (ch == ' ') nextChar();
            if (ch == charToEat) {
                nextChar();
                return true;
            }
            return false;
        }

        double parse() {
            nextChar();
            double x = parseExpression();
            if (pos < expression.length()) throw new RuntimeException("Unexpected: " + (char)ch);
            return x;
        }

        double parseExpression() {
            double x = parseTerm();
            for (;;) {
                if      (eat('+')) x += parseTerm();
                else if (eat('-')) x -= parseTerm();
                else return x;
            }
        }

        double parseTerm() {
            double x = parseFactor();
            for (;;) {
                if      (eat('*')) x *= parseFactor();
                else if (eat('/')) x /= parseFactor();
                else return x;
            }
        }

        double parseFactor() {
            if (eat('+')) return parseFactor();
            if (eat('-')) return -parseFactor();

            double x;
            int startPos = this.pos;
            if (eat('(')) {
                x = parseExpression();
                eat(')');
            } else if ((ch >= '0' && ch <= '9') || ch == '.') {
                while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                x = Double.parseDouble(expression.substring(startPos, this.pos));
            } else {
                throw new RuntimeException("Unexpected: " + (char)ch);
            }

            return x;
        }
    }

    private String formatResult(double result) {
        if (result == (long) result) {
            return String.format("%d", (long) result);
        }
        return String.format("%.2f", result);
    }

    /**
     * Handle YouTube search
     */
    private void handleYouTubeSearch(String command) {
        String query = command.toLowerCase()
                .replace("youtube", "")
                .replace("search", "")
                .replace("find", "")
                .replace("on", "")
                .replace("for", "")
                .trim();

        if (query.isEmpty()) {
            openBrowser("https://www.youtube.com");
            respondInstantly("Opening YouTube", "Opening YouTube");
        } else {
            String url = "https://www.youtube.com/results?search_query=" + Uri.encode(query);
            openBrowser(url);
            respondInstantly("🔍 Searching YouTube: " + query, "Searching YouTube for " + query);
        }
    }

    /**
     * Handle Google search
     */
    private void handleGoogleSearch(String command) {
        String query = command.toLowerCase()
                .replace("search", "")
                .replace("google", "")
                .replace("find", "")
                .replace("look up", "")
                .replace("for", "")
                .trim();

        if (!query.isEmpty()) {
            openGoogleSearch(query);
            respondInstantly("🔍 Searching: " + query, "Searching for " + query);
        } else {
            respondInstantly("What would you like me to search?", "What should I search for?");
        }
    }

    private void openGoogleSearch(String query) {
        try {
            String url = "https://www.google.com/search?q=" + Uri.encode(query);
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        } catch (Exception e) {
            Log.e(TAG, "Search error: " + e.getMessage());
        }
    }

    private void openBrowser(String url) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "Cannot open browser", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Get current time
     */
    private String getCurrentTime() {
        return new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date());
    }

    /**
     * Get current date
     */
    private String getCurrentDate() {
        return new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault()).format(new Date());
    }

    /**
     * Get current day
     */
    private String getCurrentDay() {
        return new SimpleDateFormat("EEEE", Locale.getDefault()).format(new Date());
    }

    /**
     * Get random joke
     */
    private String getRandomJoke() {
        String[] jokes = {
                "Why do programmers prefer dark mode? Because light attracts bugs! 🐛",
                "Why did the developer go broke? Because he used up all his cache! 💰",
                "How many programmers does it take to change a light bulb? None, that's a hardware problem! 💡",
                "Why do Java developers wear glasses? Because they don't C#! 👓",
                "What's a programmer's favorite hangout place? Foo Bar! 🍺"
        };
        return jokes[(int)(Math.random() * jokes.length)];
    }

    /**
     * Instant response - no backend delay
     */
    private void respondInstantly(String displayText, String speechText) {
        showLoading(false);

        if (tvResponse != null) {
            tvResponse.setText("🤖 Jarvis:\n\n" + displayText);
        }

        speak(speechText);

        if (etCommand != null) {
            etCommand.setText("");
        }
    }

    /**
     * Try backend with timeout fallback
     */
    private void tryBackendWithTimeout(final String command) {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("command", command);

        // Set timeout
        timeoutRunnable = new Runnable() {
            @Override
            public void run() {
                Log.w(TAG, "Backend timeout - using fallback");
                handleFallbackResponse(command);
            }
        };
        timeoutHandler.postDelayed(timeoutRunnable, BACKEND_TIMEOUT);

        Call<JarvisCommandResponse> call = jarvisApiService.processCommand(requestBody);
        call.enqueue(new Callback<JarvisCommandResponse>() {
            @Override
            public void onResponse(Call<JarvisCommandResponse> call, Response<JarvisCommandResponse> response) {
                // Cancel timeout
                timeoutHandler.removeCallbacks(timeoutRunnable);

                if (response.isSuccessful() && response.body() != null) {
                    handleBackendResponse(response.body());
                } else {
                    handleFallbackResponse(command);
                }
            }

            @Override
            public void onFailure(Call<JarvisCommandResponse> call, Throwable t) {
                // Cancel timeout
                timeoutHandler.removeCallbacks(timeoutRunnable);
                Log.e(TAG, "Backend error: " + t.getMessage());
                handleFallbackResponse(command);
            }
        });
    }

    private void handleBackendResponse(JarvisCommandResponse response) {
        showLoading(false);

        String responseText = response.getResponse();
        if (responseText != null && !responseText.isEmpty()) {
            if (tvResponse != null) {
                tvResponse.setText("🤖 Jarvis:\n\n" + responseText);
            }
            speak(responseText);
        }

        if (etCommand != null) etCommand.setText("");
    }

    private void handleFallbackResponse(String command) {
        String response = "I can help with:\n" +
                "🔢 Math: Just say any calculation\n" +
                "🔍 Search: 'Search [topic]'\n" +
                "📺 YouTube: 'YouTube [search]'\n" +
                "⏰ Time: 'What's the time?'\n" +
                "😄 Jokes: 'Tell me a joke'";

        respondInstantly(response, "I can help with calculations, searches, time, and jokes");
    }

    private void showSettingsInfo() {
        String info = "⚙️ Jarvis Settings\n\n" +
                "🎤 Voice: " + (isTTSReady ? "Active" : "Inactive") + "\n" +
                "⚡ Mode: Fast Local Processing\n" +
                "🌐 Backend: Fallback enabled\n" +
                "📱 All actions on your device ✅";

        respondInstantly(info, "Settings displayed");
    }

    private void showHelpDialog() {
        String help = "📱 Jarvis Commands:\n\n" +
                "🔢 CALCULATOR:\n" +
                "• 2+2, 5*3, 100/5\n" +
                "• Calculate 15*7\n" +
                "• What's 25+30?\n\n" +
                "🔍 SEARCH:\n" +
                "• Search Python\n" +
                "• YouTube tutorials\n" +
                "• Find AI news\n\n" +
                "⏰ TIME & DATE:\n" +
                "• What's the time?\n" +
                "• Tell me the date\n\n" +
                "😄 FUN:\n" +
                "• Tell me a joke\n\n" +
                "Just speak naturally! 🎤";

        respondInstantly(help, "Help information displayed");
    }

    private void showLoading(boolean show) {
        if (progressBar != null) {
            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        }
        if (btnSendCommand != null) {
            btnSendCommand.setEnabled(!show);
        }
    }

    @Override
    protected void onDestroy() {
        if (pulseAnimator != null) pulseAnimator.cancel();
        if (pulseAnimatorY != null) pulseAnimatorY.cancel();
        if (timeoutHandler != null) timeoutHandler.removeCallbacks(timeoutRunnable);

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
        if (pulseAnimator != null) pulseAnimator.pause();
        if (pulseAnimatorY != null) pulseAnimatorY.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (pulseAnimator != null) pulseAnimator.resume();
        if (pulseAnimatorY != null) pulseAnimatorY.resume();
    }
}