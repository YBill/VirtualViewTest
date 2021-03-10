package com.bill.virtualviewtest;

import android.app.Application;
import android.content.Context;

import com.bill.virtualviewtest.auxiliary.ImageTarget;
import com.bill.virtualviewtest.util.Utils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.tmall.wireless.vaf.framework.VafContext;
import com.tmall.wireless.vaf.framework.ViewManager;
import com.tmall.wireless.vaf.virtualview.Helper.ImageLoader;
import com.tmall.wireless.vaf.virtualview.event.EventData;
import com.tmall.wireless.vaf.virtualview.event.EventManager;
import com.tmall.wireless.vaf.virtualview.event.IEventProcessor;
import com.tmall.wireless.vaf.virtualview.view.image.ImageBase;

import java.util.HashMap;
import java.util.Map;

import custom.CustomKey;
import custom.NetImage;
import custom.ShapeImage;

/**
 * author : Bill
 * date : 2021/3/3
 * description :
 */
public class MyApplication extends Application {

    public static Context mContext;

    private VafContext sVafContext;
    private ViewManager sViewManager;
    private Map<String, Boolean> mLoadTemplateMap = new HashMap<>();

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
        sVafContext.getEventManager().register(EventManager.TYPE_Click, new IEventProcessor() {
            @Override
            public boolean process(EventData data) {
                String action = data.mVB.getAction();
                Utils.toast(action);
                return true;
            }
        });
        sViewManager = sVafContext.getViewManager();
        sViewManager.init(getApplicationContext());
        sViewManager.getViewFactory().registerBuilder(CustomKey.NET_IMAGE_ID, new NetImage.Builder());
        sViewManager.getViewFactory().registerBuilder(CustomKey.SHAPE_IMAGE_ID, new ShapeImage.Builder());

    }

    public VafContext getVafContext() {
        return sVafContext;
    }

    public ViewManager getViewManager() {
        return sViewManager;
    }

    public Map<String, Boolean> getLoadTemplateMap() {
        return mLoadTemplateMap;
    }
}
