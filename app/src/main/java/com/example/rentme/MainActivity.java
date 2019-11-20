package com.example.rentme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;


public class MainActivity extends AppCompatActivity {

    /**
     * Main Categories
     **/
    Button homePageBtn;
    Button lastAdsBtn;
    Button profileBtn;
    Button searchBtn;

    FrameLayout fragmentContainer;
    ProductsListFragment productsListFragment;
    CategoriesFragment categoriesFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        homePageBtn = findViewById(R.id.MainPage);
        lastAdsBtn = findViewById(R.id.LastAds);
        profileBtn = findViewById(R.id.Profile);
        fragmentContainer = findViewById(R.id.fragmentContainer);
        searchBtn = findViewById(R.id.Search);


        profileBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
        searchBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(intent);
            }
        });

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

                //send to the new fragment what category was clicked
                Bundle bundle = new Bundle();
                bundle.putString("category","מעורב");
                productsListFragment.setArguments(bundle);

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

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

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack so the user can navigate back
                transaction.replace(R.id.fragmentContainer, categoriesFragment);
                transaction.addToBackStack(null);

                // Commit the transaction
                transaction.commit();
            }
        });

        categoriesFragment = new CategoriesFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.fragmentContainer, categoriesFragment).commit();

    }
}
