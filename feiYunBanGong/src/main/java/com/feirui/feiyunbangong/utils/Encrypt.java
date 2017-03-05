package com.feirui.feiyunbangong.utils;

import com.feirui.feiyunbangong.state.Constant;

public class Encrypt {
	// 加密文件
	public static String getEnc(String enc) {
		String s = OpenIMHelper.getInstance().stringToMD5(
				Constant.ENCRYPT
						+ DateUtils.getTime("yyyy-MM-dd",
								System.currentTimeMillis()) + enc);
		return s;
	}

	// 密码加密
	public static String getMd(String md) {
		String s = OpenIMHelper.getInstance().stringToMD5(md);
		return s;
	}

}
