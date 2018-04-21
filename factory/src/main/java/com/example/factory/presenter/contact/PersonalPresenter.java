package com.example.factory.presenter.contact;

import com.example.factory.Factory;
import com.example.factory.data.Helper.UserHelper;
import com.example.factory.model.db.User;
import com.example.factory.persistence.Account;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

import me.maxandroid.factory.presenter.BasePresenter;

public class PersonalPresenter extends BasePresenter<PersonalContract.View> implements PersonalContract.Presenter{
    private User user;
    public PersonalPresenter(PersonalContract.View mView) {
        super(mView);
    }

    @Override
    public void start() {
        super.start();

        Factory.runOnAsync(new Runnable() {
            @Override
            public void run() {
                PersonalContract.View view = getView();
                if (view != null) {
                    String id = getView().getUserId();
                    User user = UserHelper.searchFirstOfNet(id);
                    onLoaded(view, user);
                }
            }
        });

    }



    private void onLoaded(final PersonalContract.View view, final User user) {
        this.user = user;
        final boolean isSelf = user.getId().equalsIgnoreCase(Account.getUserId());
        final boolean isFollow = isSelf || user.isFollow();
        final boolean allowSayHello = isFollow && !isSelf;
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                view.onLoadDone(user);
                view.setFollowStatus(isFollow);
                view.allowSayHello(allowSayHello);
            }
        });
    }
    @Override
    public User getUserPersonal() {
        return user;
    }
}
