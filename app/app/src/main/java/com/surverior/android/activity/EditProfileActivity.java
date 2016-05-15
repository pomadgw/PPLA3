package com.surverior.android.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.surverior.android.R;
import com.surverior.android.app.AppConfig;
import com.surverior.android.app.AppController;
import com.surverior.android.helper.SessionManager;
import com.surverior.android.helper.SurveriorRequest;
import com.surverior.android.helper.Validator;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Azhar Fauzan Dz on 4/16/2016.
 */
public class EditProfileActivity extends AppCompatActivity{
    private static final String TAG = EditProfileActivity.class.getSimpleName();

    private Toolbar mToolbar;

    private EditText inputName;

    private SessionManager session;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        //Membuat Toolbar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Edit Profile");
        getSupportActionBar().setSubtitle("Set Fullname");
        getSupportActionBar().setElevation(4);

        inputName = (EditText) findViewById(R.id.name_input);

        Intent intent = getIntent();
        String dataName = intent.getStringExtra(ProfileFragment.DATA_NAMA);
        inputName.setText(dataName);

        // session manager
        session = new SessionManager(getApplicationContext());

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
                final String newName = inputName.getText().toString().trim();
                if(!Validator.isValidName(newName)){
                    Toast.makeText(getApplicationContext(),
                            "Name is not Valid", Toast.LENGTH_LONG)
                            .show();
                }else{
                    SurveriorRequest req = new SurveriorRequest(Request.Method.POST, AppConfig.URL_UPDATE, session,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONObject jObj = new JSONObject(response);
                                        Log.d(TAG, "response: " + response);
                                        session.remove("INCOMPLETE_DATA");
                                        session.updateName(newName);
                                        Toast.makeText(getApplicationContext(), "Name updated", Toast.LENGTH_LONG).show();
                                        // Launch view main activity
                                        Intent intent = new Intent(EditProfileActivity.this,MainActivity.class);
                                        intent.putExtra("FROM_NAME", "OK");
                                        startActivity(intent);
                                        finish();

                                    } catch (JSONException e) {
                                        Log.d(TAG, e.getMessage());
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() {
                            // Posting params to update url
                            Map<String, String> params = new HashMap<>();
                            params.put("name", newName);
                            return params;
                        }
                    };
                    AppController.getInstance().addToRequestQueue(req, "update_user");
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
