package com.example.rentme;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    /**
     * Main Categories
     **/
    GridView gridView;
    String[] titles = {
            "קטגוריה 1",
            "קטגוריה 2",
            "קטגוריה 3",
            "קטגוריה 4",
            "קטגוריה 5",
            "קטגוריה 6",
            "קטגוריה 7",
            "Checking Exception"};


    int[] numberImages = {
            R.drawable.vacation,
            R.drawable.sport,
            R.drawable.party,
            R.drawable.chairs,
            R.drawable.electricity,
            R.drawable.worktools,
            R.drawable.chairs};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        gridView = findViewById(R.id.grid_view);

        MainAdapter adapter = new MainAdapter(MainActivity.this,titles,numberImages);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(),"You Clicked" + titles[+position], Toast.LENGTH_LONG).show();
            }
        });

    }
}
