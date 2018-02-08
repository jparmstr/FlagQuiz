package com.example.android.flagquiz;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.DecelerateInterpolator;
import android.widget.ProgressBar;
import android.widget.TextView;

public class TimerDevelopmentActivity extends AppCompatActivity {

    // View references
    TextView timerTextView;
    TextView progressCircle_text;
    ProgressBar progressBarHorizontal;
    ProgressBar progressBarCircle;

    long startTime = 0;
    int countdownTimeMilliseconds = 5 * 1000;

    // TODO: make a progress bar representing time remaining
    // TODO: make a circle representing time remaining

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer_development);

        // Get View references
        timerTextView = findViewById(R.id.textViewTimer);
        progressCircle_text = findViewById(R.id.progressCircle_text_development);
        progressBarHorizontal = findViewById(R.id.progressBarHorizontal);
        progressBarCircle = findViewById(R.id.progressCircle_development);

        // Show back button on action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        countDown_setUpProgressBars_maxValues();
        timerStart();
    }

    /*
    * Choose which method the timer calls
    * Defines how often the View(s) are updated
    * */
    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
//            showElaspedTime();
            countDown();

            timerHandler.postDelayed(this, 10);
        }
    };

    @Override
    public void onPause() {
        super.onPause();
        timerStop();
    }

    // Start the timer
    private void timerStart() {
        startTime = System.currentTimeMillis();
        timerHandler.postDelayed(timerRunnable, 0);
    }

    // Stop the timer
    private void timerStop() {
        timerHandler.removeCallbacks(timerRunnable);
    }

    // Start the circular progress bar animation
    // Note: this seems unnecessary
    private void circularProgressBar_startAnimation() {
        ObjectAnimator animation = ObjectAnimator.ofInt (progressBarCircle, "progress", 0, progressBarCircle.getMax()); // see this max value coming back here, we animate towards that value
        animation.setDuration (countdownTimeMilliseconds); //in milliseconds
        animation.setInterpolator (new DecelerateInterpolator());
        animation.start ();
    }

    // Stop the circular progress bar animation
    private void circularProgressBar_stopAnimation() {
        progressBarCircle.clearAnimation();
    }

    private void countDown_setUpProgressBars_maxValues() {
        // Set max values for progress bars
        progressBarHorizontal.setMax(countdownTimeMilliseconds);
        progressBarCircle.setMax(countdownTimeMilliseconds);

        // Start animation (only after setting max values)
        // Note: this seems unnecessary
//        circularProgressBar_startAnimation();
    }

    private void countDown() {
        long milliseconds_elapsed = System.currentTimeMillis() - startTime;
        long milliseconds_remaining = countdownTimeMilliseconds - milliseconds_elapsed;
        int centiseconds = (int) (milliseconds_remaining / 10);
        int deciseconds = (int) (milliseconds_remaining / 100);
        int seconds = (int) (milliseconds_remaining / 1000);
        int minutes = seconds / 60;

        if (milliseconds_remaining > 0) {
            // Update timer TextView
            timerTextView.setText(String.format("%d:%02d:%02d", minutes, seconds % 60, centiseconds % 100));

            // Update progress
            progressBarHorizontal.setProgress((int) milliseconds_remaining);
            progressBarCircle.setProgress((int) milliseconds_remaining);

            // Update progress circle TextView
            progressCircle_text.setText(String.format("%02d:%02d", seconds % 60, centiseconds % 100));
        } else {
            timerTextView.setText("Time Up!");

            progressBarHorizontal.setProgress(0);
            progressBarCircle.setProgress(0);

            progressCircle_text.setText("00:00");

            timerStop();
        }
    }

    // Displays text showing how much time has elapsed
    private void showElaspedTime() {
        long milliseconds = System.currentTimeMillis() - startTime;
        int centiseconds = (int) (milliseconds / 10);
        int deciseconds = (int) (milliseconds / 100);
        int seconds = (int) (milliseconds / 1000);
        int minutes = seconds / 60;

        timerTextView.setText(String.format("%d:%02d:%02d", minutes, seconds % 60, centiseconds % 100));

        // (works best with 10 millisecond delay between updates)
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_timer_development, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_resetTimer:
                // Stop the timer
                timerStop();
                // Start the timer
                timerStart();
                return true;
            case android.R.id.home:
                goToMainActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void goToMainActivity() {
        Intent intentMainActivity = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intentMainActivity);
    }

    private void goBackToQuizActivity() {
        // Switch to the Main Activity
        Intent intentQuizActivity = new Intent(getApplicationContext(), QuizActivity.class);
        startActivity(intentQuizActivity);
    }
}
