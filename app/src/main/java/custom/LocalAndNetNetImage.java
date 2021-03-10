package custom;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bill.virtualviewtest.glide.GlideApp;
import com.libra.TextUtils;
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
public class LocalAndNetNetImage extends ViewBase {

    private LocalAndNetImageView mLocalAndNetImageView;

    private int degreeId;
    private int urlId;
    private int localResId;

    private float degree;
    private String url;
    private String localRes;

    public LocalAndNetNetImage(VafContext context, ViewCache viewCache) {
        super(context, viewCache);
        mLocalAndNetImageView = new LocalAndNetImageView(context.forViewConstruction());

        StringSupport mStringSupport = context.getStringLoader();
        degreeId = mStringSupport.getStringId(CustomKey.NET_IMAGE_ATTRS_DEGREE, false);
        urlId = mStringSupport.getStringId(CustomKey.NET_IMAGE_ATTRS_URL, false);
        localResId = mStringSupport.getStringId(CustomKey.NET_IMAGE_ATTRS_LOCAL_RES, false);

    }

    @Override
    public void onComMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mLocalAndNetImageView.onComMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public void onComLayout(boolean changed, int l, int t, int r, int b) {
        mLocalAndNetImageView.onComLayout(changed, l, t, r, b);
    }

    @Override
    public void comLayout(int l, int t, int r, int b) {
        super.comLayout(l, t, r, b);
        mLocalAndNetImageView.comLayout(l, t, r, b);
    }

    @Override
    public View getNativeView() {
        return mLocalAndNetImageView;
    }

    @Override
    public int getComMeasuredWidth() {
        return mLocalAndNetImageView.getComMeasuredWidth();
    }

    @Override
    public int getComMeasuredHeight() {
        return mLocalAndNetImageView.getComMeasuredHeight();
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
        if (key == localResId) {
            if (Utils.isEL(stringValue)) {
                mViewCache.put(this, localResId, stringValue, ViewCache.Item.TYPE_FLOAT);
            } else {
                localRes = stringValue;
            }
        } else if (key == degreeId) {
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
        GlideApp.with(mContext.getApplicationContext()).clear(mLocalAndNetImageView);
    }

    @Override
    public void onParseValueFinished() {
        super.onParseValueFinished();
        if (TextUtils.isEmpty(localRes)) {
            mLocalAndNetImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            GlideApp.with(mContext.getApplicationContext())
                    .load(url)
                    .into(mLocalAndNetImageView);
        } else {
            mLocalAndNetImageView.setScaleType(ImageView.ScaleType.CENTER);
            int id = mContext.getApplicationContext().getResources().getIdentifier(
                    localRes, "drawable", mContext.getApplicationContext().getPackageName());
            if (id > 0)
                mLocalAndNetImageView.setImageResource(id);
            else
                Log.e("LocalAndNetNetImage", "Not found the image in drawable, name is :" + localRes);
        }

        mLocalAndNetImageView.setRotation(degree);

    }

    public static class Builder implements ViewBase.IBuilder {
        @Override
        public ViewBase build(VafContext context, ViewCache viewCache) {
            return new LocalAndNetNetImage(context, viewCache);
        }
    }
}
