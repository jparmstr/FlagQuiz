package com.example.android.flagquiz;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;

import java.util.Calendar;
import java.util.Random;

import static com.example.android.flagquiz.QuizQuestionsActivity.NUMBER_OF_CHOICES;

/**
 * Created by Pete on 3/1/2018.
 */

public class Question_RadioButtons extends android.support.v4.app.Fragment {

    // See: https://github.com/codepath/android_guides/wiki/Creating-and-Using-Fragments

    // Question variables (common to all questions)
    public String correctAnswer;
    public int correctAnswerPosition;
    public String correctAnswerCountryCode;
    public String[] choicesText;

    // View references
    public ImageView radioButtonQuestion_flagImageView;
    public RadioButton[] radioButtons;

//    private Random random = new Random();

    // Unique id meant to identify this fragment
    private String mTime;

    // The onCreateView method is called when Fragment should create its View object hierarchy, either dynamically or via XML layout inflation.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        setRetainInstance(true);

        if (savedInstanceState != null) {
            // Restore last state
            mTime = savedInstanceState.getString("time_key");
        } else {
            mTime = "" + Calendar.getInstance().getTimeInMillis();
        }

        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.question_radiobuttons, parent, false);
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Get View references
        radioButtonQuestion_flagImageView = view.findViewById(R.id.radioButtonQuestion_image1);
        radioButtons = new RadioButton[4];
        radioButtons[0] = view.findViewById(R.id.radiobutton1);
        radioButtons[1] = view.findViewById(R.id.radiobutton2);
        radioButtons[2] = view.findViewById(R.id.radiobutton3);
        radioButtons[3] = view.findViewById(R.id.radiobutton4);

        // Add click listeners (only allow one Radio Button selected at a time)
        for (RadioButton r : radioButtons) {
            r.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    RadioButton thisButton = (RadioButton) view;
                    for (RadioButton r : radioButtons) {
                        if (Integer.valueOf(r.getTag().toString()) != Integer.valueOf(thisButton.getTag().toString())) {
                            r.setChecked(false);
                        }
                    }
                }
            });
        }

        // Handle answer creation from QuizQuestionsActivity since it contains the Arrays and other variables we need to refer to

        // Set the flag ImageView source (correct answer)
        radioButtonQuestion_flagImageView.setImageResource(getResources().getIdentifier(correctAnswerCountryCode, "drawable", "com.example.android.flagquiz"));
        radioButtonQuestion_flagImageView.setTag(correctAnswer);
//        radioButtonQuestion_flagImageView.setBackgroundColor(ContextCompat.getColor(this, R.color.backgroundColor));

        // Set RadioButton choice texts
        for (int i = 0; i < NUMBER_OF_CHOICES; i++) {
            radioButtons[i].setText(choicesText[i]);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("time_key", mTime);
    }

    public boolean hasBeenAnswered() {
        boolean result = false;

        for (RadioButton r : radioButtons) {
            if (r.isChecked()) {
                result = true;
            }
        }

        return result;
    }

    public boolean isCorrect() {
        boolean result = false;

        for (RadioButton r : radioButtons) {
            if (r.isChecked()) {
                String thisGivenAnswer = r.getText().toString().toLowerCase();
                String thisCorrectAnswer = correctAnswer.toLowerCase();

                if (thisGivenAnswer.equals(thisCorrectAnswer)) {
                    result = true;
                }
            }
        }

        return result;
    }

    // You get 1 point for a correct answer
    public int getScore() {
        int score = 0;

        for (RadioButton r : radioButtons) {
            if (r.isChecked()) {
                String thisGivenAnswer = r.getText().toString().toLowerCase();
                String thisCorrectAnswer = correctAnswer.toLowerCase();

                if (thisGivenAnswer.equals(thisCorrectAnswer)) {
                    score++;
                }
            }
        }

        return score;
    }
}