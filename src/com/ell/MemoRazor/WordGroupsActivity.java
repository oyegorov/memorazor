package com.ell.MemoRazor;

import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import com.ell.MemoRazor.adapters.WordGroupAdapter;
import com.ell.MemoRazor.data.DatabaseHelper;
import com.ell.MemoRazor.data.WordGroup;
import com.ell.MemoRazor.helpers.DialogHelper;
import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;

public class WordGroupsActivity extends OrmLiteBaseActivity<DatabaseHelper> {
    public static final String EXTRA_GROUP_ID = "com.ell.GROUP_ID";

    private ArrayList<WordGroup> wordGroups;
    private Dao<WordGroup, Integer> wordGroupsDao;
    private ListView groupsListView;
    private WordGroupAdapter wordGroupsAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setIcon(R.drawable.group);
        setTitle(getResources().getString(R.string.wordGroups_wordGroups));

        setContentView(R.layout.wordgroups);

        try {
            wordGroupsDao = getHelper().getWordGroupDao();

            wordGroups = new ArrayList<WordGroup>(wordGroupsDao.queryBuilder().orderBy(WordGroup.CREATED_DATE_COLUMN, false).query());
        } catch (SQLException e) {
            wordGroups = new ArrayList<WordGroup>();
        }

        groupsListView = (ListView) findViewById(R.id.groups_list);
        wordGroupsAdapter = new WordGroupAdapter(this, R.layout.word_group_layout, wordGroups);
        groupsListView.setAdapter(wordGroupsAdapter);

        registerForContextMenu(groupsListView);

        groupsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                openGroup(wordGroups.get(i));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.wordgroupsmenu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final WordGroup selectedGroup = wordGroups.get(info.position);

        if (item.getItemId() == 0) {
            openGroup(selectedGroup);
        }
        if (item.getItemId() == 1) {
            renameGroup(selectedGroup);
        }
        if (item.getItemId() == 2) {
            deleteGroup(selectedGroup);
        }
        return true;
    }

    private void renameGroup(final WordGroup selectedGroup) {
        DialogHelper.RequestInput(this, getResources().getString(R.string.wordGroups_newName), new DialogHelper.OnRequestInputListener() {
            @Override
            public void onRequestInput(String input) {
                selectedGroup.setName(input);
                wordGroupsAdapter.notifyDataSetChanged();

                try {
                    if (wordGroupsDao != null) {
                        wordGroupsDao.update(selectedGroup);
                    }
                } catch (SQLException e) {
                }
            }
        });
    }

    private void deleteGroup(final WordGroup selectedGroup) {
        String messageText = String.format(getResources().getString(R.string.wordGroups_confirmDelete), selectedGroup.getName());
        DialogHelper.Confirm(this, messageText, new DialogHelper.OnConfirmListener() {
            @Override
            public void onConfirm() {
                wordGroupsAdapter.remove(selectedGroup);

                try {
                    if (wordGroupsDao != null) {
                        wordGroupsDao.delete(selectedGroup);
                    }
                } catch (SQLException e) {
                }
            }
        });
    }

    private void openGroup(WordGroup groupToOpen) {
        Intent intent = new Intent(this, WordListActivity.class);
        intent.putExtra(EXTRA_GROUP_ID, groupToOpen.getId());
        startActivity(intent);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId() == R.id.groups_list) {
            menu.setHeaderTitle(getResources().getString(R.string.wordGroups_performAction));

            menu.add(Menu.NONE, 0, 0, getResources().getString(R.string.wordGroups_open));
            menu.add(Menu.NONE, 1, 1, getResources().getString(R.string.wordGroups_rename));
            menu.add(Menu.NONE, 2, 2, getResources().getString(R.string.wordGroups_delete));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                addWordGroup();
            case R.id.action_back_to_main:
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void addWordGroup() {
        DialogHelper.RequestInput(this, getResources().getString(R.string.wordGroups_addWordGroup),
                getResources().getString(R.string.wordGroups_addWordGroupInputName), new DialogHelper.OnRequestInputListener() {
            @Override
            public void onRequestInput(String input) {
                WordGroup wordGroup = new WordGroup(input);

                wordGroupsAdapter.insert(wordGroup, 0);
                try {
                    if (wordGroupsDao != null) {
                        wordGroupsDao.create(wordGroup);
                    }
                } catch (SQLException e) {
                }
            }
        });
    }
}
