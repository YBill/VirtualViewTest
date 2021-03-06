package custom;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

import com.tmall.wireless.vaf.virtualview.core.IView;

/**
 * author : Bill
 * date : 2021/3/10
 * description :
 */
public class LocalAndNetImageView extends androidx.appcompat.widget.AppCompatImageView implements IView {

    public LocalAndNetImageView(Context context) {
        super(context);
    }

    public LocalAndNetImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public LocalAndNetImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void measureComponent(int widthMeasureSpec, int heightMeasureSpec) {
        this.measure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public void comLayout(int l, int t, int r, int b) {
        this.layout(l, t, r, b);
    }

    @Override
    public void onComMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        this.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public void onComLayout(boolean changed, int l, int t, int r, int b) {
        this.onLayout(changed, l, t, r, b);
    }

    @Override
    public int getComMeasuredWidth() {
        return this.getMeasuredWidth();
    }

    @Override
    public int getComMeasuredHeight() {
        return this.getMeasuredHeight();
    }
}
