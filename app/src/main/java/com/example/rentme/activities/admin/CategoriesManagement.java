package com.example.rentme.activities.admin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.rentme.R;
import com.example.rentme.activities.MainActivity;
import com.example.rentme.adapters.AdminCategoryManagmentAdapter;
import com.example.rentme.model.CategoryWarper;
import com.example.rentme.model.Configurations;
import com.example.rentme.model.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;

public class CategoriesManagement extends AppCompatActivity {
    Button back;
    ListView categoriesListView;
    EditText category_form_add;
    Button addCategory;
    Spinner remove_from_conf_spinner;
    ProgressBar progressBar;
    Button removeFromConf;
    Button removeFromCategories;
    Spinner remove_from_categories_spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories_management);

        categoriesListView = findViewById(R.id.categoriesListView);
        category_form_add = findViewById(R.id.category_form_add);
        addCategory = findViewById(R.id.addCategory);

        // Remove From Configurations
        remove_from_conf_spinner = findViewById(R.id.remove_from_conf_spinner);
        removeFromConf = findViewById(R.id.removeFromConf);

        //Remove from Categories
        removeFromCategories = findViewById(R.id.removeFromCategories);
        remove_from_categories_spinner = findViewById(R.id.remove_from_categories_spinner);


        progressBar = findViewById(R.id.progressbar);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference ref = firebaseDatabase.getReference("Categories");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<CategoryWarper> warpper = new ArrayList<>();
                // Inside Category X
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    String categoryName = ds.getKey();
                    List<Product> products = new ArrayList<>();
                    for(DataSnapshot dsChild : ds.getChildren()) {
                        Product product = ds.getValue(Product.class);
                        products.add(product);
                    }
                    CategoryWarper categoryWarper = new CategoryWarper(categoryName,products);
                    warpper.add(categoryWarper);
                }
                gotWarpersFromFireBase(warpper);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


        // Back Button Clicked
        back = findViewById(R.id.backMain);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void gotWarpersFromFireBase(final ArrayList<CategoryWarper> warper) {
        AdminCategoryManagmentAdapter adapter = new AdminCategoryManagmentAdapter(warper,this);
        categoriesListView.setAdapter(adapter);


        GetConfigurations();
        getCategories();
        
        //addCategory Button Clicked
        addCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                final String input = category_form_add.getText().toString();
                if(input == "") {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(CategoriesManagement.this, "קלט ריק", Toast.LENGTH_SHORT).show();
                }
                else {
                    List<String> allCategoryNames = new ArrayList<>();
                    for(int i=0; i<warper.size(); i++)
                        allCategoryNames.add(warper.get(i).getCategoryName());
                    if(allCategoryNames.contains(input)) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(CategoriesManagement.this, "קלט לא יחודי", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                        DatabaseReference ref = firebaseDatabase.getReference("Configurations").child("configurations").getRef();
                        ref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Configurations conf = dataSnapshot.getValue(Configurations.class);
                                if (conf == null)
                                    throw new NoSuchElementException("Cant Retrieve Configurations from database");

                                List<String> newCategoriesOptions = conf.getCategoriesOptions();
                                newCategoriesOptions.add(input);
                                Configurations newConf = new Configurations(conf,newCategoriesOptions);
                                firebaseDatabase.getReference("Configurations").child("configurations").removeValue();
                                firebaseDatabase.getReference("Configurations").child("configurations").setValue(newConf);
                                progressBar.setVisibility(View.GONE);
                                category_form_add.setText("");
                                Toast.makeText(CategoriesManagement.this,   "קטגורייה " + input + " נוספה לקונפגרציות", Toast.LENGTH_SHORT).show();

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });

                    }
                }
            }
        });
    }

    private void getCategories() {
        final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference ref = firebaseDatabase.getReference("Categories");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> categories = new ArrayList<>();
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    String categoryName = ds.getKey();
                    categories.add(categoryName);
                }
                gotCategories(categories);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    private void gotCategories(List<String> categories) {
        //categories spinner
        ArrayAdapter aaCat = new ArrayAdapter(this, android.R.layout.simple_spinner_item, categories);
        aaCat.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        remove_from_categories_spinner.setAdapter(aaCat);
        //categories spinner
    }


    private void GetConfigurations() {
        final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference ref = firebaseDatabase.getReference("Configurations").child("configurations").getRef();
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Configurations conf = dataSnapshot.getValue(Configurations.class);
                if (conf == null)
                    throw new NoSuchElementException("Cant Retrieve Configurations from database");
                gotConfigurations(conf);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

    private void gotConfigurations(Configurations conf) {
        //conf spinner
        ArrayAdapter aaConf = new ArrayAdapter(this, android.R.layout.simple_spinner_item, conf.getCategoriesOptions());
        aaConf.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        remove_from_conf_spinner.setAdapter(aaConf);
        //conf spinner
    }

    private class StableArrayAdapter extends ArrayAdapter<String> {

        HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

        public StableArrayAdapter(Context context, int textViewResourceId,
                                  List<String> objects) {
            super(context, textViewResourceId, objects);
            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.put(objects.get(i), i);
            }
        }

        @Override
        public long getItemId(int position) {
            String item = getItem(position);
            return mIdMap.get(item);
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

    }

}

