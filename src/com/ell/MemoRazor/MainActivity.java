package com.ell.MemoRazor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.*;
import android.widget.*;
import com.ell.MemoRazor.adapters.MainActionsAdapter;
import com.ell.MemoRazor.helpers.DialogHelper;

import java.util.ArrayList;

public class MainActivity extends ActionBarActivity {
    public static final String EXTRA_SELECTEDWORDS_ACTION = "com.ell.EXTRA_SELECTEDWORDS_ACTION";
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
        actions.add(getResources().getString(R.string.main_actionCards));
        actions.add(getResources().getString(R.string.main_actionExportImport));
        actions.add(getResources().getString(R.string.preferences));
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
                        intent.putExtra(EXTRA_SELECTEDWORDS_ACTION, "quiz");
                        startActivity(intent);
                        break;
                    case 2:
                        intent = new Intent(context, WordGroupsSelectionActivity.class);
                        intent.putExtra(EXTRA_SELECTEDWORDS_ACTION, "cards");
                        startActivity(intent);
                        break;
                    case 3:
                        intent = new Intent(context, ImportExportActivity.class);
                        startActivity(intent);
                        break;
                    case 4:
                        intent = new Intent(context, SettingsActivity.class);
                        startActivity(intent);
                        break;
                    case 5:
                        intent = new Intent(context, AboutActivity.class);
                        startActivity(intent);
                        break;
                }
            }
        });

        if (App.getSettingsNotInitialized()) {
            DialogHelper.MessageBox(context, getString(R.string.settingsNotInitialized));

            Intent intent = new Intent(context, SettingsActivity.class);
            startActivity(intent);
        }
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
