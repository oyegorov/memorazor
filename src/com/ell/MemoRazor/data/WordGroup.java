package com.ell.MemoRazor.data;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@DatabaseTable(tableName = "WordGroups")
public class WordGroup extends HistoryObject implements Serializable {
    public WordGroup(String name) {
        this.name = name;
    }

    public WordGroup() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ForeignCollection<Word> getWords() {
        return words;
    }

    public void setWords(ForeignCollection<Word> words) {
        this.words = words;
    }

    @DatabaseField(generatedId = true)
    protected int id;

    @DatabaseField
    protected String name;

    @ForeignCollectionField (eager = false)
    protected ForeignCollection<Word> words;
}
