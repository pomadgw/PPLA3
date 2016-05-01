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
import android.widget.AdapterView;
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
import com.surverior.android.model.Kota;

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
    private Spinner inputCity;
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
        inputCity = (Spinner) findViewById(R.id.city);
        inputProvince = (Spinner) findViewById(R.id.province);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);

        //input calendar
        ib = (ImageButton) findViewById(R.id.imageButton1);
        cal = Calendar.getInstance();
        day = cal.get(Calendar.DAY_OF_MONTH);
        month = cal.get(Calendar.MONTH);
        year = cal.get(Calendar.YEAR);
        inputDate = (EditText) findViewById(R.id.birthdate);

        radioGroup = (RadioGroup) findViewById(R.id.radioGroupGender);
        radioGroup.clearCheck();


        // set text date input default
        inputDate.setText(new StringBuilder().append("Birthday"));

        inputDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DATE_ID);
            }
        });

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
                "Province",
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
                "Sulawesi Tengah",
                "Sulawesi Tenggara",
                "Sulawesi Utara",
                "Sumatera Barat",
                "Sumatera Selatan",
                "Sumatera Utara",
        };

        String[] jobArray = new String[]{
                "Job",
                "Pelajar",
                "Mahasiswa",
                "Karyawan",
                "Wiraswasta"
        };

        String[] cityArray = new String[]{
                "City"
        };
        final List<String> provinceList = new ArrayList<>(Arrays.asList(provinceArray));
        final List<String> jobList = new ArrayList<>(Arrays.asList(jobArray));
        final List<String> cityList = new ArrayList<>(Arrays.asList(cityArray));

        final ArrayAdapter<String> acehAdapter =  new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item,Kota.ACEH);
        final ArrayAdapter<String> sumutAdapter =  new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item,Kota.SUMUT);
        final ArrayAdapter<String> sumbarAdapter =  new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item,Kota.SUMBAR);
        final ArrayAdapter<String> riauAdapter =  new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item,Kota.RIAU);
        final ArrayAdapter<String> kepriAdapter =  new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item,Kota.KEPRI);
        final ArrayAdapter<String> kepbangAdapter =  new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item,Kota.KEPBANG);
        final ArrayAdapter<String> jambiAdapter =  new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item,Kota.JAMBI);
        final ArrayAdapter<String> bengkuluAdapter =  new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item,Kota.BENGKULU);
        final ArrayAdapter<String> sumselAdapter =  new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item,Kota.SUMSEL);
        final ArrayAdapter<String> lampungAdapter =  new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item,Kota.LAMPUNG);
        final ArrayAdapter<String> bantenAdapter =  new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item,Kota.BANTEN);
        final ArrayAdapter<String> jakartaAdapter =  new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item,Kota.JAKARTA);
        final ArrayAdapter<String> jabarAdapter =  new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item,Kota.JABAR);
        final ArrayAdapter<String> jatengAdapter =  new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item,Kota.JATENG);
        final ArrayAdapter<String> diyAdapter =  new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item,Kota.DIY);
        final ArrayAdapter<String> jatimAdapter =  new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item,Kota.JATIM);
        final ArrayAdapter<String> baliAdapter =  new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item,Kota.BALI);
        final ArrayAdapter<String> ntbAdapter =  new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item,Kota.NTB);
        final ArrayAdapter<String> nttAdapter =  new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item,Kota.NTT);
        final ArrayAdapter<String> kalbarAdapter =  new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item,Kota.KALBAR);
        final ArrayAdapter<String> kaltengAdapter =  new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item,Kota.KALTENG);
        final ArrayAdapter<String> kalselAdapter =  new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item,Kota.KALSEL);
        final ArrayAdapter<String> kaltimAdapter =  new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item,Kota.KALTIM);
        final ArrayAdapter<String> gorontaloAdapter =  new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item,Kota.GORONTALO);
        final ArrayAdapter<String> sulselAdapter =  new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item,Kota.SULSEL);
        final ArrayAdapter<String> sultenggaraAdapter =  new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item,Kota.SULTENGGARA);
        final ArrayAdapter<String> sultengAdapter =  new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item,Kota.SULTENG);
        final ArrayAdapter<String> sulutAdapter =  new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item,Kota.SULUT);
        final ArrayAdapter<String> sulbarAdapter =  new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item,Kota.SULBAR);
        final ArrayAdapter<String> malukuAdapter =  new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item,Kota.MALUKU);
        final ArrayAdapter<String> malutAdapter =  new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item,Kota.MALUT);
        final ArrayAdapter<String> papbarAdapter =  new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item,Kota.PAPBAR);
        final ArrayAdapter<String> papuaAdapter =  new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item,Kota.PAPUA);
        final ArrayAdapter<String> kalutAdapter =  new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item,Kota.KALUT);

        // Initializing an ArrayAdapter
        final ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(
                this,R.layout.spinner_item,cityList){
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

        adapter3.setDropDownViewResource(R.layout.spinner_item);
        // Apply the adapter to the spinner
        inputCity.setAdapter(adapter3);

        // Initializing an ArrayAdapter
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
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
        inputProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView,
                                       View selectedItemView, int position, long id) {
                // Object item = parentView.getItemAtPosition(position);

                // Depend on first spinner value set adapter to 2nd spinner
                switch(position){
                    case 1:
                        inputCity.setAdapter(acehAdapter);
                        break;
                    case 2:
                        inputCity.setAdapter(baliAdapter);
                        break;
                    case 3:
                        inputCity.setAdapter(bantenAdapter);
                        break;
                    case 4:
                        inputCity.setAdapter(bengkuluAdapter);
                        break;
                    case 5:
                        inputCity.setAdapter(diyAdapter);
                        break;
                    case 6:
                        inputCity.setAdapter(gorontaloAdapter);
                        break;
                    case 7:
                        inputCity.setAdapter(jakartaAdapter);
                        break;
                    case 8:
                        inputCity.setAdapter(jambiAdapter);
                        break;
                    case 9:
                        inputCity.setAdapter(jabarAdapter);
                        break;
                    case 10:
                        inputCity.setAdapter(jatengAdapter);
                        break;
                    case 11:
                        inputCity.setAdapter(jatimAdapter);
                        break;
                    case 12:
                        inputCity.setAdapter(kalbarAdapter);
                        break;
                    case 13:
                        inputCity.setAdapter(kalselAdapter);
                        break;
                    case 14:
                        inputCity.setAdapter(kaltengAdapter);
                        break;
                    case 15:
                        inputCity.setAdapter(kaltimAdapter);
                        break;
                    case 16:
                        inputCity.setAdapter(kalutAdapter);
                        break;
                    case 17:
                        inputCity.setAdapter(kepbangAdapter);
                        break;
                    case 18:
                        inputCity.setAdapter(kepriAdapter);
                        break;
                    case 19:
                        inputCity.setAdapter(lampungAdapter);
                        break;
                    case 20:
                        inputCity.setAdapter(malukuAdapter);
                        break;
                    case 21:
                        inputCity.setAdapter(malutAdapter);
                        break;
                    case 22:
                        inputCity.setAdapter(ntbAdapter);
                        break;
                    case 23:
                        inputCity.setAdapter(nttAdapter);
                        break;
                    case 24:
                        inputCity.setAdapter(papuaAdapter);
                        break;
                    case 25:
                        inputCity.setAdapter(papbarAdapter);
                        break;
                    case 26:
                        inputCity.setAdapter(riauAdapter);
                        break;
                    case 27:
                        inputCity.setAdapter(sulbarAdapter);
                        break;
                    case 28:
                        inputCity.setAdapter(sulselAdapter);
                        break;
                    case 29:
                        inputCity.setAdapter(sultengAdapter);
                        break;
                    case 30:
                        inputCity.setAdapter(sultenggaraAdapter);
                        break;
                    case 31:
                        inputCity.setAdapter(sulutAdapter);
                        break;
                    case 32:
                        inputCity.setAdapter(sumbarAdapter);
                        break;
                    case 33:
                        inputCity.setAdapter(sumselAdapter);
                        break;
                    case 34:
                        inputCity.setAdapter(sumutAdapter);
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {// do nothing
            }
        });

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

        adapter2.setDropDownViewResource(R.layout.spinner_item);
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
                String city = inputCity.getSelectedItem().toString().trim();
                String province = inputProvince.getSelectedItem().toString().trim();
                String job = inputJob.getSelectedItem().toString().trim();
                String birth= inputDate.getText().toString().trim();

                // SQLite database handler
                db = new SQLiteHandler(getApplicationContext());

                // Fetching user details from SQLite
                HashMap<String, String> user = db.getUserDetails();
                String email = user.get("email");

                if (!name.isEmpty()
                        && !city.equals("City") && !province.isEmpty() && !province.equals("Province")
                        && !job.isEmpty() && !job.equals("Job")
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

        pDialog.setMessage("Update ...");
        showDialog();

        StringRequest strReq = new SurveriorRequest(Request.Method.POST, AppConfig.URL_UPDATE, session, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Update Response: " + response);
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
                Map<String, String> params = new HashMap<>();
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
