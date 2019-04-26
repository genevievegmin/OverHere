package com.example.overhere;

import android.app.IntentService;
import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class SoundIntentService extends IntentService {

    private FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
    SharedPreferences prefs;
    String user;

    public SoundIntentService() {
        super("SoundIntentService");
        setIntentRedelivery(true);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("What's uppppp");
        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "Sound:Wakelock");
        wakeLock.acquire();

        prefs = getApplicationContext().getSharedPreferences("Data Pref", MODE_PRIVATE);
        user = prefs.getString("User",null);
        System.out.println("yoooo user" + user);
        Log.d("lala", "onCreate: " + user);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NotificationApp.PLAY_SOUND)
                    .setSmallIcon(R.drawable.ic_notify)
                    .setContentTitle("Sound service is here people")
                    .setContentText("Hello World!");
            notificationManager.notify(1, builder.build());
            startForeground(1, builder.build());
        }
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        // Listening to data changes in Cloud Firestore
        mFirestore.collection("Users").document(user).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable DocumentSnapshot documentSnapshot, @javax.annotation.Nullable FirebaseFirestoreException e) {
                if(e != null){
                    System.out.println("laplaplalala");
                    return;
                }
                if(documentSnapshot.exists()){
                    Boolean checkRing = documentSnapshot.getBoolean("Ring");
                    //String deviceID = documentSnapshot.getString("id");
                    //if (checkRing && deviceID.equals(unique_id)){
                    if (checkRing){
                        System.out.println("Hello" + checkRing);
                    }else {System.out.println("nomoloto");}
                }
            }
        });
    }
}
