package com.example.factory.presenter.account;

import android.text.TextUtils;
import android.util.Patterns;

import com.example.factory.R;
import com.example.factory.data.helper.AccountHelper;
import com.example.factory.model.api.account.RegisterModel;
import com.example.factory.model.db.User;
import com.example.factory.persistence.Account;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

import java.util.regex.Pattern;

import me.maxandroid.factory.data.DataSource;
import me.maxandroid.factory.presenter.BasePresenter;

/**
 * Created by mxz on 18-3-17.
 */

public class RegisterPresenter extends BasePresenter<RegisterContract.View> implements RegisterContract.Presenter, DataSource.Callback<User> {
    public RegisterPresenter(RegisterContract.View mView) {
        super(mView);
    }

    @Override
    public void register(String phone, String name, String password) {
        start();
        RegisterContract.View view = getView();
        if (!checkMobile(phone)) {
            view.showError(R.string.data_account_register_invalid_parameter_mobile);
        } else if (name.length() < 2) {
            view.showError(R.string.data_account_register_invalid_parameter_name);
        } else if (password.length() < 6) {
            view.showError(R.string.data_account_register_invalid_parameter_password);
        } else {
            RegisterModel model = new RegisterModel(phone, password, name, Account.getPushId());
            AccountHelper.register(model, this);
        }
    }

    @Override
    public boolean checkMobile(String phone) {
        return !TextUtils.isEmpty(phone) && Pattern.matches(Patterns.PHONE.toString(), phone);
    }

    @Override
    public void onDataLoaded(User user) {
        final RegisterContract.View view = getView();
        if (view == null) {
            return;
        }
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                view.registerSuccess();
            }
        });
    }

    @Override
    public void onDataNotAvailable(final int strRes) {
        final RegisterContract.View view = getView();
        if (view == null) {
            return;
        }
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                view.showError(strRes);
            }
        });
    }
}
