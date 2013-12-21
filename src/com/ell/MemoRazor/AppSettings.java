package com.ell.MemoRazor;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class AppSettings {
    public static final String KEY_STUDY_LANGUAGE = "study_language";
    public static final String KEY_FIRST_LANGUAGE = "first_language";
    public static final String KEY_OTHER_LANGUAGES = "other_languages";
    public static final String KEY_SIMPLIFIED_QUIZ = "simplified_quiz";
    public static final String KEY_MAX_QUIZ_QUESTIONS = "max_quiz_questions";
    public static final String KEY_NUM_CARDS = "num_cards";
    public static final String KEY_SHOW_TIP_MAINMENU = "show_tip_mainmenu";
    public static final String KEY_SHOW_TIP_GROUPS = "show_tip_groups";
    public static final String KEY_SHOW_TIP_GROUPSSELECTION = "show_tip_groups_selection";
    public static final String KEY_SHOW_TIP_WORDS = "show_tip_words";
    public static final String KEY_SHOW_TIP_QUIZ = "show_tip_quiz";
    public static final String KEY_SHOW_TIP_SIMPLEQUIZ = "show_tip_simple_quiz";
    public static final String KEY_SHOW_TIP_CARDS = "show_tip_cards";
    public static final String KEY_SHOW_TIP_EXPORT = "show_tip_export";
    public static final String KEY_SHOW_TIP_SETTINGS = "show_tip_settings";

    private static SharedPreferences sharedPreferences;

    static {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(App.getContext());
    }

    public static boolean getSettingsNotInitialized() {
        String firstLanguage = sharedPreferences.getString("first_language", null);
        return firstLanguage == null;
    }

    public static SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    public static String getDefaultLanguage() {
        return getStringSetting(KEY_STUDY_LANGUAGE, "en");
    }

    public static void setDefaultLanguage(String value) {
        setStringSetting(KEY_STUDY_LANGUAGE, value);
    }

    public static String getFirstLanguage() {
        return getStringSetting(KEY_FIRST_LANGUAGE, "ru");
    }

    public static void setFirstLanguage(String value) {
        setStringSetting(KEY_FIRST_LANGUAGE, value);
    }

    public static boolean getShowOtherLanguages() {
        return getBooleanSetting(KEY_OTHER_LANGUAGES, false);
    }

    public static void setShowOtherLanguages(Boolean value) {
        setBooleanSetting(KEY_OTHER_LANGUAGES, value);
    }

    public static boolean getSimplifiedQuiz() {
        return getBooleanSetting(KEY_SIMPLIFIED_QUIZ, false);
    }

    public static void setSimplifiedQuiz(Boolean value) {
        setBooleanSetting(KEY_SIMPLIFIED_QUIZ, value);
    }

    public static int getNumQuizQuestions() {
        return getIntSetting(KEY_MAX_QUIZ_QUESTIONS, 10);
    }

    public static void setNumQuizQuestions(int value) {
        setIntSetting(KEY_MAX_QUIZ_QUESTIONS, value);
    }

    public static int getNumCards() {
        return getIntSetting(KEY_NUM_CARDS, 20);
    }

    public static void setNumCards(int value) {
        setIntSetting(KEY_NUM_CARDS, value);
    }

    public static void suppressTips() {
        setBooleanSetting(KEY_SHOW_TIP_GROUPS, false);
        setBooleanSetting(KEY_SHOW_TIP_GROUPSSELECTION, false);
        setBooleanSetting(KEY_SHOW_TIP_WORDS, false);
        setBooleanSetting(KEY_SHOW_TIP_QUIZ, false);
        setBooleanSetting(KEY_SHOW_TIP_MAINMENU, false);
        setBooleanSetting(KEY_SHOW_TIP_SIMPLEQUIZ, false);
        setBooleanSetting(KEY_SHOW_TIP_CARDS, false);
        setBooleanSetting(KEY_SHOW_TIP_EXPORT, false);
        setBooleanSetting(KEY_SHOW_TIP_SETTINGS, false);
    }

    public static String getStringSetting(String settingName, String defaultValue) {
        return sharedPreferences.getString(settingName, defaultValue);
    }

    public static void setStringSetting(String settingName, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(settingName, value);
        editor.commit();
    }

    public static int getIntSetting(String settingName, int defaultValue) {
        String stringValue = sharedPreferences.getString(settingName, null);
        if (stringValue == null)
            return defaultValue;

        return Integer.valueOf(stringValue);
    }

    public static void setIntSetting(String settingName, int value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(settingName, String.valueOf(value));
        editor.commit();
    }

    public static Boolean getBooleanSetting(String settingName, boolean defaultValue) {
        return sharedPreferences.getBoolean(settingName, defaultValue);
    }

    public static void setBooleanSetting(String settingName, boolean value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(settingName, value);
        editor.commit();
    }
}
