package com.feirui.feiyunbangong.utils;

import java.util.ArrayList;

import com.feirui.feiyunbangong.entity.LianXiRen;

public class MyInterface {

	// 获取到注册过的联系人信息：
	public interface OnGetRegistPhone {
		void get(ArrayList<LianXiRen> lxrs);
	}

	// 获取到未注册的联系人信息：
	public interface OnGetUnRegistPhone {
		void get(ArrayList<LianXiRen> lxrs);
	}

	// 联系人发送添加朋友的接口：
	public interface OnSendAddFriend {
		void send(String phone);
	}

	// 联系人发送短信邀请的接口：
	public interface OnSendMsg {
		void sendMsg(String phone);
	}

}
