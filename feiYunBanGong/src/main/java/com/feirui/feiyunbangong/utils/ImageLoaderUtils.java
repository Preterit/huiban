package com.feirui.feiyunbangong.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

import com.feirui.feiyunbangong.Happlication;
import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.entity.JsonBean;
import com.feirui.feiyunbangong.state.AppStore;
import com.feirui.feiyunbangong.view.SelectPicPopupWindow;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LRULimitedMemoryCache;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.L;

import org.apache.http.Header;

import java.io.File;

public class ImageLoaderUtils {

    // 试验配置：
    public static ImageLoaderConfiguration getNowConfig(Context context) {
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder() //
                .showImageForEmptyUri(R.drawable.fragment_head) //
                .showImageOnFail(R.drawable.fragment_head) //
                .cacheInMemory(true) //
                .cacheOnDisc(false) //防止加载本地图片出现错误
                //http://blog.csdn.net/shaw1994/article/details/47223133
                .build();//
        ImageLoaderConfiguration config = new ImageLoaderConfiguration//
                .Builder(context)//
                .defaultDisplayImageOptions(defaultOptions)//
                .discCacheSize(50 * 1024 * 1024)//
                .discCacheFileCount(300)// 缓存一百张图片
                // .memoryCacheSize(12 * 1024 * 1024)// 内存缓存图片；
                .memoryCacheSize((int) Runtime.getRuntime().maxMemory() / 8)// 获取应用程序最大可用内存
                .memoryCacheExtraOptions(480, 800).writeDebugLogs()
                // 即保存的每个缓存文件的最大长宽
                // 线程池内加载的数量
                .threadPoolSize(3)
                // 缓存策略你可以通过自己的内存缓存实现 ，这里用弱引用，缺点是太容易被回收了，不是很好！
                .memoryCache(new WeakMemoryCache()).build();//
        return config;
    }

    /**
     * 所有的配置参数举例
     *
     * @return
     */
    public static ImageLoaderConfiguration getWholeConfig(Context con) {
        // // 设置默认的配置，设置缓存，这里不设置可以到别的地方设置
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true).cacheOnDisc(true).build();

        // 设置缓存的路径
        // File cacheDir = new
        // File(SDCardUtils.getSDCardPath()+"imageloader/Cache");
        // L.e(SDCardUtils.getSDCardPath()+"imageloader/Cache");

