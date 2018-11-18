package com.example.suyash.uberflicker.core.net;

import android.os.Build;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class HttpManager {

    private static HttpManager instance;
    private static Object lock = new Object();
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();

    private static final int CORE_POOL_SIZE = CPU_COUNT + 1;
    private static final int MAX_POOL_SIZE = CPU_COUNT * 2 + 1;
    private static final long KEEP_ALIVE_TIME = 10;
    private ExecutorService executorService;

    private HttpManager() {
        executorService = newCachedThreadPool();
    }

    public static HttpManager getInstance() {
        if(instance == null) {
            synchronized (lock) {
                if(instance == null) {
                    instance = new HttpManager();
                }
            }
        }
        return instance;
    }

    public void executeTask(Runnable task){
        if(executorService != null) {
            executorService.execute(task);
        }
    }

    private static ExecutorService newCachedThreadPool() {
        ThreadPoolExecutor executor =  new ThreadPoolExecutor(
                CORE_POOL_SIZE,
                MAX_POOL_SIZE,
                KEEP_ALIVE_TIME,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>());

        allowCoreThreadTimeout(executor, true);

        return executor;
    }

    private static void allowCoreThreadTimeout(ThreadPoolExecutor executor, boolean value) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            executor.allowCoreThreadTimeOut(value);
        }
    }

}
