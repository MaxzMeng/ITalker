package com.example.factory.presenter.contact;

import com.example.factory.model.db.User;

import me.maxandroid.factory.presenter.BaseContract;

public interface PersonalContract {
    interface Presenter extends BaseContract.Presenter {
        User getUserPersonal();
    }

    interface View extends BaseContract.View<Presenter> {
        String getUserId();

        void onLoadDone(User user);

        void allowSayHello(boolean isAllow);

        void setFollowStatus(boolean isFollow);
    }

}
