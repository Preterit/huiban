package com.feirui.feiyunbangong.utils.NumberBadger;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by xy on 2017-11-29.
 * 三星手机适配角标
 */

public class BadgeNumberManagerSamsung {

        public static void setBadgeNumber(Context context, int num) {
            try{
                String launcherClassName = context.getPackageManager()
                        .getLaunchIntentForPackage(context.getPackageName())
                        .getComponent().getClassName();
                if (launcherClassName == null) {
                    return;
                }
                Intent intent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
                intent.putExtra("badge_count", num);
                intent.putExtra("badge_count_package_name", context.getPackageName());
                intent.putExtra("badge_count_class_name", launcherClassName);
                context.sendBroadcast(intent);
            }catch (Exception e){
                Log.e("Samsung" + " Badge error", "set Badge failed");
            }
        }
}
