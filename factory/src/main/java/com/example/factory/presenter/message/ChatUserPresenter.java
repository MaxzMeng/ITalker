package com.example.factory.presenter.message;

import com.example.factory.data.helper.UserHelper;
import com.example.factory.data.message.MessageRepository;
import com.example.factory.model.db.Message;
import com.example.factory.model.db.User;

public class ChatUserPresenter extends ChatPresenter<ChatContract.UserView>
        implements ChatContract.Presenter {
    public ChatUserPresenter(ChatContract.UserView mView, String receiverId) {
        super(new MessageRepository(receiverId), mView, receiverId, Message.RECEIVER_TYPE_NONE);
    }

    @Override
    public void start() {
        super.start();
        User receiver = UserHelper.findFromLocal(mReceiverId);
        getView().onInit(receiver);
    }
}
