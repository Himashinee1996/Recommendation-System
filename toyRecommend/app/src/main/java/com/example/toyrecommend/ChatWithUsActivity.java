package com.example.toyrecommend;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ChatWithUsActivity extends AppCompatActivity {

    private Button englishButton;
    private Button sinhalaButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_with_us);


        englishButton = (Button) findViewById(R.id.englishButton);
        englishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChatWithUsActivity.this, EnglishActivity.class);
                intent.putExtra("role", "no");
                startActivity(intent);
            }
        });

        sinhalaButton = (Button) findViewById(R.id.sinhalaButton);
        sinhalaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChatWithUsActivity.this, ActivitySinhala.class);
                intent.putExtra("role", "no");
                startActivity(intent);
            }
        });
    }
}