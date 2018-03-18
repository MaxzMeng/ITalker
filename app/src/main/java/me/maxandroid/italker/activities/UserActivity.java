package me.maxandroid.italker.activities;


import android.content.Intent;

import me.maxandroid.common.app.Activity;
import me.maxandroid.common.app.Fragment;
import me.maxandroid.italker.R;
import me.maxandroid.italker.frags.user.UpdateInfoFragment;

public class UserActivity extends Activity {

    private Fragment mCurFragment;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_user;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        mCurFragment = new UpdateInfoFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.lay_container, mCurFragment)
                .commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mCurFragment.onActivityResult(requestCode, resultCode, data);
    }
}
