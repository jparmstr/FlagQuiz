package com.example.android.flagquiz;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Pete on 3/1/2018.
 */

public class QuizQuestionsActivity extends AppCompatActivity {

    //region Constants and Instance Variables
    // Constants
    public static final int TOTAL_NUMBER_OF_COUNTRIES = 250;
    public static final int NUMBER_OF_CHOICES = 4;

    public enum questionType {radioButtons, checkBoxes, fillInTheBlank}

    // Data (full data set)
    String[] countryCodes = {};
    String[] countryNames = {};
    String[] countryPopulations = {};

    // Quiz Data (limited by quiz difficulty)
    String[] quiz_countryCodes = {};
    String[] quiz_countryNames = {};

    // View references
    LinearLayout fillInTheBlank_mainLayout;

    // Quiz variables
    Question[] questions;
    int score;
    int numberCorrect;
    int quizDifficulty;
    int quiz_number_of_countries = 250;
    int quiz_number_of_questions = 2;

    // No timers (for now)

    Random random = new Random();

    //endregion Constants and Instance Variables

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_questions);

        // Show back button on action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setTitle("Multiple Questions Quiz");

        // Retrieve string-array contents name country_data.xml
        countryCodes = getResources().getStringArray(R.array.country_codes);
        countryNames = getResources().getStringArray(R.array.country_names);
        countryPopulations = getResources().getStringArray(R.array.country_populations);

        // Receive Intent from MainActivity (quizDifficulty)
        Intent intent = getIntent();
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

        // Get view references
        fillInTheBlank_mainLayout = findViewById(R.id.questions_mainLayout);


        // Only generate questions if this isn't a saved instance state
        // Otherwise the Questions[] will be loaded in onRestoreInstanceState
        if (savedInstanceState == null) {
            generateQuestions();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putInt("quizDifficulty", quizDifficulty);

        // Save the state of each Question in questions via Parcelable
        // (Thankfully the fragments seem to be persisting automatically)
        savedInstanceState.putParcelableArray("questions", questions);

        // Data (full data set) variables can be loaded again just fine
        // Quiz Data can be loaded again
        // View references can be created again
        // quiz_number_of_countries and quiz_number_of_questions can be created again (will always be the same according to quizDifficulty)
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        quizDifficulty = savedInstanceState.getInt("quizDifficulty");
        questions = (Question[]) savedInstanceState.getParcelableArray("questions");
    }

    private void generateQuestions() {
        // Resize the questions array to hold the number_of_questions
        questions = new Question[quiz_number_of_questions];

        // Begin the transaction
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        for (int i = 0; i < quiz_number_of_questions; i++) {
            Question thisQuestion = new Question();
            thisQuestion.create();

            questions[i] = thisQuestion;

            // Generate new fragments based on thisQuestionType
            switch (thisQuestion.thisQuestionType) {
                case 0:
                    thisQuestion.question_radioButtons = new Question_RadioButtons();
                    ft.add(R.id.questions_frameLayout, thisQuestion.question_radioButtons);
                    break;
                case 1:
                    thisQuestion.question_checkBoxes = new Question_CheckBoxes();
                    ft.add(R.id.questions_frameLayout, thisQuestion.question_checkBoxes);
                    break;
                case 2:
                    thisQuestion.question_fillInTheBlank = new Question_FillInTheBlank();
                    ft.add(R.id.questions_frameLayout, thisQuestion.question_fillInTheBlank);
                    break;
            }
        }

        ft.commit();

        for (Question thisQuestion : questions) {
            // Handle answer generation (random answers, random correct answer)
            generateAnswers(thisQuestion);
        }
    }

    private void generateAnswers(Question question) {
        // Randomly choose answers
        int position;
        ArrayList<Integer> positions = new ArrayList<>();
        for (int i = 0; i < NUMBER_OF_CHOICES; i++) {
            position = random.nextInt(quiz_number_of_countries);

            // Make sure we get 3 different results
            while (positions.contains(position)) {
                position = random.nextInt(quiz_number_of_countries);
            }

            positions.add(position);
        }

        // Randomly choose correct answer (out of the previously chosen positions)
        int correctAnswerPosition_relativeToChoices = random.nextInt(NUMBER_OF_CHOICES);
        String correctAnswer = quiz_countryNames[positions.get(correctAnswerPosition_relativeToChoices)];

        switch (question.thisQuestionType) {
            case 0:
                // Initialize choices array
                question.question_radioButtons.choicesText = new String[NUMBER_OF_CHOICES];

                // Create the RadioButton texts for choices
                for (int i = 0; i < NUMBER_OF_CHOICES; i++) {
                    question.question_radioButtons.choicesText[i] = quiz_countryNames[positions.get(i)];
                }

                // Save information about the correct answer
                question.question_radioButtons.correctAnswer = correctAnswer;
                question.question_radioButtons.correctAnswerPosition = correctAnswerPosition_relativeToChoices;
                question.question_radioButtons.correctAnswerCountryCode = quiz_countryCodes[positions.get(correctAnswerPosition_relativeToChoices)];
                break;
            case 1:
                // Initialize choices array
                question.question_checkBoxes.choicesText = new String[NUMBER_OF_CHOICES];

                // Create the CheckBoxes texts for choices
                for (int i = 0; i < NUMBER_OF_CHOICES; i++) {
                    question.question_checkBoxes.choicesText[i] = quiz_countryNames[positions.get(i)];
                }

                // Save information about the first correct answer
                question.question_checkBoxes.correctAnswer1 = correctAnswer;
                question.question_checkBoxes.correctAnswer1Position = correctAnswerPosition_relativeToChoices;
                question.question_checkBoxes.correctAnswer1CountryCode = quiz_countryCodes[positions.get(correctAnswerPosition_relativeToChoices)];

                // Randomly choose a second correct answer
                int correctAnswerPosition2_relativeToChoices = random.nextInt(NUMBER_OF_CHOICES);

                // Do not choose the second correct answer twice
                while (correctAnswerPosition_relativeToChoices == correctAnswerPosition2_relativeToChoices) {
                    correctAnswerPosition2_relativeToChoices = random.nextInt(NUMBER_OF_CHOICES);
                }

                String correctAnswer2 = quiz_countryNames[positions.get(correctAnswerPosition2_relativeToChoices)];

                // Save information about the second correct answer
                question.question_checkBoxes.correctAnswer2 = correctAnswer2;
                question.question_checkBoxes.correctAnswer2Position = correctAnswerPosition2_relativeToChoices;
                question.question_checkBoxes.correctAnswer2CountryCode = quiz_countryCodes[positions.get(correctAnswerPosition2_relativeToChoices)];

                break;
            case 2:
                // Save information about the correct answer
                question.question_fillInTheBlank.correctAnswer = correctAnswer;
                question.question_fillInTheBlank.correctAnswerPosition = correctAnswerPosition_relativeToChoices;
                question.question_fillInTheBlank.correctAnswerCountryCode = quiz_countryCodes[positions.get(correctAnswerPosition_relativeToChoices)];

                break;
        }
    }

    public void submitQuestionQuizAnswers(View v) {
        // See if all questions have been answered
        if (!haveAllQuestionsBeenAnswered()) {
            displayToast("Not all questions have been answered");
            return;
        }

        // All questions have been answered

        int counter = 0;
        String summary = "";
        for (Question q : questions) {
            summary += "Question " + String.valueOf(counter) + " is " + (q.isCorrect() ? "correct" : "incorrect") + "\n";
            numberCorrect += (q.isCorrect() ? 1 : 0);
            score += q.getScore() * (quizDifficulty + 1);
            counter++;
        }

        // Temporarily, Toast a summary of correct answers
//        displayToast(summary);

        goToQuizResultsActivity();
    }

    private boolean haveAllQuestionsBeenAnswered() {
        boolean result = true;

        for (Question q : questions) {
            if (!q.hasBeenAnswered()) {
                result = false;
            }
        }

        return result;
    }

    // Display Toast notification
    public void displayToast(String textToShow) {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, textToShow, duration);
        toast.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //  this crashes the app due to onSaveInstanceState's parcelable array saving of questions[]
                //  but the back button does not suffer from this.
                //  How to give this button the same functionality as Back?
                //  Or, how to skip the savedInstanceState method if we're leaving the Activity instead of rotating the screen?

                // Actually the Question.writeToParcel method is never called _except_ when goToMainActivity is called.
                // So the parcelable quality of Question is not contributing to the Fragment persistence
                // and the out.writeValue() calls never work since the Fragments cannot be parcelable themselves

                // TODO: destroy the fragment objects somehow (I think they're being retained due to setRetainInstance(true) in their onCreateView()s
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

    private void goToQuizResultsActivity() {
        // Pass data about the quiz via Intent
        Intent intentQuizResultsActivity = new Intent(getApplicationContext(), ResultsActivity.class);
        intentQuizResultsActivity.putExtra("numberCorrect", numberCorrect);
        intentQuizResultsActivity.putExtra("quiz_number_of_questions", quiz_number_of_questions);
        intentQuizResultsActivity.putExtra("score", score);
        intentQuizResultsActivity.putExtra("difficulty", quizDifficulty);
        intentQuizResultsActivity.putExtra("streak_longest", -1);
        startActivity(intentQuizResultsActivity);
    }
}