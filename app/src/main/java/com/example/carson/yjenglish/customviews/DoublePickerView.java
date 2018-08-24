package com.example.carson.yjenglish.customviews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.example.carson.yjenglish.R;

import java.util.List;

import cn.qqtheme.framework.picker.DoublePicker;

/**
 * Created by 84594 on 2018/8/16.
 */

public class DoublePickerView extends RelativeLayout {

    private List<String> firstData;
    private List<String> secondData;

    private PickerView pickerWord;
    private PickerView pickerDay;

    private OnDoublePickerSelectListener mListener;

    private String mFirstSelectedText;
    private String mSecondSelectedText;

    public DoublePickerView(Context context) {
        super(context);
        init();
    }

    public DoublePickerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DoublePickerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }


    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.layout_double_picker, this, true);
        pickerWord = findViewById(R.id.picker_word);
        pickerDay = findViewById(R.id.picker_day);

        pickerDay.setData(firstData);
        pickerWord.setData(secondData);

        pickerDay.setOnSelectListener(new PickerView.onSelectListener() {
            @Override
            public void onSelect(String text, int pos) {
                mFirstSelectedText = text;
                if (mSecondSelectedText != null && mListener != null) {
                    mListener.onSelect(mFirstSelectedText, mSecondSelectedText);
                } else if (mListener != null) {
                    mListener.onSelect(mFirstSelectedText, pickerWord.getText());
                }
            }
        });

        pickerWord.setOnSelectListener(new PickerView.onSelectListener() {
            @Override
            public void onSelect(String text, int pos) {
                mSecondSelectedText = text;
                if (mFirstSelectedText != null && mListener != null) {
                    mListener.onSelect(mFirstSelectedText, mSecondSelectedText);
                } else if (mListener != null) {
                    mListener.onSelect(pickerDay.getText(), mSecondSelectedText);
                }
            }
        });

    }

    public void setSelected(int firstIndex, int secondIndex) {
        pickerDay.setSelected(firstIndex);
        pickerWord.setSelected(secondIndex);
    }

    public String getFirstString(int index) {
        return firstData.get(index);
    }

    public String getSecondString(int index) {
        return secondData.get(index);
    }

    public void setmListener(OnDoublePickerSelectListener mListener) {
        this.mListener = mListener;
    }

    public void setFirstData(List<String> firstData) {
        this.firstData = firstData;
    }

    public void setSecondData(List<String> secondData) {
        this.secondData = secondData;
    }

    public interface OnDoublePickerSelectListener {
        void onSelect(String firstText, String secondText);
    }
}
