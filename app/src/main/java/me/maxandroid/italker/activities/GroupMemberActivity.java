package me.maxandroid.italker.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.factory.model.db.GroupMember;
import com.example.factory.model.db.view.MemberUserModel;
import com.example.factory.presenter.group.GroupCreateContract;
import com.example.factory.presenter.group.GroupMemberContract;
import com.example.factory.presenter.group.GroupMembersPresenter;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import me.maxandroid.common.app.PresenterToolBarActivity;
import me.maxandroid.common.app.ToolBarActivity;
import me.maxandroid.common.widget.PortraitView;
import me.maxandroid.common.widget.recycler.RecyclerAdapter;
import me.maxandroid.italker.R;

public class GroupMemberActivity extends PresenterToolBarActivity<GroupMemberContract.Presenter> implements GroupMemberContract.View {
    private static final String KEY_GROUP_ID = "KET_GROUP_ID";
    private static final String KEY_GROUP_ADMIN = "KET_GROUP_ADMIN";
    @BindView(R.id.recycler)
    RecyclerView mRecycler;

    private RecyclerAdapter<MemberUserModel> mAdapter;
    private String groupId;
    private boolean isAdmin;
    public static void show(Context context, String groupId) {
        show(context, groupId, false);
    }

    public static void showAdmin(Context context, String groupId) {
        show(context, groupId, true);
    }

    private static void show(Context context, String groupId, boolean isAdmin) {
        if (TextUtils.isEmpty(groupId)) {
            return;
        }
        Intent intent = new Intent(context, GroupMemberActivity.class);
        intent.putExtra(KEY_GROUP_ID, groupId);
        intent.putExtra(KEY_GROUP_ADMIN, isAdmin);
        context.startActivity(intent);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_group_member;
    }

    @Override
    protected boolean initArgs(Bundle bundle) {
        String groupId = bundle.getString(KEY_GROUP_ID);
        this.groupId = groupId;
        this.isAdmin = bundle.getBoolean(KEY_GROUP_ADMIN);
        return !TextUtils.isEmpty(groupId);
    }

    @Override
    protected void initWidget() {
        super.initWidget();

        setTitle(R.string.title_member_list);

        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mRecycler.setAdapter(mAdapter = new RecyclerAdapter<MemberUserModel>() {
            @Override
            protected int getItemViewType(int position, MemberUserModel memberUserModel) {
                return R.layout.cell_group_create_contact;
            }

            @Override
            protected ViewHolder<MemberUserModel> onCreateViewHolder(View root, int viewType) {
                return new GroupMemberActivity.ViewHolder(root);
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        mPresenter.refresh();
    }

    @Override
    protected GroupMemberContract.Presenter initPresenter() {
        return new GroupMembersPresenter(this);
    }

    @Override
    public RecyclerAdapter<MemberUserModel> getRecyclerAdapter() {
        return mAdapter;
    }

    @Override
    public void onAdapterDataChanged() {
        hideLoading();
    }

    @Override
    public String getGroupId() {
        return groupId;
    }


    class ViewHolder extends RecyclerAdapter.ViewHolder<MemberUserModel> {

        @BindView(R.id.im_portrait)
        PortraitView mPortrait;
        @BindView(R.id.txt_name)
        TextView mName;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.findViewById(R.id.cb_select).setVisibility(View.GONE);

        }


        @Override
        protected void onBind(MemberUserModel model) {
            mPortrait.setup(Glide.with(GroupMemberActivity.this), model.portrait);
            mName.setText(model.name);
        }

        @OnClick(R.id.im_portrait)
        void onPortraitClick() {
            PersonalActivity.show(GroupMemberActivity.this, mData.userId);
        }
    }
}
