package com.ell.MemoRazor;

import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import com.ell.MemoRazor.adapters.WordGroupAdapter;
import com.ell.MemoRazor.adapters.WordGroupSelectionAdapter;
import com.ell.MemoRazor.data.DatabaseHelper;
import com.ell.MemoRazor.data.WordGroup;
import com.ell.MemoRazor.helpers.DialogHelper;
import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;

public class WordGroupsSelectionActivity extends OrmLiteBaseActivity<DatabaseHelper> {
    private ArrayList<WordGroup> wordGroups;
    private Dao<WordGroup, Integer> wordGroupsDao;
    private ListView groupsListView;
    private WordGroupSelectionAdapter wordGroupSelectionAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setIcon(R.drawable.group);
        setTitle(getResources().getString(R.string.wordGroups_selectWordGroups));

        setContentView(R.layout.wordgroups_selection);

        try {
            wordGroupsDao = getHelper().getWordGroupDao();

            wordGroups = new ArrayList<WordGroup>(wordGroupsDao.queryBuilder().orderBy(WordGroup.CREATED_DATE_COLUMN, false).query());
        } catch (SQLException e) {
            wordGroups = new ArrayList<WordGroup>();
        }

        groupsListView = (ListView) findViewById(R.id.groups_selection);
        wordGroupSelectionAdapter = new WordGroupSelectionAdapter(this, R.layout.word_group_selection_layout, wordGroups);
        groupsListView.setAdapter(wordGroupSelectionAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.wordgroupselectionmenu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_start:
                break;
            case R.id.action_back_to_main:
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
}
