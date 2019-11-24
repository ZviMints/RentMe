package com.example.rentme;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.FragmentTransaction;

public class MainFragment extends Fragment {
    Button homePageBtn;
    Button lastAdsBtn;
    ProductsListFragment productsListFragment;
    CategoriesFragment categoriesFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        homePageBtn = view.findViewById(R.id.MainPage);
        lastAdsBtn = view.findViewById(R.id.LastAds);

        lastAdsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lastAdsBtn.setBackgroundResource(R.drawable.main_header_selector);
                homePageBtn.setBackgroundColor(getResources().getColor(R.color.lightGray));
                lastAdsBtn.setCompoundDrawablesWithIntrinsicBounds(
                        null,
                        getResources().getDrawable(R.drawable.ic_apps_black_24dp_white),
                        null,
                        null
                );
                homePageBtn.setCompoundDrawablesWithIntrinsicBounds(
                        null,
                        getResources().getDrawable(R.drawable.ic_assignment_black_24dp_black),
                        null,
                        null
                );

                if (productsListFragment == null)
                    productsListFragment  = new ProductsListFragment();


                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack so the user can navigate back
                transaction.replace(R.id.fragmentContainer, productsListFragment);
                transaction.addToBackStack(null);

                // Commit the transaction
                transaction.commit();
            }
        });

        homePageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lastAdsBtn.setBackgroundColor(getResources().getColor(R.color.lightGray));
                homePageBtn.setBackgroundResource(R.drawable.main_header_selector);
                lastAdsBtn.setCompoundDrawablesWithIntrinsicBounds(
                        null,
                        getResources().getDrawable(R.drawable.ic_apps_black_24dp_black),
                        null,
                        null
                );
                homePageBtn.setCompoundDrawablesWithIntrinsicBounds(
                        null,
                        getResources().getDrawable(R.drawable.ic_assignment_black_24dp_white),
                        null,
                        null
                );

                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack so the user can navigate back
                transaction.replace(R.id.fragmentContainer, categoriesFragment);
                transaction.addToBackStack(null);

                // Commit the transaction
                transaction.commit();
            }
        });



        categoriesFragment = new CategoriesFragment();
        getActivity().getSupportFragmentManager().beginTransaction().add(R.id.fragmentContainer, categoriesFragment).commit();
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }
}
