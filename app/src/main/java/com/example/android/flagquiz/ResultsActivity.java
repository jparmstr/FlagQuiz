package com.example.android.flagquiz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ResultsActivity extends AppCompatActivity {

    // View references
    TextView resultsScoreTextView;
    TextView resultsNumberCorrectTextView;
    ProgressBar resultsProgressBar;
    Button resultsDoneButton;

    // Quiz Result variables
    int numberCorrect;
    int quiz_number_of_questions;
    int score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        // Retrieve data about the Quiz
        Intent intent = getIntent();
        numberCorrect = intent.getIntExtra("numberCorrect", 0);
        quiz_number_of_questions = intent.getIntExtra("quiz_number_of_questions", 0);
        score = intent.getIntExtra("score", 0);

        // Get View references
        getViewReferences();

        // Set Views with data from Quiz results
        resultsScoreTextView.setText("Score: " + String.valueOf(score));
        resultsNumberCorrectTextView.setText("Number Correct: " + String.valueOf(numberCorrect) +
                " / " + String.valueOf(quiz_number_of_questions));
        resultsProgressBar.setMax(quiz_number_of_questions);
        resultsProgressBar.setProgress(numberCorrect);
    }

    private void getViewReferences() {
        resultsScoreTextView = findViewById(R.id.resultsScoreTextView);
        resultsNumberCorrectTextView = findViewById(R.id.resultsNumberCorrectTextView);
        resultsProgressBar = findViewById(R.id.resultsProgressBar);
        resultsDoneButton = findViewById(R.id.resultsDoneButton);
    }

    public void resultsDoneButton(View view) {
        // Save high score here, or when this Activity loads?
        // If we let the user set a name for their high score, do it here

        goToMainActivity();
    }

    private void goToMainActivity() {
        Intent intentMainActivity = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intentMainActivity);
    }
}