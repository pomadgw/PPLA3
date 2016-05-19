package com.surverior.android.activity;

/**
 * Created by bambang on 4/15/16.
 */
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.pm.PackageInstaller;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
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


public class TimelineFragment extends Fragment implements SurveyAdapter.OnLoadMoreListener,
        SwipeRefreshLayout.OnRefreshListener {

    private SessionManager session;

    private ArrayList<Survey> surveys;
    private SurveriorRequest req;
    private RecyclerView recList;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private SurveyAdapter sa;
    private LinearLayoutManager llm;

    private final int SURVEYS_PER_PAGE = 10;
    private String appendURL=AppConfig.URL_SURVEY_GET_LIST;
    private int total=0;
    private int currentPage;
    private boolean afterRefreshed;

    private Handler timelineHandler;

    public TimelineFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        surveys = new ArrayList<>();
        session = new SessionManager(getActivity().getApplicationContext());

        sa = new SurveyAdapter(this);
        timelineHandler = new Handler();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_timeline, container, false);

        //Inisialisasi RecycleView
        recList = (RecyclerView) rootView.findViewById(R.id.timelineList);
        recList.setHasFixedSize(true);
        llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);

        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_layout) ;
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        mSwipeRefreshLayout.setRefreshing(true);
                                        getSurvey(true);
                                    }
                                }
        );

        //while(requestCount!=0){}
        Log.d("TotalSurvey", "" + surveys.size());

        //stop refresh
        mSwipeRefreshLayout.setRefreshing(false);

        // Inflate the layout for this fragment
        return rootView;

    }

    public void getSurvey(final boolean refresh){
        surveys.clear();
        if(refresh){
            sa.clear();
            sa.notifyDataSetChanged();
            Log.d("GetSurvey", "REFRESH!");
            appendURL=AppConfig.URL_SURVEY_GET_LIST;
        }
        Log.d("Timeline",appendURL);
        req = new SurveriorRequest(Request.Method.GET, appendURL, session,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jObj = new JSONObject(response);
                            JSONArray jData = jObj.getJSONArray("data");
                            for (int i = 0; i < jData.length(); i++) {
                                JSONObject surveyJSON = jData.getJSONObject(i);
                                Log.d("title" + i, surveyJSON.getString("title"));
                                Log.d("desc" + i, surveyJSON.getString("description"));
                                surveys.add(new Survey(surveyJSON.getInt("id"),
                                        surveyJSON.getString("title"),
                                        surveyJSON.getString("description")));
                            }
                            String nextPageURL = jObj.getString("next_page_url");
                            if (nextPageURL != null) appendURL = nextPageURL;
                            total = jObj.getInt("total");
                            currentPage = jObj.getInt("current_page");

                        } catch (JSONException e) {
                            Log.d("JSONSurvey", e.getMessage());
                        }

                        Log.d("TotalSurvey", "" + surveys.size());
                        //stop refresh
                        if (refresh) {
                            sa.addAll(surveys);
                            sa.setLinearLayoutManager(llm);
                            sa.setRecyclerView(recList);
                            recList.setAdapter(sa);
                        } else if(!mSwipeRefreshLayout.isRefreshing()&& !afterRefreshed){
                            sa.setProgressMore(false);
                            sa.addItemMore(surveys);
                        }
                        sa.setTotalEntries(total);
                        sa.setMoreLoading(false);
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
        getSurvey();
        timelineHandler.removeCallbacksAndMessages(null);
        afterRefreshed=true;
        getSurvey(true);
    }

    @Override
    public void onLoadMore() {
        Log.d("MainActivity_", "onLoadMore");
        if(!mSwipeRefreshLayout.isRefreshing()) {
            afterRefreshed=false;
            sa.setProgressMore(true);
            timelineHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    surveys.clear();
                    //sa.setProgressMore(false);
                    getSurvey(false);
                }
            }, 2000);
        }
    }
}
