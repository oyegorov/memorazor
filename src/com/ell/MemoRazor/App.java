package com.ell.MemoRazor;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import java.util.Locale;

public class App extends Application {
    public static final String LOCALE_UA = "uk_UA";
    public static final String LOCALE_BY = "be_BY";
    public static final String LOCALE_RU = "ru_RU";

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;

        adjustLocale();
    }

    public static Context getContext(){
        return context;
    }

    public static String getVersionName () {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            return "-1";
        }
    }

    public static int getVersionCode() {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            return -1;
        }
    }

    private void adjustLocale() {
        Resources resources = context.getResources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        Configuration config = resources.getConfiguration();

        String language = config.locale.toString();
        if (language.equals(LOCALE_UA) || language.equals(LOCALE_BY)) {
            config.locale = new Locale(LOCALE_RU);
            resources.updateConfiguration(config, displayMetrics);
        }
    }
}