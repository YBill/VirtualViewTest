package com.bill.virtualviewtest.widget;

import androidx.annotation.IntDef;

/**
 * author : Bill
 * date : 2021/3/10
 * description :
 */
@IntDef({ImageType.MODE_NONE, ImageType.MODE_ROUND_RECT, ImageType.MODE_CIRCLE})
public @interface ImageType {
    int MODE_NONE = 0;
    int MODE_ROUND_RECT = 1;
    int MODE_CIRCLE = 2;
}