package com.newsapp.aavaaz.app;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;
import com.newsapp.aavaaz.LocationManager;

public class SampleApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        LocationManager.enableLog(true);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
