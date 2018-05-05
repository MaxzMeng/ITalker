package com.example.factory.presenter.group;

import com.example.factory.Factory;
import com.example.factory.data.helper.GroupHelper;
import com.example.factory.data.helper.UserHelper;
import com.example.factory.model.db.view.MemberUserModel;
import com.example.factory.model.db.view.UserSampleModel;

import java.util.ArrayList;
import java.util.List;

import me.maxandroid.factory.presenter.BaseRecyclerPresenter;

public class GroupMembersPresenter extends BaseRecyclerPresenter<MemberUserModel, GroupMemberContract.View> implements GroupMemberContract.Presenter {

    private String id;

    public GroupMembersPresenter(GroupMemberContract.View mView) {
        super(mView);
    }

    @Override
    public void refresh() {
        start();

        Factory.runOnAsync(loader);
    }

    private Runnable loader = new Runnable() {
        @Override
        public void run() {
            GroupMemberContract.View view = getView();
            if (view == null) {
                return;
            }
            String groupId = view.getGroupId();
            List<MemberUserModel> models = GroupHelper.getMemberUsers(groupId, -1);
            refreshData(models);
        }
    };
}
