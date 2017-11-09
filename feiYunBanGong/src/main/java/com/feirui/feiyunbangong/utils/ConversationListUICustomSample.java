package com.feirui.feiyunbangong.utils;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.mobileim.aop.Pointcut;
import com.alibaba.mobileim.aop.custom.IMConversationListUI;
import com.alibaba.mobileim.conversation.YWConversation;
import com.alibaba.mobileim.conversation.YWConversationType;
import com.alibaba.mobileim.conversation.YWSystemConversation;
import com.alibaba.mobileim.kit.contact.YWContactHeadLoadHelper;
import com.feirui.feiyunbangong.state.AppStore;

/**
 * 最近会话界面的定制点(根据需要实现相应的接口来达到自定义会话列表界面)，不设置则使用openIM默认的实现 调用方设置的回调，必须继承BaseAdvice
 * 根据不同的需求实现 不同的 开放的 Advice
 * com.alibaba.mobileim.aop.pointcuts包下开放了不同的Advice.通过实现多个接口，组合成对不同的ui界面的定制
 * 这里设置了自定义会话的定制 1.CustomConversationAdvice 实现自定义会话的ui定制
 * 2.CustomConversationTitleBarAdvice 实现自定义会话列表的标题的ui定制
 * <p/>
 * 另外需要在application中将这个Advice绑定。设置以下代码
 * AdviceBinder.bindAdvice(PointCutEnum.CONVERSATION_FRAGMENT_POINTCUT,
 * CustomChattingAdviceDemo.class);
 *
 * @author jing.huai
 */
public class ConversationListUICustomSample extends IMConversationListUI {

	private static final String TAG = "ConversationListUICustomSample";

	// 必须写这个构造方法！~！！！！
	public ConversationListUICustomSample(Pointcut pointcut) {
		super(pointcut);
		
	}

	@Override
	public boolean needHideTitleView(Fragment fragment) {
		return true;
	}

	/**
	 * 返回自定义置顶回话的背景色(16进制字符串形式)
	 * 
	 * @return
	 */
	@Override
	public String getCustomTopConversationColor() {
		return "#e1f5fe";
	}

	/**
	 * 是否需要圆角矩形的头像
	 *
	 * @return true:需要圆角矩形
	 * <br>
	 * false:不需要圆角矩形，默认为圆形
	 * <br>
	 * 注：如果返回true，则需要使用{@link #getRoundRectRadius()}给出圆角的设置半径，否则无圆角效果
	 */
	@Override
	public boolean isNeedRoundRectHead() {
		return true;
	}

	/**
	 * 返回设置圆角矩形的圆角半径大小
	 *
	 * @return 0:如果{@link #isNeedRoundRectHead()}返回true，此处返回0则表示头像显示为直角正方形
	 */
	@Override
	public int getRoundRectRadius() {
		return 0;
	}

	@Override
	public View getCustomEmptyViewInConversationUI(Context context) {
		/** 以下为示例代码，开发者可以按需返回任何view */
		TextView textView = new TextView(context);
		textView.setText("还没有会话哦，快去找人聊聊吧!");
		textView.setGravity(Gravity.CENTER);
		textView.setTextSize(18);
		return textView;
	}

	/*********** 以下是定制会话item view的示例代码 ***********/
	//有几种自定义，数组元素就需要几个，数组元素值从0开始
	//private final int[] viewTypeArray = {0,1,2,3}，这样就有4种自定义View
	private final int[] viewTypeArray = {0};
	/**
	 * 自定义item view的种类数
	 * @return 种类数
	 */
	@Override
	public int getCustomItemViewTypeCount() {
		return viewTypeArray.length;
	}
	@Override
	public int getCustomItemViewType(YWConversation conversation) {
		if (conversation.getConversationType() == YWConversationType.Custom) {
			if (conversation.getConversationId().equals("sysTribe")){
				//得到系统对话
				YWSystemConversation mConversation = AppStore.mIMKit.getConversationService().getSystemConversation();
//				List<YWMessage> mList = new ArrayList<YWMessage>();
//				mList = mConversation.getMessageLoader().loadMessage(20, null);
				AppStore.mIMKit.getConversationService().markReaded(conversation); //标记当前的conversion为已读 提示消息不再出现
//				for (int i = 0; i < mList.size();i++){
//					mList.get(i).setMsgReadStatus(YWMessage.MSG_READED_STATUS);  // 标记消息为已读（本地）
//					mConversation.setMsgReadedStatusToServer(mList.get(i), new IWxCallback() { //标记消息为已读（后台）
//						@Override
//						public void onSuccess(Object... objects) {
//
//						}
//
//						@Override
//						public void onError(int i, String s) {
//
//						}
//
//						@Override
//						public void onProgress(int i) {
//
//						}
//					});
//				}

				return viewTypeArray[0];
			}
		}
		//这里必须调用基类方法返回！！

		return super.getCustomItemViewType(conversation);
	}

	/**
	 *
	 * @param fragment
	 * @param conversation
	 * @param convertView
	 * @param viewType
	 * @param headLoadHelper
	 * @param parent
	 * @return  返回一个空的view 系统提示不再出现
	 */
	@Override
	public View getCustomItemView(Fragment fragment, YWConversation conversation, View convertView, int viewType, YWContactHeadLoadHelper headLoadHelper, ViewGroup parent) {
		if (viewType == viewTypeArray[0]){
			return new View(fragment.getContext());
		}
		return super.getCustomItemView(fragment, conversation, convertView, viewType, headLoadHelper, parent);
	}


}
