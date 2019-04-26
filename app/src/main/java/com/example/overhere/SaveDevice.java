package com.example.overhere;

import android.app.Notification;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import io.opencensus.tags.Tag;

public class SaveDevice extends AppCompatActivity {

    private FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
    private String unique_id;


    SharedPreferences getPrefs;
    String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_device);
        //Get the user email from Shared Preferences.
        getPrefs = getApplicationContext().getSharedPreferences("Data Pref", MODE_PRIVATE);
        user = getPrefs.getString("User",null);
        unique_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        System.out.println("What's Up" + unique_id);

    }

    public void registerDevice(View view) {
        EditText deviceName = findViewById(R.id.deviceName);
        String dname = deviceName.getText().toString();
        Boolean ring = false;

        //Intent serviceIntent = new Intent(this, SoundIntentService.class);
        //ContextCompat.startForegroundService(this, serviceIntent);

        if (dname.isEmpty() || user.isEmpty()) {
            return;
        }
        Map<String, Object> account = new HashMap<String, Object>();
        account.put("Device", dname);
        account.put("Ring", ring);
        account.put("id", unique_id);
        mFirestore.collection("Users").document(user).set(account).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(SaveDevice.this, "Device added", Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SaveDevice.this, "Error!", Toast.LENGTH_SHORT).show();
                Log.d("Register Issue", e.toString());
            }
        });

    }
}
