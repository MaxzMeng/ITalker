package me.maxandroid.factory.presenter;

/**
 * Created by mxz on 18-3-17.
 */

public class BasePresenter<T extends BaseContract.View> implements BaseContract.Presenter {
    protected T mView;


    public BasePresenter(T mView) {
        setView(mView);
    }

    protected void setView(T view) {
        this.mView = view;
        this.mView.setPresenter(this);
    }

    protected T getView() {
        return mView;
    }

    @Override
    public void start() {
        T view = mView;
        if (view != null) {
            view.showLoading();
        }

    }

    @Override
    public void destroy() {
        T view = mView;
        mView = null;
        if (view != null) {
            view.setPresenter(null);
        }
    }
}
