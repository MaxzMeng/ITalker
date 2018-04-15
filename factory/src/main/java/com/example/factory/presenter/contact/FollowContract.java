package com.example.factory.presenter.contact;

import com.example.factory.model.card.UserCard;

import me.maxandroid.factory.presenter.BaseContract;

public interface FollowContract {
    interface Presenter extends BaseContract.Presenter {
        void follow(String id);
    }

    interface View extends BaseContract.View<Presenter> {
        void onFollowSuccess(UserCard userCard);
    }
}
