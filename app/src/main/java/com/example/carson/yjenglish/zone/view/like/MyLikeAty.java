package com.example.carson.yjenglish.zone.view.like;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.example.carson.yjenglish.EmptyValue;
import com.example.carson.yjenglish.R;
import com.example.carson.yjenglish.home.model.forviewbinder.Text;
import com.example.carson.yjenglish.home.viewbinder.feeds.EmptyViewBinder;
import com.example.carson.yjenglish.home.viewbinder.word.FieldTitleViewBinder;
import com.example.carson.yjenglish.home.viewbinder.word.TextViewBinder;
import com.example.carson.yjenglish.zone.model.forviewbinder.DailyCardItem;
import com.example.carson.yjenglish.zone.model.forviewbinder.HomeFeeds;
import com.example.carson.yjenglish.zone.model.forviewbinder.MusicItem;
import com.example.carson.yjenglish.zone.model.forviewbinder.TVFeeds;
import com.example.carson.yjenglish.zone.model.forviewbinder.WordItem;
import com.example.carson.yjenglish.zone.viewbinder.DailyCardItemBinder;
import com.example.carson.yjenglish.zone.viewbinder.HomeFeedsBinder;
import com.example.carson.yjenglish.zone.viewbinder.MusicItemBinder;
import com.example.carson.yjenglish.zone.viewbinder.TVFeedsBinder;
import com.example.carson.yjenglish.zone.viewbinder.WordItemBinder;

import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

public class MyLikeAty extends AppCompatActivity {

    private ImageView back;
    private RecyclerView recyclerView;
    private MultiTypeAdapter mAdapter;
    private Items items;

    /**
     * 届时会有一个list 装载网络加载后的数据
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_like);
        initViews();
    }

    private void initViews() {
        back = findViewById(R.id.back);
        recyclerView = findViewById(R.id.recycler_view);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        initRecyclerViews();
    }

    private void initRecyclerViews() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new MultiTypeAdapter();

        recyclerView.setAdapter(mAdapter);

        mAdapter.register(HomeFeeds.class, new HomeFeedsBinder());
        mAdapter.register(TVFeeds.class, new TVFeedsBinder());
        mAdapter.register(MusicItem.class, new MusicItemBinder());
        mAdapter.register(WordItem.class, new WordItemBinder());
        mAdapter.register(DailyCardItem.class, new DailyCardItemBinder());
        mAdapter.register(EmptyValue.class, new FieldTitleViewBinder());

        items = new Items();
        for (int i = 0; i < 2; i++) {
            items.add(new HomeFeeds("http://cdn.duitang.com/uploads/item/201507/10/20150710045602_wHEBf.jpeg",
                    "哎呦不错噢", "http://pic.qiantucdn.com/58pic/19/57/12/47B58PICxdD_1024.jpg",
                    "Carson", "16:20"));
            items.add(new TVFeeds("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1533311106938&di=a428eb3a3220df77190f2b9b2abef542&imgtype=0&src=http%3A%2F%2Fphotocdn.sohu.com%2F20150907%2Fmp30906533_1441629699374_2.jpeg",
                    "2.3k", "travel", "/trea:v(a)l/", "15:38"));
            items.add(new MusicItem("go", "走；去", "14:28"));
            items.add(new WordItem("get", "/gat/", "11:22", "http://cdn.duitang.com/uploads/item/201507/10/20150710045602_wHEBf.jpeg"));
            items.add(new DailyCardItem("http://cdn.duitang.com/uploads/item/201507/10/20150710045602_wHEBf.jpeg", "10:48"));
        }

        items.add(new EmptyValue(""));
        items.add(new EmptyValue(""));
        items.add(new EmptyValue(""));

        mAdapter.setItems(items);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.ani_left_get_into, R.anim.ani_right_sign_out);
    }
}
