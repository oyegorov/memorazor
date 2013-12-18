package com.ell.MemoRazor;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.view.*;
import android.widget.*;
import com.ell.MemoRazor.export.ExportManager;
import com.ell.MemoRazor.data.WordGroup;
import com.ell.MemoRazor.helpers.DialogHelper;
import com.j256.ormlite.dao.Dao;

import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;

public class ImportExportActivity extends MemoRazorActivity {
    public static final String UTF16 = "UTF16";
    private ArrayAdapter<String> actionsAdapter;
    private ListView actionsListView;
    private ArrayList<String> actions;
    private Dao<WordGroup, Integer> wordGroupsDao;
    private ArrayList<WordGroup> wordGroups;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.importexport);

        actions = new ArrayList<String>();
        actions.add(getResources().getString(R.string.ie_export));
        actions.add(getResources().getString(R.string.ie_export_and_share));

        actionsListView = (ListView) findViewById(R.id.importexport_actions_list);
        actionsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, actions);
        actionsListView.setAdapter(actionsAdapter);

        try {
            wordGroupsDao = getHelper().getWordGroupDao();

            wordGroups = new ArrayList<WordGroup>(wordGroupsDao.queryBuilder().orderBy(WordGroup.CREATED_DATE_COLUMN, false).query());
        } catch (SQLException e) {
            wordGroups = new ArrayList<WordGroup>();
        }

        actionsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        ExportToFile();
                        break;
                    case 1:
                        ExportAndShare();
                        break;
                }
            }
        });

        Intent intent = getIntent();

        if ("file".equals(intent.getScheme()) && "android.intent.action.VIEW".equals(intent.getAction())) {
            String filePath = intent.getData().getPath();
            Import(filePath);
        }
    }

    private void Import(String filePath) {
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

                exportManager.Import(getVersionCode(), json);
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

    private void ExportAndShare() {
        ExportManager exportManager = new ExportManager(getHelper());
        try {
            String json = exportManager.Export(getVersionCode());

            final String tempFileName = "Export.mrz";
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

    private void ExportToFile() {
        ExportManager exportManager;

        exportManager = new ExportManager(getHelper());
        try {
            final String json = exportManager.Export(getVersionCode());

            final Context context = this;
            DialogHelper.RequestInput(this, context.getResources().getString(R.string.app_name), getResources().getString(R.string.exportFileName), "MemoRazor_export.mrz", new DialogHelper.OnRequestInputListener() {
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

    private int getVersionCode() {
        try {
            return getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            return -1;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainmenu, menu);

        return super.onCreateOptionsMenu(menu);
    }
}
