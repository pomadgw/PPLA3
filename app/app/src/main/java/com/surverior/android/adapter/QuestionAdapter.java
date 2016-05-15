package com.surverior.android.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder;
import com.surverior.android.R;
import com.surverior.android.helper.CheckboxQuestion;
import com.surverior.android.helper.DropdownQuestion;
import com.surverior.android.helper.Question;
import com.surverior.android.helper.ScaleQuestion;

import java.util.ArrayList;
import java.util.Collections;
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

    public void remove(int position) {
        questionList.remove(position);
        notifyItemRemoved(position);
    }

    public void swap(int firstPosition, int secondPosition){
        Collections.swap(questionList, firstPosition, secondPosition);
        notifyItemMoved(firstPosition, secondPosition);
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


    public static class QuestionViewHolder extends ParentViewHolder {
        protected TextView question;
        protected TextView type;

        public QuestionViewHolder(View v) {
            super(v);
            question =  (TextView) v.findViewById(R.id.cardlabel_question);
            type = (TextView)  v.findViewById(R.id.cardlabel_type);
        }
    }

    public static class QuestionChildViewHolder extends ChildViewHolder {
        protected TextView option;

        public QuestionChildViewHolder(View v) {
            super(v);
            option =  (TextView) v.findViewById(R.id.child_question);

        }
    }

    public void add(Question question){
        this.questionList.add(question);
    }

    public void delete(int position){
        this.questionList.remove(position);
    }

    // For debugging
    public void logging(){
        Question question = questionList.get(questionList.size() - 1);
        Log.d("lastQuestionAdded",question.getType() + " " + question.getQuestionDetail());
        switch (question.getType()){
            case "Text":
                break;
            case "Checkbox":
            {CheckboxQuestion check = (CheckboxQuestion) question;
                ArrayList<String> choices = check.getChoices();
                for(int i = 0; i < choices.size();i++){
                    Log.d("Choice#"+i,choices.get(i));
                }
                break;}
            case "Dropdown":
            {DropdownQuestion drop = (DropdownQuestion) question;
                ArrayList<String> choices = drop.getChoices();
                for(int i = 0; i < choices.size()-1;i++){
                    Log.d("Choice#"+i,choices.get(i));
                }
                break;}
            case "Scale":
            {ScaleQuestion scale = (ScaleQuestion) question;
                Log.d("min",scale.getMinLabel());
                Log.d("max",scale.getMaxLabel());
                Log.d("range",scale.getRange()+"");
                break;}
        }
    }
}
