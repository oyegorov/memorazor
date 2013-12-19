package com.ell.MemoRazor;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.view.*;
import android.widget.*;
import com.ell.MemoRazor.export.ExportManager;
import com.ell.MemoRazor.helpers.DialogHelper;

import java.io.*;
import java.util.ArrayList;

public class ImportExportActivity extends MemoRazorActivity {
    public static final String UTF16 = "UTF16";
    public static final String IMPORT_INTENT_SCHEME = "file";
    public static final String ANDROID_INTENT_ACTION_VIEW = "android.intent.action.VIEW";
    public static final String EXPORT_FILE_NAME = "MemoRazor_export.mrz";
    public static final String TEMP_EXPORT_FILE_NAME = "Export.mrz";
    private ListView actionsListView;

    private ArrayAdapter<String> actionsAdapter;
    private ArrayList<String> actionsList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.importexport);
    }

    @Override
    protected void configureActionBar(ActionBar actionBar) {
        super.configureActionBar(actionBar);
        actionBar.setIcon(R.drawable.exportimport);
    }

    @Override
    protected void bindControls() {
        super.bindControls();
        actionsListView = (ListView) findViewById(R.id.importexport_actions_list);
    }

    @Override
    protected void initialize() {
        super.initialize();

        actionsList = new ArrayList<String>();
        actionsList.add(getResources().getString(R.string.ie_export));
        actionsList.add(getResources().getString(R.string.ie_export_and_share));

        actionsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, actionsList);
        actionsListView.setAdapter(actionsAdapter);

        actionsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        exportToFile();
                        break;
                    case 1:
                        exportAndShare();
                        break;
                }
            }
        });

        checkImportIntent();
    }

    private void checkImportIntent() {
        Intent intent = getIntent();

        if (IMPORT_INTENT_SCHEME.equals(intent.getScheme()) && ANDROID_INTENT_ACTION_VIEW.equals(intent.getAction())) {
            String filePath = intent.getData().getPath();
            importFromFile(filePath);
        }
    }

    private void importFromFile(String filePath) {
        ExportManager exportManager = new ExportManager(getHelper());

        try
        {
            FileInputStream fileInputStream = new FileInputStream(filePath);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, UTF16);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            try {

                StringBuffer fileContent = new StringBuffer("");
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    fileContent.append(line);
                }
                String json = fileContent.toString();

                exportManager.Import(App.getVersionCode(), json);
                DialogHelper.MessageBox(this, getString(R.string.importSucceeded));
            }
            finally {
                bufferedReader.close();
               inputStreamReader.close();
               fileInputStream.close();
            }
        }
        catch (Exception e) {
            DialogHelper.MessageBox(this, getString(R.string.importFailed));
            e.printStackTrace();
        }
    }

    private void exportAndShare() {
        ExportManager exportManager = new ExportManager(getHelper());
        try {
            String json = exportManager.Export(App.getVersionCode());

            final String tempFileName = TEMP_EXPORT_FILE_NAME;
            File outputFile = new File(Environment.getExternalStorageDirectory(), tempFileName);

            FileOutputStream out = null;
            try
            {
                out = new FileOutputStream(outputFile, false);
                out.write(json.getBytes(UTF16));
            }
            finally {
                if (out != null)
                    out.close();
            }

            File sendFile = new File(Environment.getExternalStorageDirectory(), tempFileName);
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(sendFile));
            shareIntent.setType("*/*");
            startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.send_to)));
            sendFile.deleteOnExit();
        } catch (Exception e) {
            DialogHelper.MessageBox(this, getString(R.string.exportFailed));
            e.printStackTrace();
        }
    }

    private void exportToFile() {
        ExportManager exportManager;

        exportManager = new ExportManager(getHelper());
        try {
            final String json = exportManager.Export(App.getVersionCode());

            final Context context = this;
            DialogHelper.RequestInput(this, context.getResources().getString(R.string.app_name), getResources().getString(R.string.exportFileName), EXPORT_FILE_NAME, new DialogHelper.OnRequestInputListener() {
                @Override
                public void onRequestInput(String input) {
                    try {
                        File outputFile = new File(Environment.getExternalStorageDirectory(), input);

                        FileOutputStream out = null;
                        try {
                            out = new FileOutputStream(outputFile, false);
                            out.write(json.getBytes(UTF16));
                        }
                        finally {
                            if (out != null)
                                out.close();
                        }
                        DialogHelper.MessageBox(context, String.format(getString(R.string.exportSuccessful), outputFile.getName()));
                    } catch (Exception e) {
                        DialogHelper.MessageBox(context, context.getString(R.string.exportFailed));
                    }
                }
            });
        } catch (Exception e) {
            DialogHelper.MessageBox(this, getString(R.string.exportFailed));
            e.printStackTrace();
        }
    }
}
