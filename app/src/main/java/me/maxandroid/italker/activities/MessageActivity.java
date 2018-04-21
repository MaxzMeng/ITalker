package me.maxandroid.italker.activities;

import android.content.Context;
import android.content.Intent;

import me.maxandroid.common.app.Activity;
import me.maxandroid.factory.model.Author;
import me.maxandroid.italker.R;

public class MessageActivity extends Activity {
    public static void show(Context context, Author author) {
        context.startActivity(new Intent(context, MessageActivity.class));
    }
    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_message;
    }
}
