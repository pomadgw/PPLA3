package com.surverior.android.helper;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Bardan Putra Prananto on 5/1/2016.
 */
public class DropdownQuestion extends Question implements Parcelable{
    private ArrayList<String> choices = new ArrayList<>();

    public DropdownQuestion(String questionDetail, ArrayList<String> choices) {
        super(questionDetail,"Dropdown");
        this.choices = choices;
    }

    public DropdownQuestion(String questionDetail, int id, ArrayList<String> choices) {
        super(questionDetail, "Checkbox", id);
        this.choices = choices;
    }

    protected DropdownQuestion(Parcel in) {
        super(in);
        choices = in.createStringArrayList();
    }

    public static final Creator<DropdownQuestion> CREATOR = new Creator<DropdownQuestion>() {
        @Override
        public DropdownQuestion createFromParcel(Parcel in) {
            return new DropdownQuestion(in);
        }

        @Override
        public DropdownQuestion[] newArray(int size) {
            return new DropdownQuestion[size];
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

    public ArrayList<String> getChoices() {
        return choices;
    }

    public void setChoices(ArrayList<String> choices) {
        this.choices = choices;
    }
}
