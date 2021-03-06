package com.bill.virtualviewtest.util;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * author : Bill
 * date : 2021/3/3
 * description :
 */
public class ThreadUtils {

    private static Handler uiHandler = new Handler(Looper.getMainLooper());

    private static ExecutorService service = Executors.newFixedThreadPool(2, new ThreadFactory() {

        private int count = 1;

        @Override
        public Thread newThread(Runnable r) {
            String threadName = "New_Thread#" + count;
            count++;
            Thread thread = new Thread(r, threadName);
            thread.setPriority(Thread.MAX_PRIORITY);
            return thread;
        }
    });

    public static void runOnMain(Runnable runnable) {
        uiHandler.post(runnable);
    }

    public static void runOnMain(Runnable runnable, long delay) {
        uiHandler.postDelayed(runnable, delay);
    }

    public static void runOnWork(Runnable runnable) {
        service.execute(runnable);
    }

}
