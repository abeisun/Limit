package com.limitwhack.limit;

import io.realm.RealmObject;

public class User extends RealmObject {

    private Integer age;
    private Integer weight;
    private String gender;
    private String emergencyNumber;
    private double BAC;


    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmergencyNumber() {
        return emergencyNumber;
    }

    public void setEmergencyNumber(String emergencyNumber) {
        this.emergencyNumber = emergencyNumber;
    }

    public double getBAC() {
        return BAC;
    }

    public void setBAC(double BAC) {
        this.BAC = BAC;
    }
}
