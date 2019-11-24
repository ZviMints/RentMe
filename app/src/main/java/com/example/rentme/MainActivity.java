package com.example.rentme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity implements ProductListAdapter.MoreDetailsButtonListener {
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

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

        searchBtn = findViewById(R.id.Search);
        profileBtn = findViewById(R.id.Profile);


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

}
