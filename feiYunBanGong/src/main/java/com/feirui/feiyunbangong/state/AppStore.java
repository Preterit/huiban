package com.feirui.feiyunbangong.state;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.graphics.Bitmap;

import com.alibaba.mobileim.YWIMKit;
import com.feirui.feiyunbangong.entity.ChildItem;
import com.feirui.feiyunbangong.entity.Group;
import com.feirui.feiyunbangong.entity.JsonBean;
import com.feirui.feiyunbangong.entity.LianXiRen;
import com.feirui.feiyunbangong.entity.MyUser;
import com.feirui.feiyunbangong.myinterface.AllInterface;

public class AppStore {
	public static JsonBean user;
	public static YWIMKit mIMKit;// 即时聊天SDK对象实现；
	public static String account;
	// 首页轮播图片集合
	public static ArrayList<HashMap<String, Object>> mpList;
	// 联系人列表集合：
	public static ArrayList<LianXiRen> lianxiren;
	// 二维码名片：
	public static Bitmap erweima;

	// 用户信息：
	public static MyUser myuser = new MyUser("", "", "", "", "", "", "", "", "");

	// 修改备注的用户手机号：
	public static String phone = "";
	// 用户邀请码：
	public static String yaoqingma = "";

	// appkey：
	public static final String appkey = "23529997";

	// 联系人集合：
	public static Map<Integer, List<ChildItem>> map;

	// 联系人列表人数集合：
	public static List<String> list = new ArrayList<>();

	// 存放activity的集合：团队的几个activity
	public static List<Activity> acts = new ArrayList<>();

	// 好友分组集合：
	public static List<Group> groups = new ArrayList<>();

	// 发送名片：
	public static AllInterface.OnSendCardListener listener;

}
