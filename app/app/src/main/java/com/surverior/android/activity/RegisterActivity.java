/**
 * Author: Ravi Tamada
 * URL: www.androidhive.info
 * twitter: http://twitter.com/ravitamada
 */
package com.surverior.android.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
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

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.surverior.android.R;
import com.surverior.android.app.AppConfig;
import com.surverior.android.app.AppController;
import com.surverior.android.helper.SQLiteHandler;
import com.surverior.android.helper.SessionManager;

public class RegisterActivity extends Activity {
    private static final String TAG = RegisterActivity.class.getSimpleName();
    private Button btnRegister;
    private Button btnLinkToLogin;
    private EditText inputFullName;
    private EditText inputEmail;
    private EditText inputPassword;
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;
    //Editing
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

    static final int DATE_ID = 999;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        inputFullName = (EditText) findViewById(R.id.name);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        inputJob = (Spinner) findViewById(R.id.job);
        inputCity = (EditText) findViewById(R.id.city);
        inputProvince = (Spinner) findViewById(R.id.province);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnLinkToLogin = (Button) findViewById(R.id.btnLinkToLoginScreen);

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
                    Toast.makeText(RegisterActivity.this, rb.getText(), Toast.LENGTH_SHORT).show();
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

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

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
                if(position == 1)
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
                if(position == 1)
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
        /*// Province input list
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.province_array2, android.R.layout.simple_spinner_item);;
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.spinner_item);
        */

        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(RegisterActivity.this,
                    MainActivity.class);
            startActivity(intent);
            finish();
        }

        // Register Button Click event
        btnRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String name = inputFullName.getText().toString().trim();
                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();
                String city = inputCity.getText().toString().trim();
                String province = inputProvince.getSelectedItem().toString().trim();
                String job = inputJob.getSelectedItem().toString().trim();
                String birth= inputDate.getText().toString().trim();


                if (!name.isEmpty() && !email.isEmpty() && !password.isEmpty()
                        && !city.isEmpty() && !province.isEmpty() && !province.equals("-- Select Province --")
                        && !job.isEmpty() && !job.equals("-- Select Job --")
                        && !birth.equals("Birthday") && !gender.equals("x")) {
                    registerUser(name, email, password,gender,birth,job,city,province);
                } else {
                    Toast.makeText(getApplicationContext(),
                            "There is empty!", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });

        // Link to Login Screen
        btnLinkToLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

    }

    /**
     * Function to store user in MySQL database will post params(tag, name,
     * email, password) to register url
     */
    private void registerUser(final String name, final String email,
                              final String password, final String gender,
                              final String birthdate, final String job,
                              final String city, final String province) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        pDialog.setMessage("Registering ...");
        showDialog();

        StringRequest strReq = new StringRequest(Method.POST, AppConfig.URL_REGISTER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    String error = jObj.getString("type");
                    if (error.equals("success")) {
                        Log.d(TAG, "Success registering....");
                        // User successfully stored in MySQL
                        // Now store the user in sqlite
//                        String uid = jObj.getString("uid");

//                        JSONObject user = jObj.getJSONObject("user");
//                        String name = user.getString("name");
//                        String email = user.getString("email");
  /*                      String gender = user.getString("gender");
                        String birth_date = user.getString("birth_date");
                        String profession = user.getString("profession");
                        String city = user.getString("city");
                        String province = user.getString("province");
                        String created_at = user.getString("created_at");
*/
                        // Inserting row in users table
//                        db.addUser(name, email, uid/*, gender, birth_date,profession, city,province, created_at*/);

                        Toast.makeText(getApplicationContext(), "User successfully registered. Try login now!", Toast.LENGTH_LONG).show();

                        // Launch login activity
                        Intent intent = new Intent(
                                RegisterActivity.this,
                                LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                String errorStr = "";
                Log.e(TAG, "Registration Error: " + error.toString());
                Log.d(TAG, "ERROR: is null? " + (error.networkResponse == null));
                if (error.networkResponse != null) {
                    try {
                        JSONObject jObj = new JSONObject(new String(error.networkResponse.data));
                        errorStr = "Error: " + jObj.getString("error");
                    } catch(JSONException e) {
                        errorStr = "Error parse JSON";
                    }
                }
                Toast.makeText(getApplicationContext(),
                        errorStr, Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", name);
                params.put("email", email);
                params.put("password", password);
                params.put("gender", gender);
                params.put("birth_date", birthdate);
                params.put("profession", job);
                params.put("city", city);
                params.put("province", province);
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
