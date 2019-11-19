package com.example.rentme;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;



public class ProductsListFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        String category = "none";
        ProductListAdapter adapter;
        View view = inflater.inflate(R.layout.fragment_products_list, container, false);
        ListView listView = view.findViewById(R.id.products_list);
        //need to delete and chage for data base/////////////////////////////////////////////////
        ArrayList<Product> items = new ArrayList<Product>();
        Product p1 = new Product("מברגה חשמלית", "אביזרים", "מוצר מעולה עובד טוב","chairs");
        Product p2 = new Product("לפטופ", "אביזרים", "השכרה גם לשבוע","chairs");
        Product p3 = new Product("כיסאות", "אביזרים", "מאזור אשדוד","chairs");
        Product p4 = new Product("אחר", "אביזרים", "אחר אחר","chairs");
        Product p5 = new Product("אחר 2", "אביזרים", "אחר אחר","chairs");
        Product p6 = new Product("אחר", "אביזרים", "אחר אחר","chairs");
        Product p7 = new Product("אחר 2", "אביזרים", "אחר אחר","chairs");
        items.add(p1);
        items.add(p2);
        items.add(p3);
        items.add(p4);
        items.add(p5);
        items.add(p6);
        items.add(p7);

        ArrayList<Product> items2 = new ArrayList<Product>();
        Product q1 = new Product("מברגה חשמלית", "מוצרי חשמל", "מוצר מעולה עובד טוב","chairs");
        Product q2 = new Product("לפטופ", "מוצרי חשמל", "השכרה גם לשבוע","chairs");
        Product q3 = new Product("כיסאות", "מוצרי חשמל", "מאזור אשדוד","chairs");
        Product q4 = new Product("אחר", "מוצרי חשמל", "אחר אחר","chairs");
        Product q5 = new Product("אחר 2", "מוצרי חשמל", "אחר אחר","chairs");
        Product q6 = new Product("אחר", "מוצרי חשמל", "אחר אחר","chairs");
        Product q7 = new Product("אחר 2", "מוצרי חשמל", "אחר אחר","chairs");
        items2.add(q1);
        items2.add(q2);
        items2.add(q3);
        items2.add(q4);
        items2.add(q5);
        items2.add(q6);
        items2.add(q7);

        ArrayList<Product> items3 = new ArrayList<Product>();
        Product r1 = new Product("מברגה חשמלית", "מוצרי חשמל", "מוצר מעולה עובד טוב","chairs");
        Product r2 = new Product("לפטופ", "גינה", "השכרה גם לשבוע","chairs");
        Product r3 = new Product("כיסאות", "למטבח", "מאזור אשדוד","chairs");
        Product r4 = new Product("אחר", "סקי", "אחר אחר","chairs");
        Product r5 = new Product("אחר 2", "ספורט", "אחר אחר","chairs");
        Product r6 = new Product("אחר", "מחנאות", "אחר אחר","chairs");
        Product r7 = new Product("אחר 2", "פנאי", "אחר אחר","chairs");
        items3.add(r1);
        items3.add(r2);
        items3.add(r3);
        items3.add(r4);
        items3.add(r5);
        items3.add(r6);
        items3.add(r7);
        //need to delete and chage for data base/////////////////////////////////////////////////

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            category = bundle.getString("category");
        }

        if (category=="אביזרים")
            adapter = new ProductListAdapter(items,getContext());//suppose to get from the data base
        else if (category == "מעורב")
            adapter = new ProductListAdapter(items3,getContext());//suppose to get from the data base
        else adapter = new ProductListAdapter(items2,getContext());//suppose to get from the data base

        listView.setAdapter(adapter);


        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }


}
