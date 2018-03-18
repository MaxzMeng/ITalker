package com.example.factory.presenter.account;

import android.support.annotation.StringRes;

import me.maxandroid.factory.presenter.BaseContract;

/**
 * Created by mxz on 18-3-17.
 */

public interface RegisterContract {
    interface View extends BaseContract.View<Presenter>{
        void registerSuccess();
    }

    interface Presenter extends BaseContract.Presenter{
        void register(String phone, String name, String password);

        boolean checkMobile(String phone);
    }
}
