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
//                    firebaseDatabase.getReference("Management Message").child(new Date().toString()).setValue(messege);
                    createNotification(messege);
                }


            }


        });
    }

    private void createNotification(String message) {
        Intent landingIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingLandingIntent = PendingIntent.getActivity(this, 0,
                landingIntent,0);
//        Notification notification = notificationBuilder;

        NotificationManager notificationManager = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);




        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            String channelId =
                    "MY_CHANNEL_ID";
            NotificationChannel channel = new NotificationChannel(channelId, "Message to Users",
                    NotificationManager.IMPORTANCE_DEFAULT);
                    channel.setDescription("deliver message from admin to users");
            notificationManager.createNotificationChannel(channel);

            NotificationCompat.Builder builder = new
                    NotificationCompat.Builder(getApplicationContext(), channelId);

            Notification notification = builder.setContentIntent(pendingLandingIntent)
                    .setWhen(System.currentTimeMillis())
                    .setAutoCancel(true)
                    .setSmallIcon(R.drawable.logo)
                    .setContentTitle("message from RentMe")
                    .setContentText(message).build();

            notificationManager.notify(1, notification);
        }




    }
}
