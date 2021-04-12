package com.iamtanshu.chatappwebsocket;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter {

    private static final int TYPE_MESSAGE_SENT = 0;
    private static final int TYPE_MESSAGE_RECEIVED = 1;
    private static final int TYPE_IMAGE_SENT = 2;
    private static final int TYPE_IMAGE_RECEIVED = 3;

    private LayoutInflater inflater;
    private List<JSONObject> messages = new ArrayList<>();

    public MessageAdapter(LayoutInflater layoutInflater) {
        inflater = layoutInflater;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case TYPE_MESSAGE_SENT:
                view = inflater.inflate(R.layout.item_sent_message, parent, false);
                return new SentMessageHolder(view);
            case TYPE_MESSAGE_RECEIVED:
                view = inflater.inflate(R.layout.item_message_received, parent, false);
                return new ReceivedMessageHolder(view);
            case TYPE_IMAGE_SENT:
                view = inflater.inflate(R.layout.item_sent_image, parent, false);
                return new SentImageHolder(view);
            case TYPE_IMAGE_RECEIVED:
                view = inflater.inflate(R.layout.item_received_photo, parent, false);
                return new ReceivedImageHolder(view);

        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        JSONObject message = messages.get(position);
        try {
            if (message.getBoolean("isSent")) {

                if (message.has("message")) {
                    SentMessageHolder messageHolder = (SentMessageHolder) holder;
                    messageHolder.messageTxt.setText(message.getString("message"));
                } else {
                    SentImageHolder imageHolder = (SentImageHolder) holder;
                    Bitmap bitmap = gitBitmapFromString(message.getString("image"));
                    imageHolder.imageView.setImageBitmap(bitmap);
                }
            } else {
                if(message.has("message")){
                    ReceivedMessageHolder messageHolder = (ReceivedMessageHolder) holder;
                    messageHolder.nameTxt.setText(message.getString("name"));
                    messageHolder.messageTxt.setText(message.getString("message"));
                }else{
                    ReceivedImageHolder imageHolder = (ReceivedImageHolder) holder;
                    imageHolder .nameTxt.setText(message.getString("name"));
                    Bitmap bitmap = gitBitmapFromString(message.getString("image"));
                    imageHolder.imageView.setImageBitmap(bitmap);

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private Bitmap gitBitmapFromString(String image) {
        byte[] bytes = Base64.decode(image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    public void addItem(JSONObject jsonObject){
        messages.add(jsonObject);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }


    public class SentMessageHolder extends RecyclerView.ViewHolder {
        TextView messageTxt;

        public SentMessageHolder(@NonNull View itemView) {
            super(itemView);
            messageTxt = itemView.findViewById(R.id.sentText);
        }
    }

    public class SentImageHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public SentImageHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }

    public class ReceivedMessageHolder extends RecyclerView.ViewHolder {
        TextView nameTxt, messageTxt;

        public ReceivedMessageHolder(@NonNull View itemView) {
            super(itemView);
            messageTxt = itemView.findViewById(R.id.receivedText);
            nameTxt = itemView.findViewById(R.id.nameTxt);
        }
    }

    public class ReceivedImageHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView nameTxt;

        public ReceivedImageHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }

    @Override
    public int getItemViewType(int position) {
        JSONObject message = messages.get(position);
        try {
            if (message.getBoolean("isSent")) {
                return message.has("message") ? TYPE_MESSAGE_SENT : TYPE_IMAGE_SENT;
            } else {
                return message.has("message") ? TYPE_MESSAGE_RECEIVED : TYPE_IMAGE_RECEIVED;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
