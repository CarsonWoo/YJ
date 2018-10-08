package com.example.carson.yjenglish.zone.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by 84594 on 2018/10/6.
 */

public class OthersPlan implements Parcelable {
    private String word_number;
    private String learned_word_number;
    private String plan;

    public OthersPlan() {}

    protected OthersPlan(Parcel in) {
        this.word_number = in.readString();
        this.learned_word_number = in.readString();
        this.plan = in.readString();
    }

    public static final Creator<OthersPlan> CREATOR = new Creator<OthersPlan>() {
        @Override
        public OthersPlan createFromParcel(Parcel in) {
            return new OthersPlan(in);
        }

        @Override
        public OthersPlan[] newArray(int size) {
            return new OthersPlan[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.word_number);
        dest.writeString(this.learned_word_number);
        dest.writeString(this.plan);
    }

    public String getWord_number() {
        return word_number;
    }

    public void setWord_number(String word_number) {
        this.word_number = word_number;
    }

    public String getLearned_word_number() {
        return learned_word_number;
    }

    public void setLearned_word_number(String learned_word_number) {
        this.learned_word_number = learned_word_number;
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }
}
