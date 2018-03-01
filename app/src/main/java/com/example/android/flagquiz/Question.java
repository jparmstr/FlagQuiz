package com.example.android.flagquiz;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import java.util.Random;

/**
 * Created by Pete on 3/1/2018.
 */

public class Question {

    public int thisQuestionType;

    // Objects representing different types of questions
    Question_RadioButtons question_radioButtons;
    Question_CheckBoxes question_checkBoxes;
    Question_FillInTheBlank question_fillInTheBlank;

//    LayoutInflater inflater;

    public Random random = new Random();

    public void create() {
        // Random question type
        thisQuestionType = random.nextInt(3);

        // The fragment instantiation is handled in QuestionQuizActivity.generateQuestions()

        // The fragment.onViewCreated() methods handle the rest of the fragment setup
    }

    public boolean hasBeenAnswered() {
        boolean result = false;

        switch (thisQuestionType) {
            case 0:
                result = question_radioButtons.hasBeenAnswered();
                break;
            case 1:
                result = question_checkBoxes.hasBeenAnswered();
                break;
            case 2:
                result = question_fillInTheBlank.hasBeenAnswered();
                break;
        }

        return result;
    }

    public boolean isCorrect() {
        boolean result = false;

        switch (thisQuestionType) {
            case 0:
                result = question_radioButtons.isCorrect();
                break;
            case 1:
                result = question_checkBoxes.isCorrect();
                break;
            case 2:
                result = question_fillInTheBlank.isCorrect();
                break;
        }

        return result;
    }
}