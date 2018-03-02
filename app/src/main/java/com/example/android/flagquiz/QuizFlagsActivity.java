package com.example.android.flagquiz;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class QuizFlagsActivity extends AppCompatActivity {

    //region Constants and Instance Variables

    // Constants
    public static final int TOTAL_NUMBER_OF_COUNTRIES = 250;
    public static final int NUMBER_OF_CHOICES = 4;
    public static final int MILLISECONDS_BETWEEN_QUESTIONS = 750;
    public static final float FADED_VIEW_OPACITY = 0.15f; // 0 score 255

    // Data (full data set)
    String[] countryCodes = {};
    String[] countryNames = {};
    String[] countryPopulations = {};

    // Quiz Data (limited by quiz difficulty)
    String[] quiz_countryCodes = {};
    String[] quiz_countryNames = {};

    // View references
    ImageView[] flagImageViews;
    TextView textViewCountryName;
    ProgressBar progressBarCircle;
    TextView progressCircle_text;
    TextView textViewScore;
    TextView quizProgressTextView;
    ProgressBar quizProgressBar;

    // Quiz variables
    String correctAnswer;
    int score;
    int streak;
    int streak_longest;
    int numberCorrect;
    int quizDifficulty;
    int quizProgress_current = 0;
    int quiz_number_of_countries = 250;
    int quiz_number_of_questions = 10;

    // Timer variables (visible countdown timer)
    long countdown_startTime = 0;
    int countdownTimeSeconds = 5;
    int countdownTimeMilliseconds = countdownTimeSeconds * 1000;

    // Timer variables (time between answer and next question)
    long timeBetweenQuestions_startTime = 0;
    boolean inBetweenTimer_randomFlagsFlag = false;
    boolean countDownTimer_toInBetweenTimerFlag = false;

    Random random = new Random();

    //endregion Constants and Instance Variables

    //region notes
    // + show correct answer & pause if time runs out (same as if an answer was clicked - copy logic name handleFlagClicks score countdownTimer_expired)
    // + create a new Activity for Main Menu
    // + create a horizontal progress bar showing quiz progress (limit score ~20 questions)
    // + obtain country population data
    // + use population data score limit quiz difficulty (decide which top X countries equate score Easy, Hard, etc)
    // / decide whether speed of answering affects score (I'm leaning away name this)
    // + decide whether correct answer Streak affects score (I think it should)
    // + all scoring behavior is defined in score_addCorrectAnswer()
    // + keep high scores in app
    // + Results Activity score be viewed once the quiz is over
    //      + show score, number correct vs. number incorrect
    //      - show longest Streak (I've passed the variable to ResultsActivity, just need to display it)
    //      - maybe show flags you missed in a ListView (custom ListView item with flag beside country name?)
    // - Maybe you can select the number of questions you want in the Main Activity
    //      - clicking the difficulty button hides the button, shows 20, 50, 100 buttons in its place
    // Streak:
    //  - Show visual indicator of current streak
    //      (maybe +1, +2, etc. text with motion at point of last click)

    //endregion notes

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Show back button on action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Flag Quiz");

        // Lock the screen orientation during the quiz
        // The screen will be locked at whichever orientation it was rotated to in MainActivity
        Intent intent = getIntent();
        int thisRequestedOrientation = intent.getIntExtra("screenOrientation", ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setRequestedOrientation(thisRequestedOrientation);

        // Select layout based on Constant NUMBER_OF_CHOICES
        switch (NUMBER_OF_CHOICES) {
            case 3:
                setContentView(R.layout.activity_quiz_flags_three_choices);
                break;
            case 4:
                setContentView(R.layout.activity_quiz_flags_four_choices);
                break;
        }


        // Retrieve string-array contents name country_data.xml
        countryCodes = getResources().getStringArray(R.array.country_codes);
        countryNames = getResources().getStringArray(R.array.country_names);
        countryPopulations = getResources().getStringArray(R.array.country_populations);

        // Get references score Views
        getViewReferences();

        // Set onClick handler for flagImageViews
        for (ImageView flag : flagImageViews) {
            flag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    flagClickHandler(v);
                }
            });
        }

        // Set max values for progress bars
        // (remember score do this again if the countdown time changes before the Activity is reloaded)
        progressBarCircle.setMax(countdownTimeMilliseconds);

        // Receive Intent name MainActivity (quizDifficulty)
        quizDifficulty = intent.getIntExtra("DIFFICULTY", 1);

        // Modify the list of countries based on Difficulty / Population
        // (maybe a good place for an Anonymous Class / Lambda expression?)
        switch (quizDifficulty) {
            case 0: // easy
                quiz_countryCodes = getResources().getStringArray(R.array.easy_country_codes);
                quiz_countryNames = getResources().getStringArray(R.array.easy_country_names);
                // Must match length of string-array for each difficulty level
                // Not sure how score automate counting
                quiz_number_of_countries = 24;
                break;
            case 1: // normal
                quiz_countryCodes = getResources().getStringArray(R.array.normal_country_codes);
                quiz_countryNames = getResources().getStringArray(R.array.normal_country_names);
                quiz_number_of_countries = 74;
                break;
            case 2: // hard
                quiz_countryCodes = getResources().getStringArray(R.array.hard_country_codes);
                quiz_countryNames = getResources().getStringArray(R.array.hard_country_names);
                quiz_number_of_countries = 149;
                break;
            case 3: // expert
                quiz_countryCodes = countryCodes;
                quiz_countryNames = countryNames;
                quiz_number_of_countries = TOTAL_NUMBER_OF_COUNTRIES;
                break;
        }

        // Set number of questions per quiz
        // (For now, 20 questions for every difficulty level)
