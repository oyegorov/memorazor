package com.ell.MemoRazor;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.*;
import android.widget.*;
import com.ell.MemoRazor.data.DatabaseHelper;
import com.ell.MemoRazor.export.ExportManager;
import com.ell.MemoRazor.export.ExportedData;
import com.ell.MemoRazor.data.Word;
import com.ell.MemoRazor.data.WordGroup;
import com.ell.MemoRazor.helpers.DialogHelper;
import com.google.gson.*;
import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.support.DatabaseConnection;
import com.j256.ormlite.table.TableUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.ArrayList;

public class ImportExportActivity extends OrmLiteBaseActivity<DatabaseHelper> {
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
        actions.add(getResources().getString(R.string.ie_import));
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

        final Context context = this;
        actionsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ExportManager exportManager;

                switch (i) {
                    case 0:
                        break;
                    case 1:
                        exportManager = new ExportManager(getHelper());
                        String json = null;
                        try {
                            json = exportManager.Export(getVersionCode());
                            File outputFile = new File(context.getFilesDir(), "export.mrz");
                            FileOutputStream out = new FileOutputStream(outputFile);
                            out.write(json.getBytes());
                            out.close();

                            DialogHelper.MessageBox(context, String.format(getString(R.string.exportSuccessful), outputFile.getPath()));
                        } catch (Exception e) {
                            DialogHelper.MessageBox(context, getString(R.string.exportFailed));
                            e.printStackTrace();
                        }
                        break;
                    case 2:
                        exportManager = new ExportManager(getHelper());
                        try {
                            json = exportManager.Export(getVersionCode());

                            Intent shareIntent = new Intent();
                            shareIntent.setAction(Intent.ACTION_SEND);
                            shareIntent.putExtra(Intent.EXTRA_STREAM, json.getBytes());
                            shareIntent.setType("*/*");
                            startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.send_to)));
                        } catch (Exception e) {
                            DialogHelper.MessageBox(context, getString(R.string.exportFailed));
                            e.printStackTrace();
                        }

                        break;
                }
            }
        });
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
