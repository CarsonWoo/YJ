package com.example.carson.yjenglish.zone.view;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.carson.yjenglish.BaseActivity;
import com.example.carson.yjenglish.MyApplication;
import com.example.carson.yjenglish.R;
import com.example.carson.yjenglish.home.model.LoadHeader;
import com.example.carson.yjenglish.utils.UserConfig;
import com.example.carson.yjenglish.zone.ZoneTask;
import com.example.carson.yjenglish.zone.model.ZoneInfo;
import com.example.carson.yjenglish.zone.presenter.ZonePresenter;
import com.example.carson.yjenglish.zone.view.comment.MyCommentAty;
import com.example.carson.yjenglish.zone.view.download.MyDownloadAty;
import com.example.carson.yjenglish.zone.view.like.MyLikeAty;
import com.example.carson.yjenglish.zone.view.plan.PlanActivity;
import com.example.carson.yjenglish.zone.viewbinder.ZoneContract;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class ZoneFragment extends Fragment implements ZoneContract.View {

    private final int REQUEST_INFO_CODE = 1000;

    private View view;

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
    private String gender;
    private String nameStr;
    private String mSignature;

    private ZoneContract.Presenter presenter;
    private ZonePresenter zonePresenter;

    public ZoneFragment() {
        // Required empty public constructor
    }

    public static ZoneFragment newInstance() {
        return new ZoneFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_zone, container, false);
        executeTask();
        bindViews();
//        imgUrl = "http://cdn.duitang.com/uploads/item/201507/10/20150710045602_wHEBf.jpeg";
//        initViews(view);
        return view;
    }

    private void bindViews() {
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

        Glide.with(this).load(R.mipmap.zone_bg_img).thumbnail(0.5f).into(bgImg);

        initInfo();
        initPlan();
        initLike();
        initDownload();
        initComment();
        initSetting();
    }

    private void initComment() {
        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toComment = new Intent(getContext(), MyCommentAty.class);
                startActivity(toComment);
                if (getActivity() != null) {
                    getActivity().overridePendingTransition(R.anim.ani_right_get_into, R.anim.ani_left_sign_out);
                }
            }
        });
    }

    private void executeTask() {
        ZoneTask task = ZoneTask.getInstance();
        zonePresenter = new ZonePresenter(task, this);
        this.setPresenter(zonePresenter);
        presenter.getUserInfo(UserConfig.getToken(getContext()));
    }

    private void initViews() {
        username.setText(nameStr);
        signature.setText(mSignature);

        Glide.with(this)
                .load(imgUrl)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .crossFade()
                .into(portrait);


    }

    private void initDownload() {
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (getActivity() != null) {
//                    String externalPath = getActivity().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
//                    for (int i = 0; i < 3; i++) {
//                        File mFile = new File(externalPath + "/lexicon/" + "考研词汇_3000单词" + i + ".txt");
//                        if (!mFile.exists()) {
//                            try {
//                                mFile.createNewFile();
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                        }/* else {
//                            Log.e("Zone", "mFile != null");
//                        }*/
//                    }
//                }
                Intent toDownload = new Intent(getContext(), MyDownloadAty.class);
                startActivity(toDownload);
                if (getActivity() != null) {
                    getActivity().overridePendingTransition(R.anim.ani_right_get_into, R.anim.ani_left_sign_out);
                }
            }
        });
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
                toInfo.putExtra("name", nameStr);
                toInfo.putExtra("sign", mSignature);
                toInfo.putExtra("gender", gender);
                startActivityForResult(toInfo, REQUEST_INFO_CODE);
                if (getActivity() != null) {
                    getActivity().overridePendingTransition(R.anim.translate_dialog_in,
                            R.anim.translate_dialog_out);
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
        if (requestCode == REQUEST_INFO_CODE && resultCode == Activity.RESULT_OK) {
            imgUrl = data.getStringExtra("portrait_url");
            nameStr = data.getStringExtra("username");
            mSignature = data.getStringExtra("signature");
            Glide.with(getContext())
                    .load(imgUrl)
                    .crossFade().into(portrait);
            username.setText(nameStr);
            signature.setText(mSignature);
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

    @Override
    public void setPresenter(ZoneContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onSuccess(ZoneInfo info) {
        if (info.getStatus().equals("200")) {
            ZoneInfo.UserInfo mInfo = info.getData();
            imgUrl = mInfo.getPortrait();
            gender = mInfo.getGender();
            nameStr = mInfo.getUsername();
            mSignature = mInfo.getPersonality_signature();
            signDays.setText(mInfo.getInsist_day());
            remember.setText(mInfo.getLearned_word());
            countDown.setText(mInfo.getRemaining_words());
            UserConfig.cacheUsername(getContext(), nameStr);
            initViews();
        } else {
//            Toast.makeText(getContext(), info.getMsg(), Toast.LENGTH_SHORT).show();
            if (info.getStatus().equals("400") && info.getMsg().equals("身份认证错误！")) {
                UserConfig.clearUserInfo(getContext());
                SharedPreferences.Editor editor = MyApplication.getContext().
                        getSharedPreferences("word_favours", MODE_PRIVATE).edit();
                editor.clear();
                editor.apply();
                DataSupport.deleteAll(LoadHeader.class);
                BaseActivity.tokenOutOfDate(getActivity());
            }
        }
    }

    @Override
    public void onFailed(String msg) {
//        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                executeTask();
            }
        }, 5000);
    }

    public interface OnZoneEventListener {
        void onSetting();
    }
}
