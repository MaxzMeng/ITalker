package me.maxandroid.italker.frags.account;


import android.content.Context;
import android.widget.Button;
import android.widget.EditText;

import com.example.factory.presenter.account.RegisterContract;
import com.example.factory.presenter.account.RegisterPresenter;

import net.qiujuer.genius.ui.widget.Loading;

import butterknife.BindView;
import butterknife.OnClick;
import me.maxandroid.common.app.Fragment;
import me.maxandroid.common.app.PresenterFragment;
import me.maxandroid.italker.R;
import me.maxandroid.italker.activities.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends PresenterFragment<RegisterContract.Presenter> implements RegisterContract.View {

    private AccountTrigger mAccountTrigger;

    @BindView(R.id.edit_phone)
    EditText mPhone;
    @BindView(R.id.edit_name)
    EditText mName;
    @BindView(R.id.edit_password)
    EditText mPassword;
    @BindView(R.id.loading)
    Loading mLoading;
    @BindView(R.id.btn_submit)
    Button mSubmit;

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    protected RegisterContract.Presenter initPresenter() {
        return new RegisterPresenter(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mAccountTrigger = (AccountTrigger) context;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_register;
    }

    @OnClick(R.id.btn_submit)
    void onSubmitClick() {
        String phone = mPhone.getText().toString();
        String name = mName.getText().toString();
        String password = mPassword.getText().toString();
        mPresenter.register(phone, name, password);
    }

    @OnClick(R.id.txt_go_login)
    void onShowLoginClick() {
        mAccountTrigger.triggerView();
    }

    @Override
    public void showError(int str) {
        super.showError(str);

        mLoading.stop();
        mPhone.setEnabled(true);
        mName.setEnabled(true);
        mPassword.setEnabled(true);
        mSubmit.setEnabled(true);
    }

    @Override
    public void showLoading() {
        super.showLoading();

        mLoading.start();
        mPhone.setEnabled(false);
        mName.setEnabled(false);
        mPassword.setEnabled(false);
        mSubmit.setEnabled(false);
    }

    @Override
    public void registerSuccess() {
        MainActivity.show(getContext());
        getActivity().finish();
    }
}
