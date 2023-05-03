package com.example.aiassistance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    TextView screen_text;
    EditText Message_txt;
    ImageButton Send_btn;
    List<message> messageList;
    messageadaptor messageadapter;
    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");
    OkHttpClient client = new OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .build();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        messageList = new ArrayList<>();

        recyclerView = findViewById(R.id.rview);
        screen_text = findViewById(R.id.screen_text);
        Message_txt = findViewById(R.id.message_edittxt);
        Send_btn = findViewById(R.id.send_btn);

        messageadapter = new messageadaptor(messageList);
        recyclerView.setAdapter(messageadapter);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setStackFromEnd(true);
        recyclerView.setLayoutManager(llm);


        Send_btn.setOnClickListener((v)->{
            String Question = Message_txt.getText().toString().trim();
            addTochat(Question,message.SENT_BY_ME);
            Message_txt.setText("");
            callapi(Question);
            screen_text.setVisibility(View.GONE);

        });


    }
    void addTochat(String Message, String sentBy){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                messageList.add(new message(Message, sentBy));
                messageadapter.notifyDataSetChanged();
                recyclerView.smoothScrollToPosition(messageadapter.getItemCount());
            }
        });


    }

    void addresponse(String response){
        messageList.remove(messageList.size()-1);
        addTochat(response, message.SENT_BY_AI);
    }

    void callapi(String Question){

        messageList.add(new message("Typing...", message.SENT_BY_AI));
        JSONObject jsonbody = new JSONObject();
        try {
           jsonbody.put("model", "gpt-3.5-turbo");

           JSONArray messagearr = new JSONArray();
           JSONObject obj = new JSONObject();
           obj.put("role", "user");
           obj.put("content", Question);
           messagearr.put(obj);


           jsonbody.put("messages", messagearr);

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        RequestBody body = RequestBody.create(jsonbody.toString(),JSON);
        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/chat/completions")
                .header("Authorization", "Bearer use your own api key of turbo 3.5")
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

                addresponse("Failed the response due to"+e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                if(response.isSuccessful()){
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.body().string());
                        JSONArray jsonArray = jsonObject.getJSONArray("choices");
                        String result = jsonArray.getJSONObject(0)
                                        .getJSONObject("message")
                                                .getString("content");
                        addresponse(result.trim());
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }


                }else{
                    addresponse("Failed the response due to"+response.body().toString());

                }
            }
        });

    }

}