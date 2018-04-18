package me.maxandroid.common.app;


import android.content.Context;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;

import me.maxandroid.common.R;
import me.maxandroid.factory.presenter.BaseContract;

public abstract class PresenterToolBarActivity<Presenter extends BaseContract.Presenter> extends ToolBarActivity implements BaseContract.View<Presenter>{
    protected Presenter mPresenter;

    protected abstract Presenter initPresenter();

    @Override
    protected void initBefore() {
        super.initBefore();
        initPresenter();
    }


    @Override
    public void showError(int str) {
        if (mPlaceHolderView != null) {
            mPlaceHolderView.triggerError(str);
        } else {
            Application.showToast(str);
        }
    }

    @Override
    public void showLoading() {

        if (mPlaceHolderView != null) {
            mPlaceHolderView.triggerLoading();
        }
    }

    protected void hideLoading() {
        if (mPlaceHolderView != null) {
            mPlaceHolderView.triggerOk();
        }
    }
    @Override
    public void setPresenter(Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.destroy();
        }
    }
}
