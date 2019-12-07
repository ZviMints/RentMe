package com.example.rentme.fragments;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.rentme.R;
import com.example.rentme.model.Configurations;
import com.example.rentme.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.NoSuchElementException;

public class SignUpFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    Button back;
    Button signUpBtn;
    FirebaseAuth firebaseAuth;
    EditText email;
    EditText password;

    Spinner areaSpin;
    EditText name;
    EditText lastname;
    EditText number;
    String selectedArea;

    ProgressBar progressBar;
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


        email = (EditText) view.findViewById(R.id.emailForm);
        password = (EditText) view.findViewById(R.id.passwordForm);

        name = (EditText) view.findViewById(R.id.nameForm);
        lastname = (EditText) view.findViewById(R.id.lastnameForm);
        areaSpin = (Spinner) view.findViewById(R.id.area_from);
        number = (EditText) view.findViewById(R.id.numberForm);

        progressBar = view.findViewById(R.id.progressbar);



        findAreaConfigurations();

        // FireBash Button
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                if (email.getText().length() < 1 || password.getText().length() < 1) {
                    Toast.makeText(getContext(), "איימל או סיסמא לא יכולים להיות ריקים", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                } else {
                    firebaseAuth.createUserWithEmailAndPassword(
                            email.getText().toString(),
                            password.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    // Check for the result
                                    if (task.isSuccessful()) {
                                        User user = new User(name.getText().toString(),
                                                lastname.getText().toString(),
                                                selectedArea,
                                                number.getText().toString(),
                                                firebaseAuth.getCurrentUser().getEmail());
                                        FirebaseDatabase.getInstance().getReference("Users")
                                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .setValue(user)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                               if(task.isSuccessful()) {
                                                   progressBar.setVisibility(View.GONE);
                                                   Toast.makeText(getContext(), "הרשמה בוצעה בהצלחה", Toast.LENGTH_SHORT).show();
                                                   if (loginFragment == null)
                                                       loginFragment = new LoginFragment();
                                                   outerTransaction(loginFragment);
                                               }
                                               else {
                                                   progressBar.setVisibility(View.GONE);
                                                   Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                               }
                                            }
                                        });
                                    } else {
                                        Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
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

    private void findAreaConfigurations() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Configurations")
                .child("configurations").getRef();
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Configurations conf = dataSnapshot.getValue(Configurations.class);
                if (conf == null)
                    throw new NoSuchElementException("Cant Retrieve Configurations from database");
                List<String> areaNames = conf.getAreaNames();
                initAreaSpin(areaNames);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void initAreaSpin(List<String> areaNames) {
        //start category spinner
        areaSpin.setOnItemSelectedListener(this);
        ArrayAdapter aaArea = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, areaNames);
        aaArea.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        areaSpin.setAdapter(aaArea);
        //end category spinner
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    private void outerTransaction(Fragment fragment) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.OuterFragmentContainer, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selectedArea = areaSpin.getItemAtPosition(areaSpin.getSelectedItemPosition()).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
