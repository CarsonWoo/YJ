package com.example.carson.yjenglish.zone.view.like;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.carson.yjenglish.EmptyValue;
import com.example.carson.yjenglish.R;
import com.example.carson.yjenglish.discover.DiscoverService;
import com.example.carson.yjenglish.home.HomeService;
import com.example.carson.yjenglish.home.view.feeds.HomeItemAty;
import com.example.carson.yjenglish.home.view.word.WordDetailActivity;
import com.example.carson.yjenglish.home.viewbinder.feeds.EmptyViewBinder;
import com.example.carson.yjenglish.tv.TVService;
import com.example.carson.yjenglish.tv.view.TVItemAty;
import com.example.carson.yjenglish.utils.CommonInfo;
import com.example.carson.yjenglish.utils.NetUtils;
import com.example.carson.yjenglish.utils.ScreenUtils;
import com.example.carson.yjenglish.utils.StatusBarUtil;
import com.example.carson.yjenglish.utils.UserConfig;
import com.example.carson.yjenglish.zone.MyFavourContract;
import com.example.carson.yjenglish.zone.MyFavourTask;
import com.example.carson.yjenglish.zone.model.MyFavourInfo;
import com.example.carson.yjenglish.zone.model.forviewbinder.DailyCardItem;
import com.example.carson.yjenglish.zone.model.forviewbinder.HomeFeeds;
import com.example.carson.yjenglish.zone.model.forviewbinder.TVFeeds;
import com.example.carson.yjenglish.zone.model.forviewbinder.WordItem;
import com.example.carson.yjenglish.zone.presenter.MyFavourPresenter;
import com.example.carson.yjenglish.zone.viewbinder.DailyCardItemBinder;
import com.example.carson.yjenglish.zone.viewbinder.HomeFeedsBinder;
import com.example.carson.yjenglish.zone.viewbinder.TVFeedsBinder;
import com.example.carson.yjenglish.zone.viewbinder.WordItemBinder;

import java.util.List;

