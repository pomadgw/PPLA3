package com.surverior.android.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.surverior.android.R;
import com.surverior.android.model.Kota;
import com.surverior.android.model.KotaFilter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class CriteriaActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private RadioGroup genderRadio;
    private EditText inputAgeFrom;
    private EditText inputAgeTo;
    private Spinner inputCity;
    private Spinner inputJob;
    private Spinner inputProvince;
    private String gender = "x";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criteria);

        //Membuat Toolbar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Create New Survey");
        getSupportActionBar().setSubtitle("Set Criteria");
        getSupportActionBar().setElevation(4);
        genderRadio = (RadioGroup) findViewById(R.id.radioGroupGender);
        genderRadio.clearCheck();
        inputAgeFrom = (EditText) findViewById(R.id.ageFrom);
        inputAgeTo = (EditText) findViewById(R.id.ageTo);
        inputJob = (Spinner) findViewById(R.id.job_spinner);
        inputCity = (Spinner) findViewById(R.id.city_spinner);
        inputProvince = (Spinner) findViewById(R.id.province_spinner);


        genderRadio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) group.findViewById(checkedId);
                if (null != rb && checkedId > -1) {
                    CharSequence text = rb.getText();
                    if (text.equals("Male")) {
                        gender = "m";
                    } else if (text.equals("Female")){
                        gender = "f";
                    } else {
                        gender = "a";
                    }

                }
            }
        });
        // Initializing a String Array
        String[] provinceArray = new String[]{
                "All",
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
                "All",
                "Pelajar",
                "Mahasiswa",
                "Karyawan",
                "Wiraswasta"
        };

        String[] cityArray = new String[]{
                "All"
        };
        final List<String> provinceList = new ArrayList<>(Arrays.asList(provinceArray));
        final List<String> jobList = new ArrayList<>(Arrays.asList(jobArray));
        final List<String> cityList = new ArrayList<>(Arrays.asList(cityArray));

        final ArrayAdapter<String> acehAdapter =  new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, KotaFilter.ACEH);
        final ArrayAdapter<String> sumutAdapter =  new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item,KotaFilter.SUMUT);
        final ArrayAdapter<String> sumbarAdapter =  new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item,KotaFilter.SUMBAR);
        final ArrayAdapter<String> riauAdapter =  new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item,KotaFilter.RIAU);
        final ArrayAdapter<String> kepriAdapter =  new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item,KotaFilter.KEPRI);
        final ArrayAdapter<String> kepbangAdapter =  new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item,KotaFilter.KEPBANG);
        final ArrayAdapter<String> jambiAdapter =  new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item,KotaFilter.JAMBI);
        final ArrayAdapter<String> bengkuluAdapter =  new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item,KotaFilter.BENGKULU);
        final ArrayAdapter<String> sumselAdapter =  new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item,KotaFilter.SUMSEL);
        final ArrayAdapter<String> lampungAdapter =  new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item,KotaFilter.LAMPUNG);
        final ArrayAdapter<String> bantenAdapter =  new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item,KotaFilter.BANTEN);
        final ArrayAdapter<String> jakartaAdapter =  new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item,KotaFilter.JAKARTA);
        final ArrayAdapter<String> jabarAdapter =  new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item,KotaFilter.JABAR);
        final ArrayAdapter<String> jatengAdapter =  new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item,KotaFilter.JATENG);
        final ArrayAdapter<String> diyAdapter =  new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item,KotaFilter.DIY);
        final ArrayAdapter<String> jatimAdapter =  new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item,KotaFilter.JATIM);
        final ArrayAdapter<String> baliAdapter =  new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item,KotaFilter.BALI);
        final ArrayAdapter<String> ntbAdapter =  new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item,KotaFilter.NTB);
        final ArrayAdapter<String> nttAdapter =  new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item,KotaFilter.NTT);
        final ArrayAdapter<String> kalbarAdapter =  new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item,KotaFilter.KALBAR);
        final ArrayAdapter<String> kaltengAdapter =  new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item,KotaFilter.KALTENG);
        final ArrayAdapter<String> kalselAdapter =  new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item,KotaFilter.KALSEL);
        final ArrayAdapter<String> kaltimAdapter =  new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item,KotaFilter.KALTIM);
        final ArrayAdapter<String> gorontaloAdapter =  new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item,KotaFilter.GORONTALO);
        final ArrayAdapter<String> sulselAdapter =  new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item,KotaFilter.SULSEL);
        final ArrayAdapter<String> sultenggaraAdapter =  new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item,KotaFilter.SULTENGGARA);
        final ArrayAdapter<String> sultengAdapter =  new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item,KotaFilter.SULTENG);
        final ArrayAdapter<String> sulutAdapter =  new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item,KotaFilter.SULUT);
        final ArrayAdapter<String> sulbarAdapter =  new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item,KotaFilter.SULBAR);
        final ArrayAdapter<String> malukuAdapter =  new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item,KotaFilter.MALUKU);
        final ArrayAdapter<String> malutAdapter =  new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item,KotaFilter.MALUT);
        final ArrayAdapter<String> papbarAdapter =  new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item,KotaFilter.PAPBAR);
        final ArrayAdapter<String> papuaAdapter =  new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item,KotaFilter.PAPUA);
        final ArrayAdapter<String> kalutAdapter =  new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item,KotaFilter.KALUT);

        // Initializing an ArrayAdapter
        final ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(
                this,R.layout.spinner_item,cityList);

        adapter3.setDropDownViewResource(R.layout.spinner_item);
        // Apply the adapter to the spinner
        inputCity.setAdapter(adapter3);

        // Initializing an ArrayAdapter
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,R.layout.spinner_item,provinceList);

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
                    case 0:
                        inputCity.setAdapter(adapter3);
                        break;
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
                this,R.layout.spinner_item,jobList);

        adapter2.setDropDownViewResource(R.layout.spinner_item);
        // Apply the adapter to the spinner
        inputJob.setAdapter(adapter2);
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
                String ageFrom = inputAgeFrom.getText().toString().trim();
                String ageTo = inputAgeTo.getText().toString().trim();
                String city = inputCity.getSelectedItem().toString().trim();
                String province = inputProvince.getSelectedItem().toString().trim();
                String job = inputJob.getSelectedItem().toString().trim();
                int x = Integer.parseInt(ageFrom);
                int y = Integer.parseInt(ageTo);
                Log.d("Criteria", gender);
                Log.d("Criteria",ageFrom);
                Log.d("Criteria",ageTo);
                Log.d("Criteria",job);
                Log.d("Criteria",province);
                Log.d("Criteria",city);
                if(!ageFrom.isEmpty() && !ageTo.isEmpty() && !gender.equals("x") && x>0 && y<101
                        && x<=y){
                    Intent i = new Intent(getApplication(), TitleActivity.class);
                    i.putExtra("gender",gender);
                    i.putExtra("age_from",ageFrom);
                    i.putExtra("age_to",ageTo);
                    i.putExtra("city",city);
                    i.putExtra("province",province);
                    i.putExtra("job",job);
                    startActivity(i);
                } else {
                    Toast.makeText(getApplicationContext(),
                            "There is invalid input!", Toast.LENGTH_LONG)
                            .show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
