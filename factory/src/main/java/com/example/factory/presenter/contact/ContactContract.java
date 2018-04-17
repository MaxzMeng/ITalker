package com.example.factory.presenter.contact;


import com.example.factory.model.db.User;

import me.maxandroid.common.widget.recycler.RecyclerAdapter;
import me.maxandroid.factory.presenter.BaseContract;

public interface ContactContract {
    interface Presenter extends BaseContract.Presenter {

    }

    interface View extends BaseContract.RecyclerView<Presenter,User> {
        RecyclerAdapter<User> getRecyclerAdapter();
        void onAdapterDataChanged();
    }
}
