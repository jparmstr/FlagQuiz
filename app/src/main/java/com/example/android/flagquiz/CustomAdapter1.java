package com.example.android.flagquiz;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pete on 2/8/2018.
 */

/*
* This serves the same purpose as CustomAdapter2
* This overrides ArrayAdapter while CustomAdapter2 overrides BaseAdapter
* I found this version on Stack Overflow but it wasn't working at first due to an unrelated error
* The 2nd version is based on earlier code that I used in mynotesapp
* I like the 2nd version better because it supports custom sorting and the getView method is simpler*/

public class CustomAdapter1 extends ArrayAdapter<HighScoreItem> {
    Context context;
    int layoutResourceId;
    ArrayList<HighScoreItem> data = null;

    public CustomAdapter1(Context context, int resource, List<HighScoreItem> objects) {
        super(context, resource, objects);
        this.layoutResourceId = resource;
        this.context = context;
        this.data = (ArrayList) objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        infoHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new infoHolder();
            holder.name = row.findViewById(R.id.nameTextView);
            holder.score = row.findViewById(R.id.scoreTextView);
            holder.difficulty = row.findViewById(R.id.difficultyTextView);

            row.setTag(holder);
        }
        else
        {
            holder = (infoHolder) row.getTag();
        }

        HighScoreItem item = data.get(position);
        holder.name.setText(item.getName());
        holder.score.setText(String.valueOf(item.getScore()));
        holder.difficulty.setText(item.getQuizDifficulty());

        return row;
    }

    private class infoHolder {
        public TextView name;
        public TextView score;
        public TextView difficulty;
    }
}
