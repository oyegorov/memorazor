package com.ell.MemoRazor;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.ell.MemoRazor.data.DatabaseHelper;
import com.j256.ormlite.android.apptools.OpenHelperManager;

public class MemoRazorActivity extends ActionBarActivity {
    private DatabaseHelper databaseHelper = null;

    protected void generateEmptyView(ListView listView, String text) {
        if (listView == null)
            throw new IllegalArgumentException("listView");
        if (text == null)
            throw new IllegalArgumentException("text");

        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.GRAY);
        addContentView(textView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        listView.setEmptyView(textView);
    }

    protected DatabaseHelper getHelper() {
        if (databaseHelper == null) {
            databaseHelper =
                    OpenHelperManager.getHelper(this, DatabaseHelper.class);
        }
        return databaseHelper;
    }

    protected void bindControls() {
    }

    protected void initialize() {
    }

    protected void configureActionBar(ActionBar actionBar) {
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);    //To change body of overridden methods use File | Settings | File Templates.
        configureActionBar(getSupportActionBar());
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);    //To change body of overridden methods use File | Settings | File Templates.
        bindControls();
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);    //To change body of overridden methods use File | Settings | File Templates.
        bindControls();
        initialize();
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);    //To change body of overridden methods use File | Settings | File Templates.
        bindControls();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (databaseHelper != null) {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }
    }
}
