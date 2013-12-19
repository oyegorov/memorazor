package com.ell.MemoRazor.translators;

import com.ell.MemoRazor.data.Word;
import com.ell.MemoRazor.helpers.LanguageHelper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

public class YandexPlaybackFetcher implements WordPlaybackFetcher {
    private static final String AUDIO_SOURCE_URL_TEMPLATE = "http://tts.voicetech.yandex.net/tts?format=mp3&quality=hi&platform=web&application=translate&text=%s&lang=%s";
    static InputStream is = null;

    @Override
    public byte[] getWordPlayback(Word word) {
        // Making HTTP request
        try {
            // defaultHttpClient
            DefaultHttpClient httpClient = new DefaultHttpClient();

            String url = String.format(AUDIO_SOURCE_URL_TEMPLATE, word.getName(), LanguageHelper.langCodeToAudioCode(word.getLanguage()));
            HttpGet httpGet = new HttpGet(url);

            httpGet.addHeader("Accept", "*/*");
            httpGet.addHeader("User-Agent", "Mozilla/5.0");
            httpGet.addHeader("Accept-Encoding", "gzip, deflate");
            httpGet.addHeader("Referer", "http://translate.yandex.ru/v1.60/swf/player.swf");

            HttpResponse httpResponse = httpClient.execute(httpGet);

            HttpEntity httpEntity = httpResponse.getEntity();

            ByteArrayOutputStream resultStream = new ByteArrayOutputStream();
            if (httpEntity.getContentType() != null && "audio/mpeg".equals(httpEntity.getContentType().getValue()) &&
                    httpEntity.getContentEncoding() != null && "gzip".equals(httpEntity.getContentEncoding().getValue())) {

                is = httpEntity.getContent();

                GZIPInputStream gzipInputStream = new GZIPInputStream(is);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                try
                {
                    byte[] buffer = new byte[100];

                    int bytesRead;
                    while ((bytesRead = gzipInputStream.read(buffer)) != -1) {
                        baos.write(buffer, 0, bytesRead);
                    }

                    return baos.toByteArray();
                }
                finally {
                    baos.close();
                    gzipInputStream.close();
                    is.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return null;
    }
}
