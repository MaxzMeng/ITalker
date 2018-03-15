package me.maxandroid.italker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import me.maxandroid.common.app.Activity;
import me.maxandroid.italker.activities.MainActivity;
import me.maxandroid.italker.frags.assist.PermissionFragment;

public class LaunchActivity extends Activity {

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_launch;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (PermissionFragment.haveAll(this, getSupportFragmentManager())) {
            MainActivity.show(this);
            finish();
        }
    }
}
