package com.example.recyclabletrashclassificationapp;

import android.content.SharedPreferences;

public class GlobalData {
    public static int myVariable = 0; // Default value


    public static void setMyVariable(int value) {
        myVariable = value;
    }

    public static int getMyVariable() {
        return myVariable;
    }
    public SharedPreferences pref ;
}
