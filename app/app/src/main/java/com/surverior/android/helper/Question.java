package com.surverior.android.helper;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Bardan Putra Prananto on 5/1/2016.
 */
public class Question implements Parcelable{
    protected String questionDetail;
    protected String type;

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
}
