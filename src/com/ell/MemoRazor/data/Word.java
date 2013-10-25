package com.ell.MemoRazor.data;

import com.ell.MemoRazor.App;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@DatabaseTable(tableName = "Words")
public class Word extends HistoryObject implements Serializable{
    public static final String WORDGROUP_ID_FIELD_NAME = "WordGroupId";
    public static final String CACHED_PLAYBACK_FIELD_NAME = "CachedPlayback";

    public Word() {
        language = App.getDefaultLanguage();
        isPlaying = false;
    }

    public Word(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Boolean getIsPlaying() {
        return isPlaying;
    }

    public void setIsPlaying(Boolean isPlaying) {
        this.isPlaying = isPlaying;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getTranscription() {
        return transcription;
    }

    public void setTranscription(String transcription) {
        this.transcription = transcription;
    }

    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = WORDGROUP_ID_FIELD_NAME, index = true)
    protected WordGroup wordGroup;

    @DatabaseField(generatedId = true)
    protected int id;

    @DatabaseField
    protected String name;

    @DatabaseField
    protected String meaning;

    @DatabaseField
    protected String memo;

    @DatabaseField
    protected String transcription;

    @DatabaseField
    protected String language;

    protected Boolean isPlaying;
}

@DatabaseTable (tableName = "Words")
class WordWithCachedPlayback extends Word {
    public byte[] getCachedPlayback() {
        return cachedPlayback;
    }

    public void setCachedPlayback(byte[] cachedPlayback) {
        this.cachedPlayback = cachedPlayback;
    }

    @DatabaseField (dataType = DataType.BYTE_ARRAY, columnName = CACHED_PLAYBACK_FIELD_NAME)
    protected byte[] cachedPlayback;
}

