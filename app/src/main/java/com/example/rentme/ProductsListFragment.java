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

        //delete after database ready/////////////////////////////////////////////////////////////
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
        //delete after database ready/////////////////////////////////////////////////////////////

        ProductListAdapter adapter = new ProductListAdapter(items3,getContext());//suppose to get from the data base
        listView.setAdapter(adapter);


        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }


}
