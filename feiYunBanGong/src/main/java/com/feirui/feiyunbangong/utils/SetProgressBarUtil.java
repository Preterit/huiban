package com.feirui.feiyunbangong.utils;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.widget.ProgressBar;

import com.feirui.feiyunbangong.R;



/**
 * 设置进度条：
 */
public class SetProgressBarUtil {

    public static void set(Activity activity,ProgressBar pb, int progress){
        pb.setMax(100);
        pb.setBackground(activity.getResources().getDrawable(R.drawable.shape_progressbar));
        if(progress<=30){
            ClipDrawable d = new ClipDrawable(new ColorDrawable(Color.YELLOW), Gravity.LEFT, ClipDrawable.HORIZONTAL);
            pb.setProgressDrawable(d);
        }else if(progress<=70){
            ClipDrawable d = new ClipDrawable(new ColorDrawable(Color.GREEN), Gravity.LEFT, ClipDrawable.HORIZONTAL);
            pb.setProgressDrawable(d);
        }else{
            ClipDrawable d = new ClipDrawable(new ColorDrawable(Color.RED), Gravity.LEFT, ClipDrawable.HORIZONTAL);
            pb.setProgressDrawable(d);
        }
        pb.setProgress(progress);
    }
}
