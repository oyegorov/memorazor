package com.ell.MemoRazor;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.MenuItem;
import android.widget.TextView;
import com.ell.MemoRazor.data.DatabaseHelper;
import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;

public class AboutActivity extends OrmLiteActivity {
    private TextView aboutText;
    private TextView email;
    private TextView projectPage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        setContentView(R.layout.about);

        aboutText = (TextView)findViewById(R.id.aboutText);
        email = (TextView)findViewById(R.id.email);
        projectPage = (TextView)findViewById(R.id.projectPage);

        aboutText.setText(String.format("MemoRazor v%s", getVersionName()));
        email.setText(Html.fromHtml("E-mail: <a href=\"mailto:yegorov.oleg@gmail.com\">yegorov.oleg@gmail.com</a>"));
        email.setMovementMethod(LinkMovementMethod.getInstance());

        projectPage.setText(Html.fromHtml(String.format(getResources().getText(R.string.projectPage).toString(),
                "<a href=\"https://github.com/oyegorov/memorazor\">https://github.com/oyegorov/memorazor/</a>")));
        projectPage.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private String getVersionName() {
        try {
            return getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            return "-1";
        }
    }
}
