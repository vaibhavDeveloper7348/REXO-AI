package com.vaibhav.quantumcore3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.content.Intent;

/**
 * MainActivity - Home screen with animated AI assistant selection
 * Features:
 * - Animated pulsing icons
 * - 3 AI assistant cards
 * - App structure guide
 * - Navigation to all activities
 */
public class MainActivity extends AppCompatActivity {

    // UI Components
    private CardView cardJackAI, cardJarvisAI, cardSQLDetection;
    private ImageView ivAnimatedIcon, ivLargeAnimation;

    // Animation Drawables
    private AnimationDrawable animationDrawable;
    private AnimationDrawable largeAnimationDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        initializeViews();

        // Setup click listeners for navigation
        setupClickListeners();
    }

    /**
     * Initialize all UI components
     */
    private void initializeViews() {
        // Card views for AI assistants
        cardJackAI = findViewById(R.id.cardJackAI);
        cardJarvisAI = findViewById(R.id.cardJarvisAI);
        cardSQLDetection = findViewById(R.id.cardSQLDetection);

        // Animated image views
        ivAnimatedIcon = findViewById(R.id.ivAnimatedIcon);
        ivLargeAnimation = findViewById(R.id.ivLargeAnimation);
    }



    /**
     * Setup click listeners for all cards
     */
    private void setupClickListeners() {
        // Jack AI - Voice Assistant
        if (cardJackAI != null) {
            cardJackAI.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    navigateToActivity(JackAiActivity.class, "Jack AI");
                }
            });
        }

        // Jarvis AI - General Intelligence
        if (cardJarvisAI != null) {
            cardJarvisAI.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    navigateToActivity(JarvisActivity.class, "Jarvis AI");
                }
            });
        }

        // SQL Guard - Security Scanner
        if (cardSQLDetection != null) {
            cardSQLDetection.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    navigateToActivity(SqlDetectionActivity.class, "SQL Guard");
                }
            });
        }
    }

    /**
     * Navigate to activity with animation
     */
    private void navigateToActivity(Class<?> targetActivity, String activityName) {
        Intent intent = new Intent(MainActivity.this, targetActivity);
        startActivity(intent);

        // Add transition animation
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    /**
     * Destroy - cleanup
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Stop all animations
        if (animationDrawable != null) {
            animationDrawable.stop();
        }

        if (largeAnimationDrawable != null) {
            largeAnimationDrawable.stop();
        }

        android.util.Log.d("MainActivity", "🗑️ MainActivity destroyed");
    }
}