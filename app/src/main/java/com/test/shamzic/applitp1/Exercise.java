package com.test.shamzic.applitp1;


public class Exercise {
    private String date;
    private String val;

    public Exercise () {} // Obligatoire pour Firebase

    public Exercise(String date, String val) {
        this.date = date;
        this.val = val;
    }
    public String getDate() { // Obligatoire pour Firebase
        return this.date;
    }

    public String getVal() { // Obligatoire pour Firebase
        return this.val;
    }
}
