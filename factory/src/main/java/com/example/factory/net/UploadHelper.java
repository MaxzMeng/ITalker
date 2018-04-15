package com.example.factory.net;

import android.text.format.DateFormat;
import android.util.Log;

import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.example.factory.Factory;


import java.io.File;
import java.util.Date;

import me.maxandroid.common.app.Application;
import me.maxandroid.utils.HashUtil;

/**
 * Created by mxz on 18-3-15.
 */

public class UploadHelper {
    private static final String TAG = UploadHelper.class.getSimpleName();
        private static final String ENDPOINT = "oss-cn-beijing.aliyuncs.com";
    private static final String BUCKET_NAME = "immax";
//    public static final String ENDPOINT = "http://oss-cn-hongkong.aliyuncs.com";
//     上传的仓库名
//    private static final String BUCKET_NAME = "italker-new";

    private static OSS getClient() {
        OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider("LTAI7tHGKyldTLU0", "HilwdqruSNkbqUzRibiymp1kGgBhvy");
        OSS oss = new OSSClient(Factory.app(), ENDPOINT, credentialProvider);
        return oss;
//        OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider(
//                "LTAIYQD07p05pHQW", "2txxzT8JXiHKEdEjylumFy6sXcDQ0G");
//        return new OSSClient(Factory.app(), ENDPOINT, credentialProvider);
    }

    private static String upload(String objKey, String path) {
        PutObjectRequest request = new PutObjectRequest(BUCKET_NAME, objKey, path);
        try {
            OSS client = getClient();
            PutObjectResult result = client.putObject(request);
            String url = client.presignPublicObjectURL(BUCKET_NAME, objKey);
            Log.d(TAG, url);
            return url;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String uploadImage(String path) {
        String key = getImageObjKey(path);
        return upload(key, path);
    }

    public static String uploadPortrait(String path) {
        String key = getPortraitObjKey(path);
        return upload(key, path);
    }

    public static String uploadAudio(String path) {
        String key = getAudioObjKey(path);
        return upload(key, path);
    }

    private static String getDataString() {
        return DateFormat.format("yyyyMM", new Date()).toString();
    }

    private static String getImageObjKey(String path) {
        String fileMd5 = HashUtil.getMD5String(new File(path));

        String dateString = getDataString();
        return String.format("image/%s/%s.jpg", dateString, fileMd5);
    }

    private static String getPortraitObjKey(String path) {
        String fileMd5 = HashUtil.getMD5String(new File(path));

        String dateString = getDataString();
        return String.format("portrait/%s/%s.jpg", dateString, fileMd5);
    }

    private static String getAudioObjKey(String path) {
        String fileMd5 = HashUtil.getMD5String(new File(path));

        String dateString = getDataString();
        return String.format("audio/%s/%s.mp3", dateString, fileMd5);
    }
}
