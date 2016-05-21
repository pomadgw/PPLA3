package com.surverior.android.helper;

import android.os.Parcel;

/**
 * Created by Bardan Putra Prananto on 5/1/2016.
 */
public class ScaleQuestion extends Question {
    private String minLabel;
    private String maxLabel;
    private int range;

    public String getMinLabel() {
        return minLabel;
    }

    public void setMinLabel(String minLabel) {
        this.minLabel = minLabel;
    }

    public String getMaxLabel() {
        return maxLabel;
    }

    public void setMaxLabel(String maxLabel) {
        this.maxLabel = maxLabel;
    }

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public ScaleQuestion(String questionDetail, String minLabel, String maxLabel, int range) {
        super(questionDetail, "Scale");
        this.minLabel = minLabel;
        this.maxLabel = maxLabel;
        this.range = range;
    }

    public ScaleQuestion(String questionDetail, int id, String minLabel, String maxLabel, int range) {
        super(questionDetail, "Scale", id);
        this.minLabel = minLabel;
        this.maxLabel = maxLabel;
        this.range = range;
    }

    public ScaleQuestion(Parcel in) {
        super(in);
        this.minLabel = in.readString();
        this.maxLabel = in.readString();
        this.range = in.readInt();
    }
    public static final Creator<ScaleQuestion> CREATOR = new Creator<ScaleQuestion>() {
        @Override
        public ScaleQuestion createFromParcel(Parcel in) {
            return new ScaleQuestion(in);
        }

        @Override
        public ScaleQuestion[] newArray(int size) {
            return new ScaleQuestion[size];
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
        dest.writeString(minLabel);
        dest.writeString(maxLabel);
        dest.writeInt(range);
    }
}
