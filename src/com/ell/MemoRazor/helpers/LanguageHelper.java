package com.ell.MemoRazor.helpers;

import com.ell.MemoRazor.App;
import com.ell.MemoRazor.AppSettings;
import com.ell.MemoRazor.R;

import java.util.ArrayList;
import java.util.HashMap;

public class LanguageHelper {
    private static HashMap<String, String> languageNameMapping;
    private static HashMap<String, Integer> languageResourceMapping;
    private static HashMap<String, String> languageAudioCodeMapping;
    private static final String langEnglish = App.getContext().getResources().getString(R.string.lang_en);
    private static final String langGerman = App.getContext().getResources().getString(R.string.lang_de);
    private static final String langRussian = App.getContext().getResources().getString(R.string.lang_ru);
    private static final String langSpanish = App.getContext().getResources().getString(R.string.lang_es);
    private static final String langItalian = App.getContext().getResources().getString(R.string.lang_it);
    private static final String langPolish = App.getContext().getResources().getString(R.string.lang_pl);
    private static final String langFrench = App.getContext().getResources().getString(R.string.lang_fr);
    private static final String langDutch = App.getContext().getResources().getString(R.string.lang_nl);

    private LanguageHelper() {
    }

    static {
        languageNameMapping = new HashMap<String, String>();
        languageNameMapping.put(langEnglish, "en");
        languageNameMapping.put(langGerman, "de");
        languageNameMapping.put(langRussian, "ru");
        languageNameMapping.put(langSpanish, "es");
        languageNameMapping.put(langPolish, "pl");
        languageNameMapping.put(langItalian, "it");
        languageNameMapping.put(langFrench, "fr");
        languageNameMapping.put(langDutch, "fr");

        languageResourceMapping = new HashMap<String, Integer>();
        languageResourceMapping.put("en", R.drawable.en);
        languageResourceMapping.put("de", R.drawable.de);
        languageResourceMapping.put("ru", R.drawable.ru);
        languageResourceMapping.put("es", R.drawable.es);
        languageResourceMapping.put("pl", R.drawable.pl);
        languageResourceMapping.put("it", R.drawable.it);
        languageResourceMapping.put("fr", R.drawable.fr);
        languageResourceMapping.put("nl", R.drawable.nl);

        languageAudioCodeMapping = new HashMap<String, String>();
        languageAudioCodeMapping.put("en", "en_GB");
        languageAudioCodeMapping.put("de", "de_DE");
        languageAudioCodeMapping.put("ru", "ru_RU");
        languageAudioCodeMapping.put("es", "es_ES");
        languageAudioCodeMapping.put("pl", "pl_PL");
        languageAudioCodeMapping.put("it", "it_IT");
        languageAudioCodeMapping.put("fr", "fr_FR");
        languageAudioCodeMapping.put("nl", "nl_NL");
    }

    public static String[] getSupportedLanguages() {
        return new String[] { langEnglish, langRussian, langSpanish, langGerman, langFrench, langItalian, langPolish  };
    }

    public static String[] getAlternativeLanguages() {
        ArrayList<String> alternativeLanguages = new ArrayList<String>();

        for (String lang : getSupportedLanguages()){
            if (!AppSettings.getDefaultLanguage().equalsIgnoreCase(getLanguageCode(lang))
                    && !AppSettings.getFirstLanguage().equalsIgnoreCase(getLanguageCode(lang))) {
                alternativeLanguages.add(lang);
            }
        }

        return alternativeLanguages.toArray(new String[0]);
    }

    public static String getLanguageCode(String languageName) {
        if (!languageNameMapping.containsKey(languageName)) {
            return null;
        }

        return languageNameMapping.get(languageName);
    }

    public static int langCodeToImage (String langCode) {
        if (!languageResourceMapping.containsKey(langCode)) {
            return -1;
        }

        return languageResourceMapping.get(langCode);
    }

    public static String langCodeToAudioCode (String langCode) {
        if (!languageAudioCodeMapping.containsKey(langCode)) {
            return null;
        }

        return languageAudioCodeMapping.get(langCode);
    }
}
