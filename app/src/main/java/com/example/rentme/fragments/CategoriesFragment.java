package com.example.rentme.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.rentme.R;
import com.example.rentme.adapters.MainAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;


public class CategoriesFragment extends Fragment {

    private GridView gridView;
    InsideCategoryFragment insideCategoryFragment;
    Button publishBtn;
    PublishFragment publishFragment;
    LoginFragment loginFragment;

    FirebaseDatabase firebaseDatabase;

    // Get From Database
    private List<String> titles = new ArrayList<>();
    private  List<Integer> titles_images = new ArrayList<>();

    ProgressBar progressBar;
    LinearLayout footerLinear;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void gotCategoriesFromFireBase(List<String> categories) {

        // Initialize titles
        this.titles = categories;

        // Initialize images
        for(int i=0; i<categories.size(); i++)
            this.titles_images.add(R.drawable.vacation);


        IntializeGridView();
    }

    private void IntializeGridView() {
        progressBar.setVisibility(View.GONE);
        footerLinear.setVisibility(View.VISIBLE);
        gridView.setVisibility(View.VISIBLE);
        MainAdapter adapter = new MainAdapter(getContext(),titles, titles_images);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (insideCategoryFragment == null)
                    insideCategoryFragment = new InsideCategoryFragment();
                Bundle bundle = new Bundle();
                bundle.putString("Categories",titles.get(+position));
                insideCategoryFragment.setArguments(bundle);
                outerTransaction(insideCategoryFragment);
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_categories, container, false);

        progressBar = view.findViewById(R.id.progressbar);
        gridView = view.findViewById(R.id.grid_view);
        footerLinear = view.findViewById(R.id.footerLinear);

        firebaseDatabase = FirebaseDatabase.getInstance();

        // Initialize Categories
        if(this.titles.isEmpty()) {
            DatabaseReference ref = firebaseDatabase.getReference("Categories");
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    List<String> categories = new ArrayList<>();
                    for(DataSnapshot ds : dataSnapshot.getChildren()) {
                        String categoryName = ds.getKey();
                        categories.add(categoryName);
                    }
                    gotCategoriesFromFireBase(categories);
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {}
            });
        } else IntializeGridView();


        // Buttons OnClick
        publishBtn = view.findViewById(R.id.publish);
        publishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(FirebaseAuth.getInstance().getCurrentUser() != null) {
                    if (publishFragment == null)
                        publishFragment = new PublishFragment();
                    outerTransaction(publishFragment);
                }
                else {
                    if (loginFragment == null)
                        loginFragment = new LoginFragment();
                    outerTransaction(loginFragment);
                }
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
