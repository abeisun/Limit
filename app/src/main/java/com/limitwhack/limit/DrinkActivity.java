package com.limitwhack.limit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class DrinkActivity extends AppCompatActivity {
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Drink");
        setContentView(R.layout.activity_drink);

        submit = findViewById(R.id.goToFeedback);

        submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(DrinkActivity.this, FeedbackActivity.class);
                startActivity(intent);
            }
        });
    }
}
