package com.example.factory.presenter.message;

import android.support.v7.util.DiffUtil;
import android.text.TextUtils;

import com.example.factory.data.helper.MessageHelper;
import com.example.factory.data.message.MessageDataSource;
import com.example.factory.model.api.message.MsgCreateModel;
import com.example.factory.model.db.Message;
import com.example.factory.persistence.Account;
import com.example.factory.presenter.BaseSourcePresenter;
import com.example.factory.utils.DiffUiDataCallback;

import java.util.List;

public class ChatPresenter<View extends ChatContract.View> extends BaseSourcePresenter<Message, Message, MessageDataSource, View>
        implements ChatContract.Presenter {
    protected String mReceiverId;
    protected int mReceiverType;

    public ChatPresenter(MessageDataSource source, View mView,
                         String receiverId, int receiverType) {
        super(source, mView);
        this.mReceiverId = receiverId;
        this.mReceiverType = receiverType;
    }

    @Override
    public void pushText(String content) {
        MsgCreateModel model = new MsgCreateModel.Builder()
                .receiver(mReceiverId, mReceiverType)
                .content(content, Message.TYPE_STR)
                .build();

        MessageHelper.push(model);
    }


    @Override
    public void pushAudio(String path, long time) {
        if (TextUtils.isEmpty(path)) {
            return;
        }
        MsgCreateModel model = new MsgCreateModel.Builder()
                .receiver(mReceiverId, mReceiverType)
                .content(path, Message.TYPE_AUDIO)
                .attach(String.valueOf(time))
                .build();
        MessageHelper.push(model);
    }

    @Override
    public void pushImage(String[] paths) {
        if (paths == null || paths.length == 0) {
            return;
        }
        for (String path : paths) {
            MsgCreateModel model = new MsgCreateModel.Builder()
                    .receiver(mReceiverId, mReceiverType)
                    .content(path, Message.TYPE_PIC)
                    .build();

            MessageHelper.push(model);
        }
    }

    @Override
    public boolean rePush(Message message) {
        if (Account.isLogin() &&
                Account.getUserId().equalsIgnoreCase(message.getSender().getId())
                && message.getStatus() == Message.STATUS_FAILED) {
            message.setStatus(Message.STATUS_CREATED);
            MsgCreateModel model = MsgCreateModel.buildWithMessage(message);
            MessageHelper.push(model);
            return true;
        }
        return false;
    }

    @Override
    public void onDataLoaded(List<Message> messages) {
        ChatContract.View view = getView();
        if (view == null) {
            return;
        }
        List<Message> old = view.getRecyclerAdapter().getItems();
        DiffUiDataCallback<Message> callback = new DiffUiDataCallback<>(old, messages);
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(callback);
        refreshData(result, messages);
    }
}