        File cacheDir = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath());

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                Happlication.getInstance().getAppContext())
                // 即保存的每个缓存文件的最大长宽
                .memoryCacheExtraOptions(480, 800)
                // 线程池内加载的数量
                .threadPoolSize(3)
                // 解释：当同一个Uri获取不同大小的图片，缓存到内存时，只缓存一个。默认会缓存多个不同的大小的相同图片
                .threadPriority(Thread.NORM_PRIORITY - 2)
                // 拒绝缓存多个图片。
                .denyCacheImageMultipleSizesInMemory()
                // 缓存策略你可以通过自己的内存缓存实现 ，这里用弱引用，缺点是太容易被回收了，不是很好！
                .memoryCache(new WeakMemoryCache())
                // 设置内存缓存的大小
                .memoryCacheSize(2 * 1024 * 1024)
                // 设置磁盘缓存大小 20M
                .discCacheSize(20 * 1024 * 1024)
                // 将保存的时候的URI名称用MD5 加密
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                // 设置图片下载和显示的工作队列排序
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                // 缓存的文件数量
                .discCacheFileCount(100)
                // 自定义缓存路径
                .discCache(new UnlimitedDiscCache(cacheDir))
                // 显示图片的参数，默认：DisplayImageOptions.createSimple()
                .defaultDisplayImageOptions(defaultOptions)
                .imageDownloader(
                        new BaseImageDownloader(con, 5 * 1000, 30 * 1000)) // connectTimeout
                // (5
                // s),
                // readTimeout
                // (30
                // s)超时时间
                // .writeDebugLogs()
                // //
                // 打开调试日志
                .build();// 开始构建

        return config;
    }

    /**
     * 比较常用的配置方案
     *
     * @return
     */
    public static ImageLoaderConfiguration getSimpleConfig(Context c) {
        // 设置缓存的路径
        File cacheDir = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath());
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                c).memoryCacheExtraOptions(480, 800) // 即保存的每个缓存文件的最大长宽
                .threadPriority(Thread.NORM_PRIORITY - 2) // 线程池中线程的个数
                .denyCacheImageMultipleSizesInMemory() // 禁止缓存多张图片
                .memoryCache(new LRULimitedMemoryCache(10 * 1024 * 1024)) // 缓存策略
                .memoryCacheSize(15 * 1024 * 1024) // 设置内存缓存的大小
                .discCacheFileNameGenerator(new Md5FileNameGenerator()) // 缓存文件名的保存方式
                .discCacheSize(20 * 1024 * 1024) // 磁盘缓存大小
                .tasksProcessingOrder(QueueProcessingType.LIFO) // 工作队列
                .discCacheFileCount(100) // 缓存的文件数量
                .discCache(new UnlimitedDiscCache(cacheDir)) // 自定义缓存路径
                // .writeDebugLogs() // Remove for release app
                .build();
        return config;
    }

    /**
     * 显示图片的所有配置
     *
     * @return
     */
    public static DisplayImageOptions getWholeOptions() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.acquiesce_in) // 设置图片在下载期间显示的图片
                .showImageForEmptyUri(R.drawable.acquiesce_in)// 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.acquiesce_in) // 设置图片加载/解码过程中错误时候显示的图片
                .cacheInMemory(true)// 设置下载的图片是否缓存在内存中
                .cacheOnDisc(true)// 设置下载的图片是否缓存在SD卡中
                .considerExifParams(true) // 是否考虑JPEG图像EXIF参数（旋转，翻转）
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)// 设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.RGB_565)// 设置图片的解码类型
                // .decodingOptions(BitmapFactory.Options
                // decodingOptions)//设置图片的解码配置
                .delayBeforeLoading(0)// int delayInMillis为你设置的下载前的延迟时间
                // 设置图片加入缓存前，对bitmap进行设置
                // .preProcessor(BitmapProcessor preProcessor)
                .resetViewBeforeLoading(true)// 设置图片在下载前是否重置，复位
                .displayer(new RoundedBitmapDisplayer(20))// 不推荐用！！！！是否设置为圆角，弧度为多少
                .displayer(new FadeInBitmapDisplayer(100))// 是否图片加载好后渐入的动画时间，可能会出现闪动
                .build();// 构建完成

        return options;
    }

    /**
     * 设置常用的设置项
     *
     * @return
     */
    public static DisplayImageOptions getSimpleOptions() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.acquiesce_in) // 设置图片在下载期间显示的图片
                .showImageForEmptyUri(R.drawable.acquiesce_in)// 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.acquiesce_in) // 设置图片加载/解码过程中错误时候显示的图片
                .cacheInMemory(true)// 设置下载的图片是否缓存在内存中
                .cacheOnDisc(true)// 设置下载的图片是否缓存在SD卡中
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)// 设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.RGB_565)// 设置图片的解码类型
                .build();// 构建完成
        return options;
    }

    static SelectPicPopupWindow menuWindow;
    /***
     * 上传图片，成功后修改view的图片资源
     */

    private static Activity activity;

    private static String api;
    private static RequestParams params;
    private static ImageView view;

    public static void UpdateMessage(String api1, RequestParams params1,
                                     ImageView view1, Activity context) {

        // 实例化SelectPicPopupWindow
        menuWindow = new SelectPicPopupWindow((Activity) context, itemsOnClick);
        activity = context;
        api = api1;
        view = view1;
        params = params1;
        imageLoader.init(getSimpleConfig(activity));
        // 显示窗口
        menuWindow.showAtLocation(activity.findViewById(R.id.rl_work),
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置
    }

    /**
     * 为弹出的拍照窗口添加按钮点击监听事件
     */
    private static OnClickListener itemsOnClick = new OnClickListener() {

        @Override
        public void onClick(View v) {
            menuWindow.dismiss();
            switch (v.getId()) {
                case R.id.view_camero_rl_takephoto:
                    Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(
                            Environment.getExternalStorageDirectory(),
                            "touxiang_user.jpg")));
                    activity.startActivityForResult(i, 2);
                    break;
                case R.id.view_camero_rl_selectphoto:
                    Intent i2 = new Intent(Intent.ACTION_PICK, null);
                    i2.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            "image/*");
                    activity.startActivityForResult(i2, 1);
                    break;
                default:
                    break;
            }
        }
    };

    public void ImageBack(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case 1:
                if (resultCode == Activity.RESULT_OK) {
                    startPhotoZoom(data.getData());
                }
                break;
            case 2:
                if (resultCode == Activity.RESULT_OK) {
                    File temp = new File(Environment.getExternalStorageDirectory()
                            + "/touxiang_user.jpg");
                    startPhotoZoom(Uri.fromFile(temp));
                }
                break;
            case 3:
                if (data != null) {
                    setPicToView(data);
                }
                break;
        }
    }

    /**
     * 拿到选择的图片
     *
     * @param picdata
     */
    private void setPicToView(Intent picdata) {
        Bundle extras = picdata.getExtras();
        if (extras != null) {
            String picStr = "";
            final Bitmap photo = extras.getParcelable("data");
            picStr = BitmapToBase64.bitmapToBase64(photo);
            view.setImageBitmap(photo);

            RequestParams params = HttpRequestParamsUtil
                    .getParams(UrlTools.modhead);
            params.put("mobile", AppStore.user.getInfor().get(0).get("mobile"));
            params.put("pic", picStr);
            String url = UrlTools.url + UrlTools.modhead;
            L.e("url=" + url + "----" + params);
            AsyncHttpServiceHelper.post(url, params,
                    new AsyncHttpResponseHandler() {

                        @Override
                        public void onSuccess(int arg0, Header[] arg1,
                                              final byte[] arg2) {
                            super.onSuccess(arg0, arg1, arg2);
                            final JsonBean json = JsonUtils
                                    .getMessage(new String(arg2));
                            activity.runOnUiThread(new Runnable() {
                                public void run() {
                                    int i = 0;
                                    i++;
                                    if ("200".equals(json.getCode())) {
                                        Toast.makeText(activity, "上传图片成功",
                                                Toast.LENGTH_SHORT).show();
                                        L.e("***************" + i);

                                        imageLoader.clearMemoryCache();
                                        imageLoader.clearDiscCache();
                                        L.e("***************"
                                                + new String(arg2));
                                        view.setImageBitmap(photo);

                                        AppStore.user
                                                .getInfor()
                                                .get(0)
                                                .put("driver_pic",
                                                        json.getInfor()
                                                                .get(0)
                                                                .get("driver_pic"));
                                        imageLoader.clearMemoryCache();
                                        imageLoader.clearDiscCache();
                                        initMyhead();
                                    } else {
                                        Toast.makeText(activity, "上传图片失败",
                                                Toast.LENGTH_SHORT).show();
                                        L.e("***************" + i);

                                    }

                                }

                            });
                        }
                    });
        }
    }

    private void initMyhead() {
        // TODO Auto-generated method stub
        // SetBitmap(view, api, params, "");

    }

    /**
     * 调节图片大小工具
     *
     * @param uri
     */
    public static void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        activity.startActivityForResult(intent, 3);
    }

    private static ImageLoader imageLoader = ImageLoader.getInstance();

    /***
     * 获取网络图片，控件的名称，api,请求的参数，返回值的键
     **/
    public static void SetBitmap(final ImageView view, String api,
                                 RequestParams params, final String key) {

        imageLoader.init(getSimpleConfig(activity));

        AsyncHttpServiceHelper.post(UrlTools.url + api, params,
                new AsyncHttpResponseHandler() {

                    @Override
                    public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                        // TODO Auto-generated method stub
                        super.onSuccess(arg0, arg1, arg2);
                        if (arg0 == 200) {

                            JsonBean jsonBean = JsonUtils
                                    .getMessage(new String(arg2));

                            imageLoader.displayImage((String) jsonBean
                                    .getInfor().get(0).get(key), view);

                        }

                    }
                });

    }

}
