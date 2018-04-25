package me.maxandroid.italker.frags.message;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.OnClick;
import me.maxandroid.common.app.Fragment;
import me.maxandroid.common.widget.adapter.TextWatcherAdapter;
import me.maxandroid.italker.R;
import me.maxandroid.italker.activities.MessageActivity;

public abstract class ChatFragment extends Fragment implements AppBarLayout.OnOffsetChangedListener {
    String mReceiverId;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.recycler)
    RecyclerView mRecyclerView;
    @BindView(R.id.appbar)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.edit_content)
    EditText mContent;
    @BindView(R.id.btn_submit)
    View mSubmit;

    @Override
    protected void initArgs(Bundle bundle) {

        super.initArgs(bundle);
        mReceiverId = bundle.getString(MessageActivity.KEY_RECEIVER_ID);
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
        initToolbar();
        initAppbar();
        initEditContent();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void initAppbar() {
        mAppBarLayout.addOnOffsetChangedListener(this);
    }

    protected void initToolbar() {
        Toolbar toolbar = mToolbar;
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
    }

    private void initEditContent() {
        mContent.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void afterTextChanged(Editable s) {
                String content = s.toString().trim();
                boolean needSendMessage = !TextUtils.isEmpty(content);
                mSubmit.setActivated(needSendMessage);
            }
        });
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

    }

    @OnClick(R.id.btn_face)
    void onFaceClick() {

    }

    @OnClick(R.id.btn_record)
    void onRecordClick() {

    }

    @OnClick(R.id.btn_submit)
    void onSubmitClick() {
        if (mSubmit.isActivated()) {

        } else {
            onMoreClick();
        }
    }

    private void onMoreClick() {
        //TODO
    }
}
