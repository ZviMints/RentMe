package com.example.rentme;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginFragment extends Fragment {
    Button back;
    Button loginBtn;
    Button signUpBtn;
    FirebaseAuth firebaseAuth;
    EditText userEmail;
    EditText userPassword;
    ProgressBar progressBar;

    MainFragment mainFragment;
    SignUpFragment signUpFragment;
    ProfileFragment profileFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);


        back = view.findViewById(R.id.backToMain);
        loginBtn = view.findViewById(R.id.btnLogin);
        signUpBtn = view.findViewById(R.id.btnGoToSignUp);
        userEmail = view.findViewById(R.id.email_form_login);
        userPassword = view.findViewById(R.id.password_form_login);
        progressBar = view.findViewById(R.id.progressbar);

        firebaseAuth = FirebaseAuth.getInstance();
        // FireBash Button
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                String usernameText = userEmail.getText().toString();
                String passwordText = userPassword.getText().toString();
                if(usernameText.length() < 1 || passwordText.length() < 1 ) {
                    Toast.makeText(getContext(),"איימל או סיסמא לא יכולים להיות ריקים",Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
                else {
                    firebaseAuth.signInWithEmailAndPassword(
                            userEmail.getText().toString(),
                            userPassword.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    // Check for the result
                                    progressBar.setVisibility(View.GONE);
                                    if (task.isSuccessful()) {

                                        if (profileFragment == null)
                                            profileFragment = new ProfileFragment();
                                        outerTransaction(profileFragment);

                                    } else {
                                        Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }
            }
        });


        // Back To Main Activity
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mainFragment == null)
                    mainFragment = new MainFragment();
                outerTransaction(mainFragment);
            }
        });


        // Go to Sign Up Page
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (signUpFragment == null)
                    signUpFragment = new SignUpFragment();
                outerTransaction(signUpFragment);

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
}

