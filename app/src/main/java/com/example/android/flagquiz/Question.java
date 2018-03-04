package com.example.android.flagquiz;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import java.util.Random;

/**
 * Created by Pete on 3/1/2018.
 */

public class Question implements Parcelable {

    public int thisQuestionType;

    // Objects representing different types of questions
    Question_RadioButtons question_radioButtons;
    Question_CheckBoxes question_checkBoxes;
    Question_FillInTheBlank question_fillInTheBlank;

    public Random random = new Random();

    //region Parcelable methods
    public Question() {

    }

    private Question(Parcel in) {
        thisQuestionType = in.readInt();
//        question_radioButtons = (Question_RadioButtons) in.readValue(Question_RadioButtons.class.getClassLoader());
//        question_checkBoxes = (Question_CheckBoxes) in.readValue(Question_CheckBoxes.class.getClassLoader());
//        question_fillInTheBlank = (Question_FillInTheBlank) in.readValue(Question_FillInTheBlank.class.getClassLoader());
    }

    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        return String.valueOf(thisQuestionType);
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(thisQuestionType);
//        out.writeValue(question_radioButtons);
//        out.writeValue(question_checkBoxes);
//        out.writeValue(question_fillInTheBlank);
    }

    public static final Parcelable.Creator<Question> CREATOR = new Parcelable.Creator<Question>() {
        public Question createFromParcel(Parcel in) {
            return new Question(in);
        }

        public Question[] newArray(int size) {
            return new Question[size];
        }
    };

    // One very important thing to pay close attention to is the order that you write and read your values
    // to and from the Parcel. They need to match up in both cases. In my example, I write the int and then
    // the String to the Parcel. Afterwards, I read them in that same exact order. The mechanism that
    // Android uses to read the Parcel is blind and completely trusts you to get the order correct,
    // or else you will run into run-time crashes.

    //endregion Parcelable methods

    public void create() {
        // Random question type
        thisQuestionType = random.nextInt(3);

        // The fragment instantiation is handled in QuizQuestionsActivity.generateQuestions()

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

    public int getScore() {
        int score = 0;

        switch (thisQuestionType) {
            case 0:
                score = question_radioButtons.getScore();
                break;
            case 1:
                score = question_checkBoxes.getScore();
                break;
            case 2:
                score = question_fillInTheBlank.getScore();
                break;
        }

        return score;
    }
}