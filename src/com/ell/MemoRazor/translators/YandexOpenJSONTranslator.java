package com.ell.MemoRazor.translators;

import android.content.res.Resources;
import com.ell.MemoRazor.App;
import com.ell.MemoRazor.R;
import com.ell.MemoRazor.data.Word;
import com.ell.MemoRazor.helpers.JSONFetcher;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class YandexOpenJSONTranslator implements Translator {
    public final static String TRANSLATION_IN_PROGRESS = App.getContext().getResources().getString(R.string.translation_expected);
    private static final String TRANSLATE_URL_TEMPLATE = "http://translate.yandex.net/dicservice.json/lookup?ui=&lang=%s-%s&text=%s&flags=3";
    public static final String YANDEX_TRANSLATION_NOT_AVAILABLE = App.getContext().getResources().getString(R.string.no_translation);
    private static final Pattern pattern = Pattern.compile("[a-zA-Z]+");

    public static boolean isTranslatable(Word word, boolean force) {
        if (!force && YANDEX_TRANSLATION_NOT_AVAILABLE.equals(word.getMeaning()))  {
            return false;
        }

        Matcher matcher = pattern.matcher(word.getName());
        if (!matcher.matches()) {
            return false;
        }

        return true;
    }

    @Override
    public Word translateWord(Word word, String sourceLang, String targetLang) {
        if (word == null)
            throw new IllegalArgumentException("word cannot be null");
        if (sourceLang == null)
            throw new IllegalArgumentException("sourceLang cannot be null");
        if (targetLang == null)
            throw new IllegalArgumentException("targetLang cannot be null");

        String url = String.format(TRANSLATE_URL_TEMPLATE, sourceLang, targetLang, word.getName());
        JSONObject json = (new JSONFetcher()).getJSONFromUrl(url);
        if (json == null) {
            return word;
        }

        StringBuilder translation = new StringBuilder();
        try {
            JSONArray defs = json.getJSONArray("def");
            if (defs.length() == 0) {
                setNoTranslation(word);
                return word;
            }

            JSONObject def = defs.getJSONObject(0);

            try {
                String transcription = def.getString("ts");
                if (!word.getMeaning().equals(TRANSLATION_IN_PROGRESS)) {
                    word.setTranscription(null);
                    return word;
                }

                word.setTranscription(transcription);
            }
            catch (JSONException e) {
                e.printStackTrace();
            }

            JSONArray translationsJson = def.getJSONArray("tr");
            for (int i=0; i < translationsJson.length(); i++) {
                JSONObject translationJson = translationsJson.getJSONObject(i);

                translation.append(translationJson.get("text"));

                if (i != translationsJson.length() - 1)
                    translation.append("; ");
            }
            if (!word.getMeaning().equals(TRANSLATION_IN_PROGRESS)) {
                word.setTranscription(null);
                return word;
            }

            word.setMeaning(translation.toString());
        } catch (JSONException e) {
            setNoTranslation(word);
            e.printStackTrace();
        }
        return word;
    }

    protected void setNoTranslation (Word word) {
        word.setMeaning(YANDEX_TRANSLATION_NOT_AVAILABLE);
    }
}
