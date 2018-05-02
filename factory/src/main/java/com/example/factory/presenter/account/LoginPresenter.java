package com.example.factory.presenter.account;

import android.text.TextUtils;

import com.example.factory.R;
import com.example.factory.data.helper.AccountHelper;
import com.example.factory.model.api.account.LoginModel;
import com.example.factory.model.db.User;
import com.example.factory.persistence.Account;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

import me.maxandroid.factory.data.DataSource;
import me.maxandroid.factory.presenter.BasePresenter;

/**
 * Created by mxz on 18-3-17.
 */

public class LoginPresenter extends BasePresenter<LoginContract.View> implements LoginContract.Presenter, DataSource.Callback<User> {
    public LoginPresenter(LoginContract.View mView) {
        super(mView);
    }

    @Override
    public void login(String phone, String password) {
        start();
        final LoginContract.View view = getView();
        if (TextUtils.isEmpty(phone) || TextUtils.isEmpty(password)) {
            view.showError(R.string.data_account_login_invalid_parameter);
        } else {
            LoginModel model = new LoginModel(phone, password, Account.getPushId());
            AccountHelper.login(model, this);
        }
    }

    @Override
    public void onDataLoaded(User user) {
        final LoginContract.View view = getView();
        if (view == null) {
            return;
        }
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                view.loginSuccess();
            }
        });
    }

    @Override
    public void onDataNotAvailable(final int strRes) {
        final LoginContract.View view = getView();
        if (view == null) {
            return;
        }
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                view.showError(strRes);
            }
        });
    }
}
