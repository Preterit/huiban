package com.feirui.feiyunbangong.im;

import com.alibaba.mobileim.aop.Pointcut;
import com.alibaba.mobileim.aop.custom.YWSDKGlobalConfig;

/**
 * 全局配置：
 */
public class MyYWSDKFlobalConfig extends YWSDKGlobalConfig {

	public MyYWSDKFlobalConfig(Pointcut pointcut) {
		super(pointcut);
	}

	/**
	 * 聊天显示“对方正在输入中”状态的开关 默认：开启
	 * 
	 * @return true： 开启 false： 关闭
	 */
	public boolean enableInputStatus() {
		return true;
	}

	/**
	 * 是否支持单聊消息的单条消息精确显示已读未读的开关 默认：关闭
	 * 
	 * @return true： 开启 false： 关闭
	 */
	public boolean enableMsgReadStatus() {
		return true;
	}

	/**
	 * 是否新消息的支持角标显示的开关
	 * 
	 * @return
	 */
	public boolean enableShortcutBadger() {
		return false;
	}

	/**
	 * 设置重新拉取profile的时间间隔
	 * 
	 * @return 重新拉取profile的时间间隔，单位：秒
	 */
	public long getReadyProfileReUpdateTimeGap() {
		return 24 * 60 * 60;
	};

}
