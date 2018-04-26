package me.maxandroid.italker.frags.message;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.factory.model.db.Message;
import com.example.factory.model.db.User;
import com.example.factory.persistence.Account;
import com.example.factory.presenter.message.ChatContract;

import net.qiujuer.genius.ui.compat.UiCompat;
import net.qiujuer.genius.ui.widget.Loading;

import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;
import me.maxandroid.common.app.PresenterFragment;
import me.maxandroid.common.widget.PortraitView;
import me.maxandroid.common.widget.adapter.TextWatcherAdapter;
import me.maxandroid.common.widget.recycler.RecyclerAdapter;
import me.maxandroid.italker.R;
import me.maxandroid.italker.activities.MessageActivity;

public abstract class ChatFragment<InitModel>
        extends PresenterFragment<ChatContract.Presenter>
        implements AppBarLayout.OnOffsetChangedListener, ChatContract.View<InitModel> {
    protected Adapter mAdapter;
    protected String mReceiverId;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.recycler)
    RecyclerView mRecyclerView;
    @BindView(R.id.appbar)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.collapsingToolbarLayout)
    CollapsingToolbarLayout mCollapsingToolbarLayout;
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
        mAdapter = new Adapter();
        mRecyclerView.setAdapter(mAdapter);
        mPresenter.start();
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
            String content = mContent.getText().toString();
            mContent.setText("");
            mPresenter.pushText(content);
        } else {
            onMoreClick();
        }
    }

    private void onMoreClick() {
        //TODO
    }

    @Override
    public RecyclerAdapter<Message> getRecyclerAdapter() {
        return mAdapter;
    }

    @Override
    public void onAdapterDataChanged() {

    }

    private class Adapter extends RecyclerAdapter<Message> {
        @Override
        protected int getItemViewType(int position, Message message) {
            boolean isRight = Objects.equals(message.getSender().getId(), Account.getUserId());
            switch (message.getType()) {
                case Message.TYPE_STR:
                    return isRight ? R.layout.cell_chat_text_right : R.layout.cell_chat_text_left;
                case Message.TYPE_AUDIO:
                    return isRight ? R.layout.cell_chat_audio_right : R.layout.cell_chat_audio_left;
                case Message.TYPE_PIC:
                    return isRight ? R.layout.cell_chat_pic_right : R.layout.cell_chat_pic_left;
                default:
                    return isRight ? R.layout.cell_chat_text_right : R.layout.cell_chat_text_left;
            }
        }

        @Override
        protected ViewHolder<Message> onCreateViewHolder(View root, int viewType) {
            switch (viewType) {
                case R.layout.cell_chat_text_right:
                case R.layout.cell_chat_text_left:
                    return new TextHolder(root);
                case R.layout.cell_chat_audio_right:
                case R.layout.cell_chat_audio_left:
                    return new AudioHolder(root);
                case R.layout.cell_chat_pic_right:
                case R.layout.cell_chat_pic_left:
                    return new PicHolder(root);
                default:
                    return new TextHolder(root);
            }
        }
    }

    class BaseHolder extends RecyclerAdapter.ViewHolder<Message> {
        @BindView(R.id.im_portrait)
        PortraitView mPortrait;
        @Nullable
        @BindView(R.id.loading)
        Loading mLoading;

        public BaseHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(Message message) {
            User sender = message.getSender();
            sender.load();
            mPortrait.setup(Glide.with(ChatFragment.this), sender);
            if (mLoading != null) {
                int status = message.getStatus();
                if (status == Message.STATUS_DONE) {
                    mLoading.stop();
                    mLoading.setVisibility(View.GONE);
                } else if (status == Message.STATUS_CREATED) {
                    mLoading.setVisibility(View.VISIBLE);
                    mLoading.setProgress(0);
                    mLoading.setForegroundColor(UiCompat.getColor(getResources(), R.color.colorAccent));
                    mLoading.start();
                } else if (status == Message.STATUS_FAILED) {
                    mLoading.setVisibility(View.VISIBLE);
                    mLoading.setProgress(1);
                    mLoading.setForegroundColor(UiCompat.getColor(getResources(), R.color.alertImportant));
                    mLoading.stop();
                }
                mPortrait.setEnabled(status == Message.STATUS_FAILED);
            }
        }

        @OnClick(R.id.im_portrait)
        void onRePushClick() {
            if (mLoading != null && mPresenter.rePush(mData)) {

            }
        }
    }

    class TextHolder extends BaseHolder {
        @BindView(R.id.txt_content)
        TextView mContent;

        public TextHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(Message message) {
            super.onBind(message);
            mContent.setText(message.getContent());
        }
    }

    class AudioHolder extends BaseHolder {
        @BindView(R.id.txt_content)
        TextView mContent;

        public AudioHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(Message message) {
            super.onBind(message);
        }
    }

    class PicHolder extends BaseHolder {

        public PicHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(Message message) {
            super.onBind(message);
        }
    }
}

