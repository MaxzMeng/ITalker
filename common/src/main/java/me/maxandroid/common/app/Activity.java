package me.maxandroid.common.app;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.*;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

import butterknife.ButterKnife;
import me.maxandroid.common.widget.convention.PlaceHolderView;

/**
 * Created by MXZ on 2018/3/9.
 */

public abstract class Activity extends AppCompatActivity {
    protected PlaceHolderView mPlaceHolderView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (initArgs(getIntent().getExtras())) {
            setContentView(getContentLayoutId());
            initWindows();
            initBefore();
            initWidget();
            initData();
        } else {
            finish();
        }

    }

    protected void initBefore() {

    }

    protected void initWindows() {
        ButterKnife.bind(this);
    }

    protected boolean initArgs(Bundle bundle) {
        return true;
    }

    protected abstract int getContentLayoutId();

    protected void initWidget() {

    }

    protected void initData() {

    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        List<android.support.v4.app.Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments != null && fragments.size() != 0) {
            for (Fragment fragment : fragments) {
                if (fragment instanceof me.maxandroid.common.app.Fragment) {
                    if (((me.maxandroid.common.app.Fragment) fragment).onBackPressed()) {
                        return;
                    }
                }
            }
        }
        super.onBackPressed();
    }

    public void setPlaceHolderView(PlaceHolderView placeHolderView) {
        this.mPlaceHolderView = placeHolderView;
    }
}
