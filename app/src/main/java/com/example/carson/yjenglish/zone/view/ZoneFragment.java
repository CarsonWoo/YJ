package com.example.carson.yjenglish.zone.view;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.carson.yjenglish.R;
import com.example.carson.yjenglish.zone.view.like.MyLikeAty;
import com.example.carson.yjenglish.zone.view.plan.PlanActivity;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ZoneFragment extends Fragment {

    private final int REQUEST_INFO_CODE = 1000;

    private OnZoneEventListener listener;

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

    private String imgUrl;


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
        imgUrl = "http://cdn.duitang.com/uploads/item/201507/10/20150710045602_wHEBf.jpeg";
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

        Glide.with(getContext()).load(imgUrl).asBitmap().into(portrait);
        username.setText("单词小霸王");
        signature.setText("一位背单词萌新");

        Glide.with(this).load(R.mipmap.zone_bg_img).thumbnail(0.5f).into(bgImg);

        initInfo();
        initPlan();
        initLike();
        initSetting();


    }

    private void initLike() {
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toLike = new Intent(getContext(), MyLikeAty.class);
                startActivity(toLike);
                if (getActivity() != null) {
                    getActivity().overridePendingTransition(R.anim.ani_right_get_into, R.anim.ani_left_sign_out);
                }
            }
        });
    }

    private void initSetting() {
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onSetting();
                }
            }
        });
    }

    private void initInfo() {
        portrait.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toInfo = new Intent(getContext(), InfoAty.class);
                toInfo.putExtra("img_url", imgUrl);
                toInfo.putExtra("name", username.getText().toString());
                toInfo.putExtra("sign", signature.getText().toString());
                startActivityForResult(toInfo, REQUEST_INFO_CODE);
                if (getActivity() != null) {
                    getActivity().overridePendingTransition(R.anim.translate_dialog_in, R.anim.translate_dialog_out);
                }
            }

        });
    }

    private void initPlan() {
        plan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toPlan = new Intent(getContext(), PlanActivity.class);
                startActivity(toPlan);
                if (getActivity() != null) {
                    getActivity().overridePendingTransition(R.anim.ani_right_get_into, R.anim.ani_left_sign_out);
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_INFO_CODE && resultCode == getActivity().RESULT_OK) {

        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnZoneEventListener) {
            listener = (OnZoneEventListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnZoneEventListener");
        }
    }

    public interface OnZoneEventListener {
        void onSetting();
    }
}
