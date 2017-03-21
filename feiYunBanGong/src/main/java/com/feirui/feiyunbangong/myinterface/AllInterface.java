package com.feirui.feiyunbangong.myinterface;

import com.alibaba.mobileim.contact.IYWContact;
import com.feirui.feiyunbangong.entity.ChildItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AllInterface {

	// 组的展开关闭状态发生改变的接口：
	public interface OnGroupStateChangedListener {
		// 分组展开时执行：
		public void onGroupExpanded(int groupPosition);

		// 分组关闭时执行：
		public void onGroupCollapsed(int groupPosition);

	}

	// 监听团队公告消息数量接口：
	public interface OnTeamNoticeNumChanged {
		public void changed(int number);
	}

	// 监听新的朋友申请消息数量接口：
	public interface OnNewFriendNumChanged {
		public void newFriendNumChanged(int number);
	}

	// 阿里百川监听发名片回调：
	public interface ISelectContactListener {
		public void onSelectCompleted(List<IYWContact> contacts);
	}

	// 阿里百川监听点击发送联系人名片回调：
	public interface OnSendCardListener {
		public void onSendCard(List<ChildItem> items);
	}

	// 当获取到已读未读消息时回调：
	public interface NoticeCallBack {
		public void callBack(ArrayList<HashMap<String, Object>> infor);
	}

}
