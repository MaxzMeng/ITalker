package com.example.factory.persistence;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.example.factory.Factory;
import com.example.factory.model.api.account.AccountRspModel;
import com.example.factory.model.db.User;
import com.example.factory.model.db.User_Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;

/**
 * Created by mxz on 18-3-17.
 */

public class Account {
    private static final String KEY_PUSH_ID = "KEY_PUSH_ID";
    private static final String KEY_IS_BIND = "KEY_IS_BIND";
    private static final String KEY_TOKEN = "KEY_TOKEN";
    private static final String KEY_USER_ID = "KEY_USER_ID";
    private static final String KEY_ACCOUNT = "KEY_ACCOUNT";
    private static String pushId;
    private static boolean isBind;
    private static String token;
    private static String userId;
    private static String account;

    private static void save(Context context) {
        // 获取数据持久化的SP
        SharedPreferences sp = context.getSharedPreferences(Account.class.getName(),
                Context.MODE_PRIVATE);
        // 存储数据
        sp.edit()
                .putString(KEY_PUSH_ID, pushId)
                .putBoolean(KEY_IS_BIND, isBind)
                .putString(KEY_TOKEN, token)
                .putString(KEY_USER_ID, userId)
                .putString(KEY_ACCOUNT, account)
                .apply();
    }

    public static void load(Context context) {
        SharedPreferences sp = context.getSharedPreferences(Account.class.getName(),
                Context.MODE_PRIVATE);
        pushId = sp.getString(KEY_PUSH_ID, "");
        isBind = sp.getBoolean(KEY_IS_BIND, false);
        token = sp.getString(KEY_TOKEN, "");
        userId = sp.getString(KEY_USER_ID, "");
        account = sp.getString(KEY_ACCOUNT, "");
    }
    public static void setPushId(String pushId) {
        Account.pushId = pushId;
        Account.save(Factory.app());
    }

    public static String getPushId() {
        return pushId;
    }


    public static boolean isLogin() {
        return !TextUtils.isEmpty(userId) && !TextUtils.isEmpty(token);
    }

    public static boolean isComplete() {
        //TODO
        return isLogin();
    }

    public static boolean isBind() {
        return isBind;
    }

    public static void setBind(boolean isBind) {
        Account.isBind = isBind;
        Account.save(Factory.app());
    }

    public static void login(AccountRspModel model) {
        Account.token = model.getToken();
        Account.account = model.getAccount();
        Account.userId = model.getUser().getId();
        save(Factory.app());
    }

    public static User getUser() {
//        return null;
        return TextUtils.isEmpty(userId) ? new User() : SQLite.select()
                .from(User.class)
                .where(User_Table.id.eq(userId))
                .querySingle();
    }

    public static String getToken() {
        return token;
    }
}
