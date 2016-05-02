package com.surverior.android.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.surverior.android.R;
import com.surverior.android.helper.CheckboxQuestion;
import com.surverior.android.helper.DropdownQuestion;
import com.surverior.android.helper.Question;
import com.surverior.android.helper.ScaleQuestion;
import com.surverior.android.helper.Survey;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bambang on 5/1/16.
 */
public class SurveyAdapter extends RecyclerView.Adapter<SurveyAdapter.SurveyViewHolder> {

    private List<Survey> surveyList;

    public SurveyAdapter(List<Survey> surveyList) {
        this.surveyList = surveyList;
    }

    @Override
    public int getItemCount() {
        return surveyList.size();
    }

    @Override
    public void onBindViewHolder(SurveyViewHolder surveyViewHolder, int i) {
        Survey q = surveyList.get(i);
        surveyViewHolder.name.setText(q.getName());
        surveyViewHolder.description.setText(q.getDescription());
    }

    @Override
    public SurveyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.card_survey, viewGroup, false);

        return new SurveyViewHolder(itemView);
    }


    public static class SurveyViewHolder extends RecyclerView.ViewHolder {
        protected TextView name;
        protected TextView description;

        public SurveyViewHolder(View v) {
            super(v);
            name =  (TextView) v.findViewById(R.id.cardlabel_name);
            description = (TextView)  v.findViewById(R.id.cardlabel_description);
        }
    }

    // For debugging
}
