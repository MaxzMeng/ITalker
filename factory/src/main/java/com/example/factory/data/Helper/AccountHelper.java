package com.example.factory.data.Helper;

import android.text.TextUtils;

import com.example.factory.Factory;
import com.example.factory.R;
import com.example.factory.model.api.RspModel;
import com.example.factory.model.api.account.AccountRspModel;
import com.example.factory.model.api.account.LoginModel;
import com.example.factory.model.api.account.RegisterModel;
import com.example.factory.model.db.User;
import com.example.factory.net.Network;
import com.example.factory.net.RemoteService;
import com.example.factory.persistence.Account;

import me.maxandroid.factory.data.DataSource;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mxz on 18-3-17.
 */

public class AccountHelper {

    public static void register(final RegisterModel model, final DataSource.Callback<User> callback) {
        RemoteService service = Network.remote();

        Call<RspModel<AccountRspModel>> call = service.accountRegister(model);
        call.enqueue(new AccountRspCallback(callback));
    }

    public static void login(final LoginModel model, final DataSource.Callback<User> callback) {
        RemoteService service = Network.remote();

        Call<RspModel<AccountRspModel>> call = service.accountLogin(model);
        call.enqueue(new AccountRspCallback(callback));
    }

    public static void bindPush(final DataSource.Callback<User> callback) {
        String pushId = Account.getPushId();
        if (TextUtils.isEmpty(pushId)) {
            return;
        }
        RemoteService service = Network.remote();
        Call<RspModel<AccountRspModel>> call = service.accountBind(pushId);
        call.enqueue(new AccountRspCallback(callback));
    }


    private static class AccountRspCallback implements Callback<RspModel<AccountRspModel>> {
        final DataSource.Callback<User> callback;

        public AccountRspCallback(DataSource.Callback<User> callback) {
            this.callback = callback;
        }

        @Override
        public void onResponse(Call<RspModel<AccountRspModel>> call, Response<RspModel<AccountRspModel>> response) {
            RspModel<AccountRspModel> rspModel = response.body();
            if (rspModel.success()) {
                AccountRspModel accountRspModel = rspModel.getResult();
                User user = accountRspModel.getUser();
//                user.save();
                DbHelper.save(User.class, user);
                Account.login(accountRspModel);

                if (accountRspModel.isBind()) {
                    Account.setBind(true);
                    if (callback != null) {
                        callback.onDataLoaded(user);
                    }
                } else {
                    bindPush(callback);
//                        callback.onDataLoaded(accountRspModel.getUser());
                }
            } else {
                Factory.decodeRspCode(rspModel, callback);
            }
        }

        @Override
        public void onFailure(Call<RspModel<AccountRspModel>> call, Throwable t) {
            if (callback != null) {
                callback.onDataNotAvailable(R.string.data_network_error);
            }
        }
    }
}
