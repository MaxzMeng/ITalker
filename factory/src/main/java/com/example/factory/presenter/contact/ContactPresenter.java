package com.example.factory.presenter.contact;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.factory.model.db.User;
import com.example.factory.model.db.User_Table;
import com.example.factory.persistence.Account;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;

import java.util.List;

import me.maxandroid.factory.presenter.BasePresenter;

public class ContactPresenter extends BasePresenter<ContactContract.View>
        implements ContactContract.Presenter {
    public ContactPresenter(ContactContract.View mView) {
        super(mView);
    }

    @Override
    public void start() {
        super.start();
        SQLite.select()
                .from(User.class)
                .where(User_Table.isFollow.eq(true))
                .and(User_Table.id.notEq(Account.getUserId()))
        .orderBy(User_Table.name,true)
        .async()
        .queryListResultCallback(new QueryTransaction.QueryResultListCallback<User>() {
            @Override
            public void onListQueryResult(QueryTransaction transaction, @NonNull List<User> tResult) {
                getView().getRecyclerAdapter().replace(tResult);
                getView().onAdapterDataChanged();
            }
        })
        .execute();
    }
}
