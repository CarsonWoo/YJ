package com.example.carson.yjenglish.msg.view;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidkun.PullToRefreshRecyclerView;
import com.androidkun.callback.PullToRefreshListener;
import com.bumptech.glide.Glide;
import com.example.carson.yjenglish.R;
import com.example.carson.yjenglish.adapter.MsgAdapters;
import com.example.carson.yjenglish.msg.model.CommentMsg;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CommentFragment extends Fragment {

    private static final String TAG = "CommentFragment";
    private static CommentFragment INSTANCE = null;

    private PullToRefreshRecyclerView recyclerView;
    private MsgAdapters.CommentAdapter mAdapter;
    private List<CommentMsg> mComments;

    public CommentFragment() {
        // Required empty public constructor
    }

    public static CommentFragment getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CommentFragment();
        }
        return INSTANCE;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comment, container, false);
        initRecyclerView(view);
        return view;
    }

    private void initRecyclerView(View view) {
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        mComments = new ArrayList<>();

        mAdapter = new MsgAdapters.CommentAdapter(getContext(), mComments);
        recyclerView.setAdapter(mAdapter);
//        for (int i = 0; i < 10; i++) {
//            CommentMsg comment = new CommentMsg();
//            comment.setUsername("carson");
//            if (i % 2 == 0) {
//                comment.setType("评论了你");
//            } else {
//                comment.setType("回复了你");
//            }
//            comment.setContent("不错不错");
//            comment.setDate("2018/8/6");
//            comment.setOrigin("原创：《大海鱼塘》");
//            comment.setImgUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1533311106938&di=a428eb3a3220df77190f2b9b2abef542&imgtype=0&src=http%3A%2F%2Fphotocdn.sohu.com%2F20150907%2Fmp30906533_1441629699374_2.jpeg");
//            mComments.add(comment);
//        }
        View emptyView = View.inflate(getContext(), R.layout.layout_error, null);
        TextView errorMsg = emptyView.findViewById(R.id.error_text);
        errorMsg.setText("暂时没有评论噢...");
        ImageView errorImg = emptyView.findViewById(R.id.error_img);
        Glide.with(getContext()).load(R.mipmap.bg_comment_box).thumbnail(0.8f).into(errorImg);
        emptyView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        recyclerView.setEmptyView(emptyView);

        recyclerView.setLoadingMoreEnabled(false);

        recyclerView.setPullToRefreshListener(new PullToRefreshListener() {
            @Override
            public void onRefresh() {
                mAdapter.notifyDataSetChanged();
                Intent itemIntent = new Intent("NEW_MSG_ITEM_ADD");
                itemIntent.putExtra("isRefresh", true);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.setRefreshComplete();
                    }
                }, 1000);
            }

            @Override
            public void onLoadMore() {

            }
        });

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                CommentMsg newComment = new CommentMsg();
//                newComment.setImgUrl(null);
//                newComment.setUsername("帅哥");
//                newComment.setOrigin("原创：嘻嘻");
//                newComment.setContent("不错嘛小子");
//                newComment.setDate("2018/9/9");
//                newComment.setType("评论了你");
//                mComments.add(newComment);
////                mAdapter.notifyDataSetChanged();
//                Intent itemIntent = new Intent("NEW_MSG_ITEM_ADD");
//                itemIntent.putExtra("tabPos", 0);
//                itemIntent.putExtra("isRefresh", false);
//                Intent msgIntent = new Intent("MSG_LIST_CHANGE");
//                if (getActivity() != null) {
//                    getActivity().sendBroadcast(itemIntent);
//                    getActivity().sendBroadcast(msgIntent);
//                }
//            }
//        }, 5000);
    }

}
