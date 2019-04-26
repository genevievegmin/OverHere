package com.example.overhere;

import android.app.Notification;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import javax.annotation.Nullable;

public class FindDevice extends AppCompatActivity {

    SharedPreferences prefs;
    String user;
    private NotificationManagerCompat notificationManager;
    private FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
    private DocumentReference accountRef;
    String unique_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_device);

        // Shared Preferences
        prefs = getApplicationContext().getSharedPreferences("Data Pref", MODE_PRIVATE);
        user = prefs.getString("User",null);

        // Cloud Firestore
        accountRef = mFirestore.collection("Users").document(user);

        notificationManager = NotificationManagerCompat.from(this);

        //unique_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        //System.out.println("Lalaland" + unique_id);

        // Listening to data changes in Cloud Firestore
        accountRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if(e != null){
                    Toast.makeText(FindDevice.this, "Error while loading data!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(documentSnapshot.exists()){
                    Boolean checkRing = documentSnapshot.getBoolean("Ring");
                    //String deviceID = documentSnapshot.getString("id");
                    //if (checkRing && deviceID.equals(unique_id)){
                    if (checkRing){
                        //Notification notification = new NotificationCompat.Builder(FindDevice.this, NotificationApp.PLAY_SOUND).build();
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(FindDevice.this, NotificationApp.PLAY_SOUND)
                                .setSmallIcon(R.drawable.ic_notify)
                                .setContentTitle("My notification")
                                .setContentText("Hello World!");
                        notificationManager.notify(1, builder.build());
                    }
                }
            }
        });


    }

    // Play device's sound
    public void playSound(View view){
    Boolean ring = true;
        accountRef.update("Ring", ring);
    }

    // Get device locaiton
    public void getLocation(View view){

    }
}
