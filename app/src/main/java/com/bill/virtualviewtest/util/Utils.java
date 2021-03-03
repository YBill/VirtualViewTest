package com.bill.virtualviewtest.util;

import android.os.Looper;
import android.widget.Toast;

import com.bill.virtualviewtest.MyApplication;

/**
 * author : Bill
 * date : 2021/3/3
 * description :
 */
public class Utils {

    public static void toast(final String text) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            realToast(text);
        } else {
            ThreadUtils.runOnMain(new Runnable() {
                @Override
                public void run() {
                    realToast(text);
                }
            });
        }
    }

    private static void realToast(String text) {
        Toast.makeText(MyApplication.mContext, text, Toast.LENGTH_SHORT).show();
    }

}
