package com.example.factory;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import me.maxandroid.common.app.Application;

/**
 * Created by mxz on 18-3-15.
 */

public class Factory {
    private static final Factory instance;
    private final Executor executor;
    static {
        instance = new Factory();

    }

    private Factory() {
        executor = Executors.newFixedThreadPool(4);
    }

    public static Application app() {
        return Application.getInstance();
    }

    public static void runOnAsync(Runnable runnable) {
        instance.executor.execute(runnable);
    }
}
