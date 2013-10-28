package com.ell.MemoRazor;

import android.content.Context;
import android.content.Intent;
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

import com.ell.MemoRazor.data.QuizAnswer;
import com.ell.MemoRazor.data.Word;
import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class QuizResultsActivity extends OrmLiteBaseActivity<DatabaseHelper> {
    private TextView quizResult;
    private TextView quizScore;
    private TextView quizSkipped;
    private TextView quizElapsed;
    private ArrayList<QuizAnswer> answers;
    private Button finishButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_results);

        //getActionBar().setIcon(R.drawable.group);
        //setTitle(getResources().getString(R.string.wordGroups_selectWordGroups));
        quizResult = (TextView)findViewById(R.id.quiz_result_yourresult);
        quizScore = (TextView)findViewById(R.id.quiz_result_score);
        quizSkipped = (TextView)findViewById(R.id.quiz_result_skipped);
        quizElapsed = (TextView)findViewById(R.id.quiz_result_elapsed);
        finishButton = (Button)findViewById(R.id.quiz_results_finish);

        final Context context = this;
        answers = (ArrayList<QuizAnswer>) getIntent().getSerializableExtra(QuizActivity.EXTRA_QUIZ_ANSWERS);
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        DisplayResults();
    }

    private void DisplayResults() {
        int skipped = 0;
        long totalElapsed = 0;
        int correctAnswers = 0;

        for (QuizAnswer answer: answers) {
            if (answer.getSkipped()) {
                skipped++;
            } else {
                if (answer.getCorrect()) {
                    correctAnswers++;
                }
            }
            totalElapsed += answer.getElapsedMilliseconds();
        }

        quizScore.setText(String.format(getResources().getString(R.string.quiz_result_score_text),
                correctAnswers, answers.size()));
        quizSkipped.setText(String.format(getResources().getString(R.string.quiz_result_skipped_text), skipped));
        quizElapsed.setText(String.format(getResources().getString(R.string.quiz_result_elapsed_text),
                (double)totalElapsed / 1000));
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
}
