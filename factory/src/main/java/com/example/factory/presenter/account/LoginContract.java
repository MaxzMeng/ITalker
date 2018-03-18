package com.example.factory.presenter.account;

import android.support.annotation.StringRes;

import me.maxandroid.factory.presenter.BaseContract;

/**
 * Created by mxz on 18-3-17.
 */

public interface LoginContract {
    interface View extends BaseContract.View<Presenter>{
        void loginSuccess();
    }

    interface Presenter extends BaseContract.Presenter {
        void login(String phone, String password);
    }
}
