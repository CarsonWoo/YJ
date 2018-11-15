package com.example.carson.yjenglish.discover.view;


import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.androidkun.PullToRefreshRecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.carson.yjenglish.BaseActivity;
import com.example.carson.yjenglish.DownloadService;
import com.example.carson.yjenglish.ImageShowActivity;
import com.example.carson.yjenglish.MyApplication;
import com.example.carson.yjenglish.R;
import com.example.carson.yjenglish.adapter.DiscoverAdapters;
import com.example.carson.yjenglish.customviews.CanotSlidingViewPager;
import com.example.carson.yjenglish.discover.DiscoverInfoContract;
import com.example.carson.yjenglish.discover.DiscoverInfoTask;
import com.example.carson.yjenglish.discover.DiscoverService;
import com.example.carson.yjenglish.discover.model.DailyCardInfo;
import com.example.carson.yjenglish.discover.model.DiscoverInfo;
import com.example.carson.yjenglish.discover.presenter.DiscoverInfoPresenter;
import com.example.carson.yjenglish.home.model.LoadHeader;
import com.example.carson.yjenglish.utils.CommonInfo;
import com.example.carson.yjenglish.utils.DialogUtils;
import com.example.carson.yjenglish.utils.FileUtils;
import com.example.carson.yjenglish.utils.NetUtils;
import com.example.carson.yjenglish.utils.ScreenUtils;
import com.example.carson.yjenglish.utils.UserConfig;
import com.tencent.connect.common.Constants;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzonePublish;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.litepal.crud.DataSupport;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class DiscoverFragment extends Fragment implements DiscoverInfoContract.View {

    private enum DOWN_LOAD_TYPE {DOWNLOAD, SHARE_QQ, SHARE_QZONE, SHARE_WECHAT, SHARE_TIMELINE};

    private CanotSlidingViewPager viewPager;
    private PullToRefreshRecyclerView rvAty, rvGame;

//    private List<String> imgUrls = new ArrayList<>();
//    private List<Integer> imgUrls = new ArrayList<>();
    private List<DiscoverInfo.DiscoverItem.DailyCard> mDailyCards;
    private List<DiscoverInfo.DiscoverItem.WelfareService> mList = new ArrayList<>();

    private List<DiscoverInfo.DiscoverItem.WelfareService> tmpList = new ArrayList<>();

    private View view;

    private Dialog mDialog;

    private MyPagerAdapter mPagerAdapter;

    private DiscoverAdapters.AtyAdapter mAdapter;
    private DiscoverAdapters.AtyAdapter tmpAdapter;

    private DiscoverInfoContract.Presenter mPresenter;
    private DiscoverInfoPresenter infoPresenter;

    private int mPagePosition;

    private int mLoadingPage = 2;

    private boolean refreshEnable = true;

    private Tencent mTencent;
    private ShareListener shareListener;
    private IWXAPI mWxApi;

    private File tmpFile;

    private int errorCount = 0;

    public DiscoverFragment() {
        // Required empty public constructor
    }

    public static DiscoverFragment newInstance() {
        return new DiscoverFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mTencent = Tencent.createInstance(UserConfig.QQ_APP_ID, MyApplication.getContext());
        shareListener = new ShareListener();
        mWxApi = MyApplication.mWXApi;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_discover, container, false);
        mDialog = DialogUtils.getInstance(getContext()).newCommonDialog("加载中", R.mipmap.gif_loading_video, true);
//        initViews(view);
        bindViews();
        executeLoadingTask();
        return view;
    }

    private void bindViews() {
        viewPager = view.findViewById(R.id.vp);
        rvAty = view.findViewById(R.id.rv_aty);
        rvGame = view.findViewById(R.id.rv_game);
    }

    public void executeLoadingTask() {
        DiscoverInfoTask task = DiscoverInfoTask.getInstance();
        infoPresenter = new DiscoverInfoPresenter(task, this);
        this.setPresenter(infoPresenter);
        mPresenter.getInfo(UserConfig.getToken(MyApplication.getContext()));
    }

    private void initViews() {

        mPagerAdapter = new MyPagerAdapter();

        viewPager.setAdapter(mPagerAdapter);
        viewPager.setOffscreenPageLimit(2);
        viewPager.setPageMargin(70);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position == mDailyCards.size() - 1) {
                    if (refreshEnable) {
                        flushData();
                    }
                }
