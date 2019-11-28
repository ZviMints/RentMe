package com.example.rentme.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.rentme.R;
import com.example.rentme.adapters.ProductListAdapter;
import com.example.rentme.fragments.InItemFragment;
import com.example.rentme.fragments.LoginFragment;
import com.example.rentme.fragments.MainFragment;
import com.example.rentme.fragments.ProfileFragment;
import com.example.rentme.fragments.SearchFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity implements ProductListAdapter.MoreDetailsButtonListener {


    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    /**
     * Main Categories
     **/

    Button profileBtn;
    Button searchBtn;

    MainFragment mainFragment;
    SearchFragment searchFragment;
    ProfileFragment profileFragment;
    LoginFragment loginFragment;
    InItemFragment inItemFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        searchBtn = findViewById(R.id.Search);
        profileBtn = findViewById(R.id.Profile);

        profileBtn.setText((firebaseUser != null) ? "פרופיל" : "התחבר/הרשם");

        profileBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (firebaseUser != null) {
                    if (profileFragment==null)
                        profileFragment = new ProfileFragment();
                    outerTransaction(profileFragment);
                } else {
                    if (loginFragment == null)
                        loginFragment = new LoginFragment();
                    outerTransaction(loginFragment);

                }
            }
        });
        searchBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (searchFragment == null)
                    searchFragment = new SearchFragment();
                outerTransaction(searchFragment);
            }
        });


        mainFragment = new MainFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.OuterFragmentContainer, mainFragment).commit();


    }

    @Override
    public void showMoreDetails() {
        if (inItemFragment == null)
            inItemFragment = new InItemFragment();
        outerTransaction(inItemFragment);
    }

    private void outerTransaction(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.OuterFragmentContainer, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void setFirebaseAuth(FirebaseAuth firebaseAuth) {
        this.firebaseAuth = firebaseAuth;
    }

    public void setFirebaseUser(FirebaseUser firebaseUser) {
        this.firebaseUser = firebaseUser;
        profileBtn.setText((firebaseUser != null) ? "פרופיל" : "התחבר/הרשם");

    }


}
