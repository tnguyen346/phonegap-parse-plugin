package com.medlei;

import android.app.Application;
import android.util.Log;

import com.medlei.ParsePlugin;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ParsePlugin.initializeParseWithApplication(this);
    }

}
