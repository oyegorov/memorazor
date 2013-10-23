package com.ell.MemoRazor.data;

import java.io.Serializable;

public class QuizAnswer implements Serializable {
    private Word word;
    private String proposedAnswer;
    private Boolean isCorrect;
    private Boolean isSkipped;
    private long elapsedMilliseconds;
    private int questionNumber;

    public Word getWord() {
        return word;
    }

    public void setWord(Word word) {
        this.word = word;
    }

    public String getProposedAnswer() {
        return proposedAnswer;
    }

    public void setProposedAnswer(String proposedAnswer) {
        this.proposedAnswer = proposedAnswer;
    }

    public Boolean getCorrect() {
        return isCorrect;
    }

    public void setCorrect(Boolean correct) {
        isCorrect = correct;
    }

    public Boolean getSkipped() {
        return isSkipped;
    }

    public void setSkipped(Boolean skipped) {
        isSkipped = skipped;
    }

    public long getElapsedMilliseconds() {
        return elapsedMilliseconds;
    }

    public void setElapsedMilliseconds(long elapsedMilliseconds) {
        this.elapsedMilliseconds = elapsedMilliseconds;
    }

    public int getQuestionNumber() {
        return questionNumber;
    }

    public void setQuestionNumber(int questionNumber) {
        this.questionNumber = questionNumber;
    }
}
