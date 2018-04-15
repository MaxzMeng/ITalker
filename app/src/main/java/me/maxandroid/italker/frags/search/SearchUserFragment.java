package me.maxandroid.italker.frags.search;


import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.factory.model.card.UserCard;
import com.example.factory.presenter.contact.FollowContract;
import com.example.factory.presenter.contact.FollowPresenter;
import com.example.factory.presenter.search.SearchContract;
import com.example.factory.presenter.search.SearchUserPresenter;

import net.qiujuer.genius.ui.Ui;
import net.qiujuer.genius.ui.compat.UiCompat;
import net.qiujuer.genius.ui.drawable.LoadingCircleDrawable;
import net.qiujuer.genius.ui.drawable.LoadingDrawable;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import me.maxandroid.common.app.Fragment;
import me.maxandroid.common.app.PresenterFragment;
import me.maxandroid.common.widget.EmptyView;
import me.maxandroid.common.widget.PortraitView;
import me.maxandroid.common.widget.recycler.RecyclerAdapter;
import me.maxandroid.italker.R;
import me.maxandroid.italker.activities.SearchActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchUserFragment extends PresenterFragment<SearchContract.Presenter> implements SearchActivity.SearchFragment, SearchContract.UserView {

    @BindView(R.id.empty)
    EmptyView mEmptyView;
    @BindView(R.id.recycler)
    RecyclerView mRecycler;

    private RecyclerAdapter<UserCard> mAdapter;

    public SearchUserFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_search_user;
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);

        mRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecycler.setAdapter(mAdapter = new RecyclerAdapter<UserCard>() {
            @Override
            protected int getItemViewType(int position, UserCard userCard) {
                return R.layout.cell_search;
            }

            @Override
            protected ViewHolder<UserCard> onCreateViewHolder(View root, int viewType) {
                return new SearchUserFragment.ViewHolder(root);
            }
        });

        mEmptyView.bind(mRecycler);
        setPlaceHolderView(mEmptyView);
    }

    @Override
    protected void initData() {
        super.initData();
        search("");
    }

    @Override
    public void search(String content) {
        mPresenter.search(content);
    }

    @Override
    public void onSearchDone(List<UserCard> userCards) {
        mAdapter.replace(userCards);
        mPlaceHolderView.triggerOkOrEmpty(mAdapter.getItemCount() > 0);
    }

    @Override
    protected SearchContract.Presenter initPresenter() {
        return new SearchUserPresenter(this);
    }

    class ViewHolder extends RecyclerAdapter.ViewHolder<UserCard>
            implements FollowContract.View {
        @BindView(R.id.im_portrait)
        PortraitView mPortraitView;
        @BindView(R.id.txt_name)
        TextView mName;
        @BindView(R.id.im_follow)
        ImageView mFollow;
        private FollowContract.Presenter mPresenter;

        public ViewHolder(View itemView) {
            super(itemView);
            new FollowPresenter(this);
        }

        @Override
        protected void onBind(UserCard userCard) {
            Glide.with(SearchUserFragment.this)
                    .load(userCard.getPortrait())
                    .centerCrop()
                    .into(mPortraitView);
            mName.setText(userCard.getName());
            mFollow.setEnabled(!userCard.isFollow());
        }

        @OnClick(R.id.im_follow)
        void onFollowClick() {
            mPresenter.follow(mData.getId());
        }

        @Override
        public void onFollowSuccess(UserCard userCard) {
            if (mFollow.getDrawable() instanceof LoadingDrawable) {
                ((LoadingDrawable) mFollow.getDrawable()).stop();
                mFollow.setImageResource(R.drawable.sel_opt_done_add);
            }
            updateData(userCard);
        }

        @Override
        public void showError(int str) {
            if (mFollow.getDrawable() instanceof LoadingDrawable) {
                LoadingDrawable drawable = (LoadingDrawable) mFollow.getDrawable();
                drawable.setProgress(1);
                drawable.stop();
            }
        }

        @Override
        public void showLoading() {
            int minSize = (int) Ui.dipToPx(getResources(), 22);
            int maxSize = (int) Ui.dipToPx(getResources(), 30);
            LoadingDrawable drawable = new LoadingCircleDrawable(minSize, maxSize);

            drawable.setBackgroundColor(0);
            drawable.setForegroundColor(new int[]{UiCompat.getColor(getResources(), R.color.white)});
            mFollow.setImageDrawable(drawable);
            drawable.start();
        }

        @Override
        public void setPresenter(FollowContract.Presenter presenter) {
            mPresenter = presenter;
        }
    }
}
