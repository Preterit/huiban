package com.feirui.feiyunbangong.utils;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.mobileim.aop.Pointcut;
import com.alibaba.mobileim.aop.custom.IMConversationListUI;
import com.alibaba.mobileim.conversation.YWConversation;
import com.alibaba.mobileim.conversation.YWConversationType;
import com.alibaba.mobileim.kit.contact.YWContactHeadLoadHelper;
import com.alibaba.mobileim.lib.presenter.conversation.CustomViewConversation;
import com.lidroid.xutils.view.annotation.event.OnItemClick;

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

	@Override
	public View getCustomEmptyViewInConversationUI(Context context) {
		/** 以下为示例代码，开发者可以按需返回任何view */
		TextView textView = new TextView(context);
		textView.setText("还没有会话哦，快去找人聊聊吧!");
		textView.setGravity(Gravity.CENTER);
		textView.setTextSize(18);
		return textView;
	}
	
	
}
