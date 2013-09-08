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

public class MainActivity extends OrmLiteBaseActivity<DatabaseHelper> {
    private MainActionsAdapter mainActionsAdapter;
    private ListView actionsListView;
    private ArrayList<String> actions;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        actions = new ArrayList<String>();
        actions.add(getResources().getString(R.string.main_actionWords));
        actions.add(getResources().getString(R.string.main_actionQuiz));
        actions.add(getResources().getString(R.string.main_actionExportImport));
        actions.add(getResources().getString(R.string.main_actionAbout));

        actionsListView = (ListView) findViewById(R.id.main_actions_list);
        mainActionsAdapter = new MainActionsAdapter(this, R.layout.main_action_layout, actions);
        actionsListView.setAdapter(mainActionsAdapter);

        final Context context = this;
        actionsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent;
                switch (i) {
                    case 0:
                        intent = new Intent(context, WordGroupsActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        intent = new Intent(context, WordGroupsSelectionActivity.class);
                        startActivity(intent);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       return true;
    }
}
