package com.iamtanshu.chatappwebsocket.Notify;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.RemoteInput;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.iamtanshu.chatappwebsocket.R;

import java.util.ArrayList;
import java.util.List;

import static com.iamtanshu.chatappwebsocket.Notify.App.CHANNEL_1_ID;
import static com.iamtanshu.chatappwebsocket.Notify.App.CHANNEL_2_ID;

public class NotifyActivity extends AppCompatActivity {

    private NotificationManagerCompat notificationManager;
    private EditText editTextTitle;
    private EditText editTextMessage;

    static List<Message> MESSAGES = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify);

        notificationManager = NotificationManagerCompat.from(this);

        editTextTitle = findViewById(R.id.edt_title);
        editTextMessage = findViewById(R.id.edt_message);
        MESSAGES.add(new Message("Good Morming","Sheshank"));
        MESSAGES.add(new Message("Hello","Me"));
        MESSAGES.add(new Message("How are you?","Sheshank"));
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT_WATCH)
    public void sendOnChannel1(View v){
sendChannel1Notification(this);
    }

    public static void sendChannel1Notification(Context context) {

        Intent activityIntent = new Intent(context, NotifyActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context,
                0, activityIntent, 0);


        RemoteInput remoteInput = new RemoteInput.Builder("key_text_reply")
                .setLabel("Your answer...")
                .build();
        Intent replayIntent = new Intent(context, DirectReplyReciver.class);
        PendingIntent replayPendingIntent = PendingIntent.getBroadcast(context,
                0, replayIntent, 0);
        NotificationCompat.Action replyAction = new NotificationCompat.Action.Builder(
                R.drawable.ic_reply,
                "Reply",
                replayPendingIntent
        ).addRemoteInput(remoteInput).build();
        NotificationCompat.MessagingStyle messagingStyle =
                new NotificationCompat.MessagingStyle(
                        "Me"
                );
        messagingStyle.setConversationTitle("Group chat");

        for(Message chatMessage: MESSAGES){
            NotificationCompat.MessagingStyle.Message notificationMessage =
                    new NotificationCompat.MessagingStyle.Message(
                            chatMessage.getText(),
                            chatMessage.getTimestamp(),
                            chatMessage.getSender()
                    );
            messagingStyle.addMessage(notificationMessage);
        }

        Notification notification = new NotificationCompat.Builder(context, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_one)
                .setStyle(messagingStyle)
                .addAction(replyAction)
                .setColor(Color.BLUE)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setContentIntent(contentIntent)
                .setAutoCancel(true)
                .setOnlyAlertOnce(true)
                .build();
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
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