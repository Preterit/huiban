package com.feirui.feiyunbangong.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.alibaba.mobileim.IYWLoginService;
import com.alibaba.mobileim.YWIMCore;
import com.alibaba.mobileim.YWIMKit;
import com.alibaba.mobileim.conversation.IYWConversationService;

public class OpenIMHelper {
	private static OpenIMHelper mInstance;

	// 创建IMKit对象
	private YWIMKit mIMKit;
	// 获取YWIMCore对象
	private YWIMCore imCore;
	// 获取登录服务
	private IYWLoginService loginService;
	// 监听事件添加和删除
	private IYWConversationService conversationService;

	public static OpenIMHelper getInstance() {
		if (mInstance == null) {
			mInstance = new OpenIMHelper();
		}
		return mInstance;
	}

	/**
	 * 获取IMCore
	 */
	public YWIMCore getIMCore() {

		return imCore;

	}

	/**
	 * 先要初始化一下各个对象，不然拿到的都是空
	 * 
	 * @param user
	 *            登录阿里百川的账号
	 */
	// public void initYWAPI(String user) {
	// L.e("user==================" + user);
	// mIMKit = YWAPI.getIMKitInstance(user, Constant.APP_KEY);
	// loginService = mIMKit.getLoginService();
	// conversationService = mIMKit.getConversationService();
	// imCore = mIMKit.getIMCore();
	// }

	/**
	 * 
	 * @return IMKit对象
	 */
	public YWIMKit getYWIMKit() {
		return mIMKit;
	}

	/**
	 * 登录服务对象
	 */
	public IYWLoginService getIYWLoginService() {
		return loginService;
	}

	/**
	 * 
	 * @return 监听事件管理对象，添加监听、删除监听
	 */
	public IYWConversationService getIYWConversationService() {
		return conversationService;
	}

	/**
	 * 设置监听事件管理对象
	 * 
	 * @param conversationService
	 */
	public void setIYWConversationService(
			IYWConversationService conversationService) {
		this.conversationService = conversationService;
	}

	/**
	 * 字符串转md5
	 * 
	 * @param string
	 * @return
	 */
	public static String stringToMD5(String string) {
		byte[] hash;
		try {
			hash = MessageDigest.getInstance("MD5").digest(
					string.getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("Huh, MD5 should be supported?", e);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Huh, UTF-8 should be supported?", e);
		}

		StringBuilder hex = new StringBuilder(hash.length * 2);
		for (byte b : hash) {
			if ((b & 0xFF) < 0x10)
				hex.append("0");
			hex.append(Integer.toHexString(b & 0xFF));
		}
		return hex.toString();
	}

}
