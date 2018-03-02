package com.example.android.flagquiz;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.Calendar;

/**
 * Created by Pete on 3/1/2018.
 */

public class Question_FillInTheBlank extends android.support.v4.app.Fragment {

    // Question variables (common to all questions)
    public String correctAnswer;
    public int correctAnswerPosition;
    public String correctAnswerCountryCode;
    public int thisQuestionType;

    // View references
    ImageView fillInTheBlankQuestion_image1;
    EditText fillInTheBlankQuestion_answer;

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
        return inflater.inflate(R.layout.question_fill_in_the_blank, parent, false);
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Get View references
        fillInTheBlankQuestion_image1 = view.findViewById(R.id.fillInTheBlankQuestion_image1);
        fillInTheBlankQuestion_answer = view.findViewById(R.id.fillInTheBlankQuestion_answer);

        // Correct answers are generated in QuizQuestionsActivity before this is triggered

        // Set the flag ImageView source (correct answer)
        fillInTheBlankQuestion_image1.setImageResource(getResources().getIdentifier(correctAnswerCountryCode, "drawable", "com.example.android.flagquiz"));
        fillInTheBlankQuestion_image1.setTag(correctAnswer);
//        fillInTheBlankQuestion_image1.setBackgroundColor(ContextCompat.getColor(this, R.color.backgroundColor));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("time_key", mTime);
    }

    public boolean hasBeenAnswered() {
        boolean result = false;

        if (fillInTheBlankQuestion_answer.getText() != null && !fillInTheBlankQuestion_answer.getText().toString().equals("")) {
            result = true;
        }

        return result;
    }

    public boolean isCorrect() {
        boolean result = false;

        String givenAnswer = fillInTheBlankQuestion_answer.getText().toString().toLowerCase();
        String thisCorrectAnswer = correctAnswer.toLowerCase();

        if (givenAnswer.equals(thisCorrectAnswer)) {
            result = true;
        }

        return result;
    }
}
