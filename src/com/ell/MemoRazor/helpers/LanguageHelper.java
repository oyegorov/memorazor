package com.ell.MemoRazor.helpers;

import android.content.Context;
import android.content.res.Resources;
import com.ell.MemoRazor.App;
import com.ell.MemoRazor.R;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;

public class LanguageHelper {
    private static HashMap<String, String> languageMapping;
    private static final String langEnglish = App.getContext().getResources().getString(R.string.lang_en);
    private static final String langGerman = App.getContext().getResources().getString(R.string.lang_de);
    private static final String langRussian = App.getContext().getResources().getString(R.string.lang_ru);
    private static final String langSpanish = App.getContext().getResources().getString(R.string.lang_es);

    private LanguageHelper() {
    }

    static {
        languageMapping = new HashMap<String, String>();
        languageMapping.put(langEnglish, "en");
        languageMapping.put(langGerman, "de");
        languageMapping.put(langRussian, "ru");
        languageMapping.put(langSpanish, "es");
    }

    public static String[] getSupportedLanguages() {
        return new String[] { langEnglish, langSpanish, langGerman, langRussian };
    }

    public static String[] getAlternativeLanguages() {
        ArrayList<String> alternativeLanguages = new ArrayList<String>();

        for (String lang : getSupportedLanguages()){
            if (!App.getDefaultLanguage().equalsIgnoreCase(getLanguageCode(lang))
                    && !App.getNativeLanguage().equalsIgnoreCase(getLanguageCode(lang))) {
                alternativeLanguages.add(lang);
            }
        }

        return alternativeLanguages.toArray(new String[0]);
    }

    public static String getLanguageCode(String languageName) {
        if (!languageMapping.containsKey(languageName)) {
            return null;
        }

        return languageMapping.get(languageName);
    }
}
