package com.iamtanshu.chatappwebsocket.Notify;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

//TODO: Setp1 create application class
public class App extends Application {
    //Todo: Step2 define notification channel
    // In actual application define the purpose of notifi. channel
    public static final String CHANNEL_1_ID = "channel1";
    public static final String CHANNEL_2_ID = "channel2";

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
    }

    private void createNotificationChannel() {
        //todo: Check whether the device is above or equal to api level 26
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            long[] a = {100,100,100,100};
            NotificationChannel channel1 = new NotificationChannel(
                    CHANNEL_1_ID,
                    "Channnel 1",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel1.setDescription("This is channel 1");
            channel1.setVibrationPattern(a);

            NotificationChannel channel2 = new NotificationChannel(
                    CHANNEL_2_ID,
                    "Channel 2",
                    NotificationManager.IMPORTANCE_LOW
            );
            channel2.setDescription("This is channel 2");

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);
            manager.createNotificationChannel(channel2);

        }
    }
}
