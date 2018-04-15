package com.example.factory.presenter.search;

import com.example.factory.model.card.GroupCard;
import com.example.factory.model.card.UserCard;

import java.util.List;

import me.maxandroid.factory.presenter.BaseContract;

public interface SearchContract {
    interface Presenter extends BaseContract.Presenter {
        void search(String content);
    }

    interface UserView extends BaseContract.View<Presenter> {
        void onSearchDone(List<UserCard> userCards);
    }

    interface GroupView extends BaseContract.View<Presenter> {
        void onSearchDone(List<GroupCard> groupCards);
    }



}
