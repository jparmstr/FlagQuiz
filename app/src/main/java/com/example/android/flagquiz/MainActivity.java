package com.example.android.flagquiz;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
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

    // TODO: Questions are in a variety of formats such as free text response, checkboxes, and radio buttons.
    // Checkboxes are only used for questions with multiple right answers. Radio buttons are only used for questions with a single right answer.

    // TODO: The app gracefully handles displaying all the content on screen when rotated. Either by updating the layout, adding a scrollable feature or some other mechanism that adheres to Android development guidelines.

    // Constants
    public static final int ACTIVITY_MENU_CHOICE = R.menu.menu_quiz_debug;
    public static final String HIGHSCORES_FILENAME = "flagQuizHighScores";
    public static final String PREFS_FILENAME = "flagquiz.preferences";

    // High Scores
    public HighScoreItem[] highScores = new HighScoreItem[10];
    private boolean initialized_highScores;

    public enum quizDifficulty {easy, normal, hard}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadPreferences();

        if (!initialized_highScores) {
            initializeHighScores();
        }

        savePreferences();

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
                difficultyButtonHandler(quizDifficulty.easy.ordinal());
            }
        });
        buttonNormalQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                difficultyButtonHandler(quizDifficulty.normal.ordinal());
            }
        });
        buttonHardQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                difficultyButtonHandler(quizDifficulty.hard.ordinal());
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

    private void difficultyButtonHandler(int difficulty) {
        Intent intent = new Intent(getApplicationContext(), QuizActivity.class);
        intent.putExtra("DIFFICULTY", difficulty); // 0

        // Pass the current screen orientation so that QuizActivity can lock it there during the quiz
        int thisOrientation = getScreenRotation();
        thisOrientation = translateScreenRotation_degrees_toActivityInfoScreenOrientation(thisOrientation);
        intent.putExtra("screenOrientation", thisOrientation);

        startActivity(intent);
    }

    // Get the screen orientation in degrees
    private int getScreenRotation() {
        final Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();

        switch (display.getRotation()) {
            case Surface.ROTATION_0:
//                System.out.println("SCREEN_ORIENTATION_PORTRAIT");
                return 0;
            case Surface.ROTATION_90:
//                System.out.println("SCREEN_ORIENTATION_LANDSCAPE");
                return 90;
            case Surface.ROTATION_180:
//                System.out.println("SCREEN_ORIENTATION_REVERSE_PORTRAIT");
                return 180;
            case Surface.ROTATION_270:
//                System.out.println("SCREEN_ORIENTATION_REVERSE_LANDSCAPE");
                return 270;
        }

        return -1;
    }

    // Translate screen rotation (degrees) to screen orientation (ActivityInfo)
    // See: https://developer.android.com/reference/android/content/pm/ActivityInfo.html#SCREEN_ORIENTATION_LANDSCAPE
    private int translateScreenRotation_degrees_toActivityInfoScreenOrientation(int degrees) {
        switch (degrees) {
            case 0:
                return ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
            case 90:
                return ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
            case 180:
                return ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
            case 270:
                return ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
            default:
                return ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
        }
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

    public void loadPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_FILENAME, 0);

        initialized_highScores = sharedPreferences.getBoolean("initialized_highScores", false);
    }

    public void savePreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_FILENAME, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean("initialized_highScores", initialized_highScores);

        editor.apply();
    }

    /*
    Set default values for each of the 10 high scores
    Happens only when the application is run for the first time
    (Saved via initialized_highScore SharedPreferences value
    * */
    private void initializeHighScores() {
        HighScoreItem testItem = new HighScoreItem();
        testItem.setName("Bob");
        testItem.setQuizDifficulty(-1);
        testItem.setScore(0);
        highScores[0] = testItem;
        highScores[1] = testItem;
        highScores[2] = testItem;
        highScores[3] = testItem;
        highScores[4] = testItem;
        highScores[5] = testItem;
        highScores[6] = testItem;
        highScores[7] = testItem;
        highScores[8] = testItem;
        highScores[9] = testItem;

        saveHighScores();

        initialized_highScores = true;

        // The Shared Preferences initialized_highScores will be saved by MainActivity.onCreate()
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