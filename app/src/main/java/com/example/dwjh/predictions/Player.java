package com.example.dwjh.predictions;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.io.Serializable;

/**
 * Created by Dave on 24/08/2015.
 */
public class Player implements Serializable {
    String name;
    int	scfc;
    int sfc;
    int cpfc;
    int brfc;
    int score;
    int pic;

    public Player(String name){
        this.name = name;
    }

    public void setPredictions(int pred_scfc, int pred_sfc, int pred_cpfc, int pred_brfc){
        Log.i("Player", "setPredictions");
        scfc = pred_scfc;
        sfc = pred_sfc;
        cpfc = pred_cpfc;
        brfc = pred_brfc;
    }

    public String getName() {return name;}

    public int getScfc(){
        return scfc;
    }

    public int getSfc(){
        return sfc;
    }

    public int getCpfc(){
        return cpfc;
    }

    public int getBrfc(){
        return brfc;
    }

    public void setName(String Name) { name = Name;};

    public void setScore(int points){
        score = points;
    }

    public int getScore(){
        return score;
    }

    public void setPic(int picture){
        pic = picture;
    }

    public int getPic(){
        return pic;
    }

}



