package me.maxandroid.italker;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Property;
import android.view.View;

import com.example.factory.persistence.Account;

import net.qiujuer.genius.res.Resource;
import net.qiujuer.genius.ui.compat.UiCompat;

import me.maxandroid.common.app.Activity;
import me.maxandroid.italker.activities.AccountActivity;
import me.maxandroid.italker.activities.MainActivity;
import me.maxandroid.italker.frags.assist.PermissionFragment;

public class LaunchActivity extends Activity {
    private ColorDrawable mBgDrawable;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_launch;
    }

    @Override
    protected void initWidget() {
        super.initWidget();

        View root = findViewById(R.id.activity_launch);
        int color = UiCompat.getColor(getResources(), R.color.colorPrimary);
        ColorDrawable drawable = new ColorDrawable(color);
        root.setBackground(drawable);
        mBgDrawable = drawable;
    }

    @Override
    protected void initData() {
        super.initData();
        startAnim(0.5f, new Runnable() {
            @Override
            public void run() {
                waitPushReceiverId();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
//        if (PermissionFragment.haveAll(this, getSupportFragmentManager())) {
//            MainActivity.show(this);
//            finish();
//        }
    }

    private void waitPushReceiverId() {
        if (Account.isLogin()) {
            if (Account.isBind()) {
                skip();
                return;
            }
        } else {
            if (!TextUtils.isEmpty(Account.getPushId())) {
                skip();
                return;
            }
        }
        getWindow().getDecorView().postDelayed(new Runnable() {
            @Override
            public void run() {
                waitPushReceiverId();
            }
        }, 500);
    }

    private void skip() {
        startAnim(1f, new Runnable() {
            @Override
            public void run() {
                reallySkip();
            }
        });

    }

    private void reallySkip() {
        if (PermissionFragment.haveAll(this, getSupportFragmentManager())) {
            if (Account.isLogin()) {
                MainActivity.show(this);
            } else {
                AccountActivity.show(this);
            }

            finish();
        }
    }


    private void startAnim(float endProgress, final Runnable endCallback) {
        int finalColor = Resource.Color.WHITE;
        ArgbEvaluator evaluator = new ArgbEvaluator();
        int endColor = (int) evaluator.evaluate(endProgress, mBgDrawable.getColor(), finalColor);

        ValueAnimator valueAnimator = ObjectAnimator.ofObject(this, property, evaluator, endColor);
        valueAnimator.setDuration(1500);
        valueAnimator.setIntValues(mBgDrawable.getColor(), endColor);
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                endCallback.run();
            }
        });
        valueAnimator.start();
    }

    private Property<LaunchActivity, Object> property = new Property<LaunchActivity, Object>(Object.class, "color") {
        @Override
        public void set(LaunchActivity object, Object value) {
            object.mBgDrawable.setColor((Integer) value);
        }

        @Override
        public Object get(LaunchActivity object) {
            return object.mBgDrawable.getColor();
        }
    };

}
