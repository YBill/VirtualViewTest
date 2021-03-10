package custom;

import android.view.View;

import com.bill.virtualviewtest.glide.GlideApp;
import com.libra.Utils;
import com.libra.expr.common.StringSupport;
import com.tmall.wireless.vaf.framework.VafContext;
import com.tmall.wireless.vaf.virtualview.core.ViewBase;
import com.tmall.wireless.vaf.virtualview.core.ViewCache;

/**
 * author : Bill
 * date : 2021/3/10
 * description : 自定义组件练习，功能简单，只是简单的网络加载图片，图片旋转
 */
public class NetImage extends ViewBase {

    private NetImageView mNetImageView;

    private int degreeId;
    private int urlId;

    private float degree;
    private String url;

    public NetImage(VafContext context, ViewCache viewCache) {
        super(context, viewCache);
        mNetImageView = new NetImageView(context.forViewConstruction());

        StringSupport mStringSupport = context.getStringLoader();
        degreeId = mStringSupport.getStringId(CustomKey.NET_IMAGE_ATTRS_DEGREE, false);
        urlId = mStringSupport.getStringId(CustomKey.NET_IMAGE_ATTRS_URL, false);
    }

    @Override
    public void onComMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mNetImageView.onComMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public void onComLayout(boolean changed, int l, int t, int r, int b) {
        mNetImageView.onComLayout(changed, l, t, r, b);
    }

    @Override
    public void comLayout(int l, int t, int r, int b) {
        super.comLayout(l, t, r, b);
        mNetImageView.comLayout(l, t, r, b);
    }

    @Override
    public View getNativeView() {
        return mNetImageView;
    }

    @Override
    public int getComMeasuredWidth() {
        return mNetImageView.getComMeasuredWidth();
    }

    @Override
    public int getComMeasuredHeight() {
        return mNetImageView.getComMeasuredHeight();
    }

    @Override
    protected boolean setAttribute(int key, float value) {
        boolean ret = true;
        if (key == degreeId) {
            degree = value;
        } else {
            ret = super.setAttribute(key, value);
        }
        return ret;
    }

    @Override
    protected boolean setAttribute(int key, String stringValue) {
        boolean ret = true;
        if (key == degreeId) {
            if (Utils.isEL(stringValue)) {
                mViewCache.put(this, degreeId, stringValue, ViewCache.Item.TYPE_FLOAT);
            }
        } else if (key == urlId) {
            if (Utils.isEL(stringValue)) {
                mViewCache.put(this, urlId, stringValue, ViewCache.Item.TYPE_STRING);
            } else {
                url = stringValue;
            }
        } else {
            ret = super.setAttribute(key, stringValue);
        }
        return ret;
    }

    @Override
    public void reset() {
        super.reset();
        GlideApp.with(mContext.getApplicationContext()).clear(mNetImageView);
    }

    @Override
    public void onParseValueFinished() {
        super.onParseValueFinished();
        mNetImageView.setRotation(degree);
        GlideApp.with(mContext.getApplicationContext())
                .load(url)
                .into(mNetImageView);
    }

    public static class Builder implements ViewBase.IBuilder {
        @Override
        public ViewBase build(VafContext context, ViewCache viewCache) {
            return new NetImage(context, viewCache);
        }
    }
}
