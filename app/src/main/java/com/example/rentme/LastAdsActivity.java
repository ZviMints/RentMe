package com.example.rentme;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class LastAdsActivity extends AppCompatActivity {
    String items[] = new String[] {"apple", "orange","potato","tomato","apple", "orange","potato","tomato","apple", "orange","potato","tomato","apple", "orange","potato","tomato","apple", "orange","potato","tomato"};
    Button homePageBtn;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.last_ads);

        ListView listView = findViewById(R.id.products_list);
        homePageBtn = findViewById((R.id.MainPage));
        homePageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,items);
        listView.setAdapter(adapter);
    }
}

