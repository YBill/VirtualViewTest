package com.bill.virtualviewtest.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ReplacementSpan;
import android.util.TypedValue;

import androidx.annotation.NonNull;

/**
 * author : Bill
 * date : 2021/3/11
 * description :
 */
public class RoundedBackgroundSpan extends ReplacementSpan {
    private Context mContext;
    private int mBgColorResId; //Icon背景颜色
    private String mText;  //Icon内文字
    private float mBgHeight;  //Icon背景高度
    private float mBgWidth;  //Icon背景宽度
    private float mRadius;  //Icon圆角半径
    private float mRightMargin; //右边距
    private float textMarginYOffset; //text在y方向的偏移
    private float mTextSize; //文字大小
    private int mTextColorResId; //文字颜色

    private Paint mBgPaint; //icon背景画笔
    private Paint mTextPaint; //icon文字画笔

    public RoundedBackgroundSpan(Context context, String text) {
        this(context, 0, text);
    }

    public RoundedBackgroundSpan(Context context, int bgColorResId, String text) {
        if (TextUtils.isEmpty(text)) {
            return;
        }
        //初始化默认数值
        initDefaultValue(context, bgColorResId, text);
        //初始化画笔
        initPaint();
        //计算背景的宽度
        this.mBgWidth = calculateBgWidth(text);

        if (mBgColorResId != 0) {
            mBgPaint.setColor(mBgColorResId);
        } else {
            LinearGradient gradient = new LinearGradient(0, mBgHeight, mBgWidth, 0,
                    Color.parseColor("#FF2626"), Color.parseColor("#FF8200"), Shader.TileMode.CLAMP);
            mBgPaint.setShader(gradient);
        }

    }

    /**
     * 初始化画笔
     */
    private void initPaint() {
        //初始化背景画笔
        mBgPaint = new Paint();
        mBgPaint.setStyle(Paint.Style.FILL);
        mBgPaint.setAntiAlias(true);

        //初始化文字画笔
        mTextPaint = new TextPaint();
        mTextPaint.setColor(mTextColorResId);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
    }

    /**
     * 初始化默认数值
     *
     * @param context
     */
    private void initDefaultValue(Context context, int bgColorResId, String text) {
        this.mContext = context.getApplicationContext();
        this.mText = text;
        this.mBgHeight = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 17f, mContext.getResources().getDisplayMetrics());
        this.mRightMargin = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, mContext.getResources().getDisplayMetrics());
        this.mRadius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, mContext.getResources().getDisplayMetrics());
        this.mTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, mContext.getResources().getDisplayMetrics());
        this.textMarginYOffset = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 1, mContext.getResources().getDisplayMetrics());
        this.mTextColorResId = mContext.getResources().getColor(android.R.color.white);
        if (bgColorResId != 0)
            this.mBgColorResId = mContext.getResources().getColor(bgColorResId);
    }

    /**
     * 计算icon背景宽度
     *
     * @param text icon内文字
     */
    private float calculateBgWidth(String text) {
        if (text.length() > 1) {
            //多字，宽度=文字宽度+padding
            Rect textRect = new Rect();
            mTextPaint.getTextBounds(text, 0, text.length(), textRect);
            float padding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, mContext.getResources().getDisplayMetrics());
            return textRect.width() + padding * 2;
        } else {
            //单字，宽高一致为正方形
            return mBgHeight;
        }
    }

    /**
     * 设置圆角
     *
     * @param radius
     */
    public void setRadius(int radius) {
        this.mRadius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, radius, mContext.getResources().getDisplayMetrics());
    }

    /**
     * 设置文字大小
     *
     * @param size
     */
    public void setTextSize(int size) {
        this.mTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, size, mContext.getResources().getDisplayMetrics());

        mTextPaint.setTextSize(mTextSize);
    }

    public void setTextBold(boolean bold) {

        if (bold) {
            mTextPaint.setTypeface(Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD));
        } else {
            mTextPaint.setTypeface(Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL));
        }
    }

    /**
     * 设置右边距
     *
     * @param rightMarginDpValue
     */
    public void setRightMarginDpValue(int rightMarginDpValue) {
        this.mRightMargin = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rightMarginDpValue, mContext.getResources().getDisplayMetrics());
    }

    /**
     * 设置宽度，宽度=背景宽度+右边距
     */
    @Override
    public int getSize(@NonNull Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
        return (int) (mBgWidth + mRightMargin);
    }

    /**
     * draw
     *
     * @param text   完整文本
     * @param start  setSpan里设置的start
     * @param end    setSpan里设置的start
     * @param x
     * @param top    当前span所在行的上方y
     * @param y      y其实就是metric里baseline的位置
     * @param bottom 当前span所在行的下方y(包含了行间距)，会和下一行的top重合
     * @param paint  使用此span的画笔
     */
    @Override
    public void draw(@NonNull Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
        if (paint == null || mBgPaint == null || mTextPaint == null)
            return;

        //画背景
        Paint.FontMetrics metrics = paint.getFontMetrics();

        float textHeight = metrics.descent - metrics.ascent;
        //算出背景开始画的y坐标
        float bgStartY = y + (textHeight - mBgHeight) / 2 + metrics.ascent;

        //画背景
        RectF bgRect = new RectF(x, bgStartY, x + mBgWidth, bgStartY + mBgHeight);
        canvas.drawRoundRect(bgRect, mRadius, mRadius, mBgPaint);

        //把字画在背景中间
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        float textRectHeight = fontMetrics.bottom - fontMetrics.top;
        canvas.drawText(mText,
                x + mBgWidth / 2,
                bgStartY + (mBgHeight - textRectHeight) / 2 - fontMetrics.top - textMarginYOffset,
                mTextPaint);
    }
}