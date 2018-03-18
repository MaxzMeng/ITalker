package com.example.factory.presenter.user;

import me.maxandroid.factory.presenter.BaseContract;

/**
 * Created by mxz on 18-3-18.
 */

public interface UpdateInfoContract {
    interface Presenter extends BaseContract.Presenter {
        void update(String photoFilePath, String desc, boolean isMan);
    }

    interface View extends BaseContract.View<Presenter> {
        void updateSucceed();
    }
}
