package com.iamtanshu.chatappwebsocket.Notify;

import android.app.RemoteInput;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

public class DirectReplyReciver extends BroadcastReceiver {
    @RequiresApi(api = Build.VERSION_CODES.KITKAT_WATCH)
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle remoteInput = RemoteInput.getResultsFromIntent(intent);
        if(remoteInput!=null){
            CharSequence replyText = remoteInput.getCharSequence("key_text_reply");
            Message answer = new Message(replyText,null);
            NotifyActivity.MESSAGES.add(
                    answer
            );
            NotifyActivity.sendChannel1Notification(context);
        }
    }
}
