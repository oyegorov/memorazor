package com.ell.MemoRazor;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class App extends Application {
    private static Context context;
    private static SharedPreferences sharedPreferences;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
    }

    public static Context getContext(){
        return context;
    }

    public static SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    public static Boolean getSettingsNotInitialized() {
        String firstLanguage = sharedPreferences.getString("first_language", null);
        return firstLanguage == null;
    }

    public static String getDefaultLanguage() {
        return getStringSetting("study_language", "en");
    }

    public static void setDefaultLanguage(String value) {
        setStringSetting("study_language", value);
    }

    public static String getFirstLanguage() {
        return getStringSetting("first_language", "ru");
    }

    public static void setFirstLanguage(String value) {
        setStringSetting("first_language", value);
    }

    public static Boolean getShowOtherLanguages() {
        return getBooleanSetting("other_languages", false);
    }

    public static void setShowOtherLanguages(Boolean value) {
        setBooleanSetting("other_languages", value);
    }

    public static Boolean getSimplifiedQuiz() {
        return getBooleanSetting("simplified_quiz", false);
    }

    public static void setSimplifiedQuiz(Boolean value) {
        setBooleanSetting("simplified_quiz", value);
    }

    public static int getNumQuizQuestions() {
        return getIntSetting("max_quiz_questions", 10);
    }

    public static void setNumQuizQuestions(int value) {
        setIntSetting("max_quiz_questions", value);
    }

    public static int getNumCards() {
        return getIntSetting("num_cards", 20);
    }

    public static void setNumCards(int value) {
        setIntSetting("num_cards", value);
    }

    private static String getStringSetting(String settingName, String defaultValue) {
        return sharedPreferences.getString(settingName, defaultValue);
    }

    private static void setStringSetting(String settingName, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(settingName, value);
        editor.commit();
    }

    private static int getIntSetting(String settingName, int defaultValue) {
        String stringValue = sharedPreferences.getString(settingName, null);
        if (stringValue == null)
            return defaultValue;

        return Integer.valueOf(stringValue);
    }

    private static void setIntSetting(String settingName, int value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(settingName, String.valueOf(value));
        editor.commit();
    }

    private static Boolean getBooleanSetting(String settingName, Boolean defaultValue) {
        return sharedPreferences.getBoolean(settingName, defaultValue);
    }

    private static void setBooleanSetting(String settingName, Boolean value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(settingName, value);
        editor.commit();
    }
}