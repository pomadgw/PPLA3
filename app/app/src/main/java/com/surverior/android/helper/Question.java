package com.surverior.android.helper;

import android.os.Parcel;
import android.os.Parcelable;

import com.bignerdranch.expandablerecyclerview.Model.ParentObject;

import java.util.List;

/**
 * Created by Bardan Putra Prananto on 5/1/2016.
 */
public class Question implements Parcelable,ParentObject{
    protected List<Object> mChild;

    public Question(String questionDetail, String type, int id) {
        this.questionDetail = questionDetail;
        this.type = type;
        this.id = id;
    }

    protected String questionDetail;
    protected String type;
    protected int id;

    public Question() {
        this.questionDetail = "";
        this.type = "";
    }

    public Question(String questionDetail) {
        this.questionDetail = questionDetail;
        this.type = "Text";
    }

    protected Question(String questionDetail, String type){
        this.questionDetail = questionDetail;
        this.type = type;
    }

    protected Question(Parcel in) {
        questionDetail = in.readString();
        type = in.readString();
    }

    public static final Creator<Question> CREATOR = new Creator<Question>() {
        @Override
        public Question createFromParcel(Parcel in) {
            return new Question(in);
        }

        @Override
        public Question[] newArray(int size) {
            return new Question[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(questionDetail);
        dest.writeString(type);
    }

    public String getQuestionDetail() {
        return questionDetail;
    }

    public String getType() {
        return type;
    }

    public void setQuestionDetail(String questionDetail) {
        this.questionDetail = questionDetail;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public List<Object> getChildObjectList() {
        return mChild;
    }

    @Override
    public void setChildObjectList(List<Object> list) {
        mChild = list;
    }

}
