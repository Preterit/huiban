package com.feirui.feiyunbangong.utils;

import android.content.Context;
import android.widget.ImageView;

import com.youth.banner.loader.ImageLoader;

/**
 * Created by rubing on 2017/3/1.
 * rubingem@163.com
 */

public class GlideImageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage((String) path, imageView);
    }
}
