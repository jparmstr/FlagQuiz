package com.example.android.flagquiz;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class MainActivity extends AppCompatActivity {

    // Constants
    public static final int ACTIVITY_MENU_CHOICE = R.menu.menu_quiz_debug;
    public static final String HIGHSCORES_FILENAME = "flagQuizHighScores";

    // High Scores
    public HighScoreItem[] highScores = new HighScoreItem[10];

    public enum quizDifficulty {easy, normal, hard}

    // This value does not persist between launches of MainActivity
    // I might need to use the Shared Preferences file to keep track of whether this array has been initialized
    // TODO: use Shared Prefs to keep track of first-run initialization boolean
    // TODO: on first run only, set default values for each of the 10 high scores
    //      (we get crashes if the highScores array has empty places)
//    private boolean initialized = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        if (!initialized) {
//            HighScoreItem testItem = new HighScoreItem();
//            testItem.setName("Bob");
//            testItem.setQuizDifficulty(-1);
//            testItem.setScore(0);
//            highScores[0] = testItem;
//            highScores[1] = testItem;
//            highScores[2] = testItem;
//            highScores[3] = testItem;
//            highScores[4] = testItem;
//            highScores[5] = testItem;
//            highScores[6] = testItem;
//            highScores[7] = testItem;
//            highScores[8] = testItem;
//            highScores[9] = testItem;
//
//            saveHighScores();
//
//            initialized = true;
//        }

        loadHighScores();

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
        buttonHighScores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentHighScores = new Intent(getApplicationContext(), HighScoresActivity.class);
                startActivity(intentHighScores);
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

    // Display Toast notification
    private void displayToast(String textToShow) {
        // Display the Toast notification
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, textToShow, duration);
        toast.show();
    }

    private void loadHighScores() {
        try {
            FileInputStream fis = getApplicationContext().openFileInput(HIGHSCORES_FILENAME);
            ObjectInputStream is = new ObjectInputStream(fis);
            highScores= (HighScoreItem[]) is.readObject();
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

    private String loadString() {
        try {
            FileInputStream inputStream = openFileInput("stringTestFile");
            InputStreamReader streamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(streamReader);

            String result = "";

            String readString = bufferedReader.readLine();
            while (readString != null) {
                result += readString;
                readString = bufferedReader.readLine();
            }

            return result;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return "File Not Found exception";
        } catch (IOException e) {
            e.printStackTrace();
            return "IO exception";
        }
    }

    private void saveString() {
        File file = new File(getApplicationContext().getFilesDir(), "stringTestFile");

        FileOutputStream fileOutputStream;

        try {
            fileOutputStream = openFileOutput("stringTestFile", MODE_PRIVATE);
            fileOutputStream.write("test value".getBytes());
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}