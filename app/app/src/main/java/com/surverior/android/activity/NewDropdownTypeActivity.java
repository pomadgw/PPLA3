package com.surverior.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.surverior.android.R;


public class NewDropdownTypeActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private LinearLayout layout;
    private Button addBtn;
    static int totalEditTexts = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_dropdown_type);

        //Membuat Toolbar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Create New Survey");
        getSupportActionBar().setSubtitle("New Dropdown Type Question");
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
                editText.setTag("EditText" + totalEditTexts);
                editText.setHint("Options " + totalEditTexts);
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
                totalEditTexts = 0;
                return true;
            case R.id.action_done:
                Intent i = new Intent(getApplication(), QuestionListActivity.class);
                totalEditTexts = 0;
                startActivity(i);
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
