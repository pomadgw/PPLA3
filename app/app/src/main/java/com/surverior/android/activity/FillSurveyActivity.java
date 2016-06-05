package com.surverior.android.activity;

import android.app.AlertDialog;
import android.app.ListFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.surverior.android.R;
import com.surverior.android.adapter.FillSurveyAdapter;
import com.surverior.android.app.AppConfig;
import com.surverior.android.app.AppController;
import com.surverior.android.helper.CheckboxQuestion;
import com.surverior.android.helper.DropdownQuestion;
import com.surverior.android.helper.Question;
import com.surverior.android.helper.ScaleQuestion;
import com.surverior.android.helper.SessionManager;
import com.surverior.android.helper.SurveriorJSONRequest;
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
    private ProgressDialog pDialog;

    private final int FIRST_INPUT_ID = 100;
    private final String TAG = "FillSurveyActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fillsurvey);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

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
        pDialog.setMessage("Getting Question...");
        pDialog.show();
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
                            pDialog.hide();
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
                if(!isAllFilled()){
                    Toast.makeText(getApplicationContext(),
                            "Please fill the empty field(s)!", Toast.LENGTH_LONG)
                            .show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("You are about to submit your response. Are you sure?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    pDialog.setMessage("Submitting");
                                    pDialog.show();
                                    try {
                                        sendJSON();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void sendJSON() throws JSONException {
        ArrayList<Question> questions = survey.questions;
        JSONObject main = new JSONObject();
        main.put("survey_id",id);
        int inputID = FIRST_INPUT_ID;

        JSONArray question = new JSONArray();
        for(int i = 0; i < questions.size();i++){
            inputID++;
            String type = questions.get(i).getType();
            JSONObject q = new JSONObject();
            q.put("id",questions.get(i).getID());
            switch (type){
                case "Text":{
                    EditText et = (EditText) findViewById(inputID);
                    if (et != null){
                        String temp = et.getText().toString().trim();
                        q.put("answer", temp);
                    }
                }
                break;
                case "Dropdown":{
                    Spinner sp = (Spinner) findViewById(inputID);
                    if (sp != null){
                        String temp = sp.getSelectedItem().toString().trim();
                        q.put("answer",temp);
                    }
                }
                    break;
                case "Scale":{
                    Spinner sp = (Spinner) findViewById(inputID);
                    if (sp != null){
                        int temp = sp.getSelectedItemPosition()+1;
                        q.put("answer",temp+"");
                    }
                }
                    break;
                case "Checkbox":{
                    CheckboxQuestion cq = (CheckboxQuestion) questions.get(i);
                    JSONArray ja = new JSONArray();
                    for(int j = 0; j < cq.getChoices().size();j++){
                        if(j>0) inputID++;
                        CheckBox cb = (CheckBox) findViewById(inputID);
                        if(cb.isChecked()){
                            ja.put(cb.getText().toString().trim());
                        }
                    }
                    q.put("answer",ja);
                }
                break;
            }
            question.put(q);
        }

        main.put("answers", question);
        Log.d("JSONResponse", main.toString());
        JsonObjectRequest jsonReq = new SurveriorJSONRequest(AppConfig.URL_ROOT+"/api/surveys/"+id+"/fill", main, session, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(getApplicationContext(), "Response submitted succesfully!", Toast.LENGTH_LONG).show();

                // How?
                pDialog.hide();
                Intent i = new Intent(getApplication(), MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("FROM_CRITERIA","OK");

                startActivity(i);
                finish();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                String errorStr = "Error: ";

                Log.e(TAG, "Update Error: " + error.getMessage());
                Log.d(TAG, "ERROR: is null? " + (error.networkResponse == null));

                if (error.networkResponse != null) {
                    Log.d(TAG, "ERROR: response data " + new String(error.networkResponse.data));
                    try {
                        JSONObject jObj = new JSONObject(new String(error.networkResponse.data));
                        errorStr += jObj.getString("message");
                    } catch(JSONException e) {
                        errorStr = "Error parse JSON: " + e.getMessage();
                    }
                } else {
                    errorStr += error.getMessage();
                }

                Toast.makeText(getApplicationContext(),
                        errorStr, Toast.LENGTH_LONG).show();
                pDialog.hide();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonReq, "JSON_ADD_SURVEY");
    }

    private boolean isAllFilled() {
        if (survey==null) return false;
        int size = survey.questions.size();
        int id = FIRST_INPUT_ID;
        for(int i = 0; i < size; i++){
            id++;
            Log.d("isAllFilled","Checking id " + id);
            Question question = survey.questions.get(i);
            String type = question.getType();
            switch (type){
                case "Text":{
                    Log.d("isAllFilled","Text");
                    EditText et = (EditText) findViewById(id);
                    if (et != null) {
                        String temp = et.getText().toString().trim();
                        if (temp.isEmpty()) return false;
                    }
                }
                    break;
                case "Dropdown":
                    Log.d("isAllFilled","Dropdown");
                    break;
                case "Scale":
                    Log.d("isAllFilled","Scale");
                    break;
                case "Checkbox":{
                    Log.d("isAllFilled","Checkbox");
                    CheckboxQuestion cq = (CheckboxQuestion) question;
                    boolean checked = false;
                    for(int j = 0; j < cq.getChoices().size();j++){
                        if(j>0) id++;
                        Log.d("isAllFilled","Checking id " + id);
                        CheckBox cb = (CheckBox) findViewById(id);
                        if(cb.isChecked()){
                            checked = true;
                        }
                    }
                    if(!checked) return false;
                }
                    break;
            }
        }
        return true;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Log.d("FillSurveyActivity","New Intent!");
        super.onNewIntent(intent);
        setIntent(intent);//must store the new intent unless getIntent() will return the old one
    }
}
