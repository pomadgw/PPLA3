package com.surverior.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.surverior.android.R;
import com.surverior.android.helper.ScaleQuestion;

import java.util.ArrayList;
import java.util.List;


public class NewScaleTypeActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private EditText inputQuestion;
    private EditText inputMax;
    private EditText inputMin;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_scale_type);

        //Membuat Toolbar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Create New Survey");
        getSupportActionBar().setSubtitle("New Scale Type Question");
        getSupportActionBar().setElevation(4);

        inputQuestion = (EditText) findViewById(R.id.question);
        inputMax = (EditText) findViewById(R.id.maxLabel);
        inputMin = (EditText) findViewById(R.id.minLabel);
        spinner = (Spinner) findViewById(R.id.range_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.range_number, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

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
                String max = inputMax.getText().toString().trim();
                String min = inputMin.getText().toString().trim();
                String range = spinner.getSelectedItem().toString().trim();
                if(!question.isEmpty() && !max.isEmpty() && !min.isEmpty() && !range.isEmpty()
                        && !(max.length()>25) && !(min.length()>25)) {
                    Intent i = new Intent(getApplication(), QuestionListActivity.class);
                    i.putExtra("NEW_QUESTION",true);
                    i.putExtra("question",new ScaleQuestion(question,min,max,Integer.parseInt(range)));
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
