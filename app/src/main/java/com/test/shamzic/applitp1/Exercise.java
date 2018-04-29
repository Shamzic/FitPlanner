package com.test.shamzic.applitp1;

import android.text.Editable;

import java.io.Serializable;

public class Exercise implements Serializable {
    private String date;
    private String val;
    private String name;
    private String nbRepetitions = "10";
    private String nbSeries = "3";

    public Exercise () {} // Obligatoire pour Firebase

    public Exercise(String name, String nbRepetitions, String  nbSeries, String date, String val) {
        this.name = name;
        this.nbRepetitions = nbRepetitions;
        this.nbSeries = nbSeries;
        this.date = date;
        this.val = val;
        this.MAJ();
    }

    public Exercise(String name, String date, String val) {
        this.name = name;
        this.date = date;
        this.val = val;
        this.MAJ();
    }

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

    public String getName() { // Obligatoire pour Firebase
        return this.name;
    }

    public String getNbRepetitions (){ return  this.nbRepetitions; }

    public String getNbSeries () {return  this.nbSeries; }

    public void setDate(int day, int month, int year) {
        date = "";
        if(day<10) {
            date +="0";
        }
        date+= day + "/";

        if((month +1)<10) { // je sais pas pourquoi, mais le mois est décallé de 1 .. ?
            date += "0";
        }

        date +=(month +1 ) + "/" + year;
    }

    public void setNbSeries (String nbSeries) {
        this.nbSeries = nbSeries;
    }

    public void setNbRepetitions (String nbRepetitions) {
        this.nbRepetitions = nbRepetitions;
    }

    public void MAJ() {
        val = name+" : "+nbSeries+" x "+nbRepetitions+" répétitions";
    }
}
