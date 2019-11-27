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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rentme.R;
import com.example.rentme.model.Product;

public class PublishFragment extends Fragment implements AdapterView.OnItemSelectedListener  {

    Button publishBtn;

    private Spinner categorySelectedSpin;
    private Spinner productConditionSpin;

    private String[] categoryNames={"בחר קטגורייה...","אביזרים","מוצרי חשמל","מטבח","גינה","ספורט"};
    private String[] statusNames={"בחר מצב...","חדש","כמו חדש","משומש","שבור"};

    private String selectedCategory = "קטגורייה...";
    private String selectedCondition = "בחר מצב...";
    private String productTitle = "";
    private int PricePerHour = -1;
    private int PricePerDay = -1;
    private String image = "";
    private String details = "";

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
        publishBtn = view.findViewById(R.id.addBtn);

        //start category spinner
        categorySelectedSpin.setOnItemSelectedListener(this);
        ArrayAdapter aaCategory = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_item,categoryNames);
        aaCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySelectedSpin.setAdapter(aaCategory);
        //end category spinner

        //start condition spinner
        productConditionSpin.setOnItemSelectedListener(this);
        ArrayAdapter aaArea = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_item,statusNames);
        aaArea.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        productConditionSpin.setAdapter(aaArea);
        //end condition spinner

        publishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productTitle =  ((EditText)view.findViewById(R.id.product_title)).getText().toString();
                selectedCategory =  ((EditText)view.findViewById(R.id.product_title)).getText().toString();
                details = ((EditText)view.findViewById(R.id.product_title)).getText().toString();
                selectedCondition = ((EditText)view.findViewById(R.id.product_title)).getText().toString();
                PricePerDay = Integer.parseInt(((EditText)view.findViewById(R.id.product_title)).getText().toString());
                PricePerHour = Integer.parseInt(((EditText)view.findViewById(R.id.product_title)).getText().toString());
                //take the values////////////////++++++++++++++++


                if ((productTitle !="")&& (selectedCategory != "קטגורייה...")&& (details != "") &&
                        (selectedCondition !=  "בחר מצב...") && (PricePerDay != -1) && (PricePerHour != -1)) {
                    Product addedProduct = new Product(productTitle, selectedCategory, details, selectedCondition, PricePerDay, PricePerHour);
                    //push database//////////////++++++++++++++++++
                }
                else
                    Toast.makeText(getContext(),"קלט לא חוקי", Toast.LENGTH_SHORT).show();
            }
         });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(getContext(),
                "\ncategory : "+ String.valueOf(categorySelectedSpin.getSelectedItem()) +
                        "\narea : "+ String.valueOf(productConditionSpin.getSelectedItem()),
                Toast.LENGTH_SHORT).show();

        selectedCategory = categorySelectedSpin.getItemAtPosition(categorySelectedSpin.getSelectedItemPosition()).toString();
        selectedCondition = productConditionSpin.getItemAtPosition(productConditionSpin.getSelectedItemPosition()).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