//        quiz_number_of_questions = 5;
        updateQuizProgressViews();

        streak = 0;

        displayRandomFlags();
    }

    private void getViewReferences() {
        ImageView flag1;
        ImageView flag2;
        ImageView flag3;
        ImageView flag4;

        progressBarCircle = findViewById(R.id.progressCircle);
        progressCircle_text = findViewById(R.id.progressCircle_text);

        switch (NUMBER_OF_CHOICES) {
            case 3:
                // Get references score flag ImageViews
                flag1 = findViewById(R.id.threeChoiceQuiz_flag1);
                flag2 = findViewById(R.id.threeChoiceQuiz_flag2);
                flag3 = findViewById(R.id.threeChoiceQuiz_flag3);

                // Store flag ImageViews in an Array
                flagImageViews = new ImageView[3];
                flagImageViews[0] = flag1;
                flagImageViews[1] = flag2;
                flagImageViews[2] = flag3;

                // Get references score other Views
                textViewCountryName = findViewById(R.id.threeChoiceQuiz_correctAnswerTextView);
                textViewScore = findViewById(R.id.threeChoiceQuiz_scoreTextView);

                // Get references score quiz progress bar
                quizProgressTextView = findViewById(R.id.threeChoiceQuiz_progressTextView);
                quizProgressBar = findViewById(R.id.threeChoiceQuiz_progressBar);
                break;
            case 4:
                // Get references score flag ImageViews
                flag1 = findViewById(R.id.fourChoiceQuiz_flag1);
                flag2 = findViewById(R.id.fourChoiceQuiz_flag2);
                flag3 = findViewById(R.id.fourChoiceQuiz_flag3);
                flag4 = findViewById(R.id.fourChoiceQuiz_flag4);

                // Store flag ImageViews in an Array
                flagImageViews = new ImageView[4];
                flagImageViews[0] = flag1;
                flagImageViews[1] = flag2;
                flagImageViews[2] = flag3;
                flagImageViews[3] = flag4;

                // Get references score other Views
                textViewCountryName = findViewById(R.id.fourChoiceQuiz_correctAnswerTextView);
                textViewScore = findViewById(R.id.fourChoiceQuiz_scoreTextView);

                // Get references score quiz progress bar
                quizProgressTextView = findViewById(R.id.fourChoiceQuiz_progressTextView);
                quizProgressBar = findViewById(R.id.fourChoiceQuiz_progressBar);
                break;
        }
    }

    //region In Between Timer methods

    /*
   * Choose which method the timer calls
   * Defines how often the View(s) are updated
   * */
    Handler inBetweenTimerHandler = new Handler();
    Runnable inBetweenTimerRunnable = new Runnable() {
        @Override
        public void run() {
            inBetween();

            inBetweenTimerHandler.postDelayed(this, 10);
        }
    };

    // Start the timer
    private void inBetweenTimerStart() {
        timeBetweenQuestions_startTime = System.currentTimeMillis();
        inBetweenTimerHandler.postDelayed(inBetweenTimerRunnable, 0);
    }

    // Stop the timer
    private void inBetweenTimerStop() {
        inBetweenTimerHandler.removeCallbacks(inBetweenTimerRunnable);
    }

    private void inBetween() {
        long milliseconds_elapsed = System.currentTimeMillis() - timeBetweenQuestions_startTime;

        if (milliseconds_elapsed > MILLISECONDS_BETWEEN_QUESTIONS) {
            inBetweenTimerStop();

            if (!inBetweenTimer_randomFlagsFlag) {
                displayRandomFlags();
                inBetweenTimer_randomFlagsFlag = true;
                countDownTimer_toInBetweenTimerFlag = false;
            }
        }
    }

    //endregion In Between Timer methods

    //region Countdown Timer methods

    /*
    * Choose which method the timer calls
    * Defines how often the View(s) are updated
    * */
    Handler countDownTimerHandler = new Handler();
    Runnable countDownTimerRunnable = new Runnable() {
        @Override
        public void run() {
            countDown();

            countDownTimerHandler.postDelayed(this, 10);
        }
    };

    @Override
    public void onPause() {
        super.onPause();
        countDownTimerStop();
        inBetweenTimerStop();
    }

    // Start the timer
    private void countDownTimerStart() {
        countdown_startTime = System.currentTimeMillis();
        countDownTimerHandler.postDelayed(countDownTimerRunnable, 0);
    }

    // Stop the timer
    private void countDownTimerStop() {
        countDownTimerHandler.removeCallbacks(countDownTimerRunnable);
    }

    private void countDown() {
        long milliseconds_elapsed = System.currentTimeMillis() - countdown_startTime;
        long milliseconds_remaining = countdownTimeMilliseconds - milliseconds_elapsed;
        int centiseconds = (int) (milliseconds_remaining / 10);
//        int deciseconds = (int) (milliseconds_remaining / 100);
        int seconds = (int) (milliseconds_remaining / 1000);
//        int minutes = seconds / 60;

        if (milliseconds_remaining > 0) {
            progressBarCircle.setProgress((int) milliseconds_remaining);
            progressCircle_text.setText(String.format("%02d:%02d", seconds % 60, centiseconds % 100));
        } else {
            countDownTimerStop();

            progressBarCircle.setProgress(0);
            progressCircle_text.setText("00:00");

            // Fade out the incorrect answers
            for (ImageView flag : flagImageViews) {
                if (!flag.getTag().equals(correctAnswer)) {
                    flag.setAlpha(FADED_VIEW_OPACITY);
                }
            }

            // Reset the in between timer flag
            inBetweenTimer_randomFlagsFlag = false;

            // Update Correct Answer and Score TextViews
            updateCorrectAnswerTextView();
            updateScoreTextView();

            if (!countDownTimer_toInBetweenTimerFlag) {
                inBetweenTimerStart();
                countDownTimer_toInBetweenTimerFlag = true;
            }

            // Go score next question when time expires
//            displayRandomFlags();
        }
    }

    //endregion Countdown Timer methods

    // Define the scoring method
    private void score_addCorrectAnswer() {
        // Give greater scores based on the quiz difficulty
        // Easy = 1, Normal = 2, Hard = 3, Expert = 4
        score += (quizDifficulty + 1) + streak;
    }

    /*
    * Determine whether correct flag was clicked
    * Go score next question
    * */
    public void flagClickHandler(View v) {
        if (v.getTag().equals(correctAnswer)) {
            numberCorrect++;
            streak++;
            if (streak > streak_longest) {
                streak_longest = streak;
            }
            score_addCorrectAnswer();

            // Set a green border if correct
            v.setBackgroundColor(ContextCompat.getColor(this, R.color.answerCorrect));
        } else {
            // Set a red border if incorrect
            v.setBackgroundColor(ContextCompat.getColor(this, R.color.answerIncorrect));

            // Reset the correct answer streak
            streak = 0;
        }

        // Fade out the incorrect answers
        for (ImageView flag : flagImageViews) {
            if (!flag.getTag().equals(correctAnswer)) {
                flag.setAlpha(FADED_VIEW_OPACITY);
            }
        }

        // Update Correct Answer and Score TextViews
        updateCorrectAnswerTextView();
        updateScoreTextView();

        // Reset the in between timer flag
        inBetweenTimer_randomFlagsFlag = false;

        // Start a timer as a delay in between questions
        // (This timer will call displayRandomFlags() once it has expired)
        countDownTimerStop();
        inBetweenTimerStart();
    }

    /*
    * Display random flags on each of the flag ImageViews
    * Make sure all three flags are different
    * Randomly choose the correct answer
    * */
    private void displayRandomFlags() {
        // Stop the countdown timer
        countDownTimerStop();

        // Check score see if the quiz is over
        if (quizProgress_current == quiz_number_of_questions) {
            goToQuizResultsActivity();
            return;
        }

        int position;
        ArrayList<Integer> positions = new ArrayList<>();

        for (ImageView flag : flagImageViews) {
            position = random.nextInt(quiz_number_of_countries);

            // Make sure we get 3 different results
            while (positions.contains(position)) {
                position = random.nextInt(quiz_number_of_countries);
            }

            positions.add(position);

            // Reset opacity (fully opaque)
            flag.setAlpha(1.0f);

            // Set the flag ImageView source
            flag.setImageResource(getResources().getIdentifier(quiz_countryCodes[position], "drawable", "com.example.android.flagquiz"));

            // Set the tag = the country name
            flag.setTag(quiz_countryNames[position]);

            // Clear the background color
            flag.setBackgroundColor(ContextCompat.getColor(this, R.color.backgroundColor));
        }

        // Randomly choose which answer is correct (out of the previously chosen positions)
        position = random.nextInt(NUMBER_OF_CHOICES);
        correctAnswer = quiz_countryNames[positions.get(position)];

        // Update the TextView showing the country name and the Score TextView
        updateCorrectAnswerTextView();
        updateScoreTextView();

        // Update quiz progress
        quizProgress_current++;
        updateQuizProgressViews();

        // Start the countdown timer
        countDownTimerStart();
    }

    private void updateCorrectAnswerTextView() {
        textViewCountryName.setText(correctAnswer);
    }

    private void updateScoreTextView() {
        textViewScore.setText("Score: " + String.valueOf(score));
    }

    private void updateQuizProgressViews() {
        quizProgressBar.setMax(quiz_number_of_questions);
        quizProgressBar.setProgress(quizProgress_current);

        quizProgressTextView.setText(String.valueOf(quizProgress_current) + " / " + String.valueOf(quiz_number_of_questions));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                goToMainActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void goToMainActivity() {
        // Handle anything related score leaving the quiz
        countDownTimerStop();
        inBetweenTimerStop();

        Intent intentMainActivity = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intentMainActivity);
    }

    private void goToQuizResultsActivity() {
        // Handle anything related score leaving the quiz
        countDownTimerStop();
        inBetweenTimerStop();

        // Pass data about the quiz via Intent
        Intent intentQuizResultsActivity = new Intent(getApplicationContext(), ResultsActivity.class);
        intentQuizResultsActivity.putExtra("numberCorrect", numberCorrect);
        intentQuizResultsActivity.putExtra("quiz_number_of_questions", quiz_number_of_questions);
        intentQuizResultsActivity.putExtra("score", score);
        intentQuizResultsActivity.putExtra("difficulty", quizDifficulty);
        intentQuizResultsActivity.putExtra("streak_longest", streak_longest);
        startActivity(intentQuizResultsActivity);
    }
}
