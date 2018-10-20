package com.example.carson.yjenglish.zone.view.comment;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.carson.yjenglish.R;
import com.example.carson.yjenglish.home.view.feeds.HomeItemAty;
import com.example.carson.yjenglish.tv.view.TVItemAty;
import com.example.carson.yjenglish.utils.StatusBarUtil;
import com.example.carson.yjenglish.utils.UserConfig;
import com.example.carson.yjenglish.zone.MyCommentContract;
import com.example.carson.yjenglish.zone.MyCommentTask;
import com.example.carson.yjenglish.zone.model.CommentModel;
import com.example.carson.yjenglish.zone.model.MyCommentInfo;
import com.example.carson.yjenglish.zone.model.forviewbinder.HomeFeeds;
import com.example.carson.yjenglish.zone.model.forviewbinder.TVFeeds;
import com.example.carson.yjenglish.zone.presenter.MyCommentPresenter;
import com.example.carson.yjenglish.zone.viewbinder.CommentViewBinder;
import com.example.carson.yjenglish.zone.viewbinder.EmptyBinder;

import java.util.ArrayList;
import java.util.List;

import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

public class MyCommentAty extends AppCompatActivity implements CommentViewBinder.OnItemClickListener,
        MyCommentContract.View{

    private ImageView back;
    private RecyclerView recyclerView;
    private MultiTypeAdapter mAdapter;
    private Items mItems;

    private List<MyCommentInfo.MyComment.VideoComment> videoComments;
    private List<MyCommentInfo.MyComment.FeedComment> feedComments;

    private MyCommentContract.Presenter mPresenter;
    private MyCommentPresenter commentPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setTheme(R.style.AppThemeWithoutTranslucent);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN |
                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
        if (StatusBarUtil.checkDeviceHasNavigationBar(this)) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
        setContentView(R.layout.activity_my_comment);

        bindViews();
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
        executeLoadTask();

    }

    private void executeLoadTask() {
        MyCommentTask task = MyCommentTask.getInstance();
        commentPresenter = new MyCommentPresenter(task, this);
        this.setPresenter(commentPresenter);
        mPresenter.getComments(UserConfig.getToken(this));
    }

    private void initRecycler() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(false);
        mAdapter = new MultiTypeAdapter();
        mItems = new Items();
        mAdapter.setItems(mItems);
        recyclerView.setAdapter(mAdapter);
        if (feedComments.size() == 0 && videoComments.size() == 0) {
            mAdapter.register(String.class, new EmptyBinder());
            mItems.add("55555~暂时没有动态噢~");
        } else {
            Log.e("Comment", feedComments.size() + "");
            Log.e("Comment", "videoComment.size() = " + videoComments.size());
            mAdapter.register(CommentModel.class, new CommentViewBinder(this));
            for (int i = 0; i < feedComments.size(); i++) {
                CommentModel model = new CommentModel(feedComments.get(i).getComment(), 0);
                model.setHomeFeeds(new HomeFeeds(feedComments.get(i).getId(),
                        feedComments.get(i).getPic(), feedComments.get(i).getTitle(),
                        feedComments.get(i).getAuthor_portrait(),
                        feedComments.get(i).getAuthor_username(), feedComments.get(i).getSet_time()));
                mItems.add(model);
            }
            for (int i = 0; i < videoComments.size(); i++) {
                CommentModel model = new CommentModel(videoComments.get(i).getComment(), 1);
                model.setTvFeeds(new TVFeeds(videoComments.get(i).getVideo_id(),
                        videoComments.get(i).getImg(), videoComments.get(i).getViews(),
                        videoComments.get(i).getWord(), videoComments.get(i).getPhonetic_symbol(),
                        videoComments.get(i).getSet_time()));
                mItems.add(model);
            }
        }
        mAdapter.notifyDataSetChanged();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.ani_left_get_into, R.anim.ani_right_sign_out);
    }

    @Override
    public void onItemClick(int itemType, String id) {
        switch (itemType) {
            case 0:
                Intent toHomeAty = new Intent(this, HomeItemAty.class);
                toHomeAty.putExtra("id", id);
                startActivity(toHomeAty);
                overridePendingTransition(R.anim.ani_right_get_into, R.anim.ani_left_sign_out);
                break;
            case 1:
                Intent toTVAty = new Intent(this, TVItemAty.class);
                toTVAty.putExtra("video_id", id);
                startActivity(toTVAty);
                overridePendingTransition(R.anim.ani_right_get_into, R.anim.ani_left_sign_out);
                break;
            default:
                break;
        }
    }

    @Override
    public void setPresenter(MyCommentContract.Presenter presenter) {
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

    }

    @Override
    public void onSuccess(MyCommentInfo info) {
        if (info.getStatus().equals("200")) {
            videoComments = info.getData().getVideo_comment();
            feedComments = info.getData().getFeeds_comment();

            initRecycler();
        }
    }
}
