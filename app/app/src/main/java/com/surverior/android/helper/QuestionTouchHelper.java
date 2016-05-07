package com.surverior.android.helper;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.surverior.android.adapter.QuestionAdapter;

/**
 * Created by bambang on 5/8/16.
 */
public class QuestionTouchHelper extends ItemTouchHelper.SimpleCallback {
    private QuestionAdapter mQuestionAdapter;

    public QuestionTouchHelper(QuestionAdapter questionAdapter){
        super(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.mQuestionAdapter = questionAdapter;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        //TODO: Not implemented here
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        //Remove item
        mQuestionAdapter.remove(viewHolder.getAdapterPosition());
    }
}


