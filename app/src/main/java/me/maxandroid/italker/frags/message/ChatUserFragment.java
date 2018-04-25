package me.maxandroid.italker.frags.message;


import android.app.Fragment;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import butterknife.BindView;
import butterknife.OnClick;
import me.maxandroid.common.widget.PortraitView;
import me.maxandroid.italker.R;
import me.maxandroid.italker.activities.PersonalActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatUserFragment extends ChatFragment {

    @BindView(R.id.im_portrait)
    PortraitView mPortraitView;

    private MenuItem mUserInfoMenuItem;
    public ChatUserFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_chat_user;
    }



    @Override
    protected void initToolbar() {
        super.initToolbar();
        Toolbar toolbar = mToolbar;
        toolbar.inflateMenu(R.menu.chat_user);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.action_person) {
                    onPortraitClick();
                }
                return false;
            }
        });

        mUserInfoMenuItem = toolbar.getMenu().findItem(R.id.action_person);
    }

    @OnClick(R.id.im_portrait)
    void onPortraitClick() {
        PersonalActivity.show(getContext(), mReceiverId);
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        super.onOffsetChanged(appBarLayout, verticalOffset);
        View view = mPortraitView;
        MenuItem menuItem = mUserInfoMenuItem;
        if (view == null || menuItem == null) {
            return;
        }

        if (verticalOffset == 0) {
            view.setVisibility(View.VISIBLE);
            view.setScaleX(1);
            view.setScaleY(1);
            view.setAlpha(1);
            menuItem.setVisible(false);
            menuItem.getIcon().setAlpha(0);
        } else {
            verticalOffset = Math.abs(verticalOffset);
            final int totalScroll = appBarLayout.getTotalScrollRange();
            if (verticalOffset >= totalScroll) {
                view.setVisibility(View.INVISIBLE);
                view.setScaleX(0);
                view.setScaleY(0);
                view.setAlpha(0);
                menuItem.setVisible(true);
                menuItem.getIcon().setAlpha(255);
            } else {
                float progress = 1 - verticalOffset / (float) totalScroll;
                view.setVisibility(View.VISIBLE);
                view.setScaleX(progress);
                view.setScaleY(progress);
                view.setAlpha(progress);
                menuItem.setVisible(true);
                menuItem.getIcon().setAlpha((int) (255-255*progress));
            }
        }
    }
}
