package custom;

import android.view.View;

import com.bill.virtualviewtest.glide.GlideApp;
import com.libra.Utils;
import com.libra.expr.common.StringSupport;
import com.libra.virtualview.common.ImageCommon;
import com.libra.virtualview.common.StringBase;
import com.tmall.wireless.vaf.framework.VafContext;
import com.tmall.wireless.vaf.virtualview.core.ViewBase;
import com.tmall.wireless.vaf.virtualview.core.ViewCache;
import com.tmall.wireless.vaf.virtualview.view.image.ImageBase;

/**
 * author : Bill
 * date : 2021/3/10
 * description :
 */
public class ShapeImage extends ViewBase {

    private ShapeImageView mShapeImageView;

    private String mSrc;
    private int mScaleType;
    private float mBorderWidth;
    private int mBorderColor;
    private int mShapeMode;
    private float mRadius;
    private float mLeftTopRadius;
    private float mRightTopRadius;
    private float mLeftBottomRadius;
    private float mRightBottomRadius;

    private int mShapeModeId;
    private int mRadiusId;
    private int mLeftTopRadiusId;
    private int mRightTopRadiusId;
    private int mLeftBottomRadiusId;
    private int mRightBottomRadiusId;

    public ShapeImage(VafContext context, ViewCache viewCache) {
        super(context, viewCache);
        mShapeImageView = new ShapeImageView(context.forViewConstruction());

        mScaleType = ImageCommon.SCALE_TYPE_CENTER_CROP;

        StringSupport mStringSupport = context.getStringLoader();
        mShapeModeId = mStringSupport.getStringId(CustomKey.SHAPE_IMAGE_ATTRS_SHAPE_MODE, false);
        mRadiusId = mStringSupport.getStringId(CustomKey.SHAPE_IMAGE_ATTRS_ROUND_RECT_RADIUS, false);
        mLeftTopRadiusId = mStringSupport.getStringId(CustomKey.SHAPE_IMAGE_ATTRS_ROUND_RECT_RADIUS_LT, false);
        mRightTopRadiusId = mStringSupport.getStringId(CustomKey.SHAPE_IMAGE_ATTRS_ROUND_RECT_RADIUS_RT, false);
        mLeftBottomRadiusId = mStringSupport.getStringId(CustomKey.SHAPE_IMAGE_ATTRS_ROUND_RECT_RADIUS_LB, false);
        mRightBottomRadiusId = mStringSupport.getStringId(CustomKey.SHAPE_IMAGE_ATTRS_ROUND_RECT_RADIUS_RB, false);
    }

    @Override
    public void onComMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mShapeImageView.onComMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public void onComLayout(boolean changed, int l, int t, int r, int b) {
        mShapeImageView.onComLayout(changed, l, t, r, b);
    }

    @Override
    public void comLayout(int l, int t, int r, int b) {
        super.comLayout(l, t, r, b);
        mShapeImageView.comLayout(l, t, r, b);
    }

    @Override
    public View getNativeView() {
        return mShapeImageView;
    }

    @Override
    public int getComMeasuredWidth() {
        return mShapeImageView.getComMeasuredWidth();
    }

    @Override
    public int getComMeasuredHeight() {
        return mShapeImageView.getComMeasuredHeight();
    }

    @Override
    protected boolean setAttribute(int key, int value) {
        boolean ret = true;
        if (key == mShapeModeId) {
            mShapeMode = value;
        } else if (key == StringBase.STR_ID_borderColor) {
            mBorderColor = value;
        } else if (key == StringBase.STR_ID_scaleType) {
            mScaleType = value;
        } else {
            ret = super.setAttribute(key, value);
        }
        return ret;
    }

    @Override
    protected boolean setAttribute(int key, float value) {
        boolean ret = true;
        if (key == mRadiusId) {
            mRadius = value;
        } else if (key == mLeftTopRadiusId) {
            mLeftTopRadius = value;
        } else if (key == mRightTopRadiusId) {
            mRightTopRadius = value;
        } else if (key == mLeftBottomRadiusId) {
            mLeftBottomRadius = value;
        } else if (key == mRightBottomRadiusId) {
            mRightBottomRadius = value;
        } else if (key == StringBase.STR_ID_borderWidth) {
            mBorderWidth = value;
        } else {
            ret = super.setAttribute(key, value);
        }
        return ret;
    }

