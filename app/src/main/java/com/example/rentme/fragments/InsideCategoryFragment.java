package com.example.rentme.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.rentme.R;
import com.example.rentme.adapters.ProductListAdapter;
import com.example.rentme.model.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.NoSuchElementException;

public class InsideCategoryFragment extends Fragment{

    Button backBtn;
    MainFragment mainFragment;
    FirebaseDatabase firebaseDatabase;
    ListView listView;
    TextView CategoryTitle;

    LinearLayout InSideCategoryLinearLayout;
    ProgressBar progressBar;

    String CategoryTitleString;
    ArrayList<Product> products = new ArrayList<Product>();
    private void gotProductsFromFireBase(ArrayList<Product> products) {
        this.products = products;
        InitializeProductListAdapter();

    }

    private void InitializeProductListAdapter() {

        InSideCategoryLinearLayout.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        ProductListAdapter adapter;
        adapter = new ProductListAdapter(products, getActivity());
        listView.setAdapter(adapter);

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {  super.onCreate(savedInstanceState);  }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_inside_category, container, false);
      
        backBtn = view.findViewById(R.id.back_to_last_page);
        listView = view.findViewById(R.id.products_list);
        CategoryTitle = view.findViewById(R.id.category_title);
        InSideCategoryLinearLayout = view.findViewById(R.id.InSideCategoryLinearLayout);
        progressBar = view.findViewById(R.id.progressbar);
        CategoryTitle = view.findViewById(R.id.category_title);

        firebaseDatabase = FirebaseDatabase.getInstance();

        Bundle bundle = this.getArguments();
        CategoryTitleString = bundle.getString("Categories");

        //set the category title
        CategoryTitle.setText(CategoryTitleString);

        // Initialize Categories
        if(this.products.isEmpty()) {
            DatabaseReference ref = firebaseDatabase.getReference("Categories").child(CategoryTitleString);
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    ArrayList<Product> products = new ArrayList<>();
                    for(DataSnapshot ds : dataSnapshot.getChildren()) {
                        Product product = ds.getValue(Product.class);
                        products.add(product);
                    }
                    gotProductsFromFireBase(products);
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {}
            });
        } else InitializeProductListAdapter();


        // Back To Main Activity
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mainFragment == null)
                    mainFragment = new MainFragment();
                outerTransaction(mainFragment);
            }
        });
        return view;
    }

    private void outerTransaction(Fragment fragment){
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.OuterFragmentContainer, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}


