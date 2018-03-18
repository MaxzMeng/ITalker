package me.maxandroid.italker;

import com.example.factory.Factory;
import com.igexin.sdk.PushManager;

import me.maxandroid.common.app.Application;

/**
 * Created by mxz on 18-3-15.
 */

public class App extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        Factory.setup();
        PushManager.getInstance().initialize(this);
    }
}
