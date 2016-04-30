package com.surverior.android.activity;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.surverior.android.R;


public class QuestionListActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private com.github.clans.fab.FloatingActionButton textFab;
    private com.github.clans.fab.FloatingActionButton checkFab;
    private com.github.clans.fab.FloatingActionButton dropFab;
    private com.github.clans.fab.FloatingActionButton scaleFab;

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
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.action_done:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
