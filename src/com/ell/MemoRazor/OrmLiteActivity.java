package com.ell.MemoRazor;

import android.support.v7.app.ActionBarActivity;
import com.ell.MemoRazor.data.DatabaseHelper;
import com.j256.ormlite.android.apptools.OpenHelperManager;

public class OrmLiteActivity extends ActionBarActivity {
    private DatabaseHelper databaseHelper = null;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (databaseHelper != null) {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }
    }

    protected DatabaseHelper getHelper() {
        if (databaseHelper == null) {
            databaseHelper =
                    OpenHelperManager.getHelper(this, DatabaseHelper.class);
        }
        return databaseHelper;
    }
}
