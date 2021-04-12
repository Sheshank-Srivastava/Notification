package com.iamtanshu.chatappwebsocket;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class ChatActivity extends AppCompatActivity implements TextWatcher {

    private static final int IMAGE_REQUEST_ID = 1;
    private String name;
    private WebSocket webSocket;
    private String SERVER_PATH = "ws://echo.websocket.org";
    private EditText messageEdit;
    private View sendBtn, pickImage;
    private RecyclerView recyclerView;
    private MessageAdapter adapter;
    public static final String TAG ="ChatActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        name = getIntent().getStringExtra("name");

        initiateSocketConnection();
    }

    private void initiateSocketConnection() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(SERVER_PATH).build();
        webSocket = client.newWebSocket(request, new SocketListener());
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        Log.i(TAG, "afterTextChanged: ");
        String string = s.toString().trim();
        if (string.isEmpty()){
            resetMessageEdit();
        }else{
            sendBtn.setVisibility(View.VISIBLE);
            pickImage.setVisibility(View.INVISIBLE);
        }

    }

    private void resetMessageEdit() {

        messageEdit.removeTextChangedListener(this);
        messageEdit.setText("");
        sendBtn.setVisibility(View.INVISIBLE);
        pickImage.setVisibility(View.VISIBLE);

        messageEdit.addTextChangedListener(this);
    }

    private class SocketListener extends WebSocketListener {
        @Override
        public void onMessage(@NotNull WebSocket webSocket, @NotNull String text) {
            super.onMessage(webSocket, text);

            Log.i(TAG, "onMessage: Message receive");
            runOnUiThread(()->{
                try {
                    JSONObject jsonObject = new JSONObject(text);
                     jsonObject.put("isSent", false);
                     adapter.addItem(jsonObject);
                }catch (JSONException e){
                    e.printStackTrace();
                }
            });
        }

        @Override
        public void onOpen(@NotNull WebSocket webSocket, @NotNull Response response) {
            super.onOpen(webSocket, response);
            runOnUiThread(() -> {
                Toast.makeText(ChatActivity.this, "Socket Connection Successful", Toast.LENGTH_SHORT).show();
                initializeView();
            });
        }
    }

    private void initializeView() {
        messageEdit = findViewById(R.id.messageEdit);
        sendBtn = findViewById(R.id.sendButton);
        pickImage = findViewById(R.id.pickImgBtn);
        recyclerView = findViewById(R.id.recyclerView);

        adapter = new MessageAdapter(getLayoutInflater());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        messageEdit.addTextChangedListener(this);

        sendBtn.setOnClickListener(v->{
            JSONObject jsonObject = new JSONObject();
            try{
                jsonObject.put("name",name);
                jsonObject.put("message",messageEdit.getText().toString());
                webSocket.send(jsonObject.toString());
                jsonObject.put("isSent",true);
                adapter.addItem(jsonObject);

                resetMessageEdit();

            }catch (JSONException e){
                e.printStackTrace();
            }
        });
        pickImage.setOnClickListener(v->{
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent,"Pick Image"),IMAGE_REQUEST_ID);
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, "onActivityResult: "+data.getDataString());
        if(requestCode== IMAGE_REQUEST_ID &&resultCode==RESULT_OK){
            try {
                InputStream inputStream = getContentResolver().openInputStream(data.getData());
                Bitmap image = BitmapFactory.decodeStream(inputStream);
                Log.i(TAG, "onActivityResult: "+data.getDataString());

                semdImage(image);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }else{
            Log.i(TAG, "onActivityResult: ");
        }
    }

    private void semdImage(Bitmap image) {
        Log.i(TAG, "semdImage: "+image.toString());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress( Bitmap.CompressFormat.JPEG,50,baos);
        String base64String = Base64.encodeToString(baos.toByteArray(),Base64.DEFAULT);
        Log.i(TAG, "semdImage: ");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name",name);
            jsonObject.put("image",base64String);
            webSocket.send(jsonObject.toString());
            jsonObject.put("isSent",true);
            adapter.addItem(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}