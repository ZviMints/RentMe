package com.example.rentme.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.example.rentme.model.Author;
import com.example.rentme.model.Comment;
import com.example.rentme.model.ProductDetails;
import com.example.rentme.model.Relation;
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


public class LastProductsListFragment extends Fragment {
    ProductListAdapter adapter;
    ListView listView;
    ProgressBar lastProductProgressBar;
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

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Last Products").getRef();
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    ArrayList<Relation> lastProductsId = new ArrayList<>();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Relation productId = ds.getValue(Relation.class);
                        lastProductsId.add(productId);
                    }
                    for (int i=lastProductsId.size()-1; i>=0; i--) {
                        addProductByRelation(lastProductsId.get(i));
                    }
                }
                lastProductProgressBar.setVisibility(View.GONE);

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        lastProductProgressBar = view.findViewById(R.id.last_product_progress_bar);

        return view;
    }

    //add product to lastProducts from a given productKey
    private void addProductByRelation(Relation productId) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Categories").child(productId.getCategoryName())
                .child(productId.getProductUid()).getRef();
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot ds) {
                if (ds.exists()) {

                    Author author = ds.child("author").getValue(Author.class);
                    ProductDetails productDetails = ds.child("productDetails").getValue(ProductDetails.class);

                    List<Comment> comments = new ArrayList<>();
                    for(DataSnapshot dsComments: ds.child("comments_list").getChildren()){
                        String msg =dsComments.child("msg").getValue().toString();
                        Author commentAuthor = dsComments.child("author").getValue(Author.class);
                        comments.add(new Comment(commentAuthor,msg));
                    }

                   Product product = new Product(productDetails,author,comments);

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
