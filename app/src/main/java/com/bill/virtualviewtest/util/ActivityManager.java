package com.bill.virtualviewtest.util;

import android.app.Activity;

import java.util.ArrayList;

/**
 * author : Bill
 * date : 2021/3/15
 * description :
 */
public class ActivityManager {

    private ActivityManager() {
    }

    public static ActivityManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        public static final ActivityManager INSTANCE = new ActivityManager();
    }

    private ArrayList<Activity> mActivityStack = new ArrayList<>();

    public void addActivity(Activity activity) {
        mActivityStack.add(0, activity);
    }

    public void removeActivity(Activity activity) {
        mActivityStack.remove(activity);
    }

    public Activity getTopActivity() {
        return mActivityStack.get(0);
    }

}
