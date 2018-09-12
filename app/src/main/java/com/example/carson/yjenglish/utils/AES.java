package com.example.carson.yjenglish.utils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.Build;
import android.util.Base64;


import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by 84594 on 2018/8/30.
 */

public class AES {

    public static String sKey = "yj147896325yjbdc";

    // 加密
    @TargetApi(Build.VERSION_CODES.O)
    @SuppressLint("NewApi")
    public static String Encrypt(String sSrc, String sKey) throws Exception {
        if (sKey == null) {
            System.out.print("Key为空null");
            return null;
        }
        // 判断Key是否为16位
        if (sKey.length() != 16) {
            System.out.print("Key长度不是16位");
            return null;
        }
        byte[] raw = sKey.getBytes("utf-8");
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");//"算法/模式/补码方式"
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(sSrc.getBytes("utf-8"));

        return Base64.encodeToString(encrypted, Base64.DEFAULT);//此处使用BASE64做转码功能，同时能起到2次加密的作用。
    }
}
