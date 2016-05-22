package com.surverior.android.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.surverior.android.R;
import com.surverior.android.helper.CheckboxQuestion;

import java.util.ArrayList;


public class
        NewCheckboxTypeActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private LinearLayout layout;
    private Button addBtn;
    private EditText inputQuestion;
    private final int EDIT_TEXT_START_ID = 100;
    private int totalEditTexts = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_checkbox_type);

        //Membuat Toolbar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Create New Survey");
        getSupportActionBar().setSubtitle("New Checkbox Type Question");
        getSupportActionBar().setElevation(4);

        layout = (LinearLayout) findViewById(R.id.checkboxlayout);
        addBtn = (Button) findViewById(R.id.addBtn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                totalEditTexts++;
                if (totalEditTexts > 50)
                    return;

                EditText editText = new EditText(getApplicationContext());
                layout.addView(editText);
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) editText.getLayoutParams();
                layoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT;
                editText.setLayoutParams(layoutParams);
                //if you want to identify the created editTexts, set a tag, like below
                editText.setId(EDIT_TEXT_START_ID+totalEditTexts);
                editText.setTag("EditText" + totalEditTexts);
                editText.setHint("Options " + totalEditTexts);
                editText.setHintTextColor(Color.GRAY);
                editText.setTextColor(Color.BLACK);
            }
        });

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
                //NavUtils.navigateUpFromSameTask(this);
                totalEditTexts = 0;
                onBackPressed();
                return true;
            case R.id.action_done:
                String question = inputQuestion.getText().toString().trim();
                ArrayList<String> choices = extractChoices();
                if(!question.isEmpty() && totalEditTexts>0 && choices != null) {
                    Log.d("totalChoice",""+choices.size());
                    Intent i = new Intent(getApplication(), QuestionListActivity.class);
                    i.putExtra("NEW_QUESTION",true);
                    i.putExtra("question",new CheckboxQuestion(question,choices));
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

    private ArrayList<String> extractChoices(){
        ArrayList<String> result = new ArrayList<>();
        for(int i = 1; i <= totalEditTexts;i++){
            EditText input = (EditText) findViewById(i+EDIT_TEXT_START_ID);
            result.add(input.getText().toString().trim());
        }
        if (isChoicesValid(result)) return result; else return null;
    }
    private boolean isChoicesValid(ArrayList<String> choices){
        boolean result = true;
        if(choices.size()<2) return false;
        for(int i=0; i < choices.size();i++){
            if(choices.get(i).isEmpty()) return false;
        }
        return result;
    }
}
