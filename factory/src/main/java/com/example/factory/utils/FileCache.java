package com.example.factory.utils;

import com.example.factory.net.Network;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;

import me.maxandroid.common.app.Application;
import me.maxandroid.utils.HashUtil;
import me.maxandroid.utils.StreamUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FileCache<Holder> {
    private File baseDir;
    private String ext;
    private CacheListener<Holder> listener;
    private SoftReference<Holder> holderSoftReference;

    public FileCache(String baseDir, String ext, CacheListener<Holder> listener) {
        this.baseDir = new File(Application.getCacheDirFile(), baseDir);
        this.ext = ext;
        this.listener = listener;
    }

    private File buildCacheFile(String path) {
        String key = HashUtil.getMD5String(path);
        return new File(baseDir, key + "." + ext);
    }

    public void download(Holder holder, String path) {
        if (path.startsWith(Application.getCacheDirFile().getAbsolutePath())) {
            listener.onDownloadSucceed(holder, new File(path));
            return;
        }
        final File cacheFile = buildCacheFile(path);
        if (cacheFile.exists() && cacheFile.length() > 0) {
            listener.onDownloadSucceed(holder, cacheFile);
            return;
        }

        holderSoftReference = new SoftReference<>(holder);
        OkHttpClient client = Network.getClient();
        Request request = new Request.Builder()
                .url(path)
                .get()
                .build();
        Call call = client.newCall(request);
        call.enqueue(new NetCallback(holder, cacheFile));
    }

    private Holder getLastHolderAndClear() {
        if (holderSoftReference == null) {
            return null;
        } else {
            Holder holder = holderSoftReference.get();
            holderSoftReference.clear();
            return holder;
        }
    }

    private class NetCallback implements Callback {
        private final SoftReference<Holder> holderSoftReference;
        private final File file;

        public NetCallback(Holder holder, File file) {
            this.holderSoftReference = new SoftReference<>(holder);
            this.file = file;
        }

        @Override
        public void onFailure(Call call, IOException e) {
            Holder holder = holderSoftReference.get();
            if (holder != null && holder == getLastHolderAndClear()) {
                FileCache.this.listener.onDownloadFailed(holder);
            }
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            InputStream inputStream = response.body().byteStream();
            if (inputStream != null && StreamUtil.copy(inputStream, file)) {
                Holder holder = holderSoftReference.get();
                if (holder != null && holder == getLastHolderAndClear()) {
                    FileCache.this.listener.onDownloadSucceed(holder, file);
                }
            } else {
                onFailure(call, null);
            }

        }
    }

    public interface CacheListener<Holder> {
        void onDownloadSucceed(Holder holder, File file);

        void onDownloadFailed(Holder holder);

    }
}
