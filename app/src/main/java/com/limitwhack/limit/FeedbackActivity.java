package com.limitwhack.limit;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.github.clans.fab.FloatingActionButton;

public class FeedbackActivity extends AppCompatActivity {

    ImageView happy;
    ImageView meh;
    ImageView sad;
    ImageView ded;
    //@SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Feedback");
        setContentView(R.layout.activity_feedback);

        happy = findViewById(R.id.happy);
        meh = findViewById(R.id.meh);
        sad = findViewById(R.id.sadness);
        ded = findViewById(R.id.hungover);

        happy.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            }
        });
        meh.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            }
        });
        sad.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            }
        });
        ded.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            }
        });
    }
}
