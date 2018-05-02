package com.example.factory.data.group;

import android.text.TextUtils;

import com.example.factory.data.BaseDbRepository;
import com.example.factory.data.helper.GroupHelper;
import com.example.factory.model.db.Group;
import com.example.factory.model.db.Group_Table;
import com.example.factory.model.db.view.MemberUserModel;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.List;

public class GroupsRepository extends BaseDbRepository<Group> implements GroupsDataSource {
    @Override
    public void load(SucceedCallback<List<Group>> callback) {
        super.load(callback);

        SQLite.select()
                .from(Group.class)
                .orderBy(Group_Table.name, true)
                .limit(100)
                .async()
                .queryListResultCallback(this)
                .execute();
    }

    @Override
    protected boolean isRequired(Group group) {
        if (group.getGroupMemberCount() > 0) {
            group.holder = buildGroupHolder(group);
        } else {
            group.holder = null;
            GroupHelper.refreshGroupMember(group);
        }
        return true;
    }

    private String buildGroupHolder(Group group) {
        List<MemberUserModel> userModels = group.getLatelyGroupMembers();
        if (userModels == null || userModels.size() == 0) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        for (MemberUserModel userModel : userModels) {
            builder.append(TextUtils.isEmpty(userModel.alias) ? userModel.name : userModel.alias);
            builder.append(",");
        }
        builder.delete(builder.lastIndexOf(","), builder.length());
        return builder.toString();
    }
}
