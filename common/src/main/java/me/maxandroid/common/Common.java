package me.maxandroid.common;

/**
 * Created by MXZ on 2018/3/9.
 */

public class Common {
    public interface Constance {
        String REGEX_MOBILE = "[1][3,4,5,7,8][0-9]{9}$";

        String API_URL = "http://59.67.152.49:28688/api/";

        long MAX_UPLOAD_IMAGE_LENGTH = 860 * 1024;
    }
}
