package com.example.rentme.fragments;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.rentme.R;
import com.example.rentme.adapters.ProductListAdapter;
import com.example.rentme.model.Author;
import com.example.rentme.model.Comment;
import com.example.rentme.model.Product;
import com.example.rentme.model.ProductDetails;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class SearchReasultFragment extends Fragment {
    SearchFragment searchFragment;

    Button backBtn;
    ListView filterProductListView;
    ProductListAdapter adapter;
    ArrayList<Product> filterProducts;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_result, container, false);
        backBtn = view.findViewById(R.id.back_to_search_page);
        filterProductListView = view.findViewById(R.id.filter_products);
        filterProducts = new ArrayList<Product>();

        String highPrice = (String) getArguments().getSerializable("higher price");
        String lowPrice = (String) getArguments().getSerializable("lower price");
        String condition = (String) getArguments().getSerializable("higher price");
        String area = (String) getArguments().getSerializable("area");
        String category = (String) getArguments().getSerializable("category");

        double lowerPriceValue = (lowPrice.length()>0)? Double.parseDouble(lowPrice) : Double.MIN_VALUE;
        double higherPriceValue = (highPrice.length()>0)? Double.parseDouble(highPrice) : Double.MAX_VALUE;

        findFilterProducts(lowerPriceValue,higherPriceValue,category,area,condition);


        adapter = new ProductListAdapter(filterProducts, getActivity());
        filterProductListView.setAdapter(adapter);


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (searchFragment == null)
                    searchFragment = new SearchFragment();
                outerTransaction(searchFragment);
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    private void outerTransaction(Fragment fragment){
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.OuterFragmentContainer, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void findFilterProducts(final double lowerPriceValue, final double higherPriceValue,final String selectedCategory,
                                    final String selectedArea, final String selectedState) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Categories").child(selectedCategory).getRef();
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot productId : dataSnapshot.getChildren()) {
                        Author author = productId.child("author").getValue(Author.class);
                        ProductDetails productDetails = productId.child("productDetails").getValue(ProductDetails.class);
                        double productPrice = Double.parseDouble(productDetails.getPrice());
                        if ((author.getArea() == selectedArea) &&(productDetails.getCondition()==selectedState)
                                &&(productPrice >= lowerPriceValue) && (productPrice <= higherPriceValue)){
                            List<Comment> comments = new ArrayList<>();
                            for(DataSnapshot dsComments: productId.child("comments_list").getChildren()){
                                String msg =dsComments.child("msg").getValue().toString();
                                Author commentAuthor = dsComments.child("author").getValue(Author.class);
                                comments.add(new Comment(commentAuthor,msg));
                            }
                            Product product = new Product(productDetails,author,comments);
                            filterProducts.add(product);
                        }

                    }
                    adapter.notifyDataSetChanged();

                }


            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

    }
}
