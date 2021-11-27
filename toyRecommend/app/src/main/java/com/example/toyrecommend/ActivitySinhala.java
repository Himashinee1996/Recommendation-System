package com.example.toyrecommend;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import java.util.ArrayList;
import static com.example.toyrecommend.Constant.URL;

public class ActivitySinhala extends AppCompatActivity {

    Button sendButton1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sinhala);

        ArrayList<String> bot = new ArrayList<>();
        bot.add("Say hello!!!");

        ArrayList<String> user = new ArrayList<>();
        user.add("");

        ListView list;

        MyListAdapter adapter = new MyListAdapter(this, bot, user);
        list = (ListView) findViewById(R.id.list1);
        list.setAdapter(adapter);

        sendButton1 = (Button) findViewById(R.id.sendButton1);
        sendButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = (EditText) findViewById(R.id.message1);
                String message = editText.getText().toString();
                editText.setText("");
                editText.setHint("Type a message");

                String url = URL;
                url = url + "chat?message=" + message + "&lang=si";

                RequestQueue requestQueue = Volley.newRequestQueue(com.example.toyrecommend.ActivitySinhala.this);
                System.out.println("Request: " + message);

                StringRequest myReq = new StringRequest(Request.Method.GET,
                        url,
                        response -> {
                            System.out.println("Response: " + response);

                            user.add(message);
                            user.add("");
                            bot.add("");
                            bot.add(response);

                            ListView list1 = (ListView) findViewById(R.id.list);
                            MyListAdapter adapter = new MyListAdapter(com.example.toyrecommend.ActivitySinhala.this, bot, user);
                            list1 = (ListView) findViewById(R.id.list1);
                            list1.setAdapter(adapter);
                        },
                        error -> {
                            System.out.println("Error: " + error);
                        });
                myReq.setRetryPolicy(new RetryPolicy() {
                    @Override
                    public int getCurrentTimeout() {
                        return 50000;
                    }

                    @Override
                    public int getCurrentRetryCount() {
                        return 50000;
                    }

                    @Override
                    public void retry(VolleyError error) throws VolleyError {

                    }
                });
                requestQueue.add(myReq);
            }
        });
    }
}