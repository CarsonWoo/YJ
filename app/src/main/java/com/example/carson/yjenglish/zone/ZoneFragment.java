package com.example.carson.yjenglish.zone;


import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.carson.yjenglish.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ZoneFragment extends Fragment {

    private CircleImageView portrait;
    private TextView username;
    private TextView signature;
    private TextView signDays;
    private TextView remember;
    private TextView countDown;
    private TextView plan;
    private TextView like;
    private TextView download;
    private TextView comment;
    private TextView setting;
    private ImageView bgImg;

    public ZoneFragment() {
        // Required empty public constructor
    }

    public static ZoneFragment newInstance() {
        return new ZoneFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_zone, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        portrait = view.findViewById(R.id.portrait);
        username = view.findViewById(R.id.username);
        signature = view.findViewById(R.id.signature);
        signDays = view.findViewById(R.id.sign_day);
        remember = view.findViewById(R.id.already_remember);
        countDown = view.findViewById(R.id.count_down);
        plan = view.findViewById(R.id.my_plan);
        like = view.findViewById(R.id.my_like);
        download = view.findViewById(R.id.my_download);
        comment = view.findViewById(R.id.my_comment);
        setting = view.findViewById(R.id.my_setting);
        bgImg = view.findViewById(R.id.zone_bg_img);

        Glide.with(getContext()).load(R.mipmap.ic_launcher_round).asBitmap().into(portrait);
        username.setText("单词小霸王");
        signature.setText("一位背单词萌新");

        Glide.with(this).load(R.drawable.zone_bg).thumbnail(0.6f).into(bgImg);
//        BitmapDrawable bd = (BitmapDrawable) bgImg.getDrawable();
//        bgImg.setImageBitmap(Bitmap.createBitmap(bd.getBitmap(), 0, 0, bd.getBitmap().getWidth(),
//                bd.getBitmap().getHeight() - 1500));
    }

}
