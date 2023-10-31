package com.example.ueeapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class CheckSchedule extends AppCompatActivity {

    EditText orderEmail;
    ImageView navCheck;
    Button btnDecline, btnApprove;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_schedule);

        String userEmail = getIntent().getStringExtra("userEmail");

        navCheck = findViewById(R.id.navCheck);
        orderEmail = findViewById(R.id.orderEmail);
        orderEmail.setText(userEmail);
        orderEmail.setEnabled(false);
        btnDecline = findViewById(R.id.btnDecline);
        btnApprove = findViewById(R.id.btnApprove);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            if(ContextCompat.checkSelfPermission(CheckSchedule.this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED){

                ActivityCompat.requestPermissions(CheckSchedule.this, new String[]{Manifest.permission.POST_NOTIFICATIONS},101);
            }
        }

        navCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LaundererOrders.class);
                startActivity(intent);
                finish();
            }
        });

        btnDecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to pass the status to the "Status" page
                Intent statusIntent = new Intent(getApplicationContext(), LaundryStatus.class);
                Toast.makeText(CheckSchedule.this,"Status Changed!",Toast.LENGTH_SHORT).show();
                statusIntent.putExtra("status", "Declined");
                statusIntent.putExtra("userEmail", userEmail);
                makeNotification();
                startActivity(statusIntent);
                finish();
            }
        });
        
        btnApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LaundryStatus.class);
                Toast.makeText(CheckSchedule.this,"Status Changed!",Toast.LENGTH_SHORT).show();
                intent.putExtra("status", "Approved");
                intent.putExtra("userEmail", userEmail);
                makeNotificationApprove();
                startActivity(intent);
                finish();
            }
        });
    }

    public void makeNotification(){
        String chanelID = "CHANNEL_ID_NOTIFICATION";
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), chanelID);

        builder.setSmallIcon(R.drawable.ic_notifications)
        .setContentTitle("Laundry Order Status")
        .setContentText("Status Changed!")
        .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        Intent intent = new Intent(getApplicationContext(), NotificationActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("data","Dear customer, Your Laundry Order has been Declined due to the lack of time availability! Sorry for the inconvenience");

        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0,intent,PendingIntent.FLAG_MUTABLE);
        builder.setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = notificationManager.getNotificationChannel(chanelID);
            if(notificationChannel == null){
                int importance = NotificationManager.IMPORTANCE_HIGH;
                notificationChannel = new NotificationChannel(chanelID, "Some description", importance);
                notificationChannel.setLightColor(Color.GREEN);
                notificationChannel.enableVibration(true);
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }

        notificationManager.notify(0,builder.build());
    }

    public void makeNotificationApprove(){
        String chanelID = "CHANNEL_ID_NOTIFICATION";
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), chanelID);

        builder.setSmallIcon(R.drawable.ic_notifications)
                .setContentTitle("Laundry Order Status")
                .setContentText("Status Changed!")
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        Intent intent = new Intent(getApplicationContext(), NotificationActivityApprove.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("data1","Dear customer, Your Laundry Order has been Approved and the pick and delivery will be done according to the scheduled dates");

        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0,intent,PendingIntent.FLAG_MUTABLE);
        builder.setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = notificationManager.getNotificationChannel(chanelID);
            if(notificationChannel == null){
                int importance = NotificationManager.IMPORTANCE_HIGH;
                notificationChannel = new NotificationChannel(chanelID, "Some description", importance);
                notificationChannel.setLightColor(Color.GREEN);
                notificationChannel.enableVibration(true);
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }

        notificationManager.notify(0,builder.build());
    }
}