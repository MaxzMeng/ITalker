package com.example.factory.data.message;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.factory.data.BaseDbRepository;
import com.example.factory.model.db.Session;
import com.example.factory.model.db.Session_Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;

import java.util.Collections;
import java.util.List;

public class SessionRepository extends BaseDbRepository<Session> implements SessionDataSource {

    @Override
    public void load(SucceedCallback<List<Session>> callback) {
        super.load(callback);
        SQLite.select()
                .from(Session.class)
                .orderBy(Session_Table.modifyAt, false)
                .limit(100)
                .async()
                .queryListResultCallback(this)
                .execute();
    }

    @Override
    protected boolean isRequired(Session session) {
        return true;
    }

    @Override
    protected void insert(Session session) {
        dataList.addFirst(session);
    }

    @Override
    public void onListQueryResult(QueryTransaction transaction, @NonNull List<Session> tResult) {
        Log.d("onListQueryResult", "onListQueryResult: "+tResult.size());
        Collections.reverse(tResult);
        super.onListQueryResult(transaction, tResult);
    }
}
