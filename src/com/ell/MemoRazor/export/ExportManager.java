package com.ell.MemoRazor.export;

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

public class ExportManager {
    private DatabaseHelper databaseHelper;

    public ExportManager(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    public String Export(int version) throws SQLException {
        ArrayList<ExportedWordGroup> exportedWordGroups = new ArrayList<ExportedWordGroup>();

        ArrayList<WordGroup> wordGroups = new ArrayList<WordGroup>();

        Dao<WordGroup, Integer> wordGroupsDao = databaseHelper.getWordGroupDao();
        wordGroups = new ArrayList<WordGroup>(wordGroupsDao.queryBuilder().orderBy(WordGroup.CREATED_DATE_COLUMN, false).query());

        for (WordGroup wg : wordGroups) {
            exportedWordGroups.add(new ExportedWordGroup(wg));
        }

        ExportedData exportedData = new ExportedData(version, exportedWordGroups);

        Gson gson = new Gson();
        return gson.toJson(exportedData);
    }

    public Boolean Import(int version, String json) {
        Gson gson = new Gson();
        ExportedData data = gson.fromJson(json, ExportedData.class);
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
