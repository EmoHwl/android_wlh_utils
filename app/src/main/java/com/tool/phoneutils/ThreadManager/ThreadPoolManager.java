package com.tool.phoneutils.ThreadManager;

import android.util.LruCache;

import java.lang.ref.WeakReference;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by wlhuang on 16-5-26.
 */
public class ThreadPoolManager {
    //获取系统分配给每个应用程序的最大内存，每个应用系统分配32M
    int maxMemory = (int) Runtime.getRuntime().maxMemory();
    int mCacheSize = maxMemory / 8;
    private static ExecutorService singleThreadPool = null;
    private static ExecutorService newCachedThreadPool = null;
    private LruCache<String, WeakReference<Future>> mFUtureCache = new LruCache<String, WeakReference<Future>>(mCacheSize) {

        @Override
        public void resize(int maxSize) {
            super.resize(maxSize);
        }
    };

    public ThreadPoolManager() {
    }

    public ExecutorService getSingleThreadPool() {
        if (singleThreadPool == null) {
            synchronized (ExecutorService.class) {
                if (singleThreadPool == null) {
                    singleThreadPool = Executors.newSingleThreadExecutor();
                }
            }
        }
        return singleThreadPool;
    }

    public ExecutorService getCachedThreadPool() {
        if (newCachedThreadPool == null) {
            synchronized (ExecutorService.class) {
                if (newCachedThreadPool == null) {
                    newCachedThreadPool = Executors.newCachedThreadPool();
                }
            }
        }
        return newCachedThreadPool;
    }
}
