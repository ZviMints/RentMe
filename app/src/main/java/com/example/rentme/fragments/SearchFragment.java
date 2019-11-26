package com.example.rentme.fragments;

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
import android.widget.Spinner;
import android.widget.Toast;

import com.example.rentme.R;

public class SearchFragment extends Fragment  implements AdapterView.OnItemSelectedListener {
    Button backBtn;

    String[] categoryNames={"בחר קטגורייה...","אביזרים","מוצרי חשמל","מטבח","גינה","ספורט"};
    String[] areaNames={"בחר אזור...","השרון","דרום","צפון","ירושלים","שומרון"};

    MainFragment mainFragment;

    Spinner mainCategorySpin;
    Spinner areaSpin;


    String selectedCategory;
    String selectedArea;

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


        //start category spinner
        mainCategorySpin = (Spinner) view.findViewById(R.id.MainCategory);

        mainCategorySpin.setOnItemSelectedListener(this);
        ArrayAdapter aaCategory = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_item,categoryNames);
        aaCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mainCategorySpin.setAdapter(aaCategory);
        //end category spinner
      
        //start area spinner
        areaSpin = (Spinner) view.findViewById(R.id.area);
        areaSpin.setOnItemSelectedListener(this);
        ArrayAdapter aaArea = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_item,areaNames);
        aaArea.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        areaSpin.setAdapter(aaArea);
        //end area spinner

        //Creating the ArrayAdapter instance having the bank name list
        //ArrayAdapter aa = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_item,bankNames);


        //aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        //mainCategorySpin.setAdapter(aa);




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
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
