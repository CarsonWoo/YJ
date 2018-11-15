package com.example.carson.yjenglish.home.view;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.carson.yjenglish.BaseActivity;
import com.example.carson.yjenglish.FullScreenVideo;
import com.example.carson.yjenglish.MyApplication;
import com.example.carson.yjenglish.R;
import com.example.carson.yjenglish.VideoCaptionModel;
import com.example.carson.yjenglish.adapter.HomeListAdapter;
import com.example.carson.yjenglish.customviews.MyVideoView;
import com.example.carson.yjenglish.home.HomeInfoContract;
import com.example.carson.yjenglish.home.HomeInfoTask;
import com.example.carson.yjenglish.home.HomeService;
import com.example.carson.yjenglish.home.model.HomeInfo;
import com.example.carson.yjenglish.home.model.LoadHeader;
import com.example.carson.yjenglish.home.model.PicHeader;
import com.example.carson.yjenglish.home.presenter.HomeInfoPresenter;
import com.example.carson.yjenglish.home.view.feeds.HomeItemAty;
import com.example.carson.yjenglish.home.view.word.SignAty;
import com.example.carson.yjenglish.home.view.word.SignInAty;
import com.example.carson.yjenglish.home.view.word.WordActivity;
import com.example.carson.yjenglish.utils.CommonInfo;
import com.example.carson.yjenglish.utils.DialogUtils;
import com.example.carson.yjenglish.utils.NetUtils;
import com.example.carson.yjenglish.utils.ScreenUtils;
import com.example.carson.yjenglish.utils.UserConfig;
import com.example.carson.yjenglish.zone.view.plan.PlanAddAty;
import com.tencent.bugly.crashreport.CrashReport;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements HomeListAdapter.OnVideoListener,
        HomeInfoContract.View {

    private final String TAG = "HomeFragment";

    public static final int REQUEST_WORD_CODE = 101;
    public static final int REQUEST_SIGN_CODE = 102;
    public static final int REQUEST_MORE_CODE = 103;
    public static final int REQUEST_ADD_PLAN_CODE = 104;
    public static final int REQUEST_TO_DETAIL = 105;
    public static final int RESULT_WORD_OK = 201;
    public static final int RESULT_SIGN_OK = 202;
    public static final int RESULT_ADD_PLAN_OK = 204;
    public static final int RESULT_LIKE_CHANGE = 205;

    private RecyclerView recyclerView;
    private MyVideoView mVideoView;

    private int mHeaderStyle;

    private View lastView;
    private int videoPos = -1;

    private RecyclerView.LayoutManager mLayoutManager;
    private HomeListAdapter mAdapter;

    private OnHomeInteractListener mListener;
    private OnFullScreenListener fullScreenListener;
    private PicHeader mPicData;
    private LoadHeader mLoadData;
    private List<String> portraits = new ArrayList<>();
    private List<HomeInfo.Data.Feed> mListData = new ArrayList<>();

    private List<VideoCaptionModel> models;

    private boolean isFullClick = false;

    private int clickFeedPos = 0;

    private String mCurDate;

    private Dialog mDialog;

    private View view;

    private HomeInfoContract.Presenter mPresenter;

    private HeaderChangeBroadcastReceiver mReceiver;

    private int mDistance = 0;

    public HomeFragment() {

    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        //如果有数据需要传输 则通过参数 设置bundle
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //作UI请求
        mDialog = DialogUtils.getInstance(getContext()).newCommonDialog("加载中",
                R.mipmap.gif_loading_video, true);
        if (mPresenter != null) {
            mPresenter.getInfo(UserConfig.getToken(MyApplication.getContext()));
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LitePal.getDatabase();
        IntentFilter filter = new IntentFilter();
        filter.addAction("HEADER_CHANGE");
        mReceiver = new HeaderChangeBroadcastReceiver();
        if (getActivity() != null) {
            getActivity().registerReceiver(mReceiver, filter);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
//        if (UserConfig.HasPlan(getContext())) {
//            loadPlanFromDB();
//        } else {
//            loadDataFromDB();
//        }
//        initViews(view);
        return view;
    }

    private void loadDataFromDB() {
        List<PicHeader> list = DataSupport.findAll(PicHeader.class);
        if (list.size() == 0) {
            mPicData = null;
        } else {
            mPicData = DataSupport.where("header_id = ?", "1").find(PicHeader.class).get(0);
        }
        mHeaderStyle = 2;
    }

    private void loadPlanFromDB() {
        List<LoadHeader> list = DataSupport.findAll(LoadHeader.class);
        if (list.size() == 0) {
            mLoadData = null;
        } else {
            mLoadData = DataSupport.where("header_id = ?", "1").find(LoadHeader.class).get(0);
        }
        mHeaderStyle = 3;
//        initViews(view);
    }

    private void showVideo(View view, final String path) {
        View v;
        removeVideoView();
        if (mVideoView == null) {
            mVideoView = new MyVideoView(getContext(), false, false);
        }
        mVideoView.stop();
        v = view.findViewById(R.id.item_video_play);
        if (v != null) v.setVisibility(View.INVISIBLE);
        v = view.findViewById(R.id.img_content);
        if (v != null) v.setVisibility(View.INVISIBLE);
        v = view.findViewById(R.id.video);
        if (v != null) {
            v.setVisibility(View.VISIBLE);
            FrameLayout fl = (FrameLayout) v;
            fl.removeAllViews();
            fl.addView(mVideoView, new ViewGroup.LayoutParams(-1, -1));
            mVideoView.setVideoPath(path);
            mVideoView.start();
        }
        mVideoView.setFullScreenListener(new MyVideoView.IFullScreenListener() {
            @Override
            public void onClickFull(boolean isFull) {
                //if isPlaying 则把progress传过去
                //需要横屏
                isFullClick = true;
                int progress = mVideoView.getPosition();
                Intent toFullScreen = new Intent(getContext(), FullScreenVideo.class);
                toFullScreen.putExtra("progress", progress);
                toFullScreen.putExtra("path", path);
                startActivityForResult(toFullScreen, 1);
                if (getActivity() != null) {
                    getActivity().overridePendingTransition(R.anim.anim_top_rotate_get_into,
                            R.anim.anim_top_rotate_sign_out);
                }
            }
        });

        mVideoView.setOnStopListener(new MyVideoView.IOnStopListener() {
            @Override
            public void onVideoStop() {
                removeVideoView();
//                mVideoView.setVisibility(View.GONE);
                Log.e(TAG, "onStop");
            }
        });

        lastView = view;
    }


    private void removeVideoView() {
        View v;
        if (lastView != null) {
            v = lastView.findViewById(R.id.item_video_play);
            if (v != null) v.setVisibility(View.VISIBLE);
            v = lastView.findViewById(R.id.img_content);
            if (v != null) v.setVisibility(View.VISIBLE);
            v = lastView.findViewById(R.id.video);
            if (v != null) {
                FrameLayout fl = (FrameLayout) v;
                fl.removeAllViews();
                fl.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.e(TAG, "onSaveInstance");
    }

    private void initViews(View view) {
        Log.e(TAG, "initViews()");
        recyclerView = view.findViewById(R.id.rv_container);
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);

        if (mHeaderStyle == 2) {
            mAdapter = new HomeListAdapter(getContext(), mListData, mListener, 2, this);

            mAdapter.setPicItem(mPicData);
            mAdapter.setmPortraitUrls(portraits);
        } else {
            mAdapter = new HomeListAdapter(getContext(), mListData, mListener, 3, this);
            mAdapter.setLoadItem(mLoadData);

        }

        initAdapterListener();
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    private void initAdapterListener() {
        mAdapter.setStartListener(new HomeListAdapter.OnStartListener() {
            @Override
            public void onStartClick(View view) {
                if (mHeaderStyle == 2) {
                    Intent toAddPlan = new Intent(getContext(), PlanAddAty.class);
                    toAddPlan.putExtra("from_intent", PlanAddAty.INTENT_FROM_HOME);
                    startActivityForResult(toAddPlan, REQUEST_ADD_PLAN_CODE);
                    if (getActivity() != null) {
                        getActivity().overridePendingTransition(R.anim.ani_right_get_into, R.anim.ani_left_sign_out);
                    }
                } else {
                    if (Build.VERSION.SDK_INT >= 23) {
                        if (ContextCompat.checkSelfPermission(MyApplication.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                            //2、申请权限: 参数二：权限的数组；参数三：请求码
                            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                        } else {
                            Intent toWord = new Intent(getContext(), WordActivity.class);
                            toWord.putExtra("type", REQUEST_WORD_CODE);
                            startActivityForResult(toWord, REQUEST_WORD_CODE);
                            if (getActivity() != null) {
                                getActivity().overridePendingTransition(R.anim.ani_right_get_into, R.anim.ani_left_sign_out);
                            }
                        }
                    } else {
                        Intent toWord = new Intent(getContext(), WordActivity.class);
                        toWord.putExtra("type", REQUEST_WORD_CODE);
                        startActivityForResult(toWord, REQUEST_WORD_CODE);
                        if (getActivity() != null) {
                            getActivity().overridePendingTransition(R.anim.ani_right_get_into, R.anim.ani_left_sign_out);
                        }
                    }
//                    Intent toSign = new Intent(getContext(), SignAty.class);
//                    toSign.putExtra("username", UserConfig.getUsername(getContext()))
//                            .putExtra("insist_day", mLoadData.getInsistCount())
//                            .putExtra("learned_word", mLoadData.getWordsCount());
//                    startActivity(toSign);

                }
            }
        });
        mAdapter.setLoadListener(new HomeListAdapter.OnLoadHeaderListener() {
            @Override
            public void onSignClick(View view) {
//                Intent toSignIn = new Intent(getContext(), SignInAty.class);
//                startActivityForResult(toSignIn, REQUEST_SIGN_CODE);
                if (Build.VERSION.SDK_INT >= 23) {
                    if (ContextCompat.checkSelfPermission(MyApplication.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                        //2、申请权限: 参数二：权限的数组；参数三：请求码
                        if (getActivity() != null) {
                            ActivityCompat.requestPermissions(getActivity(), new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                        }
                    } else {
                        Intent toSign = new Intent(getContext(), SignAty.class);
                        toSign.putExtra("username", UserConfig.getUsername(MyApplication.getContext()))
                                .putExtra("insist_day", mLoadData.getInsistCount())
                                .putExtra("learned_word", mLoadData.getWordsCount());
                        startActivityForResult(toSign, REQUEST_SIGN_CODE);
                        if (getActivity() != null) {
                            getActivity().overridePendingTransition(R.anim.ani_right_get_into, R.anim.ani_left_sign_out);
                        }
                    }
                } else {
                    Intent toSign = new Intent(getContext(), SignAty.class);
                    toSign.putExtra("username", UserConfig.getUsername(MyApplication.getContext()))
                            .putExtra("insist_day", mLoadData.getInsistCount())
                            .putExtra("learned_word", mLoadData.getWordsCount());
                    startActivityForResult(toSign, REQUEST_SIGN_CODE);
                    if (getActivity() != null) {
                        getActivity().overridePendingTransition(R.anim.ani_right_get_into, R.anim.ani_left_sign_out);
                    }
                }

            }

            @Override
            public void onMoreClick(View view) {
                Intent toWord = new Intent(getContext(), WordActivity.class);
                toWord.putExtra("type", REQUEST_MORE_CODE);
                startActivityForResult(toWord, REQUEST_MORE_CODE);
                if (getActivity() != null) {
                    getActivity().overridePendingTransition(R.anim.ani_right_get_into, R.anim.ani_left_sign_out);
                }
            }
        });

        mAdapter.setItemListener(new HomeListAdapter.OnItemClickListener() {
            /**
             * 点击item的回调
             * @param item
             * @param requestComment 是否一进入就跳转到评论区
             */
            @Override
            public void onClick(ArrayList item, boolean requestComment, int position) {
                clickFeedPos = position;
                HomeInfo.Data.Feed mItem = (HomeInfo.Data.Feed) item.get(0);
                Intent toDetail = new Intent(getContext(), HomeItemAty.class);
                toDetail.putExtra("id", mItem.getId());
                toDetail.putExtra("video_url", mItem.getVideo());
                toDetail.putExtra("img_url", mItem.getPic());
                toDetail.putExtra("username", mItem.getAuthor_username());
                toDetail.putExtra("title", mItem.getTitle());
                toDetail.putExtra("portrait_url", mItem.getAuthor_portrait());
                toDetail.putExtra("like_num", mItem.getLikes());
                toDetail.putExtra("request_comment", requestComment);
//        toDetail.putExtra("is_like", mItem.getIs_favour().equals("1"));
                startActivityForResult(toDetail, REQUEST_TO_DETAIL);
            }

            @Override
            public void onLikeClick(String id, String isFavour, TextView tv) {
                executeFavourTask(id, isFavour, tv);
            }
        });
    }

    private void executeFavourTask(String id, final String isFavour, final TextView tv) {
        Retrofit retrofit = NetUtils.getInstance().getRetrofitInstance(UserConfig.HOST);
        retrofit.create(HomeService.class).postFavours(UserConfig.getToken(MyApplication.getContext()), id)
                .enqueue(new Callback<CommonInfo>() {
                    @Override
                    public void onResponse(Call<CommonInfo> call, Response<CommonInfo> response) {
                        if (response.body().getStatus().equals("200")) {
                            String s = tv.getText().toString();
                            int num = Integer.parseInt(s);
                            if (isFavour.equals("1")) {
                                //点击响应了说明取消了赞
//                                imageView.setSelected(false);
                                tv.setText(String.valueOf(num - 1));
                            } else {
//                                imageView.setSelected(true);
                                tv.setText(String.valueOf(num + 1));
                            }

                        } else {
                            Log.e(TAG, response.body().getMsg());
                        }
                    }

                    @Override
                    public void onFailure(Call<CommonInfo> call, Throwable t) {
                        Toast.makeText(getContext(), "连接超时", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == FullScreenVideo.RESULT_VIDEO_COMPLETE) {
                isFullClick = false;
                if (mVideoView != null) {
                    mVideoView.stop();
                }
            } else if (resultCode == FullScreenVideo.RESULT_VIDEO_NOT_FINISH) {
                if (mVideoView != null) {
                    mVideoView.seekTo(data.getIntExtra("progress", 0));
                    mVideoView.resume();
                }
                isFullClick = false;
            }
        } else if (requestCode == REQUEST_WORD_CODE && resultCode == RESULT_WORD_OK) {
//            Log.e(TAG, "today finish");
            if (data != null) {
                String learned_word = data.getStringExtra("learned_word");
                if (mLoadData != null) {
                    if (learned_word != null) {
                        mLoadData.setWordsCount(mLoadData.getWordsCount() +
                                Integer.parseInt(learned_word));
                    }
                    mLoadData.setInsistCount(mLoadData.getInsistCount() + 1);
                }
            }
            if (mLoadData != null) {
                float progress = ((float) mLoadData.getWordsCount() * 100 /
                        mLoadData.getTargetCount());
                mLoadData.setProgress(progress);
                mLoadData.setTodayFinish(true);
                mLoadData.save();
            }
            mAdapter.notifyItemChanged(0);
//            onRefreshHomeItem();
            Intent toSign = new Intent(getContext(), SignAty.class);
            toSign.putExtra("username", UserConfig.getUsername(getContext()))
                    .putExtra("insist_day", mLoadData.getInsistCount())
                    .putExtra("learned_word", mLoadData.getWordsCount());
            startActivityForResult(toSign, REQUEST_SIGN_CODE);
            if (getActivity() != null) {
                getActivity().sendBroadcast(new Intent("WORDS_START_DOWNLOAD"));
                getActivity().overridePendingTransition(R.anim.ani_right_get_into, R.anim.ani_left_sign_out);
            }
        } else if (requestCode == REQUEST_SIGN_CODE && resultCode == RESULT_SIGN_OK) {
            mLoadData.setSignClick(true);
            mLoadData.save();
            mAdapter.notifyItemChanged(0);
        } else if (requestCode == REQUEST_MORE_CODE && resultCode == Activity.RESULT_OK) {
            onRefreshHomeItem();
            Log.e(TAG, "progress finish");
            if (data != null) {
                String learned_word = data.getStringExtra("learned_word");
                if (learned_word != null) {
                    mLoadData.setWordsCount(mLoadData.getWordsCount() +
                            Integer.parseInt(learned_word));
                }
            }
            float progress = ((float) mLoadData.getWordsCount() * 100 /
                    mLoadData.getTargetCount());
            mLoadData.setProgress(progress);
            mLoadData.setDailyFinish(true);
            mLoadData.save();
            mAdapter.notifyItemChanged(0);
            if (getActivity() != null) {
                getActivity().sendBroadcast(new Intent("WORDS_START_DOWNLOAD"));
            }

        } else if (requestCode == REQUEST_ADD_PLAN_CODE && resultCode == RESULT_ADD_PLAN_OK) {
//            loadPlanFromDB();
//            mHeaderStyle = 3;
//            if (mLoadData == null) {
//                Log.e(TAG, "new Instance");
//                mLoadData = new LoadHeader();
//                mLoadData.setHeader_id(1);
//                mLoadData.setTodayFinish(false);
//                mLoadData.setSignClick(false);
//            }
//            if (data != null) {
//                String rest_days = data.getStringExtra("rest_days");
//                if (rest_days != null) {
//                    mLoadData.setCountDown(Integer.parseInt(rest_days));
//                }
//                int plan_number = data.getIntExtra("plan_number", 0);
//                if (plan_number != 0) {
//                    mLoadData.setTargetCount(plan_number);
//                }
//            }
//            mLoadData.setInsistCount(0);
//            mLoadData.setWordsCount(0);
//            mLoadData.setProgress(0);
//
//            mLoadData.save();
//
//            initViews(view);
            onRefreshHomeItem();
        } else if (requestCode == REQUEST_TO_DETAIL && resultCode == RESULT_LIKE_CHANGE) {
//            Log.e(TAG, "onActivityResult(1)");
            mListData.get(clickFeedPos).setIs_favour(data.getStringExtra("favour_change"));
            RecyclerView.ViewHolder holder = recyclerView.findViewHolderForAdapterPosition(clickFeedPos + 1);
            if (holder != null && holder instanceof HomeListAdapter.HomeViewHolder) {
//                Log.e(TAG, "onActivityResult(2)");
                HomeListAdapter.HomeViewHolder homeHolder = (HomeListAdapter.HomeViewHolder) holder;
                homeHolder.ivFavour.setSelected(mListData.get(clickFeedPos).getIs_favour().equals("1"));
                int num = Integer.parseInt(homeHolder.likeNum.getText().toString());
                if (num < 1000) {
                    if (mListData.get(clickFeedPos).getIs_favour().equals("1")) {
                        if (num + 1 == 1000) {
                            homeHolder.likeNum.setText("1k+");
                        } else {
                            homeHolder.likeNum.setText(String.valueOf(num + 1));
                        }
                    } else {
                        homeHolder.likeNum.setText(String.valueOf(num - 1));
                    }
                } /*else {
                    if (num > 10000) {
                        String s = String.valueOf(num % 10000) + "w+";
                        homeHolder.likeNum.setText(s);
                    } else {
                        String s = String.valueOf(num % 1000) + "k+";
                        homeHolder.likeNum.setText(s);
                    }
                }*/
            }
//            mAdapter.notifyItemChanged(clickFeedPos + 1);//跳过头部
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Intent toWord = new Intent(getContext(), WordActivity.class);
            toWord.putExtra("type", REQUEST_WORD_CODE);
            startActivityForResult(toWord, REQUEST_WORD_CODE);
            if (getActivity() != null) {
                getActivity().overridePendingTransition(R.anim.ani_right_get_into, R.anim.ani_left_sign_out);
            }
        }  else if (requestCode == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Intent toSign = new Intent(getContext(), SignAty.class);
            toSign.putExtra("username", UserConfig.getUsername(MyApplication.getContext()))
                    .putExtra("insist_day", mLoadData.getInsistCount())
                    .putExtra("learned_word", mLoadData.getWordsCount());
            startActivityForResult(toSign, REQUEST_SIGN_CODE);
            if (getActivity() != null) {
                getActivity().overridePendingTransition(R.anim.ani_right_get_into, R.anim.ani_left_sign_out);
            }
        } else {
            Toast.makeText(getContext(), "小呗可能无法正常播放音频和进行分享噢，请在设置中开放权限吧~", Toast.LENGTH_SHORT).show();
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            if (mVideoView != null) {
                mVideoView.pause();
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e(TAG, "isFullClick = " + isFullClick);
        Log.e(TAG, "onStop");
        if (mVideoView != null) {
            if (isFullClick) {
                mVideoView.pause();
            } else {
                mVideoView.stop();
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e(TAG, "isFullClick = " + isFullClick);
        if (mVideoView != null) {
            if (isFullClick) {
                mVideoView.pause();
            } else {
                mVideoView.stop();
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnHomeInteractListener) {
            mListener = (OnHomeInteractListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnHomeInteractListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onVideoClick(View view, String url) {
//        prepareVideo(view, url);
        showVideo(view, url);
    }

    @Override
    public void setPresenter(HomeInfoContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showLoading() {
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();
        WindowManager.LayoutParams lp = mDialog.getWindow().getAttributes();
        lp.width = ScreenUtils.dp2px(getContext(), 260);
        lp.height = ScreenUtils.dp2px(getContext(), 240);
        lp.gravity = Gravity.CENTER;
        mDialog.getWindow().setAttributes(lp);
    }

    @Override
    public void hideLoading() {
        mDialog.dismiss();
    }

    @Override
    public void showError(String msg) {
//        Log.e(TAG, msg);
//        DialogUtils.getInstance(getContext()).newCommonDialog("没有网络噢...", R.mipmap.ic_no_network, false)
//                .show();
        Toast.makeText(getContext(), "请检查网络噢", Toast.LENGTH_SHORT).show();
        if (UserConfig.HasPlan(getContext())) {
            loadPlanFromDB();
            initViews(view);
        } else {
            loadDataFromDB();
            initViews(view);
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                onRefreshHomeItem();
            }
        }, 5000);
    }

    @Override
    public void setInfo(HomeInfo info) {
        Log.e(TAG, "setInfo()");
//        loadPlanFromDB();
        if (info.getStatus().equals("200")) {
            if (info.getData() != null) {
                HomeInfo.Data data = info.getData();
                UserConfig.cacheSelectedPlan(getContext(), info.getData().getMy_plan());
//                Log.e(TAG, data.getMy_plan());
//                Log.e(TAG, UserConfig.getMyPlan(getContext()));
                if (data.getFlag().equals("0") && mHeaderStyle != 2) {
                    //即没有计划
                    initPicItem(data);
                } else if (data.getFlag().equals("1") && mHeaderStyle != 3){
                    initLoadItem(data);
                }
                mListData = data.getFeeds();
                initViews(view);
            }
        } else {
            Toast.makeText(getContext(), info.getMsg(), Toast.LENGTH_SHORT).show();
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

    private void initLoadItem(HomeInfo.Data data) {
        if (!UserConfig.HasPlan(getContext())) {
            UserConfig.cacheHasPlan(getContext(), true);
        }
        mCurDate = UserConfig.getLastDate(getContext());
        //有计划时需要存入数据库
        loadPlanFromDB();
        mHeaderStyle = 3;
        if (mLoadData == null) {
            Log.e(TAG, "new Instance");
            mLoadData = new LoadHeader();
            mLoadData.setHeader_id(1);
            mLoadData.setTodayFinish(false);
            mLoadData.setSignClick(false);
            mLoadData.setDailyFinish(false);
        }
        mLoadData.setCountDown(Integer.parseInt(data.getRest_days()));
        mLoadData.setInsistCount(Integer.parseInt(data.getInsist_days()));
        mLoadData.setTargetCount(Integer.parseInt(data.getPlan_number()));
        mLoadData.setWordsCount(Integer.parseInt(data.getLearned_word()));
        float progress = ((float) Integer.parseInt(data.getLearned_word()) * 100 /
                Integer.parseInt(data.getPlan_number()));
        mLoadData.setProgress(progress);

        String level = data.getLevel();
        int levelInt = Integer.parseInt(level);
        mLoadData.setTodayFinish(levelInt > 0);
        mLoadData.setSignClick(levelInt > 1);
        mLoadData.setDailyFinish(levelInt > 2);

        mLoadData.save();
//        Date mDate = new Date();
//        @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日");
//        String dateStr = df.format(mDate);
//        Log.e(TAG, "mCurDate = " + mCurDate);
//        Log.e(TAG, "dateStr = " + dateStr);
//
//        if (!dateStr.equals(mCurDate)) {
//            mLoadData.setSignClick(false);
//            mLoadData.setTodayFinish(false);
//            mLoadData.save();
//        }
    }

    private void initPicItem(HomeInfo.Data data) {
        mHeaderStyle = 2;
        if (UserConfig.HasPlan(getContext())) {
            UserConfig.cacheHasPlan(getContext(), false);
        }
        loadDataFromDB();
        //无计划时需要存入数据库上一次的数量
        if (mPicData == null) {
            Log.e(TAG, "pic data new Instance");
            mPicData = new PicHeader();
            mPicData.setHeader_id(1);
        }
        mPicData.setNumber(data.getStudy_people());
        mPicData.save();
        portraits = data.getHead_user_portrait();
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    public interface OnHomeInteractListener {
        void onMusicPressed(View view);
        void onProgressClick(View view);
    }

    public interface OnFullScreenListener {
        void onFullScreen(String path, int progress);
    }

    public class HeaderChangeBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Retrofit retrofit = NetUtils.getInstance().getRetrofitInstance(UserConfig.HOST);
            HomeService service = retrofit.create(HomeService.class);
            service.getHomeInfo(UserConfig.getToken(MyApplication.getContext())).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<HomeInfo>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    Toast.makeText(getContext(), "网络开小差了...", Toast.LENGTH_SHORT).show();
                    onRefreshHomeItem();
                }

                @Override
                public void onNext(HomeInfo info) {
                    if (info.getStatus().equals("200")) {
                        if (info.getData().getFlag().equals("0") && mHeaderStyle != 2) {
                            //从有计划变为无计划
                            initPicItem(info.getData());
                            initViews(view);
                        } else if (info.getData().getFlag().equals("1") && mHeaderStyle != 3) {
                            //从无计划变为有计划
                            initLoadItem(info.getData());
                            initViews(view);
                        } else if (info.getData().getFlag().equals("1") && mHeaderStyle == 3) {
                            //更改选择的计划
                            initLoadItem(info.getData());
                            mAdapter.setLoadItem(mLoadData);
                            mAdapter.notifyItemChanged(0);
                        }
                    }
                }
            });
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mReceiver != null && getActivity() != null) {
            getActivity().unregisterReceiver(mReceiver);
            mReceiver = null;
        }
    }

    public void onRefreshHomeItem() {
        HomeInfoTask task = HomeInfoTask.getInstance();
        HomeInfoPresenter infoPresenter = new HomeInfoPresenter(task, HomeFragment.this);
        this.setPresenter(infoPresenter);
        mPresenter.getInfo(UserConfig.getToken(MyApplication.getContext()));
    }
}
