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
import com.example.rentme.model.Product;

import java.util.ArrayList;


public class SearchReasultFragment extends Fragment {
    SearchFragment searchFragment;

    Button backBtn;
    ListView filterProductListView;

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

        ArrayList<Product> filterProducts = (ArrayList<Product>) getArguments().getSerializable("filter products");
        ProductListAdapter adapter;
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
}
