package com.example.factory.presenter.group;

import android.support.v7.util.DiffUtil;

import com.example.factory.data.helper.GroupHelper;
import com.example.factory.data.group.GroupsDataSource;
import com.example.factory.data.group.GroupsRepository;
import com.example.factory.model.db.Group;
import com.example.factory.presenter.BaseSourcePresenter;
import com.example.factory.utils.DiffUiDataCallback;

import java.util.List;

public class GroupsPresenter extends BaseSourcePresenter<Group, Group, GroupsDataSource, GroupsContract.View>
        implements GroupsContract.Presenter {
    public GroupsPresenter(GroupsContract.View mView) {
        super(new GroupsRepository(), mView);
    }

    @Override
    public void start() {
        super.start();
        GroupHelper.refreshGroups();
    }

    @Override
    public void onDataLoaded(List<Group> groups) {
        final GroupsContract.View view = getView();
        if (view == null) {
            return;
        }
        List<Group> old = view.getRecyclerAdapter().getItems();
        DiffUiDataCallback<Group> callback = new DiffUiDataCallback<>(old, groups);
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(callback);
        refreshData(result, groups);
    }
}
