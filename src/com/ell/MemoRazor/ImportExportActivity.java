package com.ell.MemoRazor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import com.ell.MemoRazor.adapters.MainActionsAdapter;
import com.ell.MemoRazor.adapters.WordGroupAdapter;
import com.ell.MemoRazor.data.DatabaseHelper;
import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ImportExportActivity extends OrmLiteBaseActivity<DatabaseHelper> {
    private ArrayAdapter<String> actionsAdapter;
    private ListView actionsListView;
    private ArrayList<String> actions;

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

        final Context context = this;
        actionsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent;
                switch (i) {
                    case 0:
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainmenu, menu);

        return super.onCreateOptionsMenu(menu);
    }
}
