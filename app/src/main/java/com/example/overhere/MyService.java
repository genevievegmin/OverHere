package com.example.overhere;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.annotation.Nullable;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class MyService extends Service {
    private final IBinder binder = new LocalBinder();
    private FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
    SharedPreferences prefs;
    String user;
    private MediaPlayer mediaPlayer;
//    private SoundPool soundPool;
//    private int soundEff;

    public MyService(){

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        prefs = getApplicationContext().getSharedPreferences("Data Pref", MODE_PRIVATE);
        user = prefs.getString("User",null);
        System.out.println("yoooo user1234" + user);

        //final String unique_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

        mFirestore.collection("Users").document(user).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable DocumentSnapshot documentSnapshot, @javax.annotation.Nullable FirebaseFirestoreException e) {
                if(e != null){
                    return;
                }
                if(documentSnapshot.exists()){
                    Boolean checkRing = documentSnapshot.getBoolean("Ring");
                    //String deviceID = documentSnapshot.getString("id");
                    System.out.println("Hello123" + checkRing);
                    if (checkRing != null && checkRing) {

//                        AudioAttributes audioAttributes = new AudioAttributes.Builder()
//                                .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
//                                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
//                                .build();
//
//                        soundPool = new SoundPool.Builder().setMaxStreams(1).setAudioAttributes(audioAttributes).build();
//                        soundEff = soundPool.load(MyService.this, R.raw.soundeffect, 1);
//                        soundPool.play(soundEff, 1,1,0,2,1);
//                        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
//                            @Override
//                            public void onLoadComplete(SoundPool soundPool2, int sampleId, int status) {
//                                soundPool.play(soundEff, 1,1,0,2,1);
//                            }
//                        });

                        //play sound
                        mediaPlayer = MediaPlayer.create(MyService.this, R.raw.soundeffect);
                        mediaPlayer.setWakeMode(MyService.this, PowerManager.PARTIAL_WAKE_LOCK);
                        mediaPlayer.setLooping(true);
                        mediaPlayer.start();
                    }else {
                        if (mediaPlayer != null) {
                            mediaPlayer.stop();
                            mediaPlayer.release();
                            mediaPlayer = null;
                        }
                    }
                }
            }
        });
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    public class LocalBinder extends Binder {
        MyService getService() {
            // Return this instance of LocalService so clients can call public methods
            return MyService.this;
        }
    }
}