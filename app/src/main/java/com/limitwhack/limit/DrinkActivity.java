package com.limitwhack.limit;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;

public class DrinkActivity extends AppCompatActivity {
    FloatingActionButton wineBtn;
    FloatingActionButton beerBtn;
    FloatingActionButton shotBtn;
    Button endSessionBtn;
    ImageView cupImage;
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


        wineBtn = findViewById(R.id.wineFab);
        beerBtn = findViewById(R.id.beerFab);
        shotBtn = findViewById(R.id.shotFab);
        addDrinkBtn = findViewById(R.id.incrementDrinkBtn);
        numDrinksTextView = findViewById(R.id.numDrinkTextView);
        cupImage = findViewById(R.id.cupImage);
//        seekBar = findViewById(R.id.mySeekBar);
//        endSessionBtn = findViewById(R.id.endSessionBtn);


        numDrinksTextView.setText(0 + " drinks");


        updateNumDrinks();

        createNewDrinkingSession();


        wineBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                int color = Color.rgb(88,11, 28);
                cupImage.setImageResource(R.drawable.wine);
                cupImage.setColorFilter(color);
            }
        });

        beerBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                int color = Color.rgb(100,0, 0);
                cupImage.setImageResource(R.drawable.solo_cup);
                cupImage.setColorFilter(color);
            }
        });

        shotBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                int color = Color.rgb(83,83, 83);
                cupImage.setImageResource(R.drawable.shot);
                cupImage.setColorFilter(color);
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
                    numDrinksTextView.setText(currentSession.getNumDrinks() + " drinks");
//                    Log.d("progress", seekBar.getProgress()+"");

                    User user = realm.where(User.class).findFirst();
                    realm.beginTransaction();
                    user.setBAC(Calculations.calculateBAC(user.getWeight(), user.getGender(), currentSession.getNumDrinks()));
                    realm.commitTransaction();
                    Log.d("bac", user.getBAC()+"");
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.drink_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {

            case R.id.action_end:
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

                Intent notificationIntent = new Intent("android.media.action.DISPLAY_NOTIFICATION");
                notificationIntent.addCategory("android.intent.category.DEFAULT");

                PendingIntent broadcast = PendingIntent.getBroadcast(this, 100, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.SECOND, 15);
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), broadcast);
                Intent intent = new Intent(DrinkActivity.this, MainActivity.class);

                startActivity(intent);
                finish();
                return true;

            case R.id.action_text:

                //user didn't ask for permission yet
                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.SEND_SMS)
                        != PackageManager.PERMISSION_GRANTED) {

                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SEND_SMS)) {
                    }

                    else {
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 0);
                    }
                    return false;
                }

                //user asked for permission & got it
                else {
                    SmsManager sms = SmsManager.getDefault();

                    Realm realm = Realm.getDefaultInstance();
                    User user = realm.where(User.class).findFirst();    //user info from database

                    sms.sendTextMessage(user.getEmergencyNumber(), null,
                                   "Hello world I'm drunk", null, null);
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        //if request is cancelled, the result arrays are empty
        //send sms if permission was granted
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            SmsManager sms = SmsManager.getDefault();

            Realm realm = Realm.getDefaultInstance();
            User user = realm.where(User.class).findFirst();    //user info from database

            sms.sendTextMessage(user.getEmergencyNumber(), null,
                           "Hello world I'm drunk", null, null);
        }

        return;
    }
}
