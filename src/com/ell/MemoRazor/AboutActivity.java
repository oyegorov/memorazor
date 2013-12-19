package com.ell.MemoRazor;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

public class AboutActivity extends MemoRazorActivity {
    public static final String EMAIL_FIELD_TEXT = "E-mail: <a href=\"mailto:yegorov.oleg@gmail.com\">yegorov.oleg@gmail.com</a>";
    public static final String MEMORAZOR_VERSION_TEMPLATE = "MemoRazor v%s";
    public static final String PROJECT_FIELD_TEXT = "<a href=\"https://github.com/oyegorov/memorazor\">https://github.com/oyegorov/memorazor/</a>";
    private TextView aboutText;
    private TextView email;
    private TextView projectPage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
    }

    @Override
    protected void configureActionBar(ActionBar actionBar) {
        super.configureActionBar(actionBar);
        actionBar.setIcon(R.drawable.about);
    }

    @Override
    protected void bindControls() {
        super.bindControls();

        aboutText = (TextView)findViewById(R.id.aboutText);
        email = (TextView)findViewById(R.id.email);
        projectPage = (TextView)findViewById(R.id.projectPage);
    }

    @Override
    protected void initialize() {
        super.initialize();

        aboutText.setText(String.format(MEMORAZOR_VERSION_TEMPLATE, App.getVersionName()));
        email.setText(Html.fromHtml(EMAIL_FIELD_TEXT));
        email.setMovementMethod(LinkMovementMethod.getInstance());

        projectPage.setText(Html.fromHtml(String.format(getResources().getText(R.string.projectPage).toString(),
                PROJECT_FIELD_TEXT)));
        projectPage.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
