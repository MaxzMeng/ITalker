package me.maxandroid.factory.data;

import android.support.annotation.StringRes;

/**
 * Created by mxz on 18-3-17.
 */

public interface DataSource {
    interface Callback<T> extends SucceedCallback<T>, FailedCallback {

    }

    interface SucceedCallback<T> {
        void onDataLoaded(T t);
    }

    interface FailedCallback {
        void onDataNotAvailable(@StringRes int strRes);
    }
}
