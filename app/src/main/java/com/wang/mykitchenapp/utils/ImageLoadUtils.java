package com.wang.mykitchenapp.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.wang.mykitchenapp.common.Constant;

/**
 * Created by Wang on 2017/5/8.
 */

public class ImageLoadUtils {

    public final static void loadCircleImage(final Context context,final String url,final ImageView view){
        Glide.with(context).load(Constant.BASE_URL_IMAGES + url).asBitmap().centerCrop().into(new BitmapImageViewTarget(view) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                view.setImageDrawable(circularBitmapDrawable);
            }
        });
    }
}
