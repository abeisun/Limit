package com.limitwhack.limit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LineChart chart = (LineChart) findViewById(R.id.chart);
        List<Entry> entries = new ArrayList<Entry>();

        //data of previous drinking sessions
        Realm realm = Realm.getDefaultInstance();
        RealmResults<DrinkingSession> sessionsList = realm.where(DrinkingSession.class).findAll();

        //fill list with coordinates (day, num drinks)
        for (int i = 0; i < sessionsList.size(); i++) {
            Date date = sessionsList.get(i).getFormattedDate();
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            float day = cal.get(Calendar.DAY_OF_MONTH); //get day of month as float

            entries.add(new Entry(day, sessionsList.get(i).getNumDrinks()));
        }

        //add entries to graph
        if (sessionsList.size() > 0) {
            LineDataSet dataSet = new LineDataSet(entries, "Label");
            LineData lineData = new LineData(dataSet);
            chart.setData(lineData);
            chart.invalidate(); //refresh
            chart.getAxisRight().setDrawLabels(false);
        }

        else
            chart.setNoDataText("No previous drinking sessions!");

        //start new session button
        submit = findViewById(R.id.goToDrink);
        submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, DrinkActivity.class);
                startActivity(intent);
            }
        });
    }
}
