package com.example.factory.presenter.message;

import android.support.v7.util.DiffUtil;
import android.util.Log;

import com.example.factory.data.message.SessionDataSource;
import com.example.factory.data.message.SessionRepository;
import com.example.factory.model.db.Session;
import com.example.factory.presenter.BaseSourcePresenter;
import com.example.factory.utils.DiffUiDataCallback;

import java.util.List;

public class SessionPresenter extends BaseSourcePresenter<Session, Session, SessionDataSource, SessionContract.View>
        implements SessionContract.Presenter {

    public SessionPresenter( SessionContract.View mView) {
        super(new SessionRepository(), mView);
    }

    @Override
    public void onDataLoaded(List<Session> sessions) {
        SessionContract.View view = getView();
        if (view == null) {
            Log.d("onDataLoaded", "view is null");
            return;
        }
        List<Session> old = view.getRecyclerAdapter().getItems();
        DiffUiDataCallback<Session> callback = new DiffUiDataCallback<>(old, sessions);
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(callback);
        refreshData(result, sessions);
    }
}
