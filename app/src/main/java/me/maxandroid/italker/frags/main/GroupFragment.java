package me.maxandroid.italker.frags.main;


import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.factory.model.db.Group;
import com.example.factory.model.db.User;
import com.example.factory.presenter.group.GroupsContract;
import com.example.factory.presenter.group.GroupsPresenter;

import butterknife.BindView;
import butterknife.OnClick;
import me.maxandroid.common.app.Fragment;
import me.maxandroid.common.app.PresenterFragment;
import me.maxandroid.common.widget.EmptyView;
import me.maxandroid.common.widget.PortraitView;
import me.maxandroid.common.widget.recycler.RecyclerAdapter;
import me.maxandroid.italker.R;
import me.maxandroid.italker.activities.MessageActivity;
import me.maxandroid.italker.activities.PersonalActivity;


public class GroupFragment extends PresenterFragment<GroupsContract.Presenter>
        implements GroupsContract.View {

    @BindView(R.id.empty)
    EmptyView mEmptyView;
    @BindView(R.id.recycler)
    RecyclerView mRecycler;
    private RecyclerAdapter<Group> mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_group;
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
        mRecycler.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mRecycler.setAdapter(mAdapter = new RecyclerAdapter<Group>() {
            @Override
            protected int getItemViewType(int position, Group group) {
                return R.layout.cell_group_list;
            }

            @Override
            protected ViewHolder<Group> onCreateViewHolder(View root, int viewType) {
                return new GroupFragment.ViewHolder(root);
            }
        });
        mAdapter.setListener(new RecyclerAdapter.AdapterListenerImpl<Group>() {
            @Override
            public void onItemClick(RecyclerAdapter.ViewHolder holder, Group group) {
                MessageActivity.show(getContext(), group);
            }
        });

        mEmptyView.bind(mRecycler);
        setPlaceHolderView(mEmptyView);
    }

    @Override
    protected void onFirstInit() {
        super.onFirstInit();
        mPresenter.start();
    }

    @Override
    public RecyclerAdapter<Group> getRecyclerAdapter() {
        return mAdapter;
    }

    @Override
    public void onAdapterDataChanged() {
        mPlaceHolderView.triggerOkOrEmpty(mAdapter.getItemCount() > 0);
    }

    @Override
    protected GroupsContract.Presenter initPresenter() {
        return new GroupsPresenter(this);
    }

    class ViewHolder extends RecyclerAdapter.ViewHolder<Group> {

        @BindView(R.id.im_portrait)
        PortraitView mPortraitView;
        @BindView(R.id.txt_name)
        TextView mName;

        @BindView(R.id.txt_desc)
        TextView mDesc;
        @BindView(R.id.txt_member)
        TextView mMember;

        public ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(Group group) {
            mPortraitView.setup(Glide.with(GroupFragment.this), group.getPicture());
            mName.setText(group.getName());
            mDesc.setText(group.getDesc());

            if (group.holder != null && group.holder instanceof String) {
                mMember.setText((String) group.holder);
            } else {
                mMember.setText("");
            }
        }

        @OnClick(R.id.im_portrait)
        void onPortraitClick() {
            PersonalActivity.show(getContext(), mData.getId());
        }
    }
}
