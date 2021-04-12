package com.iamtanshu.chatappwebsocket.Notify;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.iamtanshu.chatappwebsocket.MainActivity;
import com.iamtanshu.chatappwebsocket.R;

import static com.iamtanshu.chatappwebsocket.Notify.App.CHANNEL_1_ID;
import static com.iamtanshu.chatappwebsocket.Notify.App.CHANNEL_2_ID;

public class NotifyActivity extends AppCompatActivity {

    private NotificationManagerCompat notificationManager;
    private EditText editTextTitle;
    private EditText editTextMessage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify);

        notificationManager = NotificationManagerCompat.from(this);

        editTextTitle = findViewById(R.id.edt_title);
        editTextMessage = findViewById(R.id.edt_message);

    }

    public void sendOnChannel1(View channel1) {
        String title = editTextTitle.getText().toString();
        String message = editTextMessage.getText().toString();

        Intent activityIntent = new Intent(this, NotifyActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this,
                0, activityIntent, 0);


        Bitmap picture = BitmapFactory.decodeResource(getResources(), R.drawable.bigpicture);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_one)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setContentIntent(contentIntent)
                .setAutoCancel(true)
                .setLargeIcon(picture)
                .setStyle(new NotificationCompat.BigPictureStyle()
                        .bigPicture(picture)
                        .bigLargeIcon(null))
                .setOnlyAlertOnce(true)
                .build();
        notificationManager.notify(1, notification);


    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void sendOnChannel2(View channel2) {
        String title = editTextTitle.getText().toString();
        String message = editTextMessage.getText().toString();
        Bitmap artWork = BitmapFactory.decodeResource(getResources(), R.drawable.index);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_2_ID)
                .setSmallIcon(R.drawable.ic_two)
                .setContentTitle(title)
                .setContentText(message)
                .setLargeIcon(artWork)
                .addAction(R.drawable.ic_dislike, "Dislike", null)
                .addAction(R.drawable.ic_previous, "Previous", null)
                .addAction(R.drawable.ic_pause, "Pause", null)
                .addAction(R.drawable.ic_next, "Next", null)
                .addAction(R.drawable.ic_like, "Like", null)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .build();
        notificationManager.notify(2, notification);


    }
}