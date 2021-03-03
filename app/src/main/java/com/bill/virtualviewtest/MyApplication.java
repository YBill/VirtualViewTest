package com.bill.virtualviewtest;

import android.app.Application;
import android.content.Context;

import com.bill.virtualviewtest.ui.ImageTarget;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.tmall.wireless.vaf.framework.VafContext;
import com.tmall.wireless.vaf.framework.ViewManager;
import com.tmall.wireless.vaf.virtualview.Helper.ImageLoader;
import com.tmall.wireless.vaf.virtualview.view.image.ImageBase;

/**
 * author : Bill
 * date : 2021/3/3
 * description :
 */
public class MyApplication extends Application {

    public static Context mContext;

    private VafContext sVafContext;
    private ViewManager sViewManager;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();

        sVafContext = new VafContext(getApplicationContext());
        sVafContext.setImageLoaderAdapter(new ImageLoader.IImageLoaderAdapter() {
            @Override
            public void bindImage(String uri, ImageBase imageBase, int reqWidth, int reqHeight) {
                RequestBuilder requestBuilder = Glide.with(mContext).asBitmap().load(uri);
                if (reqHeight > 0 || reqWidth > 0) {
                    requestBuilder.submit(reqWidth, reqHeight);
                }
                ImageTarget imageTarget = new ImageTarget(imageBase);
                requestBuilder.into(imageTarget);
            }

            @Override
            public void getBitmap(String uri, int reqWidth, int reqHeight, ImageLoader.Listener lis) {
                RequestBuilder requestBuilder = Glide.with(mContext).asBitmap().load(uri);
                if (reqHeight > 0 || reqWidth > 0) {
                    requestBuilder.submit(reqWidth, reqHeight);
                }
                ImageTarget imageTarget = new ImageTarget(lis);
                requestBuilder.into(imageTarget);
            }
        });
        sViewManager = sVafContext.getViewManager();
        sViewManager.init(getApplicationContext());

    }

    public VafContext getVafContext() {
        return sVafContext;
    }

    public ViewManager getViewManager() {
        return sViewManager;
    }
}