    @Override
    protected boolean setAttribute(int key, String stringValue) {
        boolean ret = true;
        if (key == mShapeModeId) {
            if (Utils.isEL(stringValue)) {
                mViewCache.put(this, mShapeModeId, stringValue, ViewCache.Item.TYPE_INT);
            }
        } else if (key == mRadiusId) {
            if (Utils.isEL(stringValue)) {
                mViewCache.put(this, mRadiusId, stringValue, ViewCache.Item.TYPE_FLOAT);
            }
        } else if (key == mLeftTopRadiusId) {
            if (Utils.isEL(stringValue)) {
                mViewCache.put(this, mLeftTopRadiusId, stringValue, ViewCache.Item.TYPE_FLOAT);
            }
        } else if (key == mRightTopRadiusId) {
            if (Utils.isEL(stringValue)) {
                mViewCache.put(this, mRightTopRadiusId, stringValue, ViewCache.Item.TYPE_FLOAT);
            }
        } else if (key == mLeftBottomRadiusId) {
            if (Utils.isEL(stringValue)) {
                mViewCache.put(this, mLeftBottomRadiusId, stringValue, ViewCache.Item.TYPE_FLOAT);
            }
        } else if (key == mRightBottomRadiusId) {
            if (Utils.isEL(stringValue)) {
                mViewCache.put(this, mRightBottomRadiusId, stringValue, ViewCache.Item.TYPE_FLOAT);
            }
        } else if (key == StringBase.STR_ID_borderColor) {
            if (Utils.isEL(stringValue)) {
                mViewCache.put(this, StringBase.STR_ID_borderColor, stringValue, ViewCache.Item.TYPE_COLOR);
            }
        } else if (key == StringBase.STR_ID_borderWidth) {
            if (Utils.isEL(stringValue)) {
                mViewCache.put(this, StringBase.STR_ID_borderWidth, stringValue, ViewCache.Item.TYPE_FLOAT);
            }
        } else if (key == StringBase.STR_ID_scaleType) {
            if (Utils.isEL(stringValue)) {
                mViewCache.put(this, StringBase.STR_ID_scaleType, stringValue, ViewCache.Item.TYPE_INT);
            }
        } else if (key == StringBase.STR_ID_src) {
            if (Utils.isEL(stringValue)) {
                mViewCache.put(this, StringBase.STR_ID_src, stringValue, ViewCache.Item.TYPE_STRING);
            } else {
                mSrc = stringValue;
            }
        } else {
            ret = super.setAttribute(key, stringValue);
        }
        return ret;
    }

    @Override
    public void reset() {
        super.reset();
        GlideApp.with(mContext.getApplicationContext()).clear(mShapeImageView);
    }

    @Override
    public void onParseValueFinished() {
        super.onParseValueFinished();
        mShapeImageView.setScaleType(ImageBase.IMAGE_SCALE_TYPE.get(mScaleType));
        GlideApp.with(mContext.getApplicationContext())
                .load(mSrc)
                .into(mShapeImageView);

        switch (mShapeMode) {
            case CustomKey.SHAPE_MODE_ROUND_RECT:
                if (mRadius > 0) {
                    if (mBorderWidth > 0)
                        mShapeImageView.setRoundRect(Utils.dp2px(mRadius), Utils.dp2px(mBorderWidth), mBorderColor);
                    else
                        mShapeImageView.setRoundRect(Utils.dp2px(mRadius));
                } else {
                    float[] r = new float[]{Utils.dp2px(mLeftTopRadius), Utils.dp2px(mRightTopRadius),
                            Utils.dp2px(mLeftBottomRadius), Utils.dp2px(mRightBottomRadius)};
                    if (mBorderWidth > 0)
                        mShapeImageView.setRoundRect(r, Utils.dp2px(mBorderWidth), mBorderColor);
                    else
                        mShapeImageView.setRoundRect(r);
                }
                break;
            case CustomKey.SHAPE_MODE_CIRCLE:
                if (mBorderWidth > 0)
                    mShapeImageView.setCircle(Utils.dp2px(mBorderWidth), mBorderColor);
                else
                    mShapeImageView.setCircle();
                break;
            default:
                if (mBorderWidth > 0)
                    mShapeImageView.setNormalImage(Utils.dp2px(mBorderWidth), mBorderColor);
                else
                    mShapeImageView.setNormalImage();
                break;
        }

    }

    public static class Builder implements ViewBase.IBuilder {
        @Override
        public ViewBase build(VafContext context, ViewCache viewCache) {
            return new ShapeImage(context, viewCache);
        }
    }
}
