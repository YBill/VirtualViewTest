package com.bill.virtualviewtest.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.shapes.RoundRectShape;
import android.graphics.drawable.shapes.Shape;
import android.os.Build;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

import com.bill.virtualviewtest.R;

import java.util.Arrays;

/**
 * author : Bill
 * date : 2021/3/10
 * description :
 */
public class ShapedImageView extends AppCompatImageView {

    private static final float DEFAULT_RADIUS = 0f;

    private Paint mPaint;
    private Shape mShape;
    @ImageType
    private int mShapeMode = ImageType.MODE_NONE;
    private final float[] mCornerRadius = new float[8];

    private Paint mBorderPaint;
    private float mBorderWidth; //描边的宽度
    private int mBorderColor; //描边颜色
    private Path mBorderPath;
    private float indent; // 缩进，画描边
    private RectF mBorderRect;
    private float[] mBorderRadius = new float[8];

    public ShapedImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    public ShapedImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public ShapedImageView(Context context) {
        super(context);
        init(null);
    }

    private void init(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ShapedImageView);
            mShapeMode = a.getInt(R.styleable.ShapedImageView_shape_mode, ImageType.MODE_NONE);
            mBorderWidth = a.getDimension(R.styleable.ShapedImageView_img_border_width, 0);
            mBorderColor = a.getColor(R.styleable.ShapedImageView_img_border_color, 0xffffffff);
            switch (mShapeMode) {
                case ImageType.MODE_ROUND_RECT:
                    float mRadius = a.getDimension(R.styleable.ShapedImageView_round_radius, -1);
                    if (mRadius != -1) {
                        Arrays.fill(mCornerRadius, mRadius);
                    } else {
                        float leftTop = a.getDimension(R.styleable.ShapedImageView_round_radius_left_top, DEFAULT_RADIUS);
                        float rightTop = a.getDimension(R.styleable.ShapedImageView_round_radius_right_top, DEFAULT_RADIUS);
                        float rightBottom = a.getDimension(R.styleable.ShapedImageView_round_radius_right_bottom, DEFAULT_RADIUS);
                        float leftBottom = a.getDimension(R.styleable.ShapedImageView_round_radius_left_bottom, DEFAULT_RADIUS);
                        initRoundRect(leftTop, rightTop, leftBottom, rightBottom);
                    }
                    break;
            }
            a.recycle();
        }

        if (mShapeMode != ImageType.MODE_NONE) {
            initPaint();
        }

        // 描边
        if (mBorderWidth > 0) {
            initBorder();
        }
    }

    private void initRoundRect(float leftTop, float rightTop, float leftBottom, float rightBottom) {
        mCornerRadius[0] = mCornerRadius[1] = leftTop;
        mCornerRadius[2] = mCornerRadius[3] = rightTop;
        mCornerRadius[4] = mCornerRadius[5] = rightBottom;
        mCornerRadius[6] = mCornerRadius[7] = leftBottom;
    }

    private void initPaint() {
        if (mPaint == null) {
            setLayerType(LAYER_TYPE_HARDWARE, null);
            mPaint = new Paint();
            mPaint.setAntiAlias(true);
            mPaint.setFilterBitmap(true);
            mPaint.setColor(Color.BLACK);
            mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        }
    }

    private void initBorder() {
        if (mBorderPath == null)
            mBorderPath = new Path();
        mBorderPath.reset();
        if (mBorderRect == null)
            mBorderRect = new RectF();
        mBorderRect.setEmpty();
        if (mBorderPaint == null) {
            mBorderPaint = new Paint();
            mBorderPaint.setStyle(Paint.Style.STROKE);
            mBorderPaint.setAntiAlias(true);
        }
        mBorderPaint.setColor(mBorderColor);
        mBorderPaint.setStrokeWidth(mBorderWidth);
        indent = mBorderWidth / 2; // 缩进线的一半，要不线会绘制一半
        for (int i = 0; i < mCornerRadius.length; i++) {
            mBorderRadius[i] = mCornerRadius[i] - indent;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        switch (mShapeMode) {
            case ImageType.MODE_NONE:
                super.onDraw(canvas);
                break;
            case ImageType.MODE_ROUND_RECT:
            case ImageType.MODE_CIRCLE:
                if (Build.VERSION.SDK_INT >= 28) {
                    super.onDraw(canvas);
                    makeShapeBitmap();
                    if (mShapeBitmap != null && !mShapeBitmap.isRecycled())
                        canvas.drawBitmap(mShapeBitmap, 0, 0, mPaint);
                } else {
                    int saveCount = canvas.getSaveCount();
                    canvas.save();
                    super.onDraw(canvas);
                    if (mShape != null) {
                        mShape.draw(canvas, mPaint);
                    }
                    canvas.restoreToCount(saveCount);
                }
                break;
        }

        drawBorder(canvas, this.getWidth(), this.getHeight());
    }

    private void drawBorder(Canvas canvas, float width, float height) {
        if (mBorderWidth <= 0) {
            return;
        }

        if (mShapeMode == ImageType.MODE_CIRCLE) {
            float imgSize = getWidth(); // 圆的宽=高
            float center = imgSize / 2; // 圆心
            float radius = (imgSize - mBorderWidth) / 2 + 0.5f; // 半径
            canvas.drawCircle(center, center, radius, mBorderPaint);
        } else {
            mBorderRect.set(0, 0, width, height);
            mBorderRect.inset(indent, indent);
            mBorderPath.reset();
            mBorderPath.addRoundRect(mBorderRect, mBorderRadius, Path.Direction.CCW);
            canvas.drawPath(mBorderPath, mBorderPaint);
        }

        canvas = null;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mShapeMode == ImageType.MODE_CIRCLE) {
//            int min = Math.min(getMeasuredWidth(), getMeasuredHeight());
//            float radius = (float) min / 2;
//            Arrays.fill(mCornerRadius, radius);
//            setMeasuredDimension(min, min);

            Arrays.fill(mCornerRadius, getMeasuredWidth());
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            if (mShapeMode != ImageType.MODE_NONE) {
                if (mShape == null) {
                    mShape = new RoundRectShape(mCornerRadius, null, null);
                }
                mShape.resize(getWidth(), getHeight());
            }
        }
    }

    private Bitmap mShapeBitmap;

    private void makeShapeBitmap() {
        if (mShapeBitmap == null || mShapeBitmap.isRecycled()) {
            int w = getMeasuredWidth();
            int h = getMeasuredHeight();

            if (w == 0 || h == 0) return;

            releaseBitmap(mShapeBitmap);

            mShapeBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(mShapeBitmap);
            Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
            p.setColor(Color.BLACK);
            mShape.draw(c, p);
        }
    }

    private void releaseBitmap(Bitmap bitmap) {
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
        }
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        releaseBitmap(mShapeBitmap);
    }

    ////////// 添加外部跳用方法 //////////

    public void setNormalImage() {
        setNormalImage(0, 0);
    }

    public void setNormalImage(float borderWidth, int borderColor) {
        mShapeMode = ImageType.MODE_NONE;
        setBorder(borderWidth, borderColor);
    }

    private void setBorder(float borderWidth, int borderColor) {
        mBorderWidth = borderWidth;
        mBorderColor = borderColor;
        if (mBorderWidth > 0) {
            initBorder();
            invalidate();
        }
    }

    public void setRoundRect(float radius) {
        setRoundRect(radius, 0, 0);
    }

    public void setRoundRect(float radius, float borderWidth, int borderColor) {
        float[] r = new float[]{radius, radius, radius, radius};
        setRoundRect(r, borderWidth, borderColor);
    }

    public void setRoundRect(float[] radius) {
        setRoundRect(radius, 0, 0);
    }

    public void setRoundRect(float[] radius, float borderWidth, int borderColor) {
        if (radius == null || radius.length != 4)
            return;

        mShapeMode = ImageType.MODE_ROUND_RECT;
        initRoundRect(radius[0], radius[1], radius[2], radius[3]);
        initPaint();
        invalidate();
        setBorder(borderWidth, borderColor);
    }

    public void setCircle() {
        setCircle(0, 0);
    }

    public void setCircle(float borderWidth, int borderColor) {
        mShapeMode = ImageType.MODE_CIRCLE;
        initPaint();
        invalidate();
        setBorder(borderWidth, borderColor);
    }

}