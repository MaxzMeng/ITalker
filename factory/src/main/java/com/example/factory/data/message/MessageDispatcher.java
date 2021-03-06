package com.example.factory.data.message;

import android.text.TextUtils;

import com.example.factory.data.helper.DbHelper;
import com.example.factory.data.helper.GroupHelper;
import com.example.factory.data.helper.MessageHelper;
import com.example.factory.data.helper.UserHelper;
import com.example.factory.model.card.MessageCard;
import com.example.factory.model.db.Group;
import com.example.factory.model.db.Message;
import com.example.factory.model.db.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MessageDispatcher implements MessageCenter {
    private static MessageCenter instance;
    private final Executor executor = Executors.newSingleThreadExecutor();

    public static MessageCenter instance() {
        if (instance == null) {
            synchronized (MessageDispatcher.class) {
                if (instance == null) {
                    instance = new MessageDispatcher();
                }
            }
        }
        return instance;
    }

    private MessageDispatcher() {

    }

    @Override
    public void dispatch(MessageCard... cards) {
        if (cards == null || cards.length == 0) {
            return;
        }
        executor.execute(new MessageCardHandler(cards));
    }

    private class MessageCardHandler implements Runnable {
        private final MessageCard[] cards;

        public MessageCardHandler(MessageCard... cards) {
            this.cards = cards;
        }

        @Override
        public void run() {
            List<Message> messages = new ArrayList<>();
            for (MessageCard card : cards) {
                if (card == null || TextUtils.isEmpty(card.getSenderId())
                        || TextUtils.isEmpty(card.getId())
                        || (TextUtils.isEmpty(card.getReceiverId())
                        && TextUtils.isEmpty(card.getGroupId())))
                    continue;

                Message message = MessageHelper.findFromLocal(card.getId());
                if (message != null) {
                    // 如果已经完成则不做处理
                    if (message.getStatus() == Message.STATUS_DONE)
                        continue;
                    // 新状态为完成才更新服务器时间，不然不做更新
                    if (card.getStatus() == Message.STATUS_DONE)
                        message.setCreateAt(card.getCreateAt());
                    // 更新一些会变化的内容
                    message.setContent(card.getContent());
                    message.setAttach(card.getAttach());
                    message.setStatus(card.getStatus());
                } else {
                    User sender = UserHelper.search(card.getSenderId());
                    User receiver = null;
                    Group group = null;
                    if (!TextUtils.isEmpty(card.getReceiverId())) {
                        receiver = UserHelper.search(card.getReceiverId());
                    } else if (!TextUtils.isEmpty(card.getGroupId())) {
                        group = GroupHelper.findFromLocal(card.getGroupId());
                    }
                    if (receiver == null && group == null)
                        continue;

                    message = card.build(sender, receiver, group);
                }
                messages.add(message);
            }
            if (messages.size() > 0)
                DbHelper.save(Message.class, messages.toArray(new Message[0]));
        }
    }
}
