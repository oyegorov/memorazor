package com.ell.MemoRazor.export;
import com.ell.MemoRazor.data.Word;

public class ExportedWord extends ExportedHistoryObject {
    public ExportedWord() {
    }

    public ExportedWord(Word word) {
        this.createdDate = word.getCreatedDate();
        this.modifiedDate = word.getModifiedDate();
        this.name = word.getName();
        this.meaning = word.getMeaning();
        this.memo = word.getMemo();
        this.transcription = word.getTranscription();
        this.language = word.getLanguage();
    }

    public Word toWord() {
        Word word = new Word();
        word.setCreatedDate(this.createdDate);
        word.setModifiedDate(this.modifiedDate);
        word.setName(this.name);
        word.setMeaning(this.meaning);
        word.setMemo(this.memo);
        word.setTranscription(this.transcription);
        word.setLanguage(this.language);

        return word;
    }

    public String name;
    public String meaning;
    public String memo;
    public String transcription;
    public String language;
}