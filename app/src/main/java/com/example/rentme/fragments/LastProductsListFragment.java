package com.example.rentme.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.rentme.model.Favorites;
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
    ProductListAdapter adapter;
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

        //start the adapter of the listView
        adapter = new ProductListAdapter(new ArrayList<Product>(),getActivity());//suppose to get from the data base
        listView.setAdapter(adapter);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Last Products");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    ArrayList<Favorites> lastProductsId = new ArrayList<>();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Favorites productId = ds.getValue(Favorites.class);
                        lastProductsId.add(productId);
                    }
                    for (int i=lastProductsId.size()-1; i>=0; i--) {
                        addProductByFavorites(lastProductsId.get(i));
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        return view;
    }

    //add product to lastProducts from a given Favorites
    private void addProductByFavorites(Favorites productId) {
        Product product;
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Categories").child(productId.getCategory())
                .child(productId.getFather());
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               if (dataSnapshot.exists()) {
                   Product product = dataSnapshot.getValue(Product.class);
                  // lastProducts.add(product);
                   adapter.addProduct(product);
                   adapter.notifyDataSetChanged();
               }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

}
