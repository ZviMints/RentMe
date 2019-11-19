package com.example.rentme;

import android.content.Context;
import android.os.Bundle;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.Toast;



public class CategoriesFragment extends Fragment {

    private GridView gridView;
    ProductsListFragment productsListFragment;


    String[] titles = {
            "קטגוריה 1",
            "קטגוריה 2",
            "קטגוריה 3",
            "קטגוריה 4",
            "קטגוריה 5",
            "קטגוריה 6",
            "קטגוריה 7",
            "Checking Exception"};


    int[] numberImages = {
            R.drawable.vacation,
            R.drawable.sport,
            R.drawable.party,
            R.drawable.chairs,
            R.drawable.electricity,
            R.drawable.worktools,
            R.drawable.chairs};


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_categories, container, false);

        gridView = view.findViewById(R.id.grid_view);

        MainAdapter adapter = new MainAdapter(getContext(),titles,numberImages);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getContext(),"You Clicked" + titles[+position], Toast.LENGTH_LONG).show();

                if (productsListFragment == null)
                    productsListFragment  = new ProductsListFragment();

                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragmentContainer, productsListFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

}