//                if (positionOffsetPixels == 0 && positionOffset == 0){
//                    //在这里面刷新数据
//                    flushData();
//                }
            }

            @Override
            public void onPageSelected(int position) {
                mPagePosition = position;

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        viewPager.setCurrentItem(mDailyCards.size() - 1);
        viewPager.setFocusable(true);
        viewPager.setFocusableInTouchMode(true);
//        ViewGroup.LayoutParams params = viewPager.getLayoutParams();
//        params.width = ScreenUtils.getScreenWidth(getContext()) - ScreenUtils.dp2px(getContext(), 80);
//        viewPager.setLayoutParams(params);
//        viewPager.setPageMargin(getResources().getDimensionPixelSize(R.dimen.page_margin));

        mAdapter = new DiscoverAdapters.AtyAdapter(getContext(), mList);
        rvAty.setAdapter(mAdapter);
        rvAty.setHasFixedSize(true);
        initRecycler(rvAty, "暂时没有更多活动噢\n小语正在努力筹划~", R.mipmap.welfare_place_holder);

//        tmpAdapter = new DiscoverAdapters.AtyAdapter(getContext(), tmpList);
//        rvGame.setAdapter(tmpAdapter);
//        rvGame.setHasFixedSize(true);
//        initRecycler(rvGame, "暂时还没运营噢~敬请期待~", R.mipmap.welfare_place_holder);

    }

    private void flushData() {
        Retrofit retrofit = NetUtils.getInstance().getRetrofitInstance(UserConfig.HOST);
        retrofit.create(DiscoverService.class).getMorePics(UserConfig.getToken(MyApplication.getContext()),
                String.valueOf(mLoadingPage), "6").enqueue(new Callback<DailyCardInfo>() {
            @Override
            public void onResponse(Call<DailyCardInfo> call, Response<DailyCardInfo> response) {
                DailyCardInfo info = response.body();
                if (info.getStatus().equals("200")) {
                    if (!info.getData().isEmpty()) {
                        mLoadingPage ++;
                        mDailyCards.addAll(info.getData());
                        mPagerAdapter.notifyDataSetChanged();
                    } else {
//                        DiscoverInfo.DiscoverItem.DailyCard card = mDailyCards.get(0);
//                        card.setIs_favour("1");
//                        mDailyCards.add(0, card);
//                        mPagerAdapter.notifyDataSetChanged();
                        Toast.makeText(getContext(), "没有更多咯~", Toast.LENGTH_SHORT).show();
                        refreshEnable = false;

                    }
                } else {
//                    Log.e("Discover", info.getMsg());
                }
            }

            @Override
            public void onFailure(Call<DailyCardInfo> call, Throwable t) {
                Toast.makeText(getContext(), "连接超时", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initRecycler(PullToRefreshRecyclerView rv, String text, int drawableRes) {
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setLoadingMoreEnabled(false);
        rv.setPullRefreshEnabled(false);
        rv.setNestedScrollingEnabled(false);
        View emptyView = View.inflate(getContext(), R.layout.layout_error_card, null);
        TextView tv = emptyView.findViewById(R.id.error_text);
        ImageView iv = emptyView.findViewById(R.id.error_img);
        tv.setText(text);
        Glide.with(getContext()).load(drawableRes).thumbnail(0.5f).into(iv);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        emptyView.setLayoutParams(params);
        rv.setEmptyView(emptyView);
    }

    @Override
    public void setPresenter(DiscoverInfoContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void showLoading() {
        mDialog.show();
    }

    @Override
    public void hideLoading() {
        mDialog.dismiss();
    }

    @Override
    public void showError(String msg) {
//        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
        if (errorCount < 3) {
            errorCount ++;
            Toast.makeText(MyApplication.getContext(), "连接超时 正在重新连接", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    executeLoadingTask();
                }
            }, 3000);
        } else {
            Toast.makeText(MyApplication.getContext(), "请检查网络 再次点击页面进行刷新~", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onSuccess(DiscoverInfo info) {
        if (info.getStatus().equals("200")) {
            mDailyCards = info.getData().getDaily_pic();

            mList = info.getData().getWelfare_service();
            initViews();
        } else {
//            Log.e("Discover", info.getMsg());
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


    private class MyPagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return mDailyCards.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, final int position) {
            View itemView = LayoutInflater.from(getContext()).inflate(R.layout.discover_daily_card,
                    container, false);

            itemView.setTag(position);

            ImageView bg = itemView.findViewById(R.id.bg_daily_card);
            final ImageView like = itemView.findViewById(R.id.like);
            ImageView download = itemView.findViewById(R.id.download);
            ImageView share = itemView.findViewById(R.id.share);

            like.setSelected(mDailyCards.get(position).getIs_favour().equals("1"));

            like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mDailyCards.get(position).getIs_favour().equals("1")) {
                        mDailyCards.get(position).setIs_favour("0");
                        like.setSelected(false);
                    } else {
                        mDailyCards.get(position).setIs_favour("1");
                        like.setSelected(true);
                    }
                    executeFavourTask(mDailyCards.get(position).getId());
                }
            });
            download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    executeDownloadTask(mDailyCards.get(position).getDaily_pic(), DOWN_LOAD_TYPE.DOWNLOAD);
                }
            });
            share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showShareWindow(mDailyCards.get(position).getDaily_pic());
                }
            });

            bg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent toImg = new Intent(getContext(), ImageShowActivity.class);
                    toImg.putExtra("img_url", mDailyCards.get(position).getDaily_pic());
                    startActivity(toImg);
                }
            });

            Glide.with(getContext())
                    .load(mDailyCards.get(position).getDaily_pic())
                    .placeholder(R.mipmap.daily_place_holder)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            if (bg.getScaleType() != ImageView.ScaleType.FIT_XY) {
                                bg.setScaleType(ImageView.ScaleType.FIT_XY);
                            }
                            return false;
                        }
                    })
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .dontAnimate()
                    .thumbnail(0.1f)
                    .into(bg);
            container.addView(itemView);
            return itemView;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            View view = (View) object;
            int currentPage = mPagePosition;
            if (currentPage == (Integer) view.getTag()) {
                return POSITION_NONE;
            } else {
                return POSITION_UNCHANGED;
            }
        }
    }

    private void executeFavourTask(String id) {
        Retrofit retrofit = NetUtils.getInstance().getRetrofitInstance(UserConfig.HOST);
        retrofit.create(DiscoverService.class).postDailyCardFavours(UserConfig.getToken(MyApplication.getContext()),
                id).enqueue(new Callback<CommonInfo>() {
            @Override
            public void onResponse(Call<CommonInfo> call, Response<CommonInfo> response) {
                CommonInfo info = response.body();
                if (info.getStatus().equals("200")) {
                    Log.e("Discover", "点赞成功");
                } else {
//                    Log.e("Discover", info.getMsg());
                }
            }

            @Override
            public void onFailure(Call<CommonInfo> call, Throwable t) {
                Log.e("Discover", "连接超时");
            }
        });
    }

    private void showShareWindow(final String pic_url) {
        View windowView = LayoutInflater.from(getContext()).inflate(R.layout.layout_share_popup_window,
                null, false);
        final PopupWindow window = new PopupWindow(windowView, ViewGroup.LayoutParams.MATCH_PARENT,
                ScreenUtils.dp2px(getContext(), 110));
        window.setAnimationStyle(R.style.popup_window_photo_utils_animation);
        TextView cancel = windowView.findViewById(R.id.cancel_share);
        LinearLayout wechatShare = windowView.findViewById(R.id.share_wechat);
        LinearLayout timelineShare = windowView.findViewById(R.id.share_timeline);
        LinearLayout qqShare = windowView.findViewById(R.id.share_qq);
        LinearLayout qzoneShare = windowView.findViewById(R.id.share_qzone);

        window.setBackgroundDrawable(new BitmapDrawable());
        window.setOutsideTouchable(true);
        if (getActivity().getWindow() != null) {
            window.showAsDropDown(getActivity().getWindow().getDecorView(), 0, -window.getHeight(), Gravity.BOTTOM);
        } else {
            window.showAsDropDown(view, 0, -window.getHeight(), Gravity.BOTTOM);
        }

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (window.isShowing()) {
                    window.dismiss();
                }
            }
        });

        qqShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkFileEnable(pic_url, DOWN_LOAD_TYPE.SHARE_QQ);
                window.dismiss();
            }
        });

        qzoneShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkFileEnable(pic_url, DOWN_LOAD_TYPE.SHARE_QZONE);
                window.dismiss();
            }
        });

        wechatShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkFileEnable(pic_url, DOWN_LOAD_TYPE.SHARE_WECHAT);
                window.dismiss();
            }
        });

        timelineShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkFileEnable(pic_url, DOWN_LOAD_TYPE.SHARE_TIMELINE);
                window.dismiss();
            }
        });
    }

    private void checkFileEnable(String pic_url, DOWN_LOAD_TYPE type) {
        if (type == DOWN_LOAD_TYPE.SHARE_QQ || type == DOWN_LOAD_TYPE.SHARE_QZONE) {
            if (!mTencent.isQQInstalled(MyApplication.getContext())) {
                Toast.makeText(MyApplication.getContext(), "请先安装QQ客户端", Toast.LENGTH_SHORT).show();
                return;
            }
        } else {
            if (!mWxApi.isWXAppInstalled()) {
                Toast.makeText(MyApplication.getContext(), "请先安装微信客户端", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (tmpFile == null) {
            //pic_url里包含了名字
            if (pic_url.endsWith(".jpg") || pic_url.endsWith(".png")) {
                tmpFile = FileUtils.createImageFile(MyApplication.getContext(),
                        pic_url, "每日一图");
            } else {
                tmpFile = FileUtils.createImageFile(MyApplication.getContext(),
                        pic_url + ".png", "每日一图");
            }
        }

        if (!tmpFile.exists()) {
            executeDownloadTask(pic_url, type);
            return;
        }

        executeShareTask(type, tmpFile);
    }

    /**
     * 下载到本地 可作为清理缓存的对象
     * @param imgUrl
     * @param type
     */
    private void executeDownloadTask(String imgUrl, final DOWN_LOAD_TYPE type) {
        File file = null;
        DialogUtils utils = DialogUtils.getInstance(getContext());
        final Dialog dialog = utils.newCommonDialog("请求中...", R.mipmap.gif_loading_video, true);
        dialog.setCanceledOnTouchOutside(false);
        if (type == DOWN_LOAD_TYPE.DOWNLOAD) {
            Toast.makeText(getContext(), "开始下载~", Toast.LENGTH_SHORT).show();
            file = FileUtils.createImageFile(getContext(), System.currentTimeMillis() + ".jpg",
                    "每日一图");
        } else {
            file = tmpFile;
            dialog.show();

            WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
            lp.height = ScreenUtils.dp2px(getContext(), 240);
            lp.width = ScreenUtils.dp2px(getContext(), 260);
            lp.gravity = Gravity.CENTER;
            dialog.getWindow().setAttributes(lp);
        }
        final File mFile = file;
        String url = imgUrl.substring("http://47.107.62.22/".length());

//        Log.e("Discover", url);

        Retrofit retrofit = NetUtils.getInstance().getRetrofitInstance("http://47.107.62.22/");
        retrofit.create(DownloadService.class).downFile(url)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                FileUtils.writeFile2Disk(response, mFile);
                                if (type == DOWN_LOAD_TYPE.DOWNLOAD) {
                                    if (getActivity() != null) {
                                        getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(getContext(), "下载完成噜!", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                        Intent sendIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                                        Uri uri = Uri.parse("file://" + mFile.getAbsolutePath());
                                        sendIntent.setData(uri);
                                        getActivity().sendBroadcast(sendIntent);
                                    }
                                } else {
                                    if (getActivity() != null) {
                                        getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                dialog.dismiss();
                                                executeShareTask(type, mFile);
                                            }
                                        });
                                    }
                                }
                            }
                        }).start();
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(getContext(), "下载失败了~检查一下网络呗~", Toast.LENGTH_SHORT).show();
                        if (mFile.exists()) {
                            mFile.delete();
                        }
                    }
                });
    }

    private void executeShareTask(DOWN_LOAD_TYPE type, File mFile) {
        switch (type) {
            case SHARE_QQ:
                executeShare2QQ(mFile);
                break;
            case SHARE_QZONE:
                executeShare2QZone(mFile);
                break;
            case SHARE_WECHAT:
                executeShare2Wechat(mFile);
                break;
            case SHARE_TIMELINE:
                executeShare2Timeline(mFile);
                break;
            default:
                break;
        }
    }

    private void executeShare2Timeline(File file) {
        Bitmap bitmap = BitmapFactory.decodeFile(file.getPath());

        WXImageObject imageObject = new WXImageObject(bitmap);

        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imageObject;

        /**
         * 注意压缩!!!!缩略图不能超过32kb 图片不能超过10MB
         */

        Bitmap thumbBitmap = Bitmap.createScaledBitmap(bitmap, 150, 150, true);

        msg.thumbData = getWXThumb(thumbBitmap);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.scene = SendMessageToWX.Req.WXSceneTimeline;
        req.message = msg;
        req.transaction = String.valueOf(System.currentTimeMillis());//要唯一 判断是哪个分享
        mWxApi.sendReq(req);
    }

    private void executeShare2Wechat(File file) {
        Bitmap bitmap = BitmapFactory.decodeFile(file.getPath());

        WXImageObject imageObject = new WXImageObject(bitmap);

        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imageObject;

        /**
         * 注意压缩!!!!缩略图不能超过32kb 图片不能超过10MB
         */

        Bitmap thumbBitmap = Bitmap.createScaledBitmap(bitmap, 150, 150, true);

        msg.thumbData = getWXThumb(thumbBitmap);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.scene = SendMessageToWX.Req.WXSceneSession;
        req.message = msg;
        req.transaction = String.valueOf(System.currentTimeMillis());//要唯一 判断是哪个分享
        mWxApi.sendReq(req);
    }

    private byte[] getWXThumb(Bitmap thumbBitmap) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        int quality = 100;
        thumbBitmap.compress(Bitmap.CompressFormat.JPEG, quality, output);
        byte[] result = output.toByteArray();
        return result;
    }

    private void executeShare2QZone(File file) {
        try {
            Bundle bundle = new Bundle();
            bundle.putInt(QzonePublish.PUBLISH_TO_QZONE_KEY_TYPE, QzonePublish.PUBLISH_TO_QZONE_TYPE_PUBLISHMOOD);
            bundle.putString(QzonePublish.PUBLISH_TO_QZONE_SUMMARY, "说说正文");

            ArrayList<String> imgList = new ArrayList<>();
            imgList.add(file.getPath());

            bundle.putStringArrayList(QzonePublish.PUBLISH_TO_QZONE_IMAGE_URL, imgList);
//            bundle.putString(QzoneShare.SHARE_TO_QQ_APP_NAME, "背呗背单词");
//            bundle.putString(QzoneShare.SHARE_TO_QQ_EXT_INT, "其它附加功能");

            mTencent.publishToQzone(getActivity(), bundle, shareListener);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void executeShare2QQ(File file) {
        try {
            Bundle bundle = new Bundle();
            bundle.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_IMAGE);
            bundle.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, file.getPath());
            bundle.putString(QQShare.SHARE_TO_QQ_APP_NAME, "背呗背单词");
            bundle.putString(QQShare.SHARE_TO_QQ_EXT_INT, "其它附加功能");

            mTencent.shareToQQ(getActivity(), bundle, shareListener);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mTencent != null && shareListener != null) {
            Tencent.onActivityResultData(requestCode, resultCode, data, shareListener);
            if (requestCode == Constants.REQUEST_API) {
                if (resultCode == Constants.REQUEST_QQ_SHARE ||
                        resultCode == Constants.REQUEST_OLD_SHARE ||
                        resultCode == Constants.REQUEST_QZONE_SHARE) {
                    Tencent.handleResultData(data, shareListener);
                }
            }
        }
    }

    private class ShareListener implements IUiListener {
        @Override
        public void onComplete(Object o) {
            Toast.makeText(getContext(), "分享成功", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(UiError uiError) {
            Toast.makeText(getContext(), "分享失败", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel() {
            Toast.makeText(getContext(), "分享取消", Toast.LENGTH_SHORT).show();
        }
    }
}
