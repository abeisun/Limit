package com.limitwhack.limit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

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

        Realm realm = Realm.getDefaultInstance();   //database

        /*
        //make fake objs
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                for (int i = 0; i < 10; i++) {
                    Date date = new Date();
                    Calendar cal = Calendar.getInstance();
                    cal.setTime ( date ); // convert your date to Calendar object
                    int daysToDecrement = -1 * i;
                    cal.add(Calendar.DATE, daysToDecrement);
                    date = cal.getTime(); // again get back your date object
                    String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(date);

                    //String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                    DrinkingSession currentSession = bgRealm.createObject(DrinkingSession.class, currentDate);
                    int randDrinks = ThreadLocalRandom.current().nextInt(0, 11);
                    currentSession.setNumDrinks(randDrinks);
                    currentSession.setFormattedDate(date);
                }
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Log.d("creating new session: ", "success" );
            }
        });
        */



        GraphView graph = (GraphView) findViewById(R.id.graph); //make graph

        //list of previous drinking sessions
        RealmResults<DrinkingSession> sessionsList = realm.where(DrinkingSession.class).findAll();

        DataPoint[] coordsList = new DataPoint[sessionsList.size()];    //list of coordinates

        //fill list with coordinates
        for (int i = 0; i < sessionsList.size(); i++) {
            DataPoint point = new DataPoint(sessionsList.get(i).getFormattedDate().getTime(),
                                            sessionsList.get(i).getNumDrinks());
            coordsList[i] = point;
            Log.d("mypoint", sessionsList.get(i).getDate());
        }

        long minDate = Long.MAX_VALUE, maxDate = Long.MIN_VALUE;

        for (int i = 0; i < sessionsList.size(); i++) {
            if (sessionsList.get(i).getFormattedDate().getTime() < minDate)
                minDate = sessionsList.get(i).getFormattedDate().getTime();

            if (sessionsList.get(i).getFormattedDate().getTime() > maxDate)
                maxDate = sessionsList.get(i).getFormattedDate().getTime();

            //Log.d("mylist", coordsList[i].getX() + " " + coordsList[i].getY());
        }

        //add coordinates to graph
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(coordsList);
        graph.addSeries(series);

        //set date label formatter
        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(this));
        graph.getGridLabelRenderer().setNumHorizontalLabels(3); // only 4 because of the space

        //manual x bounds
        graph.getViewport().setMinX(minDate);
        graph.getViewport().setMaxX(maxDate);
        graph.getViewport().setXAxisBoundsManual(true);

        //manual y bounds
//        graph.getViewport().setMinY(0);
//        graph.getViewport().setYAxisBoundsManual(true);

        // as we use dates as labels, the human rounding to nice readable numbers is not necessary
        //graph.getGridLabelRenderer().setHumanRounding(false);


        submit = findViewById(R.id.goToDrink);
        submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, DrinkActivity.class);
                startActivity(intent);
            }
        });
    }
}
