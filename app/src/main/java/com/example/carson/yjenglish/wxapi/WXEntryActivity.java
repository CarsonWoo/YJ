package com.example.carson.yjenglish.wxapi;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.carson.yjenglish.MyApplication;
import com.example.carson.yjenglish.R;
import com.example.carson.yjenglish.utils.UserConfig;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import cn.qqtheme.framework.util.LogUtils;

public class WXEntryActivity extends AppCompatActivity implements IWXAPIEventHandler {

    private final String TAG = getClass().getSimpleName();

    private static final int RETURN_MSG_TYPE_LOGIN = 1;
    private static final int RETURN_MSG_TYPE_SHARE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setTheme(R.style.AppThemeWithoutTranslucent);
        }
        Log.e(TAG, "onCreate");
        Log.e(TAG, "tag = " + TAG);
        MyApplication.mWXApi.handleIntent(getIntent(), this);
    }

    // 微信发送请求到第三方应用时，会回调到该方法
    @Override
    public void onReq(BaseReq baseReq) {

    }

    // 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
    //app发送消息给微信，处理返回消息的回调
    @Override
    public void onResp(BaseResp resp) {
        /**
         * 不能对resp进行打印输出
         */
//        Log.e(TAG, resp.errStr);
//        Log.e(TAG, resp.errCode + "");
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                switch (resp.getType()) {
                    case RETURN_MSG_TYPE_LOGIN:
                        //拿到了微信返回的code,立马再去请求access_token
                        String code = ((SendAuth.Resp) resp).code;
                        String openId = resp.openId;
                        Log.e(TAG, "openId = " + openId);
                        Log.e(TAG, "code = " + code);
//                        finish();
                        //就在这个地方，用网络库什么的或者自己封的网络api，发请求去咯，注意是get请求
                        getOpenId(code);
                        break;

                    case RETURN_MSG_TYPE_SHARE:
                        Toast.makeText(MyApplication.getContext(), "分享成功", Toast.LENGTH_SHORT).show();
                        finish();
                        break;
                }
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
            case BaseResp.ErrCode.ERR_USER_CANCEL:
            default:
                if (RETURN_MSG_TYPE_SHARE == resp.getType()) {
//                    Toast.makeText(MyApplication.getContext(), "分享失败", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else {
//                    Toast.makeText(MyApplication.getContext(), "失败", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
        }
    }

    private void getOpenId(String code) {
//        String urlStr = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" +
//                UserConfig.WECHAT_APP_ID + "&secret="+Constants.APP_Secret+
//                "&code="+code+"&grant_type=authorization_code";

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        MyApplication.mWXApi.handleIntent(getIntent(), this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(TAG, "onStop");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "onResume");
    }
}
