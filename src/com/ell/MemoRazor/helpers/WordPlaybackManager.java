package com.ell.MemoRazor.helpers;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import com.ell.MemoRazor.App;
import com.ell.MemoRazor.data.DatabaseHelper;
import com.ell.MemoRazor.data.Word;
import com.ell.MemoRazor.translators.YandexOpenJSONTranslator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Random;

public class WordPlaybackManager {
    private final String AUDIO_SOURCE_URL_TEMPLATE = "http://tts.voicetech.yandex.net/tts?format=mp3&quality=hi&platform=web&application=translate&text=%s&lang=en_GB";
    private DatabaseHelper databaseHelper;
    private Activity activity;
    private Boolean isFetching;

    public WordPlaybackManager(DatabaseHelper databaseHelper, Activity activity) {
        this.databaseHelper = databaseHelper;
        this.activity = activity;
        isFetching = false;
    }

    public void PlayWord (Word word) {
        byte[] cachedPlayback = null;
        try {
            cachedPlayback = databaseHelper.getWordPlaybackCache(word);
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        if (cachedPlayback != null) {
            playMp3(cachedPlayback);
            return;
        }

        if (isFetching)
            return;

        isFetching = true;

        new AsyncTask<Word, Void, byte[]>() {
            @Override
            protected void onPreExecute() {
            }

            @Override
            protected byte[] doInBackground(Word... words) {
                Word selectedWord = words[0];

                WordPlaybackFetcher wordPlaybackFetcher = new WordPlaybackFetcher();
                byte[] mp3data = wordPlaybackFetcher.getWordPlayback(String.format(AUDIO_SOURCE_URL_TEMPLATE, selectedWord.getName()));

                try {
                    databaseHelper.cacheWordPlayback(selectedWord, mp3data);
                } catch (SQLException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }

                isFetching = false;
                return mp3data;
            }

            @Override
            protected void onPostExecute(byte[] mp3data) {
                if (mp3data != null) {
                    playMp3(mp3data);
               }
            }
        }.execute(word);
    }

    private void playMp3(byte[] mp3SoundByteArray) {
        if (mp3SoundByteArray == null)
            return;

        try {
            Random r = new Random(System.currentTimeMillis());
            // create temp file that will hold byte array
            File tempMp3 = File.createTempFile(String.valueOf(r.nextLong()), "mp3", App.getContext().getCacheDir());
            tempMp3.deleteOnExit();
            FileOutputStream fos = new FileOutputStream(tempMp3);
            fos.write(mp3SoundByteArray);
            fos.close();

            MediaPlayer mediaPlayer = new MediaPlayer();

            FileInputStream fis = new FileInputStream(tempMp3);
            mediaPlayer.setDataSource(fis.getFD());

            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException ex) {
            String s = ex.toString();
            ex.printStackTrace();
        }
    }
}
