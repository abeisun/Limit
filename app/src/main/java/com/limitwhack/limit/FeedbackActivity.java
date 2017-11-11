package com.limitwhack.limit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import javax.xml.bind.annotation.*;

import com.github.clans.fab.FloatingActionButton;

public class FeedbackActivity extends AppCompatActivity {

    FloatingActionButton happy;
    FloatingActionButton meh;
    FloatingActionButton sad;
    FloatingActionButton ded;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        setTitle("Feedback");


        //Drawable hungover = getResources().getDrawable


    }
}
