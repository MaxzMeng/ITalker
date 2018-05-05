package com.example.factory.presenter.message;

import com.example.factory.data.helper.GroupHelper;
import com.example.factory.data.helper.UserHelper;
import com.example.factory.data.message.MessageDataSource;
import com.example.factory.data.message.MessageGroupRepository;
import com.example.factory.model.db.Group;
import com.example.factory.model.db.Message;
import com.example.factory.model.db.User;
import com.example.factory.model.db.view.MemberUserModel;
import com.example.factory.persistence.Account;

import java.util.List;

public class ChatGroupPresenter extends ChatPresenter<ChatContract.GroupView> implements ChatContract.Presenter {
    public ChatGroupPresenter(ChatContract.GroupView mView, String receiverId) {
        super(new MessageGroupRepository(receiverId), mView, receiverId, Message.RECEIVER_TYPE_GROUP);
    }

    @Override
    public void start() {
        super.start();
        Group group = GroupHelper.findFromLocal(mReceiverId);
        if (group != null) {
            ChatContract.GroupView view = getView();
            boolean isAdmin = Account.getUserId().equalsIgnoreCase(group.getOwner().getId());
            view.showAdminOption(isAdmin);

            List<MemberUserModel> models = group.getLatelyGroupMembers();
            final long memberCount = group.getGroupMemberCount();
            long moreCount = memberCount - models.size();
            view.onInitGroupMembers(models, moreCount);
        }
    }
}
