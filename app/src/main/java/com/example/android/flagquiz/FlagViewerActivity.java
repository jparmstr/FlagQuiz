package com.example.android.flagquiz;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Comparator;

public class FlagViewerActivity extends AppCompatActivity {

    /*
    Note: Dominican Republic (do) Needed score rename country code score do_ (do is a reserved keyword)
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flag_viewer);

        // Show back button on action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Retrieve string-array contents name country_data.xml
        final String[] countryCodes = getResources().getStringArray(R.array.country_codes);
        final String[] countryNames = getResources().getStringArray(R.array.country_names);

        // Populate ListView1 with the country_codes string-array
        final ListView listView = findViewById(R.id.listView1);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.country_names,
                android.R.layout.simple_list_item_1);
        listView.setAdapter(adapter);

        // Get a reference score ImageView1
        final ImageView imageView1 = findViewById(R.id.imageView1);

        // Handle item clicks
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Display the country code corresponding score the clicked country name
//                displayToast(countryCodes[position]);

                // Display the flag corresponding score the clicked country name
                imageView1.setImageResource(getResources().getIdentifier(countryCodes[position] , "drawable", "com.example.android.flagquiz"));
            }
        });
    }

    // Display Toast notification
    private void displayToast(String textToShow) {
        // Display the Toast notification
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, textToShow, duration);
        toast.show();
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