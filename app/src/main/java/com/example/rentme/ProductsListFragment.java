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
        View view = inflater.inflate(R.layout.fragment_products_list, container, false);
        ListView listView = view.findViewById(R.id.products_list);

        ArrayList<Product> items = new ArrayList<Product>();
        Product p1 = new Product("מברגה חשמלית", "כלי עבודה", "מוצר מעולה עובד טוב","chairs");
        Product p2 = new Product("לפטופ", "מוצרי חשמל", "השכרה גם לשבוע","chairs");
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

        ProductListAdapter adapter = new ProductListAdapter(items,getContext());
        listView.setAdapter(adapter);


        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }


}
