package com.ell.MemoRazor;

import android.app.Application;
import android.content.Context;

public class App extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }

    public static Context getContext(){
        return context;
    }

    public static String getDefaultLanguage() {
        return "en";
    }

    public static String getNativeLanguage() {
        return "ru";
    }
}