package com.example.carson.yjenglish.zone.view.comment;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.carson.yjenglish.R;
import com.example.carson.yjenglish.zone.model.CommentModel;
import com.example.carson.yjenglish.zone.model.forviewbinder.HomeFeeds;
import com.example.carson.yjenglish.zone.model.forviewbinder.WordItem;
import com.example.carson.yjenglish.zone.viewbinder.CommentViewBinder;
import com.example.carson.yjenglish.zone.viewbinder.EmptyBinder;

import java.util.ArrayList;
import java.util.List;

import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

public class MyCommentAty extends AppCompatActivity implements CommentViewBinder.OnItemClickListener {

    private ImageView back;
    private RecyclerView recyclerView;
    private MultiTypeAdapter mAdapter;
    private Items mItems;

    private List<String> mList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_comment);

        bindViews();
    }

    private void initData() {
        mList.add("");
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
        initData();
        initRecycler();
    }

    private void initRecycler() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new MultiTypeAdapter();
        mItems = new Items();
        mAdapter.setItems(mItems);
        recyclerView.setAdapter(mAdapter);
        if (mList.size() == 0) {
            mAdapter.register(String.class, new EmptyBinder());
            mItems.add("55555~暂时没有动态噢~");
        } else {
            mAdapter.register(CommentModel.class, new CommentViewBinder(this));
            CommentModel model = new CommentModel("hello", 0);
            model.setHomeFeeds(new HomeFeeds("http://pic.qiantucdn.com/58pic/19/57/12/47B58PICxdD_1024.jpg",
                    "”从英“的365里路", "http://cdn.duitang.com/uploads/item/201507/10/20150710045602_wHEBf.jpeg",
                    "Julia", "刚刚"));
            mItems.add(model);
            model = new CommentModel("Carson", "还行吧", "我觉得OK", 3);
            model.setWordItem(new WordItem("car", "/cаr/", "12:10",
                    "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1533311106938&di=a428eb3a3220df77190f" +
                            "2b9b2abef542&imgtype=0&src=http%3A%2F%2Fphotocdn.sohu.com%2F20150907%2Fmp30906533_1441629699374_2.jpeg"));
            mItems.add(model);
        }
        mAdapter.notifyDataSetChanged();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.ani_left_get_into, R.anim.ani_right_sign_out);
    }

    @Override
    public void onItemClick(int itemType) {
        switch (itemType) {
            case 0:
                Log.e("MyCommentAty", "homeItem");
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                Log.e("MyCommentAty", "wordItem");
                break;
            case 4:
                break;
            default:
                break;
        }
    }
}
