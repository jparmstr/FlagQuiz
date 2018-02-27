package com.example.android.flagquiz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Arrays;

import static com.example.android.flagquiz.MainActivity.HIGHSCORES_FILENAME;

public class HighScoresActivity extends AppCompatActivity {

    public HighScoreItem[] highScores = new HighScoreItem[10];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_scores);

        // Show back button on action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        loadHighScores();

        // Populate ListView with High Scores
        final ListView highScoresListView = findViewById(R.id.highScoresListView);
        // Prepare data for CustomAdapter2 (convert from Array to ArrayList and remove empty entries)
        ArrayList<HighScoreItem> data = new ArrayList<HighScoreItem>(Arrays.asList(highScores));
        while (data.remove(null)) ;
        // Specify sort option based on enum in CustomAdapter2
        CustomAdapter2.sortOptionNames sort = CustomAdapter2.sortOptionNames.sortByScore;
        CustomAdapter2 adapter = new CustomAdapter2(this, data, sort);
        highScoresListView.setAdapter(adapter);
    }

    private void loadHighScores() {
        try {
            FileInputStream fis = getApplicationContext().openFileInput(HIGHSCORES_FILENAME);
            ObjectInputStream is = new ObjectInputStream(fis);
            highScores = (HighScoreItem[]) is.readObject();
            is.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
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
        Intent intentMainActivity = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intentMainActivity);
    }
}
