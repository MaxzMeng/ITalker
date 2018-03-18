package me.maxandroid.factory.presenter;

import android.support.annotation.StringRes;

/**
 * Created by mxz on 18-3-17.
 */

public interface BaseContract {
    interface View<T extends Presenter> {
        void showError(@StringRes int str);

        void showLoading();

        void setPresenter(T presenter);
    }

    interface Presenter {
        void start();

        void destroy();
    }
}
