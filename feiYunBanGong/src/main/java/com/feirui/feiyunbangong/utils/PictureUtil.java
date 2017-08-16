package com.feirui.feiyunbangong.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;

import com.feirui.feiyunbangong.view.SelectPicPopupWindow;

public class PictureUtil {

	// 图片选择：
	public static SelectPicPopupWindow toastChoicePic(Activity activity,
			OnClickListener listener, View v) {
		SelectPicPopupWindow window = null;
		window = new SelectPicPopupWindow(activity, listener);
		// 显示窗口
		window.showAtLocation(v, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0,
				0); // 设置layout在PopupWindow中显示的位置
		return window;
	}

	// 拍照：
	public static String paiZhao(SelectPicPopupWindow window, Activity activity) {
		window.dismiss();
		String path = "";
		Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		File file = new File(Environment.getExternalStorageDirectory(),
				System.currentTimeMillis() + ".jpg");
		i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
		path = file.getAbsolutePath();
		activity.startActivityForResult(i, 2);
		return path;
	}
	// 拍照File：
	public static File paiZhaoFile(SelectPicPopupWindow window, Activity activity) {
		window.dismiss();
		Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		File file = new File(Environment.getExternalStorageDirectory(),
				System.currentTimeMillis() + ".jpg");
		i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
		activity.startActivityForResult(i, 2);
		return file;
	}

	// 从相册选择：
	public static void xiangCe(SelectPicPopupWindow window, Activity activity) {
		window.dismiss();
		Intent i2 = new Intent(Intent.ACTION_PICK);
		i2.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				"image/*");
		activity.startActivityForResult(i2, 1);
	}

	public static void startPhotoZoom(Uri uri, Activity activity, String path) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
		// intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("crop", "true");
		// // outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 250);
		intent.putExtra("outputY", 250);
		intent.putExtra("return-data", true);
		File file = new File(Environment.getExternalStorageDirectory(),
				System.currentTimeMillis() + ".jpg");
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file)); // 专入目标文件
		intent.putExtra("outputFormat", "JPG"); // 输入文件格式
		path = file.getAbsolutePath();
		activity.startActivityForResult(intent, 3);
	}
	
	
	

}
