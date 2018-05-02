package com.example.factory.presenter.group;


import com.example.factory.model.db.Group;
import com.example.factory.model.db.User;

import me.maxandroid.common.widget.recycler.RecyclerAdapter;
import me.maxandroid.factory.presenter.BaseContract;

public interface GroupsContract {
    interface Presenter extends BaseContract.Presenter {

    }

    interface View extends BaseContract.RecyclerView<Presenter,Group> {
        RecyclerAdapter<Group> getRecyclerAdapter();
        void onAdapterDataChanged();
    }
}
