package com.example.overhere;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class NotificationApp extends Application {
    public static final String PLAY_SOUND = "playSound";

    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationChannels();
    }

    private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel plsound = new NotificationChannel(
                    PLAY_SOUND,
                    "Play sound",
                    NotificationManager.IMPORTANCE_HIGH
            );
            //pSound.setDescription("Playing sound on the lost device");

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(plsound);

        }
    }
}
