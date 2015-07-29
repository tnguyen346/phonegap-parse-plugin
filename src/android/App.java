package com.medlei;

import android.app.Application;
import android.util.Log;

import org.apache.cordova.core.ParsePlugin;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ParsePlugin.initializeParseWithApplication(this);
    }

}
