package com.example.carson.yjenglish.customviews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.example.carson.yjenglish.R;

/**
 * Created by 84594 on 2018/8/10.
 */

public class CorrectOrWrongImageView extends RelativeLayout {

    private RoundRectImageView mainImg;
    private RoundRectImageView checkImg;

    public CorrectOrWrongImageView(Context context) {
        super(context);
        init(context);
    }

    public CorrectOrWrongImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CorrectOrWrongImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.layout_correct_or_wrong_graph, this, true);
        mainImg = findViewById(R.id.main_img);
        checkImg = findViewById(R.id.check_img);
    }

    public RoundRectImageView getMainImg() {
        return mainImg;
    }

    public RoundRectImageView getCheckImg() {
        return checkImg;
    }
}
