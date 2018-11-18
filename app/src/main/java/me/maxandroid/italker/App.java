package me.maxandroid.italker;

import android.content.IntentFilter;

import com.example.factory.Factory;
import com.igexin.sdk.PushManager;

import me.maxandroid.common.app.Application;

/**
 * Created by mxz on 18-3-15.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Factory.setup();
        PushManager.getInstance().initialize(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.igexin.sdk.action.sxjLlJy9Lu5lfVuya7c6q");
        MessageReceiver receiver = new MessageReceiver();
        registerReceiver(receiver, intentFilter);
    }
}
