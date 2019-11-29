package com.example.rentme.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.rentme.R;
import com.example.rentme.adapters.MainAdapter;


public class CategoriesFragment extends Fragment {

    private GridView gridView;
    LastProductsListFragment productsListFragment;
    InsideCategoryFragment insideCategoryFragment;
    Button publishBtn;
    PublishFragment publishFragment;


    String[] titles = {
            "אביזרים",
            "מוצרי חשמל",
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
                if (insideCategoryFragment == null)
                    insideCategoryFragment = new InsideCategoryFragment();
                Bundle bundle = new Bundle();
                bundle.putString("category",titles[+position]);
                insideCategoryFragment.setArguments(bundle);
                outerTransaction(insideCategoryFragment);


            }
        });
        publishBtn = view.findViewById(R.id.publish);
        publishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (publishFragment ==null)
                    publishFragment = new PublishFragment();
                outerTransaction(publishFragment);
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
