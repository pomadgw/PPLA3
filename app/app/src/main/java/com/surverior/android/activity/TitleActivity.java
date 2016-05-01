package com.surverior.android.activity;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.surverior.android.R;


public class TitleActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    /*private String gender;
    private String ageFrom;
    private String ageTo;
    private String job;
    private String province;
    private String city;*/
    private EditText inputTitle;
    private EditText inputDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title);

        //Getting data from previous activity for debugging
        /*Bundle extras = getIntent().getExtras();
        gender = extras.getString("gender");
        ageFrom = extras.getString("age_from");
        ageTo = extras.getString("age_to");
        job = extras.getString("job");
        province = extras.getString("province");
        city = extras.getString("city");
        Log.d("FilterCriteria",gender);
        Log.d("FilterCriteria",ageFrom);
        Log.d("FilterCriteria",ageTo);
        Log.d("FilterCriteria",job);
        Log.d("FilterCriteria",province);
        Log.d("FilterCriteria",city);*/

        inputTitle = (EditText) findViewById(R.id.title);
        inputDescription = (EditText) findViewById(R.id.description);
        //Membuat Toolbar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Create New Survey");
        getSupportActionBar().setSubtitle("Set Title and Description");
        getSupportActionBar().setElevation(4);

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
                String title = inputTitle.getText().toString().trim();
                String description = inputDescription.getText().toString().trim();
                if(!title.isEmpty() && !description.isEmpty()){
                    Bundle extras = getIntent().getExtras();
                    extras.putBoolean("NEW_SURVEY", true);
                    extras.putString("title",title);
                    extras.putString("description",description);
                    Intent i = new Intent(getApplication(), QuestionListActivity.class);
                    i.putExtras(extras);
                    startActivity(i);
                } else {

                }

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
