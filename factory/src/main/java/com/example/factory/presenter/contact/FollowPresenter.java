package com.example.factory.presenter.contact;

import com.example.factory.data.helper.UserHelper;
import com.example.factory.model.card.UserCard;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

import me.maxandroid.factory.data.DataSource;
import me.maxandroid.factory.presenter.BasePresenter;

public class FollowPresenter extends BasePresenter<FollowContract.View>
        implements FollowContract.Presenter, DataSource.Callback<UserCard> {

    public FollowPresenter(FollowContract.View mView) {
        super(mView);
    }

    @Override
    public void follow(String id) {
        start();
        UserHelper.follow(id, this);
    }

    @Override
    public void onDataLoaded(final UserCard userCard) {
        final FollowContract.View view = getView();
        if (view != null) {
            Run.onUiAsync(new Action() {
                @Override
                public void call() {
                    view.onFollowSuccess(userCard);
                }
            });
        }
    }

    @Override
    public void onDataNotAvailable(final int strRes) {
        final FollowContract.View view = getView();
        if (view != null) {
            Run.onUiAsync(new Action() {
                @Override
                public void call() {
                    view.showError(strRes);
                }
            });
        }
    }
}
