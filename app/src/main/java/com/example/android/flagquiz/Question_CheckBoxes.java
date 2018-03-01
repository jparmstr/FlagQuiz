package com.example.android.flagquiz;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import static com.example.android.flagquiz.QuestionQuizActivity.NUMBER_OF_CHOICES;

/**
 * Created by Pete on 3/1/2018.
 */

public class Question_CheckBoxes extends android.support.v4.app.Fragment {

    // Question variables (common to all questions)
    public String correctAnswer1;
    public String correctAnswer2;
    public int correctAnswer1Position;
    public int correctAnswer2Position;
    public String correctAnswer1CountryCode;
    public String correctAnswer2CountryCode;
    public String[] choicesText;

    public int thisQuestionType;

    // View references
    ImageView checkboxQuestion_image1;
    ImageView checkboxQuestion_image2;
    CheckBox[] checkBoxes;

    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.question_checkboxes, parent, false);
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Get View references
        checkboxQuestion_image1 = view.findViewById(R.id.checkboxQuestion_image1);
        checkboxQuestion_image2 = view.findViewById(R.id.checkboxQuestion_image2);
        checkBoxes = new CheckBox[4];
        checkBoxes[0] = view.findViewById(R.id.checkbox1);
        checkBoxes[1] = view.findViewById(R.id.checkbox2);
        checkBoxes[2] = view.findViewById(R.id.checkbox3);
        checkBoxes[3] = view.findViewById(R.id.checkbox4);

        // Add click handlers (display Toast message if more than 2 answers are selected)
        for (CheckBox c : checkBoxes) {
            c.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int checkedCount = numberChecked();
                    if (checkedCount > 2) {
                        // Not sure how to display a Toast from a fragment...
                    }
                }
            });
        }

        // Correct answers are generated in QuestionQuizActivity before this is triggered

        // Set the first flag ImageView source (correct answer 1)
        checkboxQuestion_image1.setImageResource(getResources().getIdentifier(correctAnswer1CountryCode, "drawable", "com.example.android.flagquiz"));
        checkboxQuestion_image1.setTag(correctAnswer1);
//        checkboxQuestion_image1.setBackgroundColor(ContextCompat.getColor(this, R.color.backgroundColor));

        /// Set the second flag ImageView source (correct answer 2)
        checkboxQuestion_image2.setImageResource(getResources().getIdentifier(correctAnswer2CountryCode, "drawable", "com.example.android.flagquiz"));
        checkboxQuestion_image2.setTag(correctAnswer2);
//        checkboxQuestion_image2.setBackgroundColor(ContextCompat.getColor(this, R.color.backgroundColor));

        // Set CheckBox choice texts
        for (int i = 0; i < NUMBER_OF_CHOICES; i++) {
            checkBoxes[i].setText(choicesText[i]);
        }
    }

    // The question is answered when 2 and only 2 CheckBoxes are checked
    public boolean hasBeenAnswered() {
        boolean result = false;
        int checkedCount = numberChecked();

        if (checkedCount == 2) {
            result = true;
        }

        return result;
    }

    public boolean isCorrect() {
        boolean result = true;

        String thisCorrectAnswer1 = correctAnswer1.toLowerCase();
        String thisCorrectAnswer2 = correctAnswer2.toLowerCase();

        for (CheckBox c : checkBoxes) {
            if (c.isChecked()) {
                String thisGivenAnswer = c.getText().toString().toLowerCase();
                if (!thisGivenAnswer.equals(thisCorrectAnswer1) && !thisGivenAnswer.equals(thisCorrectAnswer2)) {
                    result = false;
                }
            }
        }

        return result;
    }

    public int numberChecked() {
        int checkedCount = 0;

        for (CheckBox c : checkBoxes) {
            if (c.isChecked()) {
                checkedCount++;
            }
        }

        return checkedCount;
    }
}
