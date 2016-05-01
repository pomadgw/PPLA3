package com.surverior.android.helper;

import android.os.Parcel;

import java.util.ArrayList;

/**
 * Created by Bardan Putra Prananto on 5/1/2016.
 */
public class CheckboxQuestion extends Question{
    private ArrayList<String> choices = new ArrayList<>();

    public CheckboxQuestion(String questionDetail, ArrayList<String> choices) {
        super(questionDetail,"Checkbox");
        this.choices = choices;
    }

    protected CheckboxQuestion(Parcel in) {
        super(in);
        choices = in.createStringArrayList();
    }

    public static final Creator<CheckboxQuestion> CREATOR = new Creator<CheckboxQuestion>() {
        @Override
        public CheckboxQuestion createFromParcel(Parcel in) {
            return new CheckboxQuestion(in);
        }

        @Override
        public CheckboxQuestion[] newArray(int size) {
            return new CheckboxQuestion[size];
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
        dest.writeStringList(choices);
    }
}