import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MyLikeAty extends AppCompatActivity implements MyFavourContract.View,
HomeFeedsBinder.OnHomeFeedItemClickListener, TVFeedsBinder.OnTVFeedsClickListener,
WordItemBinder.OnWordItemClickListener, DailyCardItemBinder.OnDailyCardItemClickListener {

    private final String HOME_FEED = "feeds";
    private final String VIDEO_ITEM = "video";
    private final String DAILY_PIC = "daily_pic";
    private final String WORD_ITEM = "word";

    private ImageView back;
    private RecyclerView recyclerView;
    private MultiTypeAdapter mAdapter;
    private Items items;


    private MyFavourContract.Presenter mPresenter;
    private MyFavourPresenter favourPresenter;

    private int errorCount = 0;

    private List<MyFavourInfo.Favours> mList;

    /**
     * 届时会有一个list 装载网络加载后的数据
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setTheme(R.style.AppThemeWithoutTranslucent);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN |
                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
        if (StatusBarUtil.checkDeviceHasNavigationBar(this)) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        }
        setContentView(R.layout.activity_my_like);
        executeLoadTask();
        bindViews();
    }

    private void executeLoadTask() {
        MyFavourTask task = MyFavourTask.getInstance();
        favourPresenter = new MyFavourPresenter(task, this);
        this.setPresenter(favourPresenter);
        mPresenter.getMyFavours(UserConfig.getToken(this));
    }

    private void bindViews() {
        back = findViewById(R.id.back);
        recyclerView = findViewById(R.id.recycler_view);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

//        initRecyclerViews();
    }

    private void initRecyclerViews() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new MultiTypeAdapter();

        recyclerView.setAdapter(mAdapter);
        items = new Items();
//        mAdapter.register(EmptyValue.class, new FieldTitleViewBinder());

        if (mList.size() == 0) {
            mAdapter.register(EmptyValue.class, new EmptyViewBinder());
            items.add(new EmptyValue("还没有喜欢过噢~快去看看吧~", R.mipmap.bg_like_box));
        } else {
            mAdapter.register(HomeFeeds.class, new HomeFeedsBinder(this));
            mAdapter.register(TVFeeds.class, new TVFeedsBinder(this));
//            mAdapter.register(MusicItem.class, new MusicItemBinder());
            mAdapter.register(WordItem.class, new WordItemBinder(this));
            mAdapter.register(DailyCardItem.class, new DailyCardItemBinder(this));
            for (int i = 0; i < mList.size(); i++) {
                switch (mList.get(i).getType()) {
                    case HOME_FEED:
                        items.add(new HomeFeeds(mList.get(i).getId(),
                                mList.get(i).getPic(), mList.get(i).getTitle(),
                                mList.get(i).getAuthor_portrait(),
                                mList.get(i).getAuthor_username(),
                                mList.get(i).getSet_time()));
                        break;
                    case VIDEO_ITEM:
                        items.add(new TVFeeds(mList.get(i).getVideo_id(),
                                mList.get(i).getImg(), mList.get(i).getViews(),
                                mList.get(i).getWord(), mList.get(i).getPhonetic_symbol(),
                                mList.get(i).getSet_time()));
                        break;
                    case WORD_ITEM:
                        items.add(new WordItem(mList.get(i).getId(), mList.get(i).getWord(),
                                mList.get(i).getPhonetic_symbol(), mList.get(i).getSet_time(),
                                mList.get(i).getPic()));
                        break;
                    case DAILY_PIC:
                        items.add(new DailyCardItem(mList.get(i).getId(), mList.get(i).getSmall_pic(),
                                mList.get(i).getDaily_pic(),
                                mList.get(i).getSet_time()));
                        break;
                    default:
                        break;
                }
            }
        }



//        items.add(new EmptyValue(""));
//        items.add(new EmptyValue(""));
//        items.add(new EmptyValue(""));

        mAdapter.setItems(items);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.ani_left_get_into, R.anim.ani_right_sign_out);
    }

    @Override
    public void setPresenter(MyFavourContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showError(String msg) {
        errorCount ++;
        if (errorCount < 3) {
            Toast.makeText(this, "网络开小差了...正在重试", Toast.LENGTH_SHORT).show();
            executeLoadTask();
        } else {
            if (items != null && mAdapter != null) {
                items.clear();
                mAdapter.register(EmptyValue.class, new EmptyViewBinder());
                items.add(new EmptyValue("网络开小差了...", R.mipmap.bg_plan_box));
                mAdapter.notifyDataSetChanged();
            }
//            Toast.makeText(this, "请检查一下网络状态噢~", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onSuccess(MyFavourInfo info) {
        MyFavourInfo mInfo = info;
        if (mInfo.getStatus().equals("200")) {
            mList = mInfo.getData();
            initRecyclerViews();
        }
    }

    @Override
    public void onDailyCardDelete(final String id, final int pos) {
        View contentView = View.inflate(this, R.layout.layout_tips_dialog, null);
        TextView tipsText = contentView.findViewById(R.id.tips_text);
        TextView tipsCancel = contentView.findViewById(R.id.tips_cancel);
        TextView tipsConfirm = contentView.findViewById(R.id.tips_confirm);
        tipsText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        tipsText.setText("确定删除吗");
        final AlertDialog mDialog = new AlertDialog.Builder(this).setView(contentView).create();
        Window window = mDialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        mDialog.show();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = ScreenUtils.dp2px(this, 260);
        lp.height = ScreenUtils.dp2px(this, 240);
        lp.y = ScreenUtils.getScreenHeight(this) / 2 - ScreenUtils.dp2px(this, 260);
        window.setAttributes(lp);

        tipsCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
            }
        });
        tipsConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
                executeDislikeDailyCardTask();
            }

            private void executeDislikeDailyCardTask() {
                Retrofit retrofit = NetUtils.getInstance().getRetrofitInstance(UserConfig.HOST);
                retrofit.create(DiscoverService.class).postDailyCardFavours(UserConfig.getToken(MyLikeAty.this),
                        id).enqueue(new Callback<CommonInfo>() {
                    @Override
                    public void onResponse(Call<CommonInfo> call, Response<CommonInfo> response) {
                        CommonInfo info = response.body();
                        if (info.getStatus().equals("200")) {
                            items.remove(pos);
                            mAdapter.notifyItemRemoved(pos);
                        } else {
                            Log.e("MyLike", info.getMsg());
                        }
                    }

                    @Override
                    public void onFailure(Call<CommonInfo> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "请求失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    public void onWordClick(String id) {
        Intent intent = new Intent(this, WordDetailActivity.class);
        intent.putExtra("from_intent", 1);
        intent.putExtra("word_id", id);
        intent.putExtra("is_favour", true);
        startActivity(intent);
        overridePendingTransition(R.anim.ani_right_get_into, R.anim.ani_left_sign_out);
    }

    @Override
    public void onWordDelete(final String id, final int pos) {
        View contentView = View.inflate(this, R.layout.layout_tips_dialog, null);
        TextView tipsText = contentView.findViewById(R.id.tips_text);
        TextView tipsCancel = contentView.findViewById(R.id.tips_cancel);
        TextView tipsConfirm = contentView.findViewById(R.id.tips_confirm);
        tipsText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        tipsText.setText("确定删除吗");
        final AlertDialog mDialog = new AlertDialog.Builder(this).setView(contentView).create();
        Window window = mDialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        mDialog.show();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = ScreenUtils.dp2px(this, 260);
        lp.height = ScreenUtils.dp2px(this, 240);
        lp.y = ScreenUtils.getScreenHeight(this) / 2 - ScreenUtils.dp2px(this, 260);
        window.setAttributes(lp);

        tipsCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
            }
        });
        tipsConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
                executeDislikeWordTask();
            }

            private void executeDislikeWordTask() {
                Retrofit retrofit = NetUtils.getInstance().getRetrofitInstance(UserConfig.HOST);
                retrofit.create(HomeService.class).postWordFavours(UserConfig.getToken(MyLikeAty.this),
                        id).enqueue(new Callback<CommonInfo>() {
                    @Override
                    public void onResponse(Call<CommonInfo> call, Response<CommonInfo> response) {
                        CommonInfo info = response.body();
                        if (info.getStatus().equals("200")) {
                            items.remove(pos);
                            mAdapter.notifyItemRemoved(pos);
                        } else {
                            Log.e("MyLike", info.getMsg());
                        }
                    }

                    @Override
                    public void onFailure(Call<CommonInfo> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "请求失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    public void onVideoItemClick(String video_id) {
        Intent intent = new Intent(this, TVItemAty.class);
        intent.putExtra("video_id", video_id);
        startActivity(intent);
        overridePendingTransition(R.anim.ani_right_get_into, R.anim.ani_left_sign_out);
    }

    @Override
    public void onVideoDelete(final String video_id, final int pos) {
        View contentView = View.inflate(this, R.layout.layout_tips_dialog, null);
        TextView tipsText = contentView.findViewById(R.id.tips_text);
        TextView tipsCancel = contentView.findViewById(R.id.tips_cancel);
        TextView tipsConfirm = contentView.findViewById(R.id.tips_confirm);
        tipsText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        tipsText.setText("确定删除吗");
        final AlertDialog mDialog = new AlertDialog.Builder(this).setView(contentView).create();
        Window window = mDialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        mDialog.show();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = ScreenUtils.dp2px(this, 260);
        lp.height = ScreenUtils.dp2px(this, 240);
        lp.y = ScreenUtils.getScreenHeight(this) / 2 - ScreenUtils.dp2px(this, 260);
        window.setAttributes(lp);

        tipsCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
            }
        });
        tipsConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
                executeDislikeVideoTask();
            }

            private void executeDislikeVideoTask() {
                Retrofit retrofit = NetUtils.getInstance().getRetrofitInstance(UserConfig.HOST);
                retrofit.create(TVService.class).postFavours(UserConfig.getToken(MyLikeAty.this),
                        video_id).enqueue(new Callback<CommonInfo>() {
                    @Override
                    public void onResponse(Call<CommonInfo> call, Response<CommonInfo> response) {
                        CommonInfo info = response.body();
                        if (info.getStatus().equals("200")) {
                            items.remove(pos);
                            mAdapter.notifyItemRemoved(pos);
                        } else {
                            Log.e("MyLike", info.getMsg());
                        }
                    }

                    @Override
                    public void onFailure(Call<CommonInfo> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "请求失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    public void onHomeFeedDelete(final String id, final int pos) {
        View contentView = View.inflate(this, R.layout.layout_tips_dialog, null);
        TextView tipsText = contentView.findViewById(R.id.tips_text);
        TextView tipsCancel = contentView.findViewById(R.id.tips_cancel);
        TextView tipsConfirm = contentView.findViewById(R.id.tips_confirm);
        tipsText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        tipsText.setText("确定删除吗");
        final AlertDialog mDialog = new AlertDialog.Builder(this).setView(contentView).create();
        Window window = mDialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        mDialog.show();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = ScreenUtils.dp2px(this, 260);
        lp.height = ScreenUtils.dp2px(this, 240);
        lp.y = ScreenUtils.getScreenHeight(this) / 2 - ScreenUtils.dp2px(this, 260);
        window.setAttributes(lp);


        tipsCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
            }
        });
        tipsConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
                executeDislikeHomeFeedsTask();
            }

            private void executeDislikeHomeFeedsTask() {
                Retrofit retrofit = NetUtils.getInstance().getRetrofitInstance(UserConfig.HOST);
                retrofit.create(HomeService.class).postFavours(UserConfig.getToken(MyLikeAty.this),
                        id).enqueue(new Callback<CommonInfo>() {
                    @Override
                    public void onResponse(Call<CommonInfo> call, Response<CommonInfo> response) {
                        CommonInfo info = response.body();
                        if (info.getStatus().equals("200")) {
                            items.remove(pos);
                            mAdapter.notifyItemRemoved(pos);
                        }
                    }

                    @Override
                    public void onFailure(Call<CommonInfo> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "请求失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    @Override
    public void onHomeFeedClick(String id) {
        Intent intent = new Intent(this, HomeItemAty.class);
        intent.putExtra("id", id);
        startActivity(intent);
        overridePendingTransition(R.anim.ani_right_get_into, R.anim.ani_left_sign_out);
    }
}
