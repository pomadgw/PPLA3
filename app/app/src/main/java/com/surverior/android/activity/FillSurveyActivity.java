package com.surverior.android.activity;

import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInstaller;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.surverior.android.adapter.FillSurveyAdapter;
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
    private FillSurveyAdapter fsa;

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

        //Inisialisasi RecycleView
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
                                        survey.questions.add(new DropdownQuestion(temp.getString("question"),temp.getInt("id"),choices));
                                        break;
                                }
                            }
                            fsa = new FillSurveyAdapter(survey.questions);
                            RecyclerView recView = (RecyclerView) findViewById(R.id.fill_layout);
                            recView.setHasFixedSize(true);
                            LinearLayoutManager llm = new LinearLayoutManager(getApplication());
                            llm.setOrientation(LinearLayoutManager.VERTICAL);
                            recView.setLayoutManager(llm);
                            recView.setAdapter(fsa);
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
