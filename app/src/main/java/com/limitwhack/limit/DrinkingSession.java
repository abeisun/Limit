package com.limitwhack.limit;

import java.text.SimpleDateFormat;
import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;


public class DrinkingSession extends RealmObject {
    @PrimaryKey
    private String date;
    private Integer numDrinks = 0;

    public DrinkingSession() {
        String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        this.date = date;
        this.numDrinks = 0;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return this.date;
    }

    public int getNumDrinks() {
        return this.numDrinks;
    }

    public void increaseNumDrinks() {
        this.numDrinks++;
    }

    public void decreaseNumDrinks() {
        this.numDrinks--;
    }

    public void setNumDrinks(int numDrinks) {
        this.numDrinks = numDrinks;
    }
}
