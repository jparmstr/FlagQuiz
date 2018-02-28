package com.example.android.flagquiz;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Pete on 2/9/2018.
 */

public class HighScoreItemAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private String[] quizDifficulties;
    private int[] scores;
    private String[] names;

    public HighScoreItemAdapter(Context c, ArrayList<HighScoreItem> entries, sortOptionNames sort) {
        // Sort entries
        switch (sort) {
            case sortByScore:
                Collections.sort(entries, new sortByScore());
                break;
        }

        // Initialize the local arrays
        quizDifficulties = new String[entries.size()];
        names = new String[entries.size()];
        scores = new int[entries.size()];

        // Retrieve the data from 'entries' (already have data, now extract individual fields)
        int counter = 0;
        for (HighScoreItem t : entries) {
            quizDifficulties[counter] = t.getQuizDifficulty();
            scores[counter] = t.getScore();
            names[counter] = t.getName();
//            Date date = new Date(t.getCreationDate());
//            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormatString);
//            creationDates[counter] = simpleDateFormat.format(date);
            counter++;
        }

        inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return names.length;
    }

    @Override
    public Object getItem(int position) {
        return names[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO: figure out what this warning is saying about a View Holder pattern
        View v = inflater.inflate(R.layout.high_score_item, null);
        TextView difficultyTextView = v.findViewById(R.id.difficultyTextView);
        TextView scoreTextView = v.findViewById(R.id.scoreTextView);
        TextView nameTextView = v.findViewById(R.id.nameTextView);

        difficultyTextView.setText(quizDifficulties[position]);
        scoreTextView.setText(String.valueOf(scores[position]));
        nameTextView.setText(names[position]);

        return v;
    }

    private class sortByScore implements Comparator<HighScoreItem> {
        @Override
        public int compare(HighScoreItem obj2, HighScoreItem obj1) {
            // If scores are exactly the same, sort by name instead
            if (obj1.getScore() == obj2.getScore()) {
                // Change this to sort by date if we add Date attribute to HighScoreItem
                return -1 * (obj1.getName().compareToIgnoreCase(obj2.getName()));
            } else {
                // I created my own compare methods because Android Studio was complaining
                // about API Level for Integer.compare and API 15
                return compareInts(obj1.getScore(), obj2.getScore());
            }
        }
    }

    private int compareInts(int i1, int i2) {
        return i1 - i2;
    }

    public enum sortOptionNames {
        sortByScore("Sort By Score");

        private String name;

        sortOptionNames(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return name;
        }

        public static sortOptionNames[] array() {
            return sortOptionNames.values();
        }

        public static String[] stringArray() {
            String[] result = new String[sortOptionNames.values().length];

            int counter = 0;
            for (sortOptionNames s : sortOptionNames.values()) {
                result[counter] = s.name;
                counter++;
            }

            return result;
        }

        public static sortOptionNames fromString(String name) {
            for (sortOptionNames s : sortOptionNames.values()) {
                if (s.name.equalsIgnoreCase(name)) {
                    return s;
                }
            }
            throw new IllegalArgumentException("No enum found with the name: " + name);
        }
    }
}