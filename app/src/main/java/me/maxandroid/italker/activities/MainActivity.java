package me.maxandroid.italker.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;

import net.qiujuer.genius.ui.Ui;
import net.qiujuer.genius.ui.widget.FloatActionButton;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnticipateInterpolator;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ViewTarget;
import com.example.factory.persistence.Account;

import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;
import me.maxandroid.common.app.Activity;
import me.maxandroid.common.widget.PortraitView;
import me.maxandroid.italker.R;
import me.maxandroid.italker.activities.AccountActivity;
import me.maxandroid.italker.frags.assist.PermissionFragment;
import me.maxandroid.italker.frags.main.ActiveFragment;
import me.maxandroid.italker.frags.main.ContactFragment;
import me.maxandroid.italker.frags.main.GroupFragment;
import me.maxandroid.italker.helper.NavHelper;


public class MainActivity extends Activity
        implements BottomNavigationView.OnNavigationItemSelectedListener, NavHelper.OnTabChangedListener<Integer> {
    @BindView(R.id.appbar)
    View mLayAppbar;

    @BindView(R.id.im_portrait)
    PortraitView mPortrait;
    @BindView(R.id.txt_title)
    TextView mTitle;
    @BindView(R.id.lay_container)
    FrameLayout mContainer;
    @BindView(R.id.navigation)
    BottomNavigationView mNavigation;
    @BindView(R.id.btn_action)
    FloatActionButton mAction;
    private NavHelper<Integer> mNavHelper;

    public static void show(Context context) {
        context.startActivity(new Intent(context, MainActivity.class));
    }

    @Override
    protected boolean initArgs(Bundle bundle) {
        if (Account.isLogin()) {
            if (Account.isComplete()) {
                return super.initArgs(bundle);
            } else {
                UserActivity.show(this);
                return false;
            }
        } else {
            AccountActivity.show(this);
            return false;
        }
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        mNavHelper = new NavHelper<>(this, R.id.lay_container, getSupportFragmentManager(), this);
        mNavHelper.add(R.id.action_home, new NavHelper.Tab<>(ActiveFragment.class, R.string.title_home))
                .add(R.id.action_group, new NavHelper.Tab<>(GroupFragment.class, R.string.title_group))
                .add(R.id.action_contact, new NavHelper.Tab<>(ContactFragment.class, R.string.title_contact));
        mNavigation.setOnNavigationItemSelectedListener(this);
        Glide.with(this)
                .load(R.drawable.bg_src_morning)
                .centerCrop()
                .into(new ViewTarget<View, GlideDrawable>(mLayAppbar) {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        this.view.setBackground(resource.getCurrent());
                    }
                });


    }

    @Override
    protected void initData() {
        super.initData();
        Menu menu = mNavigation.getMenu();
        menu.performIdentifierAction(R.id.action_home, 0);
    }

    @OnClick(R.id.im_search)
    void onSearchMenuClick() {
        int type = Objects.equals(mNavHelper.getCurrentTab().extra, R.string.title_group) ? SearchActivity.TYPE_GROUP : SearchActivity.TYPE_USER;
        SearchActivity.show(this, type);
    }

    @OnClick(R.id.btn_action)
    void onActionClick() {
        if (Objects.equals(mNavHelper.getCurrentTab().extra, R.string.title_group)) {

        } else {
            SearchActivity.show(this, SearchActivity.TYPE_USER);
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return mNavHelper.performClickMenu(item.getItemId());
    }

    @Override
    public void onTabChanged(NavHelper.Tab<Integer> newTab, NavHelper.Tab<Integer> oldTab) {
        mTitle.setText(newTab.extra);

        float transY = 0;
        float rotation = 0;
        if (Objects.equals(newTab.extra, R.string.title_home)) {
            transY = Ui.dipToPx(getResources(), 76);
        } else if (Objects.equals(newTab.extra, R.string.title_group)) {
            mAction.setImageResource(R.drawable.ic_group_add);
            rotation = -360;
        } else {
            mAction.setImageResource(R.drawable.ic_contact_add);
            rotation = 360;
        }

        mAction.animate()
                .rotation(rotation)
                .translationY(transY)
                .setInterpolator(new AnticipateInterpolator(1))
                .setDuration(480)
                .start();
    }
}
