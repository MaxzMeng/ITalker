package com.example.factory;

import android.support.annotation.StringRes;

import com.example.factory.data.group.GroupCenter;
import com.example.factory.data.group.GroupDispatcher;
import com.example.factory.data.message.MessageCenter;
import com.example.factory.data.message.MessageDispatcher;
import com.example.factory.data.user.UserCenter;
import com.example.factory.data.user.UserDispatcher;
import com.example.factory.model.api.RspModel;
import com.example.factory.persistence.Account;
import com.example.factory.utils.DBFlowExclusionStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import me.maxandroid.common.app.Application;
import me.maxandroid.factory.data.DataSource;

/**
 * Created by mxz on 18-3-15.
 */

public class Factory {
    private static final Factory instance;
    private final Executor executor;
    private final Gson gson;

    static {
        instance = new Factory();
    }

    private Factory() {
        executor = Executors.newFixedThreadPool(4);
        gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
                .setExclusionStrategies(new DBFlowExclusionStrategy())
                .create();
    }

    public static void setup() {
        FlowManager.init(new FlowConfig.Builder(app())
                .openDatabasesOnInit(true)
                .build());
        Account.load(app());
    }

    public static Application app() {
        return Application.getInstance();
    }

    public static void runOnAsync(Runnable runnable) {
        instance.executor.execute(runnable);
    }

    public static Gson getGson() {
        return instance.gson;
    }

    public static void decodeRspCode(RspModel model, DataSource.FailedCallback callback) {
        if (model == null)
            return;

        // 进行Code区分
        switch (model.getCode()) {
            case RspModel.SUCCEED:
                return;
            case RspModel.ERROR_SERVICE:
                decodeRspCode(R.string.data_rsp_error_service, callback);
                break;
            case RspModel.ERROR_NOT_FOUND_USER:
                decodeRspCode(R.string.data_rsp_error_not_found_user, callback);
                break;
            case RspModel.ERROR_NOT_FOUND_GROUP:
                decodeRspCode(R.string.data_rsp_error_not_found_group, callback);
                break;
            case RspModel.ERROR_NOT_FOUND_GROUP_MEMBER:
                decodeRspCode(R.string.data_rsp_error_not_found_group_member, callback);
                break;
            case RspModel.ERROR_CREATE_USER:
                decodeRspCode(R.string.data_rsp_error_create_user, callback);
                break;
            case RspModel.ERROR_CREATE_GROUP:
                decodeRspCode(R.string.data_rsp_error_create_group, callback);
                break;
            case RspModel.ERROR_CREATE_MESSAGE:
                decodeRspCode(R.string.data_rsp_error_create_message, callback);
                break;
            case RspModel.ERROR_PARAMETERS:
                decodeRspCode(R.string.data_rsp_error_parameters, callback);
                break;
            case RspModel.ERROR_PARAMETERS_EXIST_ACCOUNT:
                decodeRspCode(R.string.data_rsp_error_parameters_exist_account, callback);
                break;
            case RspModel.ERROR_PARAMETERS_EXIST_NAME:
                decodeRspCode(R.string.data_rsp_error_parameters_exist_name, callback);
                break;
            case RspModel.ERROR_ACCOUNT_TOKEN:
                Application.showToast(R.string.data_rsp_error_account_token);
                instance.logout();
                break;
            case RspModel.ERROR_ACCOUNT_LOGIN:
                decodeRspCode(R.string.data_rsp_error_account_login, callback);
                break;
            case RspModel.ERROR_ACCOUNT_REGISTER:
                decodeRspCode(R.string.data_rsp_error_account_register, callback);
                break;
            case RspModel.ERROR_ACCOUNT_NO_PERMISSION:
                decodeRspCode(R.string.data_rsp_error_account_no_permission, callback);
                break;
            case RspModel.ERROR_UNKNOWN:
            default:
                decodeRspCode(R.string.data_rsp_error_unknown, callback);
                break;
        }
    }

    private static void decodeRspCode(@StringRes final int resId,
                                      final DataSource.FailedCallback callback) {
        if (callback != null)
            callback.onDataNotAvailable(resId);
    }


    /**
     * 收到账户退出的消息需要进行账户退出重新登录
     */
    private void logout() {

    }

    public static void dispatchPush(String message) {

    }

    public static UserCenter getUserCenter() {
        return UserDispatcher.instance();
    }

    public static MessageCenter getMessageCenter() {
        return MessageDispatcher.instance();
    }

    public static GroupCenter getGroupCenter() {
        return GroupDispatcher.instance();
    }

}
