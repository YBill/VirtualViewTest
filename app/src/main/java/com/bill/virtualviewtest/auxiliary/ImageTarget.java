package com.bill.virtualviewtest.auxiliary;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.tmall.wireless.vaf.virtualview.Helper.ImageLoader;
import com.tmall.wireless.vaf.virtualview.view.image.ImageBase;

/**
 * author : Bill
 * date : 2021/3/3
 * description :
 */
public class ImageTarget extends CustomTarget<Bitmap> {

    private ImageBase mImageBase;
    private ImageLoader.Listener mListener;

    public ImageTarget(ImageBase imageBase) {
        mImageBase = imageBase;
    }

    public ImageTarget(ImageLoader.Listener listener) {
        mListener = listener;
    }

    @Override
    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
        mImageBase.setBitmap(resource, true);
        if (mListener != null) {
            mListener.onImageLoadSuccess(resource);
        }
    }

    @Override
    public void onLoadCleared(@Nullable Drawable placeholder) {
        if (mListener != null) {
            mListener.onImageLoadFailed();
        }
    }
}