package com.example.carson.yjenglish.home.view.word;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.carson.yjenglish.MyApplication;
import com.example.carson.yjenglish.R;
import com.example.carson.yjenglish.home.HomeService;
import com.example.carson.yjenglish.home.view.HomeFragment;
import com.example.carson.yjenglish.utils.CommonInfo;
import com.example.carson.yjenglish.utils.NetUtils;
import com.example.carson.yjenglish.utils.ScreenUtils;
import com.example.carson.yjenglish.utils.StatusBarUtil;
import com.example.carson.yjenglish.utils.UserConfig;
import com.tencent.connect.common.Constants;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzonePublish;
import com.tencent.connect.share.QzoneShare;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/** 打卡分享页面 */
public class SignAty extends AppCompatActivity {

    private ImageView bg;
    private ImageView back;
    private ImageButton wechatFriend;
    private ImageButton wechat;
    private ImageButton qq;
    private ImageButton qqZone;

    private String username;
    private int insist_day;
    private int learned_word;

    private File imgFile;

    private Tencent mTencent;
    private QQShareListener shareListener;

    private boolean isRequestFinish = false;

//    private

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
        setContentView(R.layout.activity_sign);

        mTencent = Tencent.createInstance(UserConfig.QQ_APP_ID, MyApplication.getContext());
        shareListener = new QQShareListener();

        username = getIntent().getStringExtra("username");
        insist_day = getIntent().getIntExtra("insist_day", 0);
        learned_word = getIntent().getIntExtra("learned_word", 0);

        Log.e("SignAty", username);
        Log.e("SignAty", insist_day + "");
        Log.e("SignAty", learned_word + "");

        @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");


        imgFile = new File(Environment.getExternalStorageDirectory() + "/背呗背单词/分享/" +
                df.format(new Date()) + ".png");
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                createRecorderFile();
//            }
//        }).start();
        if (!imgFile.exists()) {
            Log.e("Sign", "not exist");
            imgFile.getParentFile().mkdirs();
//            imgFile.delete();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    createFile();
                }
            }).start();
        } else {
            initViews();
        }
//        initViews();
    }

    private void createFile() {
        int res[] = new int[] {R.mipmap.word_loading_img, R.mipmap.daily_pic_1, R.mipmap.daily_pic_2,
        R.mipmap.daily_pic_3, R.mipmap.daily_pic_4, R.mipmap.daily_pic_5, R.mipmap.daily_pic_6,
        R.mipmap.daily_pic_7, R.mipmap.daily_pic_8, R.mipmap.daily_pic_9,
        R.mipmap.daily_pic_10, R.mipmap.daily_pic_11, R.mipmap.daily_pic_12, R.mipmap.daily_pic_13,
        R.mipmap.daily_pic_14};

        int rNum = (int) (Math.random() * res.length);

        //创建bitmap
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), res[rNum])
                .copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(bitmap);

        //
        Log.e("Sign", "screen width = " + ScreenUtils.getScreenWidth(this));


        Rect rect = new Rect(5, ScreenUtils.dp2px(this, 58),
                ScreenUtils.dp2px(this, 312) - 5,
                ScreenUtils.dp2px(this, 218));
        Log.e("Sign", "rect width = " + rect.width());
        Paint bgPaint = new Paint();
        bgPaint.setColor(Color.parseColor("#1e6c6c6c"));
        canvas.drawRoundRect(new RectF(rect), 10, 10, bgPaint);

        Paint textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(ScreenUtils.dp2px(this, 22));
        textPaint.setTextAlign(Paint.Align.CENTER);

        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        float top = fontMetrics.top;
        float bottom = fontMetrics.bottom;

        int baseLineY = (int) (rect.centerY() - ScreenUtils.dp2px(this, 60) - top / 2 - bottom / 2);

        canvas.drawText(username, rect.centerX(), baseLineY, textPaint);

        textPaint.setTextSize(ScreenUtils.dp2px(this, 16));

        fontMetrics = textPaint.getFontMetrics();
        top = fontMetrics.top;
        bottom = fontMetrics.bottom;

        baseLineY = (int) (rect.centerY() - ScreenUtils.dp2px(this, 30) - top / 2 - bottom / 2);

        canvas.drawText("\"透过语境记住你\"", rect.centerX(), baseLineY, textPaint);

        canvas.drawLine(rect.centerX(), rect.centerY() + 30, rect.centerX(),
                rect.centerY() + ScreenUtils.dp2px(this, 65) - 30, textPaint);

        textPaint.setTextSize(ScreenUtils.dp2px(this, 12));

        fontMetrics = textPaint.getFontMetrics();
        top = fontMetrics.top;
        bottom = fontMetrics.bottom;

        baseLineY = (int) (rect.centerY() + 20 - top / 2 - bottom / 2);
        canvas.drawText("已坚持天数", rect.centerX() - 200, baseLineY, textPaint);

        canvas.drawText("已背单词数", rect.centerX() + 200, baseLineY, textPaint);

        textPaint.setTextSize(ScreenUtils.dp2px(this, 36));

        fontMetrics = textPaint.getFontMetrics();
        top = fontMetrics.top;
        bottom = fontMetrics.bottom;

        baseLineY = (int) (rect.centerY() + ScreenUtils.dp2px(this, 40) - top / 2 - bottom / 2);

        canvas.drawText(String.valueOf(insist_day), rect.centerX() - 200, baseLineY, textPaint);
        canvas.drawText(String.valueOf(learned_word), rect.centerX() + 200, baseLineY, textPaint);


        try {
            OutputStream os = new FileOutputStream(imgFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 80, os);
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                initViews();
            }
        });
    }

    private void initViews() {
        back = findViewById(R.id.back);
        wechatFriend = findViewById(R.id.btn_wechat_friend);
        wechat = findViewById(R.id.btn_wechat);
        qq = findViewById(R.id.btn_qq);
        qqZone = findViewById(R.id.btn_qq_zone);
        bg = findViewById(R.id.img_bg);

        Glide.with(this).load(R.mipmap.share_img).thumbnail(0.5f).into(bg);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        wechat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                executeShare2Wechat();
            }
        });

        wechatFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                executeShare2TimeLine();
            }
        });

        qq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                executeShare2QQ();
            }
        });

        qqZone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                executeShare2QZone();
            }
        });
    }

    private void executeShare2QZone() {
        if (!mTencent.isQQInstalled(getApplicationContext())) {
            Toast.makeText(getApplicationContext(), "请先安装QQ客户端", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!imgFile.exists()) {
            return;
        }

        try {

            Bundle bundle = new Bundle();
            bundle.putInt(QzonePublish.PUBLISH_TO_QZONE_KEY_TYPE, QzonePublish.PUBLISH_TO_QZONE_TYPE_PUBLISHMOOD);
            bundle.putString(QzonePublish.PUBLISH_TO_QZONE_SUMMARY, "说说正文");

            ArrayList<String> imgList = new ArrayList<>();
            imgList.add(imgFile.getPath());

            bundle.putStringArrayList(QzonePublish.PUBLISH_TO_QZONE_IMAGE_URL, imgList);
//            bundle.putString(QzoneShare.SHARE_TO_QQ_APP_NAME, "背呗背单词");
//            bundle.putString(QzoneShare.SHARE_TO_QQ_EXT_INT, "其它附加功能");

            mTencent.publishToQzone(this, bundle, shareListener);
            isRequestFinish = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void executeShare2QQ() {
        if (!mTencent.isQQInstalled(getApplicationContext())) {
            Toast.makeText(getApplicationContext(), "请先安装QQ客户端", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!imgFile.exists()) {
            return;
        }
        try {

            Bundle bundle = new Bundle();
            bundle.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_IMAGE);
            bundle.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, imgFile.getPath());
            bundle.putString(QQShare.SHARE_TO_QQ_APP_NAME, "背呗背单词");
            bundle.putString(QQShare.SHARE_TO_QQ_EXT_INT, "其它附加功能");

            mTencent.shareToQQ(this, bundle, shareListener);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void executeShare2TimeLine() {
        if (!MyApplication.mWXApi.isWXAppInstalled()) {
            Toast.makeText(this, "还没有安装微信", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!imgFile.exists()) {
            return;
        }
        Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getPath());

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
        MyApplication.mWXApi.sendReq(req);
        isRequestFinish = true;
    }

    private void executeShare2Wechat() {
        if (!MyApplication.mWXApi.isWXAppInstalled()) {
            Toast.makeText(this, "还没有安装微信", Toast.LENGTH_SHORT).show();
            isRequestFinish = false;
            return;
        }
//
//        WXWebpageObject webpage = new WXWebpageObject();
//        webpage.webpageUrl = "http://www.baidu.com";
//
//        WXMediaMessage msg = new WXMediaMessage(webpage);
//        msg.title = "背呗";
//        msg.description = "透过语境记住你";
//
        if (!imgFile.exists()) {
            return;
        }
        Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getPath());

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
        MyApplication.mWXApi.sendReq(req);
        isRequestFinish = true;
    }

    private byte[] getWXThumb(Bitmap thumbBitmap) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        int quality = 100;
        thumbBitmap.compress(Bitmap.CompressFormat.PNG, quality, output);
        byte[] result = output.toByteArray();
        return result;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.ani_left_get_into, R.anim.ani_right_sign_out);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Tencent.onActivityResultData(requestCode, resultCode, data, shareListener);

        if (requestCode == Constants.REQUEST_API) {
            if (resultCode == Constants.REQUEST_QQ_SHARE ||
                    resultCode == Constants.REQUEST_OLD_SHARE ||
                    resultCode == Constants.REQUEST_QZONE_SHARE) {
                Tencent.handleResultData(data, shareListener);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("Sign", "onResume");
        Log.e("Sign", "isRequestFinish = " + isRequestFinish);
        if (isRequestFinish) {
//            Intent toSignIn = new Intent(this, SignInAty.class);
//            startActivity(toSignIn);
//            setResult(HomeFragment.RESULT_SIGN_OK);
//            finishAfterTransition();
//            Intent toSignIn = new Intent(SignAty.this, SignInAty.class);
//            startActivity(toSignIn);
//            setResult(HomeFragment.RESULT_SIGN_OK);
//            finishAfterTransition();
//            Toast.makeText(this, "分享成功", Toast.LENGTH_SHORT).show();
//            finishAfterTransition();
            executePostTask();
        }
    }

    private void executePostTask() {
        Retrofit retrofit = NetUtils.getInstance().getRetrofitInstance(UserConfig.HOST);
        retrofit.create(HomeService.class).doSignTask(UserConfig.getToken(this))
                .enqueue(new Callback<CommonInfo>() {
                    @Override
                    public void onResponse(Call<CommonInfo> call, Response<CommonInfo> response) {
                        CommonInfo info = response.body();
                        if (info.getStatus().equals("200")) {
                            Intent toSignIn = new Intent(SignAty.this, SignInAty.class);
                            startActivity(toSignIn);
                            setResult(HomeFragment.RESULT_SIGN_OK);
                            finishAfterTransition();
                        } else {
                            Toast.makeText(SignAty.this, info.getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<CommonInfo> call, Throwable t) {
                        Toast.makeText(SignAty.this, "连接超时，正在重试...", Toast.LENGTH_SHORT).show();
                        executePostTask();
                    }
                });
    }

    private class QQShareListener implements IUiListener {
        @Override
        public void onComplete(Object response) {
//            Log.e("SignAty", "response = " + response);
            Toast.makeText(getApplicationContext(), "分享成功", Toast.LENGTH_SHORT).show();
            Intent toSignIn = new Intent(SignAty.this, SignInAty.class);
            startActivity(toSignIn);
            setResult(HomeFragment.RESULT_SIGN_OK);
//            finishAfterTransition();
            executePostTask();
//            finishAfterTransition();
        }

        @Override
        public void onError(UiError uiError) {
            if (uiError.errorMessage != null && !uiError.errorMessage.isEmpty()) {
                Log.e("SignAty", uiError.errorMessage);
            }
            Toast.makeText(SignAty.this, "分享失败", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel() {
            Toast.makeText(SignAty.this, "分享取消", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (imgFile.exists()) {
            imgFile.delete();
        }
    }
}
