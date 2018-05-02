package com.example.factory.presenter.contact;

import android.support.v7.util.DiffUtil;

import com.example.factory.data.helper.UserHelper;
import com.example.factory.data.user.ContactDataSource;
import com.example.factory.data.user.ContactRepository;
import com.example.factory.model.db.User;
import com.example.factory.presenter.BaseSourcePresenter;
import com.example.factory.utils.DiffUiDataCallback;

import java.util.List;

import me.maxandroid.common.widget.recycler.RecyclerAdapter;
import me.maxandroid.factory.data.DataSource;

public class ContactPresenter extends BaseSourcePresenter<User, User, ContactDataSource, ContactContract.View>
        implements ContactContract.Presenter, DataSource.SucceedCallback<List<User>> {
    public ContactPresenter(ContactContract.View mView) {
        super(new ContactRepository(), mView);
        mSource = new ContactRepository();
    }

    @Override
    public void start() {
        super.start();
        UserHelper.refreshContacts();
    }

    @Override
    public void onDataLoaded(List<User> users) {
        final ContactContract.View view = getView();
        if (view == null) {
            return;
        }
        RecyclerAdapter<User> adapter = view.getRecyclerAdapter();
        List<User> old = adapter.getItems();
        DiffUtil.Callback callback = new DiffUiDataCallback<>(old, users);
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(callback);
        refreshData(result, users);
    }
}
