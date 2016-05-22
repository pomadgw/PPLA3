package com.surverior.android.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.surverior.android.R;
import com.surverior.android.helper.CheckboxQuestion;
import com.surverior.android.helper.DropdownQuestion;
import com.surverior.android.helper.Question;
import com.surverior.android.helper.ScaleQuestion;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bambang on 5/22/16.
 */
public class FillSurveyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TEXT = 0;
    private final int VIEW_DROPDOWN = 1;
    private final int VIEW_CHECKBOX = 2;
    private final int VIEW_SCALE = 3;
    private final int FIRST_INPUT_ID = 100;
    private int id = 0;

    private List<Question> questionList;

    public FillSurveyAdapter(List<Question> questionList) {
        this.questionList = questionList;
    }

    @Override
    public int getItemCount() {
        return questionList.size();
    }

    @Override
    public int getItemViewType(int position) {
        String type = questionList.get(position).getType();
        if (type.equals("Text")) {
            Log.d("getItemViewType","Masuk tipe text");
            return VIEW_TEXT;
        } else if (type.equals("Dropdown")) {
            Log.d("getItemViewType","Masuk tipe dropdown");
            return VIEW_DROPDOWN;
        } else if (type.equals("Checkbox")) {
            Log.d("getItemViewType","Masuk tipe checkbox");
            return VIEW_CHECKBOX;
        } else {
            Log.d("getItemViewType","Masuk tipe scale");
            return VIEW_SCALE;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TextViewHolder) {
            onBindViewHolder((TextViewHolder) holder,position);
        } else if (holder instanceof DropdownViewHolder) {
            onBindViewHolder((DropdownViewHolder) holder,position);
        } else if (holder instanceof CheckboxViewHolder) {
            onBindViewHolder((CheckboxViewHolder) holder,position);
        } else {
            onBindViewHolder((ScaleViewHolder) holder,position);
        }
    }

    public void onBindViewHolder(TextViewHolder tvh, int i) {
        Question q = questionList.get(i);
        tvh.question.setText(q.getQuestionDetail());
        id++;
        tvh.answer.setId(FIRST_INPUT_ID+id);
    }

    public void onBindViewHolder(DropdownViewHolder dvh, int i) {
        DropdownQuestion q = (DropdownQuestion) questionList.get(i);
        dvh.question.setText(q.getQuestionDetail());

        ArrayList<String> choices = q.getChoices();
        dvh.spinnerArrayAdapter.addAll(choices);
        dvh.answer.setAdapter(dvh.spinnerArrayAdapter);
        id++;
        dvh.answer.setId(FIRST_INPUT_ID+id);
    }

    public void onBindViewHolder(CheckboxViewHolder cvh, int i) {
        CheckboxQuestion q = (CheckboxQuestion) questionList.get(i);
        cvh.question.setText(q.getQuestionDetail());

        ArrayList<String> choices = q.getChoices();
        for (int j = 0; j < choices.size(); j++) {
            CheckBox c = new CheckBox(cvh.context);
            cvh.checkboxLayout.addView(c);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) c.getLayoutParams();
            layoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT;
            c.setLayoutParams(layoutParams);
            c.setText(choices.get(j));
            id++;
            c.setId(FIRST_INPUT_ID+id);
        }
    }

    public void onBindViewHolder(ScaleViewHolder svh, int i) {
        ScaleQuestion q = (ScaleQuestion) questionList.get(i);
        svh.question.setText(q.getQuestionDetail());

        ArrayList<String> scales = new ArrayList<>();
        for (int j = 1; j <= q.getRange(); j++) {
            if (j == 1) {
                scales.add(j + " - " + q.getMinLabel());
            }else if (j == q.getRange()) {
                scales.add(j + " - " + q.getMaxLabel());
            }else{
                scales.add(""+j);
            }
        }

        svh.spinnerArrayAdapter.addAll(scales);
        svh.answer.setAdapter(svh.spinnerArrayAdapter);
        id++;
        svh.answer.setId(FIRST_INPUT_ID+id);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (i == VIEW_TEXT) {
            return new TextViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_text_type, viewGroup, false));
        } else if (i == VIEW_DROPDOWN) {
            return new DropdownViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_dropdown_type, viewGroup, false));
        } else if (i == VIEW_CHECKBOX) {
            return new CheckboxViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_checkboxes_type, viewGroup, false));
        } else {
            return new ScaleViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_scale_type, viewGroup, false));
        }
    }

    public static class TextViewHolder extends RecyclerView.ViewHolder {
        protected TextView question;
        protected EditText answer;

        public TextViewHolder(View v) {
            super(v);
            question = (TextView) v.findViewById(R.id.text_question);
            answer = (EditText)  v.findViewById(R.id.text_answer);
        }
    }

    public static class DropdownViewHolder extends RecyclerView.ViewHolder {
        protected TextView question;
        protected Spinner answer;
        protected ArrayAdapter<String> spinnerArrayAdapter;

        public DropdownViewHolder(View v) {
            super(v);
            question =  (TextView) v.findViewById(R.id.dropdown_question);
            answer = (Spinner)  v.findViewById(R.id.dropdown_answer);
            spinnerArrayAdapter = new ArrayAdapter<>(v.getContext(), android.R.layout.simple_spinner_item); //selected item will look like a spinner set from XML
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        }
    }

    public static class CheckboxViewHolder extends RecyclerView.ViewHolder {
        protected TextView question;
        protected LinearLayout checkboxLayout;
        protected Context context;

        public CheckboxViewHolder(View v) {
            super(v);
            question =  (TextView) v.findViewById(R.id.checkbox_question);
            checkboxLayout = (LinearLayout) v.findViewById(R.id.checkbox_answer);
            context = v.getContext();
        }
    }

    public static class ScaleViewHolder extends RecyclerView.ViewHolder {
        protected TextView question;
        protected Spinner answer;
        protected ArrayAdapter<String> spinnerArrayAdapter;

        public ScaleViewHolder(View v) {
            super(v);
            question =  (TextView) v.findViewById(R.id.scale_question);
            answer = (Spinner)  v.findViewById(R.id.scale_answer);
            spinnerArrayAdapter = new ArrayAdapter<>(v.getContext(), android.R.layout.simple_spinner_item); //selected item will look like a spinner set from XML
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        }
    }
}
