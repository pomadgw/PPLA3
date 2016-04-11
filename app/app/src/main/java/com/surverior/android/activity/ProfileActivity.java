package com.surverior.android.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.surverior.android.R;
import com.surverior.android.app.AppConfig;
import com.surverior.android.app.AppController;
import com.surverior.android.helper.SQLiteHandler;
import com.surverior.android.helper.SessionManager;
import com.surverior.android.helper.SurveriorRequest;
import com.surverior.android.helper.TokenHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Azhar Fauzan Dz on 3/28/2016.
 */
public class ProfileActivity extends Activity {
    private static final String TAG = ProfileActivity.class.getSimpleName();
    private ProgressDialog pDialog;
    private SessionManager session;
    private Button btnSubmit;
    private EditText inputFullName;
    private EditText inputCity;
    private Spinner inputJob;
    private Spinner inputProvince;
    private EditText inputDate;
    private ImageButton ib;
    private Calendar cal;
    private int day;
    private int month;
    private int year;
    private RadioGroup radioGroup;
    public String gender = "x";
    private String email;

    private SQLiteHandler db;

    static final int DATE_ID = 999;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        inputFullName = (EditText) findViewById(R.id.name);
        inputJob = (Spinner) findViewById(R.id.job);
        inputCity = (EditText) findViewById(R.id.city);
        inputProvince = (Spinner) findViewById(R.id.province);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);

        //input calendar
        ib = (ImageButton) findViewById(R.id.imageButton1);
        cal = Calendar.getInstance();
        day = cal.get(Calendar.DAY_OF_MONTH);
        month = cal.get(Calendar.MONTH);
        year = cal.get(Calendar.YEAR);
        inputDate = (EditText) findViewById(R.id.birthdate);

        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioGroup.clearCheck();



        // set text date input default
        inputDate.setText(new StringBuilder().append("Birthday"));

        //calendar image listener
        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DATE_ID);
            }
        });

        //gender radio button listener
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) group.findViewById(checkedId);
                if (null != rb && checkedId > -1) {
                    Toast.makeText(ProfileActivity.this, rb.getText(), Toast.LENGTH_SHORT).show();
                    if (rb.getText().equals("Male")) {
                        gender = "m";
                    } else {
                        gender = "f";
                    }

                }
            }
        });

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // Session manager
        session = new SessionManager(getApplicationContext());

        // Initializing a String Array
        String[] provinceArray = new String[]{
                "-- Select Province --",
                "Aceh",
                "Bali",
                "Banten",
                "Bengkulu",
                "D.I.Yogyakarta",
                "Gorontalo",
                "Jakarta",
                "Jambi",
                "Jawa Barat",
                "Jawa Tengah",
                "Jawa Timur",
                "Kalimantan Barat",
                "Kalimantan Selatan",
                "Kalimantan Tengah",
                "Kalimantan Timur",
                "Kalimantan Utara",
                "Kepulauan Bangka Belitung",
                "Kepulauan Riau",
                "Lampung",
                "Maluku",
                "Maluku Utara",
                "Nusa Tenggara Barat",
                "Nusa Tenggara Timur",
                "Papua",
                "Papua Barat",
                "Riau",
                "Sulawesi Barat",
                "Sulawesi Selatan",
                "Sulawesi Tenggara",
                "Sulawesi Utara",
                "Sumatera Barat",
                "Sumatera Selatan",
                "Sumatera Utara",
        };

        String[] jobArray = new String[]{
                "-- Select Job --",
                "Pelajar",
                "Mahasiswa",
                "Karyawan",
                "Wiraswasta"
        };

        final List<String> provinceList = new ArrayList<>(Arrays.asList(provinceArray));
        final List<String> jobList = new ArrayList<>(Arrays.asList(jobArray));
        // Initializing an ArrayAdapter
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,R.layout.spinner_item,provinceList){
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    // Disable the second item from Spinner
                    return false;
                }
                else
                {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position==0) {
                    // Set the disable item text color
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };

        adapter.setDropDownViewResource(R.layout.spinner_item);
        // Apply the adapter to the spinner
        inputProvince.setAdapter(adapter);

        // Initializing an ArrayAdapter
        final ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(
                this,R.layout.spinner_item,jobList){
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    // Disable the second item from Spinner
                    return false;
                }
                else
                {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position==0) {
                    // Set the disable item text color
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };

        adapter.setDropDownViewResource(R.layout.spinner_item);
        // Apply the adapter to the spinner
        inputJob.setAdapter(adapter2);

        // Check if user is already logged in or not
        if (!session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(ProfileActivity.this,
                    LoginActivity.class);
            startActivity(intent);
            finish();
        }

        // update Button Click event
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String name = inputFullName.getText().toString().trim();
                String city = inputCity.getText().toString().trim();
                String province = inputProvince.getSelectedItem().toString().trim();
                String job = inputJob.getSelectedItem().toString().trim();
                String birth= inputDate.getText().toString().trim();

                // SQLite database handler
                db = new SQLiteHandler(getApplicationContext());

                // Fetching user details from SQLite
                HashMap<String, String> user = db.getUserDetails();
                String email = user.get("email");

                if (!name.isEmpty()
                        && !city.isEmpty() && !province.isEmpty() && !province.equals("-- Select Province --")
                        && !job.isEmpty() && !job.equals("-- Select Job --")
                        && !birth.equals("Birthday") && !gender.equals("x")) {
                    updateProfileUser(name,gender,birth,job,city,province,email);
                } else {
                    Toast.makeText(getApplicationContext(),
                            "There is empty!", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });


    }

    /**
     * Function to store user in MySQL database will post params(tag, name,
     * email, password) to update url
     */
    private void updateProfileUser(final String name, final String gender,
                              final String birthdate, final String job,
                              final String city, final String province, final String email) {
        // Tag used to cancel the request
        String tag_string_req = "req_update";
        String token = session.getToken();
        Log.d("MainActivity", "AAA:" + token);
        TokenHandler tokendb = new TokenHandler(token, session);

        pDialog.setMessage("Update ...");
        showDialog();

        StringRequest strReq = new SurveriorRequest(Request.Method.POST, AppConfig.URL_UPDATE, tokendb, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Update Response: " + response.toString());
                hideDialog();

                session.remove("INCOMPLETE_DATA");

                Toast.makeText(getApplicationContext(), "Data sudah diupdate!", Toast.LENGTH_LONG).show();

                // Launch login activity
                Intent intent = new Intent(
                        ProfileActivity.this,
                        MainActivity.class);
                startActivity(intent);
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
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to update url
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", name);
                params.put("gender",gender);
                params.put("birth_date",birthdate);
                params.put("profession",job);
                params.put("city",city);
                params.put("province",province);
                params.put("email",email);
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_ID:

                // open datepicker dialog.
                // set date picker for current date
                // add pickerListener listner to date picker
                return new DatePickerDialog(this, pickerListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        @Override
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {

            year = selectedYear;
            month = selectedMonth;
            day = selectedDay;

            // Show selected date
            inputDate.setText(new StringBuilder().append(year).append("-").append(month + 1)
                    .append("-").append(day));

        }
    };
}
