package com.limitwhack.limit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Date;

import io.realm.Realm;

public class DrinkActivity extends AppCompatActivity {
    FloatingActionButton endDrinkingSessionBtn;
    FloatingActionButton changeCupTypeBtn;
    FloatingActionButton changeDrinkTypeBtn;
    SeekBar seekBar;
    TextView numDrinksTextView;
    Button addDrinkBtn;
    Realm realm;
    String currentDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Drink");
        setContentView(R.layout.activity_drink);
        currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());


        //get database instance
        realm = Realm.getDefaultInstance();


        endDrinkingSessionBtn = findViewById(R.id.fabDone);
        changeCupTypeBtn = findViewById(R.id.fabCup);
        changeDrinkTypeBtn = findViewById(R.id.fabDrink);
        addDrinkBtn = findViewById(R.id.incrementDrinkBtn);
        numDrinksTextView = findViewById(R.id.numDrinkTextView);
        seekBar = findViewById(R.id.mySeekBar);

        numDrinksTextView.setText("" + 0);


        updateNumDrinks();

        createNewDrinkingSession();

        endDrinkingSessionBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(DrinkActivity.this, FeedbackActivity.class);
                startActivity(intent);
            }
        });

        addDrinkBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                realm.executeTransactionAsync(new Realm.Transaction() {
                    @Override
                    public void execute(Realm bgRealm) {
                        String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                        DrinkingSession currentSession = bgRealm.where(DrinkingSession.class).equalTo("date", currentDate).findFirst();
                        currentSession.increaseNumDrinks();
                        updateNumDrinks();
                    }
                }, new Realm.Transaction.OnSuccess() {
                    @Override
                    public void onSuccess() {
                        Log.d("updates num drinks: ", "success");
                    }
                }, new Realm.Transaction.OnError() {
                    @Override
                    public void onError(Throwable error) {
                        Log.d("doesn't drinks: ", error.toString());

                    }
                });

            }
        });
    }

    public void updateNumDrinks() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Realm realm = Realm.getDefaultInstance();

                DrinkingSession currentSession = realm.where(DrinkingSession.class).equalTo("date", currentDate).findFirst();

                if(currentSession != null) {
                    numDrinksTextView.setText("" + currentSession.getNumDrinks());
                    Log.d("progress", seekBar.getProgress()+"");
                }
            }
        });
    }


    public void createNewDrinkingSession() {
        DrinkingSession currentSession = realm.where(DrinkingSession.class).equalTo("date", currentDate).findFirst();
        if(currentSession != null) {
            updateNumDrinks();
            return;
        }
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                DrinkingSession currentSession = bgRealm.createObject(DrinkingSession.class, currentDate);
                currentSession.setNumDrinks(0);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Log.d("creating new session: ", "sucess" );
            }
        });

    }
}
