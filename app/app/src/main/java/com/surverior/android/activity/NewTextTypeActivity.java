package com.surverior.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.surverior.android.R;
import com.surverior.android.helper.Question;


public class NewTextTypeActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private EditText inputQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_text_type);

        //Membuat Toolbar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Create New Survey");
        getSupportActionBar().setSubtitle("New Text Type Question");
        getSupportActionBar().setElevation(4);

        inputQuestion = (EditText) findViewById(R.id.question);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_wizard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_done:
                String question = inputQuestion.getText().toString().trim();
                if(!question.isEmpty()) {
                    Intent i = new Intent(getApplication(), QuestionListActivity.class);
                    i.putExtra("question",new Question(question));
                    i.putExtra("NEW_QUESTION", true);
                    i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(i);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "There is invalid input!", Toast.LENGTH_LONG)
                            .show();
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
