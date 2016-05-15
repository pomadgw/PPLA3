package com.surverior.android.activity;

import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.surverior.android.R;
import com.surverior.android.helper.CheckboxQuestion;
import com.surverior.android.helper.DropdownQuestion;
import com.surverior.android.helper.Question;
import com.surverior.android.helper.ScaleQuestion;
import com.surverior.android.helper.Survey;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by bambang on 5/12/16.
 */
public class FillSurveyActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private Survey survey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title);

        //Membuat Toolbar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Fill Survey");
//      getSupportActionBar().setSubtitle();
        getSupportActionBar().setElevation(4);

        //Getting the desired survey
        //Cuma boong-boongan doang
        survey = new Survey("hehe","hehe");
        ArrayList<Question> qs = survey.getQuestions();

        //Set layout for generating qs
        //Still probably on development
        LinearLayout ll = (LinearLayout) findViewById(R.id.fill_layout);
        LayoutInflater inflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for (int i = 0; i < qs.size(); i++) {
            Question currentQ = qs.get(i);
            String type = currentQ.getType();
            switch (type){
                case "Text": {
                    View v = inflater.inflate(R.layout.layout_text_type, ll);
                    TextView text = (TextView) v.findViewById(R.id.question);
                    EditText et = (EditText) v.findViewById(R.id.answer);
                    text.setText(currentQ.getQuestionDetail());
                    //et.setId(hehehe);
                    ll.addView(v);
                    break;
                }
                case "Checkbox": {
                    CheckboxQuestion cq = (CheckboxQuestion) currentQ;
                    ArrayList<String> choices = cq.getChoices();

                    View v = inflater.inflate(R.layout.layout_text_type, ll);
                    TextView text = (TextView) v.findViewById(R.id.question);
                    LinearLayout checkboxLayout = (LinearLayout) v.findViewById(R.id.answer);

                    text.setText(currentQ.getQuestionDetail());
                    for (int j = 0; j < choices.size(); j++) {
                        CheckBox c = new CheckBox(getApplicationContext());
                        checkboxLayout.addView(c);
                        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) c.getLayoutParams();
                        layoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT;
                        c.setLayoutParams(layoutParams);
                        c.setText(choices.get(j));
                        //c.setId(hehehe);
                    }

                    ll.addView(v);
                    break;
                }
                case "Dropdown": {
                    DropdownQuestion dq = (DropdownQuestion) currentQ;
                    ArrayList<String> choices = dq.getChoices();

                    View v = inflater.inflate(R.layout.layout_text_type, ll);
                    TextView text = (TextView) v.findViewById(R.id.question);
                    Spinner s = (Spinner) v.findViewById(R.id.answer);
                    //s.setId(hehehe);

                    text.setText(currentQ.getQuestionDetail());
                    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, choices); //selected item will look like a spinner set from XML
                    spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    s.setAdapter(spinnerArrayAdapter);
                    ll.addView(v);
                    break;
                }
                case "Scale": {
                    ScaleQuestion sq = (ScaleQuestion) currentQ;
                    ArrayList<String> scales = new ArrayList<>();
                    for (int j = 1; j <= sq.getRange(); j++) {
                        if (j == 0) {
                            scales.add(j, j + " - " + sq.getMinLabel());
                        }else if (j == sq.getRange()) {
                            scales.add(j, j + " - " + sq.getMaxLabel());
                        }else{
                            scales.add(j, ""+j);
                        }
                    }
                    View v = inflater.inflate(R.layout.layout_text_type, ll);
                    TextView text = (TextView) v.findViewById(R.id.question);
                    Spinner s = (Spinner) v.findViewById(R.id.answer);
                    //s.setId(hehehe);

                    text.setText(currentQ.getQuestionDetail());
                    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, scales); //selected item will look like a spinner set from XML
                    spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    s.setAdapter(spinnerArrayAdapter);
                    ll.addView(v);
                    break;
                }
            }
        }



    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_wizard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i;
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_done:
                i = new Intent(getApplication(), TimelineFragment.class);
                startActivity(i);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed(){
        Log.d("FillSurveyActivity","Back Pressed!");
        Intent i = new Intent(getApplication(), TimelineFragment.class);
        startActivity(i);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Log.d("FillSurveyActivity","New Intent!");
        super.onNewIntent(intent);
        setIntent(intent);//must store the new intent unless getIntent() will return the old one
    }
}
