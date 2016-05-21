package com.surverior.android.activity;

import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInstaller;
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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.surverior.android.R;
import com.surverior.android.app.AppConfig;
import com.surverior.android.app.AppController;
import com.surverior.android.helper.CheckboxQuestion;
import com.surverior.android.helper.DropdownQuestion;
import com.surverior.android.helper.Question;
import com.surverior.android.helper.ScaleQuestion;
import com.surverior.android.helper.SessionManager;
import com.surverior.android.helper.SurveriorRequest;
import com.surverior.android.helper.Survey;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bambang on 5/12/16.
 */
public class FillSurveyActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private Survey survey;
    private SessionManager session;
    private Bundle extras;
    private int id;
    private JSONObject jObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fillsurvey);

        //Membuat Toolbar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Fill Survey");
//      getSupportActionBar().setSubtitle();
        getSupportActionBar().setElevation(4);

        session = new SessionManager(getApplicationContext());
        extras = getIntent().getExtras();
        id = extras.getInt("id");

        //Getting the desired survey
        getSurveyJSON();
    }

    private void getSurveyJSON(){
        SurveriorRequest req = new SurveriorRequest(Request.Method.GET, AppConfig.URL_SURVEY_GET_QUESTION+id, session,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            jObj = new JSONObject(response);
                            JSONArray jData = jObj.getJSONArray("questions");
                            Log.d("JSONQuestion",jObj.toString());

                            //do something
                            survey = new Survey(id,jObj.getString("title").toString().trim(),jObj.getString("description").toString().trim());
                            for(int i = 0; i < jData.length();i++){
                                JSONObject temp = jData.getJSONObject(i);
                                String type = temp.getString("type");
                                ArrayList<String> choices;
                                switch (type){
                                    case "text":
                                        survey.questions.add(new Question(temp.getString("question"),"Text",temp.getInt("id")));
                                        break;
                                    case "scale":
                                        survey.questions.add(new ScaleQuestion(temp.getString("question"),temp.getInt("id"),temp.getJSONObject("args").getString("min_label"),temp.getJSONObject("args").getString("max_label"),temp.getJSONObject("args").getInt("max_val")));
                                        break;
                                    case "checkbox":
                                        choices = new ArrayList<>();
                                        JSONArray jsonChoice = temp.getJSONObject("args").getJSONArray("choices");
                                        for(int j = 0;j<jsonChoice.length();j++){
                                            choices.add(jsonChoice.getString(j));
                                        }
                                        survey.questions.add(new CheckboxQuestion(temp.getString("question"),temp.getInt("id"),choices));
                                        break;
                                    case "option":
                                        choices = new ArrayList<>();
                                        JSONArray jsonOption = temp.getJSONObject("args").getJSONArray("options");
                                        for(int j = 0;j<jsonOption.length();j++){
                                            choices.add(jsonOption.getString(j));
                                        }
                                        survey.questions.add(new CheckboxQuestion(temp.getString("question"),temp.getInt("id"),choices));
                                        break;
                                }
                            }

                            // punya objek survey yg datanya dari getsurveyjson
                            //Cuma boong-boongan doang
                            //survey = new Survey("hehe","hehe");
                            ArrayList<Question> qs = survey.getQuestions();

                            //Set layout for generating qs
                            //Still probably on development
                            //TODO: Bambang fix generate view
                            LinearLayout ll = (LinearLayout) findViewById(R.id.fill_layout);
                            LayoutInflater inflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            //generate view
                            for (int i = 0; i < qs.size(); i++) {
                                Question currentQ = qs.get(i);
                                String type = currentQ.getType();
                                switch (type){
                                    case "Text": {
                                        Log.d("Text","Generating View Text");
                                        View v = inflater.inflate(R.layout.layout_text_type, ll);
                                        TextView text = (TextView) v.findViewById(R.id.text_question);
                                        EditText et = (EditText) v.findViewById(R.id.text_answer);
                                        text.setText(currentQ.getQuestionDetail());
                                        //et.setId(hehehe);
                                        ll.addView(v);
                                        Log.d("Text", "DONE!");
                                        break;
                                    }
                                    case "Checkbox": {
                                        Log.d("Checkbox","Generating View Checkbox");
                                        CheckboxQuestion cq = (CheckboxQuestion) currentQ;
                                        ArrayList<String> choices = cq.getChoices();

                                        View v = inflater.inflate(R.layout.layout_text_type, ll);
                                        TextView text = (TextView) v.findViewById(R.id.checkbox_question);
                                        LinearLayout checkboxLayout = (LinearLayout) v.findViewById(R.id.checkbox_answer);

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
                                        Log.d("Checkbox", "DONE!");
                                        break;
                                    }
                                    case "Dropdown": {
                                        Log.d("Dropdown", "Generating View Dropdown");
                                        DropdownQuestion dq = (DropdownQuestion) currentQ;
                                        ArrayList<String> choices = dq.getChoices();

                                        View v = inflater.inflate(R.layout.layout_text_type, ll);
                                        TextView text = (TextView) v.findViewById(R.id.dropdown_question);
                                        Spinner s = (Spinner) v.findViewById(R.id.dropdown_answer);
                                        //s.setId(hehehe);

                                        text.setText(currentQ.getQuestionDetail());
                                        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(getApplication(), android.R.layout.simple_spinner_item, choices); //selected item will look like a spinner set from XML
                                        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        s.setAdapter(spinnerArrayAdapter);
                                        ll.addView(v);
                                        Log.d("Dropdown", "DONE!");
                                        break;
                                    }
                                    case "Scale": {
                                        Log.d("Scale", "Generating View Scale");
                                        ScaleQuestion sq = (ScaleQuestion) currentQ;
                                        ArrayList<String> scales = new ArrayList<>();
                                        for (int j = 1; j <= sq.getRange(); j++) {
                                            if (j == 0) {
                                                scales.add(j + " - " + sq.getMinLabel());
                                            }else if (j == sq.getRange()) {
                                                scales.add(j + " - " + sq.getMaxLabel());
                                            }else{
                                                scales.add(""+j);
                                            }
                                        }
                                        View v = inflater.inflate(R.layout.layout_text_type, ll);
                                        TextView text = (TextView) v.findViewById(R.id.scale_question);
                                        Spinner s = (Spinner) v.findViewById(R.id.scale_answer);
                                        //s.setId(hehehe);

                                        text.setText(currentQ.getQuestionDetail());
                                        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(getApplication(), android.R.layout.simple_spinner_item, scales); //selected item will look like a spinner set from XML
                                        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        s.setAdapter(spinnerArrayAdapter);
                                        ll.addView(v);
                                        Log.d("Scale", "DONE!");
                                        break;
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            Log.d("JSONSurvey", e.getMessage());
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }

        });

        AppController.getInstance().addToRequestQueue(req);
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
