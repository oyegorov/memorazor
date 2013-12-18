package com.ell.MemoRazor;

import android.os.Bundle;
import com.ell.MemoRazor.helpers.DialogHelper;

public class SettingsActivity extends MemoRazorActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.settings);

        if (getIntent().getBooleanExtra(MainActivity.EXTRA_FIRSTTIME_SETTINGS, false)) {
            DialogHelper.MessageBox(this, getString(R.string.settingsNotInitialized));
        }
    }
}
