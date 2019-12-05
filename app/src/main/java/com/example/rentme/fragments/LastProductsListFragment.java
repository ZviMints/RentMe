package com.example.rentme.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.rentme.model.Product;
import com.example.rentme.adapters.ProductListAdapter;
import com.example.rentme.R;
import com.example.rentme.model.sortByLastUploaded;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;


public class LastProductsListFragment extends Fragment {


    ListView listView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_last_products_list, container, false);
         listView = view.findViewById(R.id.products_list);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Last Products");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Product> lastProducts = new ArrayList<>();
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    Product product = ds.getValue(Product.class);
                    lastProducts.add(product);
                    Collections.sort(lastProducts,new sortByLastUploaded());
                }
                    //start the adapter of the listView
                    ProductListAdapter adapter = new ProductListAdapter(lastProducts,getActivity());//suppose to get from the data base
                    listView.setAdapter(adapter);

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

}
