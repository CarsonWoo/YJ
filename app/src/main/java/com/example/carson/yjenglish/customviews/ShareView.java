package com.example.carson.yjenglish.customviews;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.carson.yjenglish.R;

/**
 * Created by 84594 on 2018/10/20.
 */

public class ShareView extends FrameLayout {

    private final int IMAGE_WIDTH = 750;
    private final int IMAGE_HEIGHT = 1333;

    private TextView tvName;
    private TextView signDays;
    private TextView signWords;
    private TextView tvSlogan;

    public ShareView(@NonNull Context context) {
        super(context);
        initViews();
    }

    private void initViews() {
        View layout = View.inflate(getContext(), R.layout.layout_share, this);
        tvName = layout.findViewById(R.id.share_username);
        tvSlogan = layout.findViewById(R.id.share_slogan);
        signDays = layout.findViewById(R.id.sign_day);
        signWords = layout.findViewById(R.id.sign_word);
    }

    public void setUsername(String username) {
        tvName.setText(username);
    }

    public void setWords(String words) {
        signWords.setText(words);
    }

    public void setDays(String days) {
        signDays.setText(days);
    }

    public Bitmap createImage() {
        int res[] = new int[] {R.mipmap.daily_pic_1, R.mipmap.daily_pic_2,
                R.mipmap.daily_pic_3, R.mipmap.daily_pic_4, R.mipmap.daily_pic_5, R.mipmap.daily_pic_6,
                R.mipmap.daily_pic_7, R.mipmap.daily_pic_8, R.mipmap.daily_pic_9,
                R.mipmap.daily_pic_11, R.mipmap.daily_pic_12, R.mipmap.daily_pic_13,
                R.mipmap.daily_pic_14};

        int rNum = (int) (Math.random() * res.length);



        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), res[rNum]).copy(Bitmap.Config.ARGB_8888, true);

        int widthMeasureSpec = MeasureSpec.makeMeasureSpec(bitmap.getWidth(), MeasureSpec.EXACTLY);
        int heightMeasureSpec = MeasureSpec.makeMeasureSpec(bitmap.getHeight(), MeasureSpec.EXACTLY);

        measure(widthMeasureSpec, heightMeasureSpec);
        layout(0, 0, bitmap.getWidth(), bitmap.getHeight());
        Log.e("ShareView", "bitmap width = " + bitmap.getWidth());
        Log.e("ShareView", "bitmap height = " + bitmap.getHeight());

//        Bitmap finalBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight());

        Canvas canvas = new Canvas(bitmap);
        draw(canvas);

        return bitmap;
    }
}
