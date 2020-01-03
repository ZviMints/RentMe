package com.example.rentme.activities.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.rentme.R;
import com.example.rentme.activities.MainActivity;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

public class UserMessageManagement extends AppCompatActivity {
    Button sendMessage;
    EditText messageContent;

    final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_user_message_management);

        messageContent = findViewById(R.id.message_content);
        sendMessage = findViewById(R.id.publish_message);

        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);

                String messege = messageContent.getText().toString();
                if (messege.compareTo("") != 0){
                    firebaseDatabase.getReference("Management Message").child("last massage").setValue(messege);
                }


            }


        });
    }


}
