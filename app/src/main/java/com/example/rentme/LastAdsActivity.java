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

import java.util.ArrayList;

public class LastAdsActivity extends AppCompatActivity {
   // String items[] = new String[] {"apple", "orange","potato","tomato","apple", "orange","potato","tomato","apple", "orange","potato","tomato","apple", "orange","potato","tomato","apple", "orange","potato","tomato"};

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
        ArrayList<Product> items = new ArrayList<Product>();
        Product p1 = new Product("table", "furnitur", "big and brown");
        Product p2 = new Product("table2", "furnitur2", "big and brown2");
        Product p3 = new Product("table3", "furnitur3", "big and brown3");
        items.add(p1);
        items.add(p2);
        items.add(p3);

        ProductListAdapter adapter = new ProductListAdapter(items,LastAdsActivity.this);
        listView.setAdapter(adapter);
    }
}

