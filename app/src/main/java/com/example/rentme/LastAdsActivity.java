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
        Product p1 = new Product("מברגה חשמלית", "כלי עבודה", "מוצר מעולה עובד טוב","chairs");
        Product p2 = new Product("לפטופ", "מוצרי חשמל", "השכרה גם לשבוע","chairs");
        Product p3 = new Product("כיסאות", "אביזרים", "מאזור אשדוד","chairs");
        Product p4 = new Product("אחר", "אביזרים", "אחר אחר","chairs");
        Product p5 = new Product("אחר 2", "אביזרים", "אחר אחר","chairs");

        items.add(p1);
        items.add(p2);
        items.add(p3);
        items.add(p4);
        items.add(p5);

        ProductListAdapter adapter = new ProductListAdapter(items,LastAdsActivity.this);
        listView.setAdapter(adapter);
    }
}

