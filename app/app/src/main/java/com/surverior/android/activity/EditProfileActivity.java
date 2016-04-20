package com.surverior.android.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.surverior.android.helper.TokenHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Azhar Fauzan Dz on 4/16/2016.
 */
public class EditProfileActivity extends Activity{
    private static final String TAG = EditProfileActivity.class.getSimpleName();

    private TextView name;
    private EditText inputName;
    private Button btnSubmit;

    private TokenHandler tokendb;
    private SessionManager session;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        name = (TextView) findViewById(R.id.name_view);
        inputName = (EditText) findViewById(R.id.name_input);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);

        Intent intent = getIntent();
        String dataName = intent.getStringExtra(ViewProfileActivity.DATA_NAMA);
        inputName.setText(dataName);

        // session manager
        session = new SessionManager(getApplicationContext());

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final String newName = inputName.getText().toString().trim();
                if(newName.equals("")){
                    Toast.makeText(getApplicationContext(),
                            "Nama tidak valid", Toast.LENGTH_LONG)
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
                                        Toast.makeText(getApplicationContext(), "Name Updated", Toast.LENGTH_LONG).show();
                                        // Launch view profile activity
                                        Intent intent = new Intent(
                                                EditProfileActivity.this,
                                                ViewProfileActivity.class);
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

            }
        });

    }
}
