package com.example.factory.presenter.message;

import com.example.factory.model.db.Group;
import com.example.factory.model.db.Message;
import com.example.factory.model.db.User;
import com.example.factory.model.db.view.MemberUserModel;

import java.util.List;

import me.maxandroid.factory.presenter.BaseContract;

public interface ChatContract {
    interface Presenter extends BaseContract.Presenter {
        void pushText(String content);

        void pushAudio(String path);

        void pushImage(String[] paths);

        boolean rePush(Message message);
    }

    interface View<InitModel> extends BaseContract.RecyclerView<Presenter, Message> {
        void onInit(InitModel model);
    }

    interface UserView extends View<User> {

    }

    interface GroupView extends View<Group> {
        void showAdminOption(boolean isAdmin);

        void onInitGroupMembers(List<MemberUserModel> members, long moreCount);
    }
}
