package com.feirui.feiyunbangong.im;

import android.support.v4.app.Fragment;

import com.alibaba.mobileim.aop.Pointcut;
import com.alibaba.mobileim.aop.custom.IMConversationListOperation;
import com.alibaba.mobileim.conversation.YWConversation;
import com.alibaba.mobileim.conversation.YWConversationType;

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
public class MyIMConversationListOperation extends IMConversationListOperation {

	public MyIMConversationListOperation(Pointcut pointcut) {
		super(pointcut);
	}

	/**
	 * 返回自定义会话和群会话的头像 url 该方法只适用设置自定义会话和群会话的头像，设置单聊会话头像请参考
	 * {@link com.taobao.openimui.sample.UserProfileSampleHelper}
	 *
	 * @param fragment
	 * @param conversation
	 *            会话 可以通过
	 *            conversation.getConversationId拿到用户设置的会话id以根据不同的逻辑显示不同的头像
	 * @return
	 */
	@Override
	public String getConversationHeadPath(Fragment fragment,
			YWConversation conversation) {
		if (conversation.getConversationType() == YWConversationType.Tribe) {
			return "http://7xlpp2.com1.z0.glb.clouddn.com/%40/res/ugc/12D76150-4D07-4A78-9F0C-A18FDE7B7047.png";
		}
		// 如果开发者需要异步获取url的话，先return
		// null，在url获取到后，调用notifyContactProfileUpdate进行刷新
		// 刷新后，SDK会再次调用getConversationHeadPath方法

		// 刷新调用，请参考：LoginSampleHelper.getInstance().getIMKit().getContactService().notifyContactProfileUpdate();

		return "http://tp2.sinaimg.cn/1721410501/50/40033657718/0";
	}

	

}
