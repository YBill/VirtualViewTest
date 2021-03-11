package custom;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;

import com.bill.virtualviewtest.widget.RoundedBackgroundSpan;
import com.libra.Utils;
import com.libra.expr.common.StringSupport;
import com.tmall.wireless.vaf.framework.VafContext;
import com.tmall.wireless.vaf.virtualview.core.ViewBase;
import com.tmall.wireless.vaf.virtualview.core.ViewCache;
import com.tmall.wireless.vaf.virtualview.view.text.NativeText;

/**
 * author : Bill
 * date : 2021/3/11
 * description : TextView 前面加标签
 */
public class TagText extends NativeText {

    private int mTagId;

    private String mTag;

    public TagText(VafContext context, ViewCache viewCache) {
        super(context, viewCache);
        StringSupport mStringSupport = context.getStringLoader();
        mTagId = mStringSupport.getStringId(CustomKey.TAG_TEXT_ATTRS_TAG, false);
    }

    @Override
    protected boolean setAttribute(int key, String stringValue) {
        boolean ret = true;
        if (key == mTagId) {
            if (Utils.isEL(stringValue)) {
                mViewCache.put(this, mTagId, stringValue, ViewCache.Item.TYPE_STRING);
            } else {
                mTag = stringValue;
            }
        } else {
            ret = super.setAttribute(key, stringValue);
        }
        return ret;
    }

    @Override
    public void reset() {
        super.reset();
    }

    @Override
    public void onParseValueFinished() {
        super.onParseValueFinished();

        if (!TextUtils.isEmpty(mTag)) {
            String title = mTag + mText;
            SpannableString spannableString = new SpannableString(title);
            RoundedBackgroundSpan roundedBackgroundSpan;
            roundedBackgroundSpan = new RoundedBackgroundSpan(mContext.forViewConstruction(), mTag);
            spannableString.setSpan(roundedBackgroundSpan, 0, mTag.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            mNative.setText(spannableString);
        }

    }

    public static class Builder implements ViewBase.IBuilder {
        @Override
        public ViewBase build(VafContext context, ViewCache viewCache) {
            return new TagText(context, viewCache);
        }
    }
}
