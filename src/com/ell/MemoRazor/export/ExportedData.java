package com.ell.MemoRazor.export;

import com.ell.MemoRazor.data.WordGroup;

import java.util.ArrayList;
import java.util.Hashtable;

public class ExportedData {
    private int versionCode;
    private Hashtable<String, Object> settings;
    private ArrayList<ExportedWordGroup> wordGroups;

    public ExportedData (int versionCode, ArrayList<ExportedWordGroup> wordGroups, Hashtable<String, Object> settings) {
        this.versionCode = versionCode;
        this.wordGroups = wordGroups;
        this.settings = settings;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public ArrayList<ExportedWordGroup> getWordGroups() {
        return wordGroups;
    }

    public void setWordGroups(ArrayList<ExportedWordGroup> wordGroups) {
        this.wordGroups = wordGroups;
    }

    public Hashtable<String, Object> getSettings() {
        return settings;
    }

    public void setSettings(Hashtable<String, Object> settings) {
        this.settings = settings;
    }
}
