package com.surverior.android.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.surverior.android.R;
import com.surverior.android.helper.Question;

import java.util.List;

/**
 * Created by bambang on 5/1/16.
 */
public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder> {

    private List<Question> questionList;

    public QuestionAdapter(List<Question> questionList) {
        this.questionList = questionList;
    }

    @Override
    public int getItemCount() {
        return questionList.size();
    }

    @Override
    public void onBindViewHolder(QuestionViewHolder questionViewHolder, int i) {
        Question q = questionList.get(i);
        questionViewHolder.question.setText(q.getQuestionDetail());
        questionViewHolder.type.setText(q.getType());
    }

    @Override
    public QuestionViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.card_question, viewGroup, false);

        return new QuestionViewHolder(itemView);
    }


    public static class QuestionViewHolder extends RecyclerView.ViewHolder {
        protected TextView question;
        protected TextView type;

        public QuestionViewHolder(View v) {
            super(v);
            question =  (TextView) v.findViewById(R.id.cardlabel_question);
            type = (TextView)  v.findViewById(R.id.cardlabel_type);
        }
    }
}
