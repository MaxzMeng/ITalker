package me.maxandroid.common.app;


import android.app.ProgressDialog;
import android.content.DialogInterface;

import me.maxandroid.common.R;
import me.maxandroid.factory.presenter.BaseContract;

public abstract class PresenterToolBarActivity<Presenter extends BaseContract.Presenter> extends ToolBarActivity implements BaseContract.View<Presenter> {
    protected Presenter mPresenter;
    protected ProgressDialog mLoadingDialog;

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
        } else {
            ProgressDialog dialog = mLoadingDialog;
            if (dialog == null) {
                dialog = new ProgressDialog(this,R.style.AppTheme_Dialog_Alert_Light);
                dialog.setCanceledOnTouchOutside(false);
                dialog.setCancelable(true);
                dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        finish();
                    }
                });
                dialog.show();
                mLoadingDialog = dialog;
            }

            dialog.setMessage(getText(R.string.prompt_loading));
        }
    }

    protected void hideDialogLoading() {
        ProgressDialog dialog = mLoadingDialog;
        if (dialog != null) {
            mLoadingDialog = null;
            dialog.dismiss();
        }
    }

    protected void hideLoading() {
        hideDialogLoading();
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
