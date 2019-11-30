package com.example.rentme.fragments;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rentme.activities.MainActivity;
import com.example.rentme.R;
import com.example.rentme.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.NoSuchElementException;

public class ProfileFragment extends Fragment {

    TextView titleName;
    TextView titleLastName;
    TextView titleNumber;
    TextView titleArea;
    TextView titleEmail;


    Button logoutBtn;
    Button back;

    LinearLayout mainLinear;
    LinearLayout informationLinear;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;

    MainFragment mainFragment;
    ProgressBar progressBar;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        titleName = view.findViewById(R.id.tvFirstName);
        titleLastName = view.findViewById(R.id.tvLastName);
        progressBar = view.findViewById(R.id.progressbar);

        titleNumber = view.findViewById(R.id.tvNumber);
        titleArea = view.findViewById(R.id.tvArea);
        titleEmail = view.findViewById(R.id.tvEmail);

        logoutBtn = view.findViewById(R.id.signOut);
        back = view.findViewById(R.id.backToMain);
        mainLinear = view.findViewById(R.id.mainLinear);
        informationLinear = view.findViewById(R.id.InformationLinear);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();

        // Initialize User
        DatabaseReference ref = firebaseDatabase.getReference("Users").child(firebaseUser.getUid()).getRef();
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if(user == null) throw new NoSuchElementException("Cant Retrieve user from database");
                gotUserFromFireBase(user);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                Toast.makeText(getContext(),"התנתקת בהצלחה",Toast.LENGTH_SHORT).show();
                startActivity(intent);
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
        return view;
    }


    private void gotUserFromFireBase(User user) {

        progressBar.setVisibility(View.GONE);
        mainLinear.setVisibility(LinearLayout.VISIBLE);
        informationLinear.setVisibility(LinearLayout.VISIBLE);
        logoutBtn.setVisibility(LinearLayout.VISIBLE);

        String name = user.getName();
        String lastname = user.getLastname();
        String area = user.getArea();
        String number = user.getNumber();
        String email = user.getEmail();

        this.titleName.setText(name);
        this.titleLastName.setText(lastname);
        this.titleNumber.setText(number);
        this.titleArea.setText(area);
        this.titleEmail.setText(email);

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

