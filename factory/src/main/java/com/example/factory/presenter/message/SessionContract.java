package com.example.factory.presenter.message;

import com.example.factory.model.db.Session;

import me.maxandroid.factory.presenter.BaseContract;

public interface SessionContract {
    interface Presenter extends BaseContract.Presenter {

    }


    interface View extends BaseContract.RecyclerView<Presenter, Session> {

    }
}

