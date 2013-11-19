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

    public static Boolean getSettingsNotInitialized() {
        String firstLanguage = sharedPreferences.getString("first_language", null);
        return firstLanguage == null;
    }

    public static String getDefaultLanguage() {
        return sharedPreferences.getString("study_language", "en");
    }

    public static void setDefaultLanguage(String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("study_language", value);
        editor.commit();
    }

    public static String getFirstLanguage() {
        return sharedPreferences.getString("first_language", "ru");
    }

    public static void setFirstLanguage(String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("first_language", value);
        editor.commit();
    }

    public static Boolean getShowOtherLanguages() {
        return sharedPreferences.getBoolean("other_languages", false);
    }

    public static void setShowOtherLanguages(Boolean value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("other_languages", value);
        editor.commit();
    }

    public static Boolean getSimplifiedQuiz() {
        return sharedPreferences.getBoolean("simplified_quiz", false);
    }

    public static void setSimplifiedQuiz(Boolean value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("simplified_quiz", value);
        editor.commit();
    }

    public static int getNumQuizQuestions() {
        return sharedPreferences.getInt("max_quiz_questions", 20);
    }

    public static void setNumQuizQuestions(int value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("max_quiz_questions", value);
        editor.commit();
    }

    public static int getNumCards() {
        return sharedPreferences.getInt("num_cards", 20);
    }

    public static void setNumCards(int value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("num_cards", value);
        editor.commit();
    }
}