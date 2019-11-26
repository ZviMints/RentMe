package com.example.rentme.dao;


import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseUser;
import com.example.rentme.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.NoSuchElementException;

public class UserDAO {

    private static User user;

    public UserDAO(FirebaseDatabase firebaseDatabase, String uId) {
        // Initailize User
        DatabaseReference ref = firebaseDatabase.getReference("Users").child(uId).getRef();
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                if(user == null) throw new NoSuchElementException("Cant Retrieve user from database");
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    public String getFirstName() {
       return  (user == null) ? "null" : user.getName();
    }
    public String getLastName() {
        return  (user == null) ? "null" : user.getLastname();

    }
    public String getPhone() {
        return  (user == null) ? "null" : user.getNumber();

    }
    public String getEmail() {
        return  (user == null) ? "null" : "";
    }

    public String getArea() {
        return (user == null) ? "null" : user.getArea();
    }
}
