package com.example.factory.presenter;

import java.util.List;

import me.maxandroid.factory.data.DataSource;
import me.maxandroid.factory.data.DbDataSource;
import me.maxandroid.factory.presenter.BaseContract;
import me.maxandroid.factory.presenter.BaseRecyclerPresenter;

public abstract class BaseSourcePresenter<Data, ViewModel, Source extends DbDataSource, View extends BaseContract.RecyclerView>
        extends BaseRecyclerPresenter<ViewModel, View>
        implements DataSource.SucceedCallback<List<Data>> {
    protected Source mSource;

    public BaseSourcePresenter(Source source, View mView) {
        super(mView);
        this.mSource = source;
    }

    @Override
    public void start() {
        super.start();
        if (mSource != null) {
            mSource.load(this);
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        mSource.dispose();
        mSource = null;
    }
}
