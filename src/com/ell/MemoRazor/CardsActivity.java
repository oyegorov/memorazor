package com.ell.MemoRazor;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.ell.MemoRazor.data.DatabaseHelper;

import com.ell.MemoRazor.data.Word;
import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;

import java.util.ArrayList;
import java.util.Random;

public class CardsActivity extends OrmLiteBaseActivity<DatabaseHelper> {
    public static final String EXTRA_QUIZ_ANSWERS = "com.ell.QUIZ_ANSWERS";

    private Random random = new Random(System.currentTimeMillis());
    private int currentIndex = 0;
    private int totalSteps;
    private Boolean isWordShown = false;

    private Word currentWord;
    private ArrayList<Word> allWords;
    private ArrayList<Integer> wordIndices;

    private TextView card;
    private TextView cardNumber;
    private Button nextButton;
    private Button prevButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cards);
        //getActionBar().setIcon(R.drawable.group);
        //setTitle(getResources().getString(R.string.wordGroups_selectWordGroups));
        card = (TextView) findViewById(R.id.cards_card);
        cardNumber = (TextView) findViewById(R.id.cards_cardnumber);
        nextButton = (Button) findViewById(R.id.cards_next);
        prevButton = (Button) findViewById(R.id.cards_prev);

        allWords = (ArrayList<Word>) getIntent().getSerializableExtra(WordGroupsSelectionActivity.EXTRA_SELECTED_WORDS);
        ArrayList<Integer> availableIndices = new ArrayList<Integer>();
        for (int i = 0; i < allWords.size(); i++) {
            availableIndices.add(i);
        }

        wordIndices = new ArrayList<Integer>();
        for (int i=0; i < 20 && i < allWords.size(); i++) {
            int index = random.nextInt(availableIndices.size());
            wordIndices.add(availableIndices.get(index));
            availableIndices.remove(index);
        }

        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Move(false);
            }
        });
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Move(true);
            }
        });
        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isWordShown =!isWordShown;
                RefreshWord();
            }
        });

        RefreshWord();
        RefreshSteps();
    }

    private void Move(Boolean forward) {
        if (forward) {
            currentIndex++;
        } else {
            currentIndex--;
        }
        currentIndex = (currentIndex + wordIndices.size()) % wordIndices.size();
        isWordShown = false;

        RefreshWord();
        RefreshSteps();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.wordgroupselectionmenu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_back_to_main:
                finish();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void RefreshWord() {
        int currentWordIndex = wordIndices.get(currentIndex);
        currentWord = allWords.get(currentWordIndex);

        card.setText(isWordShown ? currentWord.getName() : currentWord.getMeaning());
    }

    private void RefreshSteps() {
        totalSteps = wordIndices.size();

        String labelText = String.format(getResources().getString(R.string.cards_cardNumber),
                currentIndex + 1,
                totalSteps);
        cardNumber.setText(labelText);
    }
}
