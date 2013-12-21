package com.ell.MemoRazor.export;

import android.content.SharedPreferences;
import com.ell.MemoRazor.App;
import com.ell.MemoRazor.AppSettings;
import com.ell.MemoRazor.data.DatabaseHelper;
import com.ell.MemoRazor.data.Word;
import com.ell.MemoRazor.data.WordGroup;
import com.google.gson.Gson;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.DatabaseConnection;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

public class ExportManager {
    private DatabaseHelper databaseHelper;

    public ExportManager(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    public String exportToJson(int version) throws SQLException {
        ArrayList<ExportedWordGroup> exportedWordGroups = new ArrayList<ExportedWordGroup>();

        ArrayList<WordGroup> wordGroups = new ArrayList<WordGroup>();

        Dao<WordGroup, Integer> wordGroupsDao = databaseHelper.getWordGroupDao();
        wordGroups = new ArrayList<WordGroup>(wordGroupsDao.queryBuilder().orderBy(WordGroup.CREATED_DATE_COLUMN, false).query());

        for (WordGroup wg : wordGroups) {
            exportedWordGroups.add(new ExportedWordGroup(wg));
        }

        Hashtable<String, Object> settings = new Hashtable<String, Object>();

        Map<String, ?> sharedPreferencesList = AppSettings.getSharedPreferences().getAll();
        Iterator it = sharedPreferencesList.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry)it.next();
            settings.put(pairs.getKey().toString(), pairs.getValue());
        }

        ExportedData exportedData = new ExportedData(version, exportedWordGroups, settings);

        Gson gson = new Gson();
        return gson.toJson(exportedData);
    }

    public Boolean importJson(int version, String json) {
        Gson gson = new Gson();
        ExportedData data = gson.fromJson(json, ExportedData.class);

        Iterator it = data.getSettings().entrySet().iterator();

        try
        {
            SharedPreferences sharedPreferences = AppSettings.getSharedPreferences();
            SharedPreferences.Editor preferencesEditor = sharedPreferences.edit();
            while (it.hasNext()) {
                Map.Entry pairs = (Map.Entry)it.next();
                if (pairs.getValue() instanceof String)
                    preferencesEditor.putString(pairs.getKey().toString(), pairs.getValue().toString());
                if (pairs.getValue() instanceof Boolean)
                    preferencesEditor.putBoolean(pairs.getKey().toString(), (Boolean)pairs.getValue());
            }
            preferencesEditor.commit();
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        try {
            DatabaseConnection conn = databaseHelper.getWordGroupDao().startThreadConnection();
            Savepoint savepoint = null;
            try {
                savepoint = conn.setSavePoint(null);

                TableUtils.clearTable(databaseHelper.getConnectionSource(), WordGroup.class);
                for (ExportedWordGroup ewg: data.getWordGroups()) {
                    WordGroup wg = ewg.toWordGroup();
                    databaseHelper.getWordGroupDao().create(wg);
                    databaseHelper.getWordGroupDao().refresh(wg);

                    for (ExportedWord ew : ewg.words) {
                        Word w = ew.toWord();
                        wg.getWords().add(w);
                    }

                    databaseHelper.getWordGroupDao().update(wg);
                }
            } finally {
                conn.commit(savepoint);
                databaseHelper.getWordGroupDao().endThreadConnection(conn);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
