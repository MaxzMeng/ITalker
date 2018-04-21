package me.maxandroid.italker.frags.main;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.factory.model.db.User;
import com.example.factory.presenter.contact.ContactContract;
import com.example.factory.presenter.contact.ContactPresenter;

import butterknife.BindView;
import butterknife.OnClick;
import me.maxandroid.common.app.PresenterFragment;
import me.maxandroid.common.widget.EmptyView;
import me.maxandroid.common.widget.PortraitView;
import me.maxandroid.common.widget.recycler.RecyclerAdapter;
import me.maxandroid.italker.R;
import me.maxandroid.italker.activities.MessageActivity;
import me.maxandroid.italker.activities.PersonalActivity;


public class ContactFragment extends PresenterFragment<ContactContract.Presenter>
        implements ContactContract.View {


    @BindView(R.id.empty)
    EmptyView mEmptyView;
    @BindView(R.id.recycler)
    RecyclerView mRecycler;
    private RecyclerAdapter<User> mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_contact;
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
        mRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecycler.setAdapter(mAdapter = new RecyclerAdapter<User>() {
            @Override
            protected int getItemViewType(int position, User userCard) {
                return R.layout.cell_contact_list;
            }

            @Override
            protected ViewHolder<User> onCreateViewHolder(View root, int viewType) {
                return new ContactFragment.ViewHolder(root);
            }
        });

        mEmptyView.bind(mRecycler);
        setPlaceHolderView(mEmptyView);
        mAdapter.setListener(new RecyclerAdapter.AdapterListenerImpl<User>() {
            @Override
            public void onItemClick(RecyclerAdapter.ViewHolder holder, User user) {
                MessageActivity.show(getContext(), user);
            }
        });
    }

    @Override
    public RecyclerAdapter<User> getRecyclerAdapter() {
        return mAdapter;
    }

    @Override
    public void onAdapterDataChanged() {
        mPlaceHolderView.triggerOkOrEmpty(mAdapter.getItemCount() > 0);
    }

    @Override
    protected void onFirstInit() {
        super.onFirstInit();
        mPresenter.start();
    }

    @Override
    protected ContactContract.Presenter initPresenter() {
        return new ContactPresenter(this);
    }

    class ViewHolder extends RecyclerAdapter.ViewHolder<User> {

        @BindView(R.id.im_portrait)
        PortraitView mPortraitView;
        @BindView(R.id.txt_name)
        TextView mName;

        @BindView(R.id.txt_desc)
        TextView mDesc;

        public ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(User user) {
            mPortraitView.setup(Glide.with(ContactFragment.this), user);
            mName.setText(user.getName());
            mDesc.setText(user.getDesc());
        }

        @OnClick(R.id.im_portrait)
        void onPortraitClick() {
            PersonalActivity.show(getContext(), mData.getId());
        }
    }
}
