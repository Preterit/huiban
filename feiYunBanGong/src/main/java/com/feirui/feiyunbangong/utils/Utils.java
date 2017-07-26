package com.feirui.feiyunbangong.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;

import com.feirui.feiyunbangong.dialog.LoadingDialog;
import com.feirui.feiyunbangong.entity.JsonBean;
import com.feirui.feiyunbangong.view.TextImageView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;

import org.apache.http.Header;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {


    /**
     * 弹出一个Alertdialog
     * @param context 上下文
     * @param title	标题
     * @param message 正文
     * @param listener	点击确定与取消的监听
     * @param flag	是否取消
     * @return
     */
    public static AlertDialog creatDialog(Context context, CharSequence title,
                                          CharSequence message,
                                          final DialogListener listener,
                                          boolean flag){
        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onYesClick(dialog,which);
                    }
                })
                .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onNoClick(dialog,which);
                    }
                }).create();
        alertDialog.setCancelable(flag);
        return alertDialog;

    }


    public interface DialogListener{
        /**
         * 点击确定的监听
         * @param dialog
         * @param which
         */
        public void onYesClick(DialogInterface dialog, int which);
        /**
         * 点击取消的监听
         * @param dialog
         * @param which
         */
        public void onNoClick(DialogInterface dialog, int which);
    }

    /**
     * 将汉字转成拼音
     *
     * @param hanzi 汉字或字母
     * @return 拼音
     */
    @SuppressLint("DefaultLocale")
    public static String getPinyin(String hanzi) {

        StringBuilder sb = new StringBuilder();
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.UPPERCASE);
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);

        // 由于不能直接对多个汉子转换，只能对单个汉子转换
        char[] arr = hanzi.toCharArray();

        // 只对首位进行转换：
        for (int i = 0; i < 1; i++) {
            if (Character.isWhitespace(arr[i])) {
                continue;
            }
            try {
                String[] pinyinArr = PinyinHelper.toHanyuPinyinStringArray(arr[i], format);
                if (pinyinArr != null) {
                    sb.append(pinyinArr[0]);
                } else {
                    sb.append(arr[i]);
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("TAG", "不是正确的汉字");
            }
        }

        char word_02 = sb.toString().toUpperCase().charAt(0);

        boolean isWord = false;

        for (char i = 'A'; i < 'Z' + 1; i++) {
            if (word_02 == i) {
                isWord = true;
                break;
            }
        }

        // 如果不是字母，则分到“#”组里；
        if (!isWord) {
            word_02 = '#';
        }

        return (word_02 + "");
    }

    /**
     * 验证手机号：
     *
     * @param phone 手机号；
     * @return 是手机号返回true 否则返回false;
     */
    public static boolean isPhone(String phone) {
        boolean flag = false;
        if (phone == null || TextUtils.isEmpty(phone)) {
                return flag;
        }
        Pattern p = Pattern.compile("^((13[0-9])|(15[^4])|(18[0,2,3,5-9])|(17[0-8])|(147))\\d{8}$");
        //"^((13[0-9])|(15[^4,\\D])|(14[57])|(17[0])|(17[6])|(17[7])|(18[0,0-9]))\\d{8}$"

        Matcher m = p.matcher(phone);

        System.out.println(m.matches() + "---");
        return m.matches();
    }

    /**
     * 检测邮箱地址是否合法
     *
     * @param email
     * @return true合法 false不合法
     */
    public static boolean isEmail(String email) {
        if (null == email || "".equals(email))
            return false;
        // Pattern p = Pattern.compile("\\w+@(\\w+.)+[a-z]{2,3}"); //简单匹配
        Pattern p = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");// 复杂匹配
        Matcher m = p.matcher(email);
        return m.matches();
    }

    // 获取屏幕分辨率：
    public static void getFenBianLv(Activity activity) {
        DisplayMetrics mDisplayMetrics = new DisplayMetrics();

        activity.getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);

        int W = mDisplayMetrics.widthPixels;

        int H = mDisplayMetrics.heightPixels;

        Log.e("Main", "Width = " + W);

        Log.e("Main", "Height = " + H);
    }

    // 获得无头像显示的内容：
    public static String getPicTitle(String title) {

        String name = "";

        StringBuffer sb = new StringBuffer(title);

        if (title != null && title.length() > 2) {
            String s = sb.substring(sb.length() - 2, sb.length());
            sb.delete(0, sb.length());
            sb.append(s);
        }

        if (Utils.isPhone(title)) {
            name = "手机";
        } else {
            name = sb.toString();
        }
        return name;
    }

    /**
     * 设置头像：
     *
     * @param title
     * @param iv
     * @param head
     */
    public static void setImage(String title, TextImageView iv, String head) {
        if (head == null || "img/1_1.png".equals(head) || head.equals("")) {
            iv.setText(getPicTitle(title));
        } else {
            ImageLoader.getInstance().displayImage(head, iv);
        }
    }

    /**
     * 设置下拉刷新：
     *
     * @param swipe_container
     */
    public static void setRefresh(SwipeRefreshLayout swipe_container, OnRefreshListener listener) {
        swipe_container.setOnRefreshListener(listener);
        swipe_container.setColorScheme(
                android.R.color.holo_orange_dark,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    /**
     * 网络请求：
     */
    public static void doPost(final LoadingDialog dialog, final Activity activity, String url, RequestParams params,
                              final HttpCallBack call) {

        if (dialog != null) {
            dialog.show();
        }

        AsyncHttpServiceHelper.post(url, params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                final JsonBean bean = JsonUtils.getMessage(new String(arg2));
                if ("200".equals(bean.getCode())) {
                    Log.e("tag", "AsyncHttpServiceHelper----success: " +bean.getCode());
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            call.success(bean);

                            if (dialog != null) {
                                dialog.dismiss();
                            }
                        }
                    });
                } else {
                    Log.e("tag", "AsyncHttpServiceHelper----failure: " +bean.getMsg());
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            call.failure(bean.getMsg());
                            if (dialog != null) {
                                dialog.dismiss();
                            }
                        }
                    });
                }
            }

            @Override
            public void onFinish() {
                Log.e("tag", "AsyncHttpServiceHelper----onFinish: " );
                activity.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Log.e("TAG", "结束！！！！");
                        call.finish();
                        if (dialog != null) {
                            dialog.dismiss();
                        }
                    }
                });
            }

            @Override
            public void onFailure(final int arg0, Header[] arg1, byte[] arg2, final Throwable arg3) {
                Log.e("tag", "AsyncHttpServiceHelper----onFailure: " );
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        call.failure("请检查网络！");
                        Log.e("tag","服务器异常的原因--------"+arg3+"---"+arg0);
                        if (dialog != null) {
                            dialog.dismiss();
                        }
                    }
                });
            }
        });

    }

    // 网络请求方法回调：
    public interface HttpCallBack {
        public void success(JsonBean bean);

        public void failure(String msg);

        public void finish();// 结束；
    }

}
