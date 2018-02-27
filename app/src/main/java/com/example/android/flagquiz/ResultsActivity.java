package com.example.android.flagquiz;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import static com.example.android.flagquiz.MainActivity.HIGHSCORES_FILENAME;

public class ResultsActivity extends AppCompatActivity {

    // View references
    TextView resultsScoreTextView;
    TextView resultsNumberCorrectTextView;
    ProgressBar resultsProgressBar;
    Button resultsDoneButton;
    TextView resultsNewHighScoreTextView;
    TextView resultsNewHighScore_nameTextView;
    Button resultsNewHighScoreButton;

    // Quiz Result variables
    int numberCorrect;
    int quiz_number_of_questions;
    int score;
    int streak_longest;
    int quizDifficulty;

    // High Scores
    public HighScoreItem[] highScores = new HighScoreItem[10];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        // Load High Scores
        loadHighScores();

        // Retrieve data about the Quiz
        Intent intent = getIntent();
        numberCorrect = intent.getIntExtra("numberCorrect", 0);
        quiz_number_of_questions = intent.getIntExtra("quiz_number_of_questions", 0);
        score = intent.getIntExtra("score", 0);
        quizDifficulty = intent.getIntExtra("difficulty", -1);
        streak_longest = intent.getIntExtra("streak_longest", 0);

        // Get View references
        getViewReferences();

        // Set Views with data name Quiz results
        resultsScoreTextView.setText("Score: " + String.valueOf(score));
        resultsNumberCorrectTextView.setText("Number Correct: " + String.valueOf(numberCorrect) +
                " / " + String.valueOf(quiz_number_of_questions));
        resultsProgressBar.setMax(quiz_number_of_questions);
        resultsProgressBar.setProgress(numberCorrect);

        // Hide New High Score controls if not a new high score
        if (score < lowestHighScoreValue()) {
            resultsNewHighScoreTextView.setVisibility(View.GONE);
            resultsNewHighScoreButton.setVisibility(View.GONE);
        }
    }

    public void click_newHighScore_EnterYourName(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Title");

        // Set up the input
        final EditText input = new EditText(this);

        // Need to set margin on EditText input

        // Specify the type of input expected
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        builder.setTitle("Enter Your Name");

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Create a new high score item
                HighScoreItem thisHighScore = new HighScoreItem();

                thisHighScore.setQuizDifficulty(quizDifficulty);
                thisHighScore.setName(input.getText().toString());
                thisHighScore.setScore(score);

                // When the Activity loaded we only let the New High Score Views be visible if
                // this was indeed a new high score. Therefore we don't need to make that check
                // again here.
                highScores[lowestHighScoreIndex()] = thisHighScore;

                saveHighScores();

                // Update the Results Views
                resultsNewHighScoreButton.setVisibility(View.GONE);
                resultsNewHighScore_nameTextView.setVisibility(View.VISIBLE);
                resultsNewHighScore_nameTextView.setText(thisHighScore.getName());
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    //region Methods to manage High Scores array

    private int lowestHighScoreValue() {
        int lowest = highScores[0].getScore();

        for (HighScoreItem h : highScores) {
            if (h == null) {
                lowest = 0;
            } else {
                if (h.getScore() < lowest) {
                    lowest = h.getScore();
                }
            }
        }

        return lowest;
    }

    private int lowestHighScoreIndex() {
        int lowest = highScores[0].getScore();
        int index = 0;
        int counter = 0;

        for (HighScoreItem h : highScores) {
            if (h == null) {
                lowest = 0;
                index = counter;
            } else {
                if (h.getScore() < lowest) {
                    lowest = h.getScore();
                    index = counter;
                }
            }

            counter++;
        }

        return index;
    }

    // TODO: can I import the loadHighScores(), saveHighScores() from MainActivity like I do with HIGHSCORES_FILENAME?
    private void loadHighScores() {
        try {
            FileInputStream fis = getApplicationContext().openFileInput(HIGHSCORES_FILENAME);
            ObjectInputStream is = new ObjectInputStream(fis);
            highScores = (HighScoreItem[]) is.readObject();
            is.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void saveHighScores() {
        try {
            FileOutputStream fos = getApplicationContext().openFileOutput(HIGHSCORES_FILENAME, Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(highScores);
            os.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //endregion Methods to manage High Scores array

    // Display Toast notification
    private void displayToast(String textToShow) {
        // Display the Toast notification
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, textToShow, duration);
        toast.show();
    }

    private void getViewReferences() {
        resultsScoreTextView = findViewById(R.id.resultsScoreTextView);
        resultsNumberCorrectTextView = findViewById(R.id.resultsNumberCorrectTextView);
        resultsProgressBar = findViewById(R.id.resultsProgressBar);
        resultsDoneButton = findViewById(R.id.resultsDoneButton);
        resultsNewHighScoreTextView = findViewById(R.id.newHighScoreTextView);
        resultsNewHighScoreButton = findViewById(R.id.buttonNewHighScore_EnterYourName);
        resultsNewHighScore_nameTextView = findViewById(R.id.resultsNewHighScore_nameTextView);
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