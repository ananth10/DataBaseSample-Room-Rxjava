package com.ananth.databasesample.application;

import android.app.Application;
import android.content.Context;

import com.ananth.databasesample.di.AppComponent;
import com.ananth.databasesample.di.DaggerAppComponent;

/**
 * Created by Babu on 10/12/2017.
 */

public class MyApplication extends Application {
    private AppComponent component;

    @Override
    public void onCreate() {
        super.onCreate();

        component= DaggerAppComponent.builder()
                .build();
    }
    public static AppComponent getComponent(Context context) {
        return ((MyApplication)context.getApplicationContext()).component;
    }
}
