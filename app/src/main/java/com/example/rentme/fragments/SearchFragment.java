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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.rentme.R;
import com.example.rentme.model.Configurations;
import com.example.rentme.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class SearchFragment extends Fragment  implements AdapterView.OnItemSelectedListener {
    Button backBtn;

    private List<String> categoryNames = new ArrayList<>();
    private List<String> areaNames = new ArrayList<>();
    private List<String> stateNames = new ArrayList<>();

    MainFragment mainFragment;

    Spinner mainCategorySpin;
    Spinner areaSpin;
    Spinner stateSpin;

    String selectedCategory;
    String selectedArea;
    String selectedState;

    FirebaseDatabase firebaseDatabase;
    LinearLayout mainLinear;
    ProgressBar progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        backBtn = view.findViewById(R.id.backToMain);
        mainLinear = view.findViewById(R.id.mainLinear);
        progressBar = view.findViewById(R.id.progressbar);
        areaSpin = view.findViewById(R.id.area);
        stateSpin = view.findViewById(R.id.state);

        mainCategorySpin =  view.findViewById(R.id.MainCategory);

        firebaseDatabase = FirebaseDatabase.getInstance();
        // Initialize Configurations
        DatabaseReference ref = firebaseDatabase.getReference("Configurations").child("user_configurations").getRef();
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Configurations conf = dataSnapshot.getValue(Configurations.class);
                if(conf == null) throw new NoSuchElementException("Cant Retrieve Configurations from database");
                gotConfigurationsFromFireBase(conf);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        backBtn = view.findViewById(R.id.back);
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

    private void gotConfigurationsFromFireBase(Configurations conf) {
            progressBar.setVisibility(View.GONE);
            mainLinear.setVisibility(LinearLayout.VISIBLE);

            this.categoryNames = conf.getCategoriesOptions();
            this.areaNames = conf.getAreaNames();
            this.stateNames = conf.getStateOptions();

            InitializeSpinners();
    }

    private void InitializeSpinners() {
        //start category spinner

        mainCategorySpin.setOnItemSelectedListener(this);
        ArrayAdapter aaCategory = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_item,categoryNames);
        aaCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mainCategorySpin.setAdapter(aaCategory);
        //end category spinner

        //start area spinner
        areaSpin.setOnItemSelectedListener(this);
        ArrayAdapter aaArea = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_item,areaNames);
        aaArea.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        areaSpin.setAdapter(aaArea);
        // end area spinner

        //start state spinner
        stateSpin.setOnItemSelectedListener(this);
        ArrayAdapter aaState = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_item,stateNames);
        aaState.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        stateSpin.setAdapter(aaState);
        //end category spinner
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        selectedCategory = mainCategorySpin.getItemAtPosition(mainCategorySpin.getSelectedItemPosition()).toString();
        selectedArea = areaSpin.getItemAtPosition(areaSpin.getSelectedItemPosition()).toString();
        selectedState = stateSpin.getItemAtPosition(stateSpin.getSelectedItemPosition()).toString();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
