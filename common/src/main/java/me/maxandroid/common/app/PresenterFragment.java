package me.maxandroid.common.app;

import android.content.Context;

import me.maxandroid.factory.presenter.BaseContract;

/**
 * Created by mxz on 18-3-17.
 */

public abstract class PresenterFragment<Presenter extends BaseContract.Presenter> extends Fragment
        implements BaseContract.View<Presenter> {
    protected Presenter mPresenter;

    protected abstract Presenter initPresenter();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
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


    @Override
    public void setPresenter(Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.destroy();
        }
    }
}
