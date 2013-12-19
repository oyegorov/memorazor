package com.ell.MemoRazor;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ell.MemoRazor.data.Word;
import com.ell.MemoRazor.helpers.LanguageHelper;

import java.util.ArrayList;
import java.util.Random;

public class CardsActivity extends MemoRazorActivity {
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

    private ImageView cardLang;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cards);
    }

    @Override
    protected void configureActionBar(ActionBar actionBar) {
        super.configureActionBar(actionBar);
        actionBar.setIcon(R.drawable.cards);
    }

    @Override
    protected void bindControls() {
        super.bindControls();

        card = (TextView) findViewById(R.id.cards_card);
        cardNumber = (TextView) findViewById(R.id.cards_cardnumber);
        nextButton = (Button) findViewById(R.id.cards_next);
        prevButton = (Button) findViewById(R.id.cards_prev);
        cardLang = (ImageView) findViewById(R.id.card_lang);
    }

    @Override
    protected void initialize() {
        super.initialize();

        allWords = (ArrayList<Word>) getIntent().getSerializableExtra(WordGroupsSelectionActivity.EXTRA_SELECTED_WORDS);
        ArrayList<Integer> availableIndices = new ArrayList<Integer>();
        for (int i = 0; i < allWords.size(); i++) {
            availableIndices.add(i);
        }

        wordIndices = new ArrayList<Integer>();
        for (int i=0; i < App.getNumCards() && i < allWords.size(); i++) {
            int index = random.nextInt(availableIndices.size());
            wordIndices.add(availableIndices.get(index));
            availableIndices.remove(index);
        }

        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeCard(false);
            }
        });
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeCard(true);
            }
        });
        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isWordShown =!isWordShown;
                updateWordUi();
            }
        });

        updateWordUi();
        updateStepsUi();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void changeCard(boolean goForward) {
        if (goForward) {
            currentIndex++;
        } else {
            currentIndex--;
        }
        currentIndex = (currentIndex + wordIndices.size()) % wordIndices.size();
        isWordShown = false;

        updateWordUi();
        updateStepsUi();
    }

    private void updateWordUi() {
        int currentWordIndex = wordIndices.get(currentIndex);
        currentWord = allWords.get(currentWordIndex);

        card.setText(isWordShown ? currentWord.getName() : currentWord.getMeaning());
        cardLang.setImageResource(LanguageHelper.langCodeToImage(currentWord.getLanguage()));
    }

    private void updateStepsUi() {
        totalSteps = wordIndices.size();

        String labelText = String.format(getResources().getString(R.string.cards_cardNumber),
                currentIndex + 1,
                totalSteps);
        cardNumber.setText(labelText);
    }
}
