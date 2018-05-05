package com.example.factory.presenter.group;

import com.example.factory.model.db.view.MemberUserModel;

import me.maxandroid.factory.presenter.BaseContract;
import me.maxandroid.factory.presenter.BasePresenter;

public interface GroupMemberContract {
    interface Presenter extends BaseContract.Presenter {
        void refresh();
    }

    interface View extends BaseContract.RecyclerView<Presenter, MemberUserModel> {
        String getGroupId();
    }

}
