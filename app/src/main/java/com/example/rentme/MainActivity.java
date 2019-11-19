package com.example.rentme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
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


        profileBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
        lastAdsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lastAdsBtn.setBackgroundResource(R.drawable.main_header_selector);
                homePageBtn.setBackgroundColor(getResources().getColor(R.color.lightGray));
                if (productsListFragment == null)
                    productsListFragment  = new ProductsListFragment();

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
