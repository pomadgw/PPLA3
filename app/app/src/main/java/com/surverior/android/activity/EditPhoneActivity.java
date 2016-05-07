package com.surverior.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
public class EditPhoneActivity extends AppCompatActivity{
    private static final String TAG = EditPhoneActivity.class.getSimpleName();

    private Toolbar mToolbar;

    private EditText inputPhone;

    private SessionManager session;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_profile);

        //Membuat Toolbar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Edit Profile");
        getSupportActionBar().setSubtitle("Set Phone Number");
        getSupportActionBar().setElevation(4);

        inputPhone = (EditText) findViewById(R.id.phone_input);

        Intent intent = getIntent();
        String dataPhone = intent.getStringExtra(ProfileFragment.DATA_NAMA);
        inputPhone.setText(dataPhone);

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
                final String newPhone = inputPhone.getText().toString().trim();
                if(!Validator.isValidPhone(newPhone)){
                    Toast.makeText(getApplicationContext(),
                            "Phone is not Valid", Toast.LENGTH_LONG)
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
                                        Toast.makeText(getApplicationContext(), "Phone updated", Toast.LENGTH_LONG).show();
                                        // Launch view main activity
                                        Intent intent = new Intent(EditPhoneActivity.this,MainActivity.class);
                                        intent.putExtra("FROM_PHONE", "OK");
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
                            params.put("phone_number", newPhone);
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
