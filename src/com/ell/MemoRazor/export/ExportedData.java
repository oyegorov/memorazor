package com.ell.MemoRazor.export;

import com.ell.MemoRazor.data.WordGroup;

import java.util.ArrayList;

public class ExportedData {
    private int versionCode;

    private ArrayList<ExportedWordGroup> wordGroups;

    public ExportedData (int versionCode, ArrayList<ExportedWordGroup> wordGroups) {
        this.versionCode = versionCode;
        this.wordGroups = wordGroups;
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
}
