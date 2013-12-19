package com.ell.MemoRazor.translators;

import com.ell.MemoRazor.data.Word;

public interface WordPlaybackFetcher {
    public byte[] getWordPlayback(Word word);
}
