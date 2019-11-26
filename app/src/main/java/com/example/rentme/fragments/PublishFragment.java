package com.example.rentme.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.rentme.R;

public class PublishFragment extends Fragment implements AdapterView.OnItemSelectedListener  {

    Spinner categorySelectedSpin;
    Spinner productConditionSpin;

    String[] categoryNames={"בחר קטגורייה...","אביזרים","מוצרי חשמל","מטבח","גינה","ספורט"};
    String[] statusNames={"בחר מצב...","חדש","כמו חדש","משומש","שבור"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_publish, container, false);
        categorySelectedSpin = (Spinner)view.findViewById(R.id.select_category);
        productConditionSpin = (Spinner)view.findViewById(R.id.product_condition);

        //start category spinner
        categorySelectedSpin.setOnItemSelectedListener(this);
        ArrayAdapter aaCategory = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_item,categoryNames);
        aaCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySelectedSpin.setAdapter(aaCategory);
        //end category spinner

        //start area spinner
        productConditionSpin.setOnItemSelectedListener(this);
        ArrayAdapter aaArea = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_item,statusNames);
        aaArea.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        productConditionSpin.setAdapter(aaArea);
        //end area spinner
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//        Toast.makeText(getContext(),
//                "\ncategory : "+ String.valueOf(mainCategorySpin.getSelectedItem()) +
//                        "\narea : "+ String.valueOf(areaSpin.getSelectedItem()),
//                Toast.LENGTH_SHORT).show();
//
//        selectedCategory = mainCategorySpin.getItemAtPosition(mainCategorySpin.getSelectedItemPosition()).toString();
//        selectedArea = areaSpin.getItemAtPosition(areaSpin.getSelectedItemPosition()).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
