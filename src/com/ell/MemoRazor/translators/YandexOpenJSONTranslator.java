package com.ell.MemoRazor.translators;

import android.content.res.Resources;
import com.ell.MemoRazor.App;
import com.ell.MemoRazor.R;
import com.ell.MemoRazor.data.Word;
import com.ell.MemoRazor.helpers.JSONFetcher;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class YandexOpenJSONTranslator implements Translator {
    private static final String TRANSLATE_URL_TEMPLATE = "http://translate.yandex.net/dicservice.json/lookup?ui=&lang=%s-%s&text=%s&flags=3";
    private static final String TRANSLATE_PHRASE_URL_TEMPLATE = "http://translate.yandex.net/api/v1/tr.json/translate?lang=%s-%s&text=%s&srv=tr-text";
    public static final String YANDEX_TRANSLATION_NOT_AVAILABLE = App.getContext().getResources().getString(R.string.no_translation);

    public static boolean isTranslatable(Word word, boolean force) {
        if (!force && YANDEX_TRANSLATION_NOT_AVAILABLE.equals(word.getMeaning()))  {
            return false;
        }
        if (word.getName() == null || word.getName().trim().length() == 0){
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

        JSONObject json = null;
        try {
            String url = String.format(TRANSLATE_URL_TEMPLATE,
                    sourceLang,
                    targetLang,
                    URLEncoder.encode(word.getName().toLowerCase(), "UTF-8"));
            json = (new JSONFetcher()).getJSONFromUrl(url);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (json == null) {
            return word;
        }

        StringBuilder translation = new StringBuilder();
        try {
            if (!json.has("defs")) {
                // try to find text translation
                JSONArray textTranslationArray = json.getJSONArray("text");
                if (textTranslationArray == null) {
                    setNoTranslation(word);
                } else {
                    word.setMeaning(textTranslationArray.getString(0));
                    word.setTranscription(null);
                }

                return word;
            }

            JSONArray defs = json.getJSONArray("def");
            JSONObject def = defs.getJSONObject(0);

            try {
                String transcription = def.getString("ts");
                if (!word.getFetchingTranslation()) {
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
            if (!word.getFetchingTranslation()) {
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
