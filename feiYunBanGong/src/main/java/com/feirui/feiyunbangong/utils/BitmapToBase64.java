package com.feirui.feiyunbangong.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.util.Base64;

public class BitmapToBase64 {
	/**
	 * bitmap转Base64
	 */
	public static String bitmapToBase64(Bitmap bitmap) {

		String result = "";
		ByteArrayOutputStream bos = null;
		try {
			if (null != bitmap) {
				bos = new ByteArrayOutputStream();
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);// 图片压缩
				bos.flush();// 刷出图片
				bos.close();
				byte[] bitmapByte = bos.toByteArray();
				result = Base64.encodeToString(bitmapByte, Base64.DEFAULT);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != null) {
				try {
					bos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}
}

