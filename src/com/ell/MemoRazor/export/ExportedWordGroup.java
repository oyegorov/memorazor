package com.ell.MemoRazor.export;

import com.ell.MemoRazor.data.Word;
import com.ell.MemoRazor.data.WordGroup;

public class ExportedWordGroup extends ExportedHistoryObject {

    public ExportedWordGroup() {
    }

    public ExportedWordGroup(WordGroup wordGroup) {
        this.createdDate = wordGroup.getCreatedDate();
        this.modifiedDate = wordGroup.getModifiedDate();
        this.name = wordGroup.getName();
        this.language = wordGroup.getLanguage();
        this.words = new ExportedWord[wordGroup.getWords().size()];
        int i=0;
        for (Word w : wordGroup.getWords()) {
            this.words[i] = new ExportedWord(w);
            i++;
        }
    }

    public WordGroup toWordGroup() {
        WordGroup wordGroup = new WordGroup();
        wordGroup.setCreatedDate(this.createdDate);
        wordGroup.setModifiedDate(this.modifiedDate);
        wordGroup.setName(this.name);
        wordGroup.setLanguage(this.language);

        return wordGroup;
    }

    public String name;
    public String language;
    public ExportedWord[] words;
}
