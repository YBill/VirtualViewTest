package com.bill.virtualviewtest.util;

import android.graphics.Color;
import android.os.Looper;
import android.util.TypedValue;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.ColorInt;

import com.bill.virtualviewtest.MyApplication;
import com.google.android.material.snackbar.Snackbar;

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

    public static void bottomToast(View view, CharSequence text) {
        bottomToast(view, text, Color.RED);
    }

    public static void bottomToast(View view, CharSequence text, @ColorInt int color) {
        Snackbar.make(view, text, Snackbar.LENGTH_INDEFINITE)
                .setTextColor(color)
                .setAction("Close", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                })
                .show();
    }

    public static float dp2Px(float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, MyApplication.mContext.getResources().getDisplayMetrics());
    }

}
