package com.surverior.android.adapter;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.surverior.android.R;
import com.surverior.android.helper.Survey;

import java.util.List;

/**
 * Created by Azhar Fauzan on 5/14/16.
 */
public class MySurveyAdapter extends RecyclerView.Adapter<MySurveyAdapter.SurveyViewHolder> {

    private List<Survey> surveyList;

    public MySurveyAdapter(List<Survey> surveyList) {
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
        surveyViewHolder.download.setImageResource(R.drawable.ic_get_app_black_24dp);
    }

    @Override
    public SurveyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.card_mysurvey, viewGroup, false);

        return new SurveyViewHolder(itemView);
    }


    public static class SurveyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected TextView name;
        protected TextView description;
        protected ImageView download;

        public SurveyViewHolder(View v) {
            super(v);
            name =  (TextView) v.findViewById(R.id.cardlabel_name);
            description = (TextView)  v.findViewById(R.id.cardlabel_description);
            download = (ImageView) v.findViewById(R.id.download_survey);
            Log.d("DIBUAT","OK");
            download.setOnClickListener(this);
        }

        @Override
        public void onClick(final View view) {
            Toast.makeText(view.getContext(),"Download Survey " + name.getText(),Toast.LENGTH_LONG).show();
        }
    }

    // For debugging
}
