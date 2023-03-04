package com.example.notemanagement;

import android.app.Application;

import com.example.notemanagement.data.DataLocalManager;

public class MyApplication extends Application {
    @Override
    public void onCreate(){
        super.onCreate();
        DataLocalManager.init(getApplicationContext());
    }
}
