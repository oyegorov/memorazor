package com.ell.MemoRazor;

import android.os.Bundle;
import android.support.v4.preference.PreferenceFragment;

public class MemoRazorPreferencesFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}
