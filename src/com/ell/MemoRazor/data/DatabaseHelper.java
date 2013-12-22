package com.ell.MemoRazor.data;

import java.sql.SQLException;
import java.util.List;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTableConfig;
import com.j256.ormlite.table.TableUtils;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "memoRazor.db";
    private static final int DATABASE_VERSION = 1;

    private Dao<Word, Integer> wordDao = null;
    private Dao<WordWithCachedPlayback, Integer> wordWithCachedPlaybackDao = null;
    private RuntimeExceptionDao<Word, Integer> wordRuntimeDao = null;
    private Dao<WordGroup, Integer> wordGroupDao = null;
    private RuntimeExceptionDao<WordGroup, Integer> wordGroupRuntimeDao = null;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            Log.i(DatabaseHelper.class.getName(), "onCreate");

            DatabaseTableConfig<WordWithCachedPlayback> databaseTableConfig = DatabaseTableConfig.fromClass(connectionSource, WordWithCachedPlayback.class);
            List<String> statements = TableUtils.getCreateTableStatements(connectionSource, databaseTableConfig);
            
            TableUtils.createTable(connectionSource, WordWithCachedPlayback.class);
            TableUtils.createTable(connectionSource, WordGroup.class);
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't create database", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, Word.class, true);
            TableUtils.dropTable(connectionSource, WordGroup.class, true);

            onCreate(db, connectionSource);
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't drop databases", e);
            throw new RuntimeException(e);
        }
    }

    public void cacheWordPlayback (Word word, byte[] playback) throws SQLException {
        WordWithCachedPlayback wordWithCachedPlayback = getWordWithCachedPlaybackDao().queryForId(word.getId());
        wordWithCachedPlayback.cachedPlayback = playback;
        getWordWithCachedPlaybackDao().update(wordWithCachedPlayback);
    }

    public byte[] getWordPlaybackCache(Word word) throws SQLException {
        WordWithCachedPlayback wordWithCachedPlayback = getWordWithCachedPlaybackDao().queryForId(word.getId());
        return wordWithCachedPlayback.getCachedPlayback();
    }

    public boolean isPlaybackCached(Word word) {
        WordWithCachedPlayback wordWithCachedPlayback = null;
        try {
            wordWithCachedPlayback = getWordWithCachedPlaybackDao().queryForId(word.getId());
            return wordWithCachedPlayback.getCachedPlayback() != null;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Dao<WordWithCachedPlayback, Integer> getWordWithCachedPlaybackDao() throws SQLException {
        if (wordWithCachedPlaybackDao == null) {
            wordWithCachedPlaybackDao = getDao(WordWithCachedPlayback.class);
        }
        return wordWithCachedPlaybackDao;
    }

    public Dao<Word, Integer> getWordDao() throws SQLException {
        if (wordDao == null) {
            wordDao = getDao(Word.class);
        }
        return wordDao;
    }

    public Dao<WordGroup, Integer> getWordGroupDao() throws SQLException {
        if (wordGroupDao == null) {
            wordGroupDao = getDao(WordGroup.class);
        }
        return wordGroupDao;
    }

    /**
     * Returns the RuntimeExceptionDao (Database Access Object) version of a Dao for our Word class. It will
     * create it or just give the cached value. RuntimeExceptionDao only through RuntimeExceptions.
     */
    public RuntimeExceptionDao<Word, Integer> getWordRuntimeDao() {
        if (wordRuntimeDao == null) {
            wordRuntimeDao = getRuntimeExceptionDao(Word.class);
        }
        return wordRuntimeDao;
    }

    public RuntimeExceptionDao<WordGroup, Integer> getWordGroupRuntimeDao() {
        if (wordGroupRuntimeDao == null) {
            wordGroupRuntimeDao = getRuntimeExceptionDao(WordGroup.class);
        }
        return wordGroupRuntimeDao;
    }

    /**
     * Close the database connections and clear any cached DAOs.
     */
    @Override
    public void close() {
        super.close();
        wordGroupRuntimeDao = null;
        wordRuntimeDao = null;
    }
}
