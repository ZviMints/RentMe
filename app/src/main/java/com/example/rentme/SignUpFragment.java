package com.example.rentme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
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

public class SignUpFragment extends Fragment {
    Button back;
    Button signUpBtn;
    SignUpFragment signUpFragment;
    FirebaseAuth firebaseAuth;
    EditText email;
    EditText password;
    ProgressBar progressBar;
    ProfileFragment profileFragment;
    LoginFragment loginFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        firebaseAuth = FirebaseAuth.getInstance();

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);


        back = view.findViewById(R.id.backToLogin);
        signUpBtn = view.findViewById(R.id.btnSignUp);


        email = (EditText)view.findViewById(R.id.emailForm);

        password = (EditText)view.findViewById(R.id.passwordForm);

        progressBar = view.findViewById(R.id.progressbar);

        // FireBash Button
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                if(email.getText().length() < 1 || password.getText().length() < 1 ) {
                    Toast.makeText(getContext(),"איימל או סיסמא לא יכולים להיות ריקים",Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
                else {
                    firebaseAuth.createUserWithEmailAndPassword(
                            email.getText().toString(),
                            password.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    // Check for the result
                                    progressBar.setVisibility(View.GONE);
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getContext(),"הרשמה בוצעה בהצלחה",Toast.LENGTH_SHORT).show();
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
                if (loginFragment == null)
                    loginFragment = new LoginFragment();
                outerTransaction(loginFragment);

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
