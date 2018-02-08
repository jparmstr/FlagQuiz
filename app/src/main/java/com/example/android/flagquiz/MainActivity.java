package com.example.android.flagquiz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    // Constants
    public static final int ACTIVITY_MENU_CHOICE = R.menu.menu_quiz_debug;

    public enum quizDifficulty {easy, normal, hard}

    ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get button references
        Button buttonEasyQuiz = findViewById(R.id.buttonQuizEasy);
        Button buttonNormalQuiz = findViewById(R.id.buttonQuizNormal);
        Button buttonHardQuiz = findViewById(R.id.buttonQuizHard);
        Button buttonFlagViewer = findViewById(R.id.buttonFlagViewer);
        Button buttonHighScores = findViewById(R.id.buttonHighScores);

        // Add button click handlers
        buttonEasyQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentEasyQuiz = new Intent(getApplicationContext(), QuizActivity.class);
                intentEasyQuiz.putExtra("DIFFICULTY", quizDifficulty.easy.ordinal()); // 0
                startActivity(intentEasyQuiz);
            }
        });
        buttonNormalQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentNormalQuiz = new Intent(getApplicationContext(), QuizActivity.class);
                intentNormalQuiz.putExtra("DIFFICULTY", quizDifficulty.normal.ordinal()); // 1
                startActivity(intentNormalQuiz);
            }
        });
        buttonHardQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentHardQuiz = new Intent(getApplicationContext(), QuizActivity.class);
                intentHardQuiz.putExtra("DIFFICULTY", quizDifficulty.hard.ordinal()); // 2
                startActivity(intentHardQuiz);
            }
        });
        buttonFlagViewer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentFlagViewer = new Intent(getApplicationContext(), FlagViewerActivity.class);
                startActivity(intentFlagViewer);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(ACTIVITY_MENU_CHOICE, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuItemTimerDevelopment:
                goToTimerDevelopmentActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void goToTimerDevelopmentActivity() {
        Intent intentTimerDevelopmentActivity = new Intent(getApplicationContext(), TimerDevelopmentActivity.class);
        startActivity(intentTimerDevelopmentActivity);
    }
}