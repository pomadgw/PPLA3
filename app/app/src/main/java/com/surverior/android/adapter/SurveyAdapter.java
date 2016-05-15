package com.surverior.android.adapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.surverior.android.R;
import com.surverior.android.activity.FillSurveyActivity;
import com.surverior.android.activity.MainActivity;
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
public class SurveyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    private List<Survey> surveyList;
    private int totalEntries;
    private static Toast t;

    private OnLoadMoreListener onLoadMoreListener;
    private LinearLayoutManager mLinearLayoutManager;

    private boolean isMoreLoading = false;
    private int visibleThreshold = 1;
    int firstVisibleItem, visibleItemCount, totalItemCount;

    public interface OnLoadMoreListener{
        void onLoadMore();
    }

    public SurveyAdapter(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener=onLoadMoreListener;
        surveyList =new ArrayList<>();
    }

    public void setTotalEntries(int totalEntries) {
        this.totalEntries = totalEntries;
    }

    public void setLinearLayoutManager(LinearLayoutManager linearLayoutManager){
        this.mLinearLayoutManager=linearLayoutManager;
    }

    public void setRecyclerView(RecyclerView mView){
        mView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                visibleItemCount = recyclerView.getChildCount();
                totalItemCount = mLinearLayoutManager.getItemCount();
                firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();
                Log.d("isMoreLoading",isMoreLoading+"");
                Log.d("totalEntries",totalEntries+"");
                Log.d("totalItemCount",totalItemCount+"");
                if (!isMoreLoading && (totalItemCount - visibleItemCount)<= (firstVisibleItem + visibleThreshold)
                        && (totalItemCount<totalEntries)) {
                    if (onLoadMoreListener != null) {
                        onLoadMoreListener.onLoadMore();
                    }
                    isMoreLoading = true;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return surveyList.size();
    }

    public void onBindViewHolder(SurveyViewHolder surveyViewHolder, int i) {
        Survey q = surveyList.get(i);
        surveyViewHolder.id=q.getID();
        surveyViewHolder.name.setText(q.getName());
        surveyViewHolder.description.setText(q.getDescription());
    }

    @Override
    public int getItemViewType(int position) {
        return surveyList.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if(i==VIEW_ITEM) {
            return new SurveyViewHolder(LayoutInflater.
                    from(viewGroup.getContext()).
                    inflate(R.layout.card_survey, viewGroup, false));
        } else {
            return new ProgressViewHolder(LayoutInflater.
                    from(viewGroup.getContext()).
                    inflate(R.layout.item_progress, viewGroup, false));
        }

    }

    public void addAll(List<Survey> lst){
        surveyList.clear();
        surveyList.addAll(lst);
        notifyDataSetChanged();
    }

    public void addItemMore(List<Survey> lst){
        surveyList.addAll(lst);
        notifyItemRangeChanged(0, surveyList.size());
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof SurveyViewHolder) {
            onBindViewHolder((SurveyViewHolder)holder,position);
        }
    }

    public void setMoreLoading(boolean isMoreLoading) {
        this.isMoreLoading=isMoreLoading;
    }

    public void setProgressMore(final boolean isProgress) {
        if (isProgress) {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    surveyList.add(null);
                    notifyItemInserted(surveyList.size() - 1);
                }
            });
        } else {
            surveyList.remove(surveyList.size() - 1);
            notifyItemRemoved(surveyList.size());
        }
    }

    public static class SurveyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        protected TextView name;
        protected TextView description;
        protected int id;

        public SurveyViewHolder(View v){
            super(v);
            name =  (TextView) v.findViewById(R.id.cardlabel_name);
            description = (TextView)  v.findViewById(R.id.cardlabel_description);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(final View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            builder.setMessage("Continue to filling \"" + name.getText().toString().trim() + "\" survey?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int idDialog) {
                            Intent i = new Intent(v.getContext(), FillSurveyActivity.class);
                            i.putExtra("id",id);
                            v.getContext().startActivity(i);
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
            /*if(t!=null)t.cancel();
            t = Toast.makeText(v.getContext(),"Survey id = " + id,Toast.LENGTH_SHORT);
            t.show();*/
        }

        private Survey getQuestion(int id) {
            return null;
        }
    }

    static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar pBar;
        public ProgressViewHolder(View v) {
            super(v);
            pBar = (ProgressBar) v.findViewById(R.id.pBar);
        }
    }

    public void clear(){
        surveyList.clear();
    }

    // For debugging
}
