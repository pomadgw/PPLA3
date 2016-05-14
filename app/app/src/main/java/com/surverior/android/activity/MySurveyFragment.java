package com.surverior.android.activity;

/**
 * Created by bambang on 4/15/16.
 */
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.surverior.android.R;
import com.surverior.android.adapter.MySurveyAdapter;
import com.surverior.android.adapter.SurveyAdapter;
import com.surverior.android.app.AppConfig;
import com.surverior.android.app.AppController;
import com.surverior.android.helper.SessionManager;
import com.surverior.android.helper.SurveriorRequest;
import com.surverior.android.helper.Survey;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MySurveyFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private SessionManager session;

    private ArrayList<Survey> surveys;
    private SurveriorRequest req;
    private RecyclerView recList;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private FloatingActionButton fab;

    public MySurveyFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        surveys = new ArrayList<>();
        session = new SessionManager(getActivity().getApplicationContext());


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_mysurvey, container, false);

        //Inisialisasi RecycleView
        recList = (RecyclerView) rootView.findViewById(R.id.mySurveyList);
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);

        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_layout2) ;

        mSwipeRefreshLayout.setOnRefreshListener(this);

        mSwipeRefreshLayout.post(new Runnable() {
                                     @Override
                                     public void run() {
                                         mSwipeRefreshLayout.setRefreshing(true);
                                         getSurvey();
                                     }
                                 }
        );

        //stop refresh
        mSwipeRefreshLayout.setRefreshing(false);

        fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getActivity(), CriteriaActivity.class);
                startActivity(i);
            }
        });
        // Inflate the layout for this fragment
        return rootView;
    }

    public void getSurvey(){
        if(!surveys.isEmpty()){
            surveys.clear();
            Log.d("Masuk","reclist ga kosong");
        }
        req = new SurveriorRequest(Request.Method.GET, AppConfig.URL_SURVEY_GET_SURVEY, session,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jObj = new JSONObject(response);
                            JSONArray jData = jObj.getJSONArray("data");
                            for(int i = 0; i < jData.length(); i++){
                                JSONObject surveyJSON = jData.getJSONObject(i);
                                surveys.add(new Survey(surveyJSON.getString("title"),
                                        surveyJSON.getString("description")));
                            }
                        } catch (JSONException e) {
                            Log.d("JSONSurvey", e.getMessage());
                        }
                        MySurveyAdapter sa = new MySurveyAdapter(surveys);

                        recList.setAdapter(sa);
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mSwipeRefreshLayout.setRefreshing(false);
            }

        });

        AppController.getInstance().addToRequestQueue(req);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onRefresh() {
        Log.d("Masuk","TEs");
        getSurvey();
    }
}