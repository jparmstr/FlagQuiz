package com.example.android.flagquiz;

import java.io.Serializable;

/**
 * Created by Pete on 2/8/2018.
 */

public class HighScoreItem implements Serializable {
    private int quizDifficulty = -1;
    private int score = 0;
    private String name;
    // Enforce 3-character limit on name?
    // Add date? To sort earlier tied scores over more recent ones

    public String getQuizDifficulty() {
        switch (quizDifficulty) {
            case 0:
                return "Easy";
            case 1:
                return "Normal";
            case 2:
                return "Hard";
            case 3:
                return "Expert";
            default:
                return "Unknown";
        }
    }

    public void setQuizDifficulty(int quizDifficulty) {
        this.quizDifficulty = quizDifficulty;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
