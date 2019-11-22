package com.example.rentme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    Button back;
    Button loginBtn;
    Button signUpBtn;
    FirebaseAuth firebaseAuth;
    EditText userEmail;
    EditText userPassword;
    ProgressBar progressBar;
    Toast emptyEmailOrPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        back = findViewById(R.id.backToMain);
        loginBtn = findViewById(R.id.btnLogin);
        signUpBtn = findViewById(R.id.btnGoToSignUp);
        userEmail = findViewById(R.id.email_form_login);
        userPassword = findViewById(R.id.password_form_login);
        progressBar = findViewById(R.id.progressbar);

        firebaseAuth = FirebaseAuth.getInstance();
        // FireBash Button
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                String usernameText = userEmail.getText().toString();
                String passwordText = userPassword.getText().toString();
                if(usernameText.length() < 1 || passwordText.length() < 1 ) {
                    Toast.makeText(LoginActivity.this,"איימל או סיסמא לא יכולים להיות ריקים",Toast.LENGTH_SHORT).show();
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
                                        Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
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
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });


        // Go to Sign Up Page
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

}
