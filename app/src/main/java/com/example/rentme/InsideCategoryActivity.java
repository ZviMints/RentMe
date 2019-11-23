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

public class InsideCategoryActivity extends AppCompatActivity implements ProductListAdapter.MoreDetailsButtonListener{

    Button homePageBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inside_category);

        ListView listView = findViewById(R.id.products_list);

        //need to delete and change for data base/////////////////////////////////////////////////
        ArrayList<Product> items = new ArrayList<Product>();
        Product p1 = new Product("מברגה חשמלית", "אביזרים", "מוצר מעולה עובד טוב", "chairs");
        Product p2 = new Product("לפטופ", "אביזרים", "השכרה גם לשבוע", "chairs");
        Product p3 = new Product("כיסאות", "אביזרים", "מאזור אשדוד", "chairs");
        Product p4 = new Product("אחר", "אביזרים", "אחר אחר", "chairs");
        Product p5 = new Product("אחר 2", "אביזרים", "אחר אחר", "chairs");
        Product p6 = new Product("אחר", "אביזרים", "אחר אחר", "chairs");
        Product p7 = new Product("אחר 2", "אביזרים", "אחר אחר", "chairs");
        items.add(p1);
        items.add(p2);
        items.add(p3);
        items.add(p4);
        items.add(p5);
        items.add(p6);
        items.add(p7);

        ArrayList<Product> items2 = new ArrayList<Product>();
        Product q1 = new Product("מברגה חשמלית", "מוצרי חשמל", "מוצר מעולה עובד טוב", "chairs");
        Product q2 = new Product("לפטופ", "מוצרי חשמל", "השכרה גם לשבוע", "chairs");
        Product q3 = new Product("כיסאות", "מוצרי חשמל", "מאזור אשדוד", "chairs");
        Product q4 = new Product("אחר", "מוצרי חשמל", "אחר אחר", "chairs");
        Product q5 = new Product("אחר 2", "מוצרי חשמל", "אחר אחר", "chairs");
        Product q6 = new Product("אחר", "מוצרי חשמל", "אחר אחר", "chairs");
        Product q7 = new Product("אחר 2", "מוצרי חשמל", "אחר אחר", "chairs");
        items2.add(q1);
        items2.add(q2);
        items2.add(q3);
        items2.add(q4);
        items2.add(q5);
        items2.add(q6);
        items2.add(q7);

        //need to delete and change for data base/////////////////////////////////////////////////
        String category = "none";
        Intent intent = getIntent();
        ProductListAdapter adapter;

        //get the category name that was clicked
        if (null != intent) //Null Checking
            category = intent.getStringExtra("category");

        if (category.equals("אביזרים"))
            adapter = new ProductListAdapter(items, InsideCategoryActivity.this);//suppose to get from the data base
        else
            adapter = new ProductListAdapter(items2, InsideCategoryActivity.this);//suppose to get from the data base

        listView.setAdapter(adapter);

        //set the category title
        TextView CategoryTitle = findViewById(R.id.category_title);
        CategoryTitle.setText(category);

    }

    @Override
    public void showMoreDetails() {
        Intent intent = new Intent(getApplicationContext(), InItemActivity.class);
        startActivity(intent);

    }
}


