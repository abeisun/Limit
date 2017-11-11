package com.limitwhack.limit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.github.clans.fab.FloatingActionButton;

public class DrinkActivity extends AppCompatActivity {
    FloatingActionButton submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Drink");
        setContentView(R.layout.activity_drink);

        submit = findViewById(R.id.fabDone);

        submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(DrinkActivity.this, FeedbackActivity.class);
                startActivity(intent);
            }
        });
    }
}
