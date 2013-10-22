package com.ell.MemoRazor;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.ell.MemoRazor.data.DatabaseHelper;

import com.ell.MemoRazor.data.Word;
import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class QuizActivity extends OrmLiteBaseActivity<DatabaseHelper> {
    private Random random = new Random(System.currentTimeMillis());
    private int currentStep = 1;
    private int totalSteps;

    private Word currentWord;
    private ArrayList<Integer> availableIndices;
    private ArrayList<Word> allWords;
    private TextView quizWordNumber;
    private TextView quizTranslation;
    private EditText quizAnswer;
    private Button quizNext;
    private Button quizSkip;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz);
        //getActionBar().setIcon(R.drawable.group);
        //setTitle(getResources().getString(R.string.wordGroups_selectWordGroups));
        quizWordNumber = (TextView)findViewById(R.id.quizWordNumber);
        quizTranslation = (TextView)findViewById(R.id.quizTranslation);
        quizAnswer = (EditText)findViewById(R.id.quizAnswer);
        quizNext = (Button)findViewById(R.id.quizNext);
        quizSkip = (Button)findViewById(R.id.quizSkip);

        allWords = (ArrayList<Word>)getIntent().getSerializableExtra(WordGroupsSelectionActivity.EXTRA_SELECTED_WORDS);
        availableIndices = new ArrayList<Integer>();
        for (int i=0; i < allWords.size(); i++) {
            availableIndices.add(i);
        }

        PickNewWord();
        RefreshSteps();

        addQuizNextHandler();
        addQuizSkipHandler();
        addQuizAnswerChanged();
    }

    private void addQuizAnswerChanged() {
        quizAnswer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                quizNext.setEnabled(quizAnswer.getText().length() > 0);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        quizAnswer.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_NULL
                        && keyEvent.getAction() == KeyEvent.ACTION_DOWN && quizAnswer.getText().length() > 0) {
                    Next();
                }
                return true;
            }
        });
    }

    private void addQuizSkipHandler() {
        quizSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Skip();
            }
        });
    }

    private void Skip() {
        if (currentStep >= totalSteps) {

        } else {
            PickNewWord();
            RefreshSteps();
        }
    }

    private void addQuizNextHandler() {
        quizNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Next();
            }
        });
    }

    private void Next() {
        if (currentStep >= totalSteps) {

        } else {
            currentStep++;
            PickNewWord();
            RefreshSteps();

            quizAnswer.setText("");
        }
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

    private void PickNewWord() {
        int i = random.nextInt(availableIndices.size());
        int currentWordIndex = availableIndices.get(i);
        availableIndices.remove(i);

        currentWord = allWords.get(currentWordIndex);
        quizTranslation.setSingleLine();
        quizTranslation.setSingleLine(false);
        quizTranslation.setText(currentWord.getMeaning());
    }

    private void RefreshSteps() {
        totalSteps = Math.min(20, availableIndices.size() + currentStep);

        String labelText = String.format(getResources().getString(R.string.quiz_word_number),
                currentStep,
                totalSteps);
        quizWordNumber.setText(labelText);
    }
}
