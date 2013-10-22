package com.ell.MemoRazor.translators;

import com.ell.MemoRazor.data.Word;
import com.ell.MemoRazor.helpers.JSONFetcher;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class YandexOpenJSONTranslator implements Translator {
    private static final String TRANSLATE_URL_TEMPLATE = "http://translate.yandex.net/dicservice.json/lookup?ui=&lang=en-ru&text=%s&flags=3";
    private static final String YANDEX_TRANSLATION_NOT_AVAILABLE = "(нет перевода)";
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
    public Word translateWord(Word word) {
        String url = String.format(TRANSLATE_URL_TEMPLATE, word.getName());
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
