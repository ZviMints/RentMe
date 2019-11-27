package com.example.rentme.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

public class PublishFragment extends Fragment implements AdapterView.OnItemSelectedListener  {

    private Button publishBtn;
    private Button backMenu;

    private Spinner categorySelectedSpin;
    private Spinner productConditionSpin;
    private EditText titleLayer;
    private EditText detailsLayer;
    private EditText pricePerDayLayer;
    private EditText pricePerHourLayer;

    private String[] categoryNames={"בחר קטגורייה...","אביזרים","מוצרי חשמל","מטבח","גינה","ספורט"};
    private String[] statusNames={"בחר מצב...","חדש","כמו חדש","משומש","שבור"};

    public String selectedCategory = "קטגורייה...";
    public String selectedCondition = "בחר מצב...";
    public String productTitle = "";
    public String PricePerHour = "";
    public String PricePerDay = "";
    public String image = "";
    public String details = "";

    private MainFragment mainFragment;

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
        titleLayer = (EditText)view.findViewById(R.id.product_title);
        detailsLayer = (EditText)view.findViewById(R.id.details);
        pricePerDayLayer = (EditText)view.findViewById(R.id.price_per_day);
        pricePerHourLayer = (EditText)view.findViewById(R.id.price_per_hour);
        backMenu = view.findViewById(R.id.backMain);

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
                productTitle =  titleLayer.getText().toString();
                details = detailsLayer.getText().toString();
                PricePerDay = pricePerDayLayer.getText().toString();
                PricePerHour = pricePerHourLayer.getText().toString();



                if ((productTitle.length() > 0)&& (selectedCategory != "בחר קטגורייה...")&& (details.length() > 0) &&
                        (selectedCondition !=  "בחר מצב...") && (PricePerDay.length() > 0) && (PricePerHour.length() > 0)) {
                    Product addedProduct = new Product(productTitle, selectedCategory, details, selectedCondition, PricePerDay, PricePerHour);
                    FirebaseDatabase.getInstance().getReference("category")
                            .child(selectedCategory).child(getRandomString()+": "+productTitle).setValue(addedProduct);

                }
                else
                    Toast.makeText(getContext(),"קלט לא חוקי", Toast.LENGTH_SHORT).show();
            }
         });

        backMenu.setOnClickListener(new View.OnClickListener() {
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

    private String getRandomString(){
        Random r = new Random();

        String alphabet = "123xyz";
        String ans="";
        for (int i = 0; i < 15; i++) {
            ans+=alphabet.charAt(r.nextInt(alphabet.length()));
        }
        return ans;
    }


    private void outerTransaction(Fragment fragment){
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.OuterFragmentContainer, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
