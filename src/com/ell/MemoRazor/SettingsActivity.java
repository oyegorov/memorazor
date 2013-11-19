package com.ell.MemoRazor;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import com.ell.MemoRazor.helpers.DialogHelper;

public class SettingsActivity extends PreferenceActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        if (getIntent().getBooleanExtra(MainActivity.EXTRA_FIRSTTIME_SETTINGS, false)) {
            DialogHelper.MessageBox(this, getString(R.string.settingsNotInitialized));
        }
    }
}
