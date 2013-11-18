package com.ell.MemoRazor;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
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

import com.ell.MemoRazor.data.QuizAnswer;
import com.ell.MemoRazor.data.Word;
import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class QuizActivity extends OrmLiteActivity {
    public static final String EXTRA_QUIZ_ANSWERS = "com.ell.QUIZ_ANSWERS";

    private Random random = new Random(System.currentTimeMillis());
    private int currentStep = 1;
    private int totalSteps;

    private long currentQuestionStartTime;

    private Word currentWord;
    private ArrayList<QuizAnswer> answers;
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

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        setContentView(R.layout.quiz);
        //getActionBar().setIcon(R.drawable.group);
        //setTitle(getResources().getString(R.string.wordGroups_selectWordGroups));
        quizWordNumber = (TextView) findViewById(R.id.quizWordNumber);
        quizTranslation = (TextView) findViewById(R.id.quizTranslation);
        quizAnswer = (EditText) findViewById(R.id.quizAnswer);
        quizNext = (Button) findViewById(R.id.quizNext);
        quizSkip = (Button) findViewById(R.id.quizSkip);

        answers = new ArrayList<QuizAnswer>();
        allWords = (ArrayList<Word>) getIntent().getSerializableExtra(WordGroupsSelectionActivity.EXTRA_SELECTED_WORDS);
        availableIndices = new ArrayList<Integer>();
        for (int i = 0; i < allWords.size(); i++) {
            availableIndices.add(i);
        }

        PickNewWord();
        RefreshSteps();

        addQuizNextHandler();
        addQuizSkipHandler();
        addQuizAnswerChanged();

        currentQuestionStartTime = System.currentTimeMillis();
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
                    NextWord(false);
                }
                return true;
            }
        });
    }

    private void addQuizSkipHandler() {
        quizSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NextWord(true);
            }
        });
    }

    private void addQuizNextHandler() {
        quizNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NextWord(false);
            }
        });
    }

    private void NextWord(Boolean skip) {
        if (currentStep <= totalSteps) {
            QuizAnswer answer = new QuizAnswer();
            answer.setElapsedMilliseconds(System.currentTimeMillis() - currentQuestionStartTime);
            answer.setWord(currentWord);
            answer.setQuestionNumber(currentStep);
            if (skip) {
                answer.setCorrect(false);
                answer.setSkipped(true);
            } else {
                answer.setSkipped(false);
                answer.setProposedAnswer(quizAnswer.getText().toString().trim());
                answer.setCorrect(answer.getProposedAnswer().equalsIgnoreCase(currentWord.getName()));
            }
            answers.add(answer);

            if (currentStep == totalSteps) {
                Intent quizResultsIntent = new Intent(this, QuizResultsActivity.class);
                quizResultsIntent.putExtra(EXTRA_QUIZ_ANSWERS, answers);
                startActivity(quizResultsIntent);
            } else {
                currentStep++;
                PickNewWord();
                RefreshSteps();

                quizAnswer.setText("");
                currentQuestionStartTime = System.currentTimeMillis();
            }
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
            default:
                return super.onOptionsItemSelected(item);
        }
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
