package com.surverior.android.activity;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.surverior.android.R;
import com.surverior.android.adapter.QuestionAdapter;
import com.surverior.android.helper.CheckboxQuestion;
import com.surverior.android.helper.DropdownQuestion;
import com.surverior.android.helper.Question;
import com.surverior.android.helper.ScaleQuestion;
import com.surverior.android.helper.Survey;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class QuestionListActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private String gender;
    private String ageFrom;
    private String ageTo;
    private String job;
    private String province;
    private String city;
    private String title;
    private String description;
    private com.github.clans.fab.FloatingActionButton textFab;
    private com.github.clans.fab.FloatingActionButton checkFab;
    private com.github.clans.fab.FloatingActionButton dropFab;
    private com.github.clans.fab.FloatingActionButton scaleFab;

    private Bundle extras;
    private static Survey survey;
    private static QuestionAdapter qa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_list);

        //Membuat Toolbar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Create New Survey");
        getSupportActionBar().setSubtitle("Your Question List");
        getSupportActionBar().setElevation(4);

        extras = getIntent().getExtras();
        if (extras.getBoolean("NEW_SURVEY")) {
            gender = extras.getString("gender");
            ageFrom = extras.getString("age_from");
            ageTo = extras.getString("age_to");
            job = extras.getString("job");
            province = extras.getString("province");
            city = extras.getString("city");
            title = extras.getString("title");
            description = extras.getString("description");
            // for debugging
            Log.d("FilterCriteria", gender);
            Log.d("FilterCriteria",ageFrom);
            Log.d("FilterCriteria",ageTo);
            Log.d("FilterCriteria",job);
            Log.d("FilterCriteria",province);
            Log.d("FilterCriteria",city);
            Log.d("FilterCriteria",title);
            Log.d("FilterCriteria",description);
            // initiate survey if still null
            survey = new Survey(title,description,gender,Integer.parseInt(ageFrom),
                    Integer.parseInt(ageTo),job,province,city);
            qa = new QuestionAdapter(survey.questions);
        }

        if(extras.getBoolean("NEW_QUESTION")){
            Question question = (Question) extras.getParcelable("question");
            survey.questions.add(question);

            //for debugging
            qa.logging();
            //qa.add(question);
        }

        //Inisialisasi RecycleView
        RecyclerView recList = (RecyclerView) findViewById(R.id.cardList);
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);

        recList.setAdapter(qa);

        //Inisialisasi FAB untuk tiap question type
        textFab = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.menu_text);
        checkFab = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.menu_checkboxes);
        dropFab = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.menu_dropdown);
        scaleFab = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.menu_scale);

        //Set listener untuk masing-masing FAB
        textFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent i = new Intent(getApplication(), NewTextTypeActivity.class);
                startActivity(i);
            }
        });
        checkFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent j = new Intent(getApplication(), NewCheckboxTypeActivity.class);
                startActivity(j);
            }
        });
        dropFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent k = new Intent(getApplication(), NewDropdownTypeActivity.class);
                startActivity(k);
            }
        });
        scaleFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent l = new Intent(getApplication(), NewScaleTypeActivity.class);
                startActivity(l);
            }
        });
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
                //NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.action_done:
                try {
                    sendJSON();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void sendJSON() throws JSONException {
        JSONObject main = new JSONObject();
        main.put("title",survey.getName());
        main.put("description",survey.getDescription());
        main.put("questions",generateQuestionJSONArray());
        Log.d("JSON",main.toString());
    }

    private JSONArray generateQuestionJSONArray() throws JSONException {
        JSONArray result = new JSONArray();
        for(int i = 0; i < survey.questions.size(); i++){
            JSONObject temp = new JSONObject();
            Question q = survey.questions.get(i);
            String type = q.getType();
            temp.put("question",q.getQuestionDetail());
            switch (type){
                case "Text": {
                    temp.put("type", "text");
                    temp.put("args", new JSONObject());
                    break;
                }
                case "Checkbox": {
                    CheckboxQuestion c = (CheckboxQuestion) q;
                    temp.put("type", "checkbox");
                    JSONObject args = new JSONObject();
                        JSONArray choices = new JSONArray();
                        ArrayList<String> cChoices = c.getChoices();
                        for(int j = 0; j < cChoices.size();j++){
                            choices.put(cChoices.get(j));
                        }
                        args.put("choices",choices);
                    temp.put("args",args);
                    break;
                }
                case "Dropdown": {
                    DropdownQuestion d = (DropdownQuestion) q;
                    temp.put("type", "option");
                    JSONObject args = new JSONObject();
                        args.put("type","dropdown");
                        JSONArray options = new JSONArray();
                        ArrayList<String> dChoices = d.getChoices();
                        for(int j = 0; j < dChoices.size();j++){
                                 options.put(dChoices.get(j));
                                }
                        args.put("options",options);
                    temp.put("args",args);
                    break;
                }
                case "Scale": {
                    ScaleQuestion s = (ScaleQuestion) q;
                    temp.put("type", "scale");
                    JSONObject args = new JSONObject();
                        args.put("min_val",1);
                        args.put("max_val",s.getRange());
                        args.put("min_label",s.getMinLabel());
                        args.put("max_label",s.getMaxLabel());
                    temp.put("args",args);
                    break;
                }
            }
            result.put(temp);
        }
        return result;
    }

    @Override
    public void onBackPressed(){
        if(!extras.getBoolean("NEW_SURVEY")){
            survey.questions.remove(survey.questions.size()-1);
        }
        QuestionListActivity.super.onBackPressed();
    }
    // Method untuk generate question asal untuk keperluan viewing
    private List<Question> createList(int size) {
        List<Question> result = new ArrayList<Question>();
        for (int i=1; i <= size; i++) {
            Question q = new Question();
            q.setQuestionDetail("question" + i);
            q.setType("type" + i);
            result.add(q);
        }
        return result;
    }
}
