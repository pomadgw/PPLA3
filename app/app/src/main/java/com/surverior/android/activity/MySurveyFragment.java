package com.surverior.android.activity;

/**
 * Created by bambang on 4/15/16.
 */
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.surverior.android.R;
import com.surverior.android.adapter.SurveyAdapter;
import com.surverior.android.helper.Survey;

import java.util.ArrayList;
import java.util.List;


public class MySurveyFragment extends Fragment {

    private FloatingActionButton fab;

    public MySurveyFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


//        fab.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                doMyThing();
//            }
//        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_mysurvey, container, false);

        //Inisialisasi RecycleView
        RecyclerView recList = (RecyclerView) rootView.findViewById(R.id.mySurveyList);
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);

        SurveyAdapter sa = new SurveyAdapter(createList(30));
        recList.setAdapter(sa);

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

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private List createList(int size) {

        List result = new ArrayList();
        for (int i=1; i <= size; i++) {
            Survey s = new Survey("name "+ i, "description " + i);
            result.add(s);
        }
        return result;
    }



}