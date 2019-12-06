package com.example.rentme.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.rentme.model.RelationCategoryProduct;
import com.example.rentme.model.Product;
import com.example.rentme.adapters.ProductListAdapter;
import com.example.rentme.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;


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
        adapter = new ProductListAdapter(new ArrayList<Product>(), getActivity());
        listView.setAdapter(adapter);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Last Products");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<RelationCategoryProduct> relations = new ArrayList<>();
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    RelationCategoryProduct relation = ds.getValue(RelationCategoryProduct.class);
                    relations.add(relation);
                }
                // WHY ITS NULL?!?!?!?
                if(true) throw new NoSuchElementException(relations.toString());
                GotAllRelations(relations);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        return view;
    }

    private void GotAllRelations(List<RelationCategoryProduct> relations) {
        for (int i = relations.size() - 1; i >= 0; i--) {
            InitializeRelation(relations.get(i));
        }
    }

    private void InitializeRelation(RelationCategoryProduct relation) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference ref = firebaseDatabase.getReference("Categories")
                .child(relation.getCategoryName())
                .child(relation.getProductUID());
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Product product = dataSnapshot.getValue(Product.class);
                    adapter.addProduct(product);
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

}
