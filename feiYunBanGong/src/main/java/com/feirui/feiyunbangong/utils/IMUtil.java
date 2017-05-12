package com.feirui.feiyunbangong.utils;

import com.alibaba.mobileim.YWIMCore;
import com.alibaba.mobileim.aop.AdviceBinder;
import com.alibaba.mobileim.aop.PointCutEnum;
import com.alibaba.mobileim.channel.cloud.contact.YWProfileInfo;
import com.alibaba.mobileim.channel.event.IWxCallback;
import com.alibaba.mobileim.contact.IYWContact;
import com.alibaba.mobileim.contact.IYWContactCacheUpdateListener;
import com.alibaba.mobileim.contact.IYWContactHeadClickCallback;
import com.alibaba.mobileim.contact.IYWContactHeadClickListener;
import com.alibaba.mobileim.contact.IYWContactOperateNotifyListener;
import com.alibaba.mobileim.contact.IYWContactProfileCallback;
import com.alibaba.mobileim.contact.IYWContactService;
import com.alibaba.mobileim.contact.IYWCrossContactProfileCallback;
import com.alibaba.mobileim.contact.IYWDBContact;
import com.alibaba.mobileim.contact.YWRichContentContact;
import com.alibaba.mobileim.conversation.YWConversation;
import com.alibaba.mobileim.conversation.YWConversationCreater;
import com.alibaba.mobileim.conversation.YWMessage;
import com.alibaba.mobileim.conversation.YWMessageChannel;
import com.alibaba.mobileim.gingko.presenter.contact.IContactProfileUpdateListener;
import com.alibaba.mobileim.lib.model.message.YWSystemMessage;
import com.alibaba.mobileim.lib.presenter.contact.IContactListListener;
import com.feirui.feiyunbangong.activity.tribe.AtMsgListOperationSample;
import com.feirui.feiyunbangong.activity.tribe.AtMsgListUISample;
import com.feirui.feiyunbangong.activity.tribe.SelectTribeAtMemberSample;
import com.feirui.feiyunbangong.activity.tribe.SendAtMsgDetailUISample;
import com.feirui.feiyunbangong.im.MyChatUI;
import com.feirui.feiyunbangong.im.MyIMChattingPageOperateion;
import com.feirui.feiyunbangong.im.MyYWSDKFlobalConfig;
import com.feirui.feiyunbangong.state.AppStore;

import java.util.List;

/**
 * 即时聊天业务类：
 */
public class IMUtil {

	public static void bind() {
		// 会话列表：
		AdviceBinder.bindAdvice(PointCutEnum.CONVERSATION_FRAGMENT_UI_POINTCUT,
				ConversationListUICustomSample.class);

		/*
		 * // 会话列表： AdviceBinder.bindAdvice(
		 * PointCutEnum.CONVERSATION_FRAGMENT_OPERATION_POINTCUT,
		 * MyIMConversationListOperation.class);
		 */

		// 单聊列表：
		AdviceBinder.bindAdvice(PointCutEnum.CHATTING_FRAGMENT_UI_POINTCUT,
				MyChatUI.class);

		// 全局配置；
		AdviceBinder.bindAdvice(PointCutEnum.YWSDK_GLOBAL_CONFIG_POINTCUT,
				MyYWSDKFlobalConfig.class);

		// 单聊窗口：
		AdviceBinder.bindAdvice(PointCutEnum.CHATTING_FRAGMENT_POINTCUT,
				MyIMChattingPageOperateion.class);

		//@消息界面
		AdviceBinder.bindAdvice(PointCutEnum.TRIBE_FRAGMENT_AT_MSG_DETAIL, SendAtMsgDetailUISample.class);
		AdviceBinder.bindAdvice(PointCutEnum.TRIBE_ACTIVITY_AT_MSG_LIST, AtMsgListUISample.class);
		AdviceBinder.bindAdvice(PointCutEnum.TRIBE_ACTIVITY_SELECT_AT_MEMBER, SelectTribeAtMemberSample.class);
		AdviceBinder.bindAdvice(PointCutEnum.TRIBE_ACTIVITY_AT_MSG_LIST_OP, AtMsgListOperationSample.class);



	}

	// 发送第一条打招呼消息：
	public static void sendMsg(String phone) {

		YWIMCore imCore = AppStore.mIMKit.getIMCore();
		// 创建一条文本或者表情消息
		final YWConversationCreater conversationCreater = imCore
				.getConversationService().getConversationCreater();
		YWMessage msg = YWMessageChannel.createTextMessage("您好！");
		YWConversation conversation = conversationCreater
				.createConversationIfNotExist(phone);
		// 将消息发送给对方
		conversation.getMessageSender().sendMessage(msg, 5000,
				new IWxCallback() {

					@Override
					public void onSuccess(Object... arg0) {
						// 发送成功

					}

					@Override
					public void onProgress(int arg0) {
					}

					@Override
					public void onError(int arg0, String arg1) {
						// 发送失败
					}
				});
	}

	// 修改本地数据库中的备注：
	public static void remark(String phone, String name, IWxCallback callback) {
		IYWContactService contactService = AppStore.mIMKit.getContactService();
		contactService.chgContactRemark(phone, AppStore.appkey, name, callback);

		new IYWContactService() {

			@Override
			public void updateProfileInfoToServer(YWProfileInfo arg0,
					IWxCallback arg1) {
				// TODO Auto-generated method stub

			}

			@Override
			public void updateProfileInfo(String arg0, YWProfileInfo arg1) {
				// TODO Auto-generated method stub

			}

			@Override
			public void updateContactSystemMessage(YWSystemMessage arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void unRegisterContactsListener(IContactListListener arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void syncCrossContactsOnlineStatus(List<IYWContact> arg0,
					IWxCallback arg1) {
				// TODO Auto-generated method stub

			}

			@Override
			public void syncContactsOnlineStatus(List<IYWContact> arg0,
					IWxCallback arg1) {
				// TODO Auto-generated method stub

			}

			@Override
			public void syncContacts(IWxCallback arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void syncBlackContacts(IWxCallback arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void setCrossContactProfileCallback(
					IYWCrossContactProfileCallback arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void setContactProfileCallback(IYWContactProfileCallback arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void setContactMsgRecType(IYWContact arg0, int arg1,
					int arg2, IWxCallback arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void setContactHeadClickListener(
					IYWContactHeadClickListener arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void setContactHeadClickCallback(
					IYWContactHeadClickCallback arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void removeProfileUpdateListener(
					IContactProfileUpdateListener arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void removeContactOperateNotifyListener(
					IYWContactOperateNotifyListener arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void removeContactCacheUpdateListener(
					IYWContactCacheUpdateListener arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void removeBlackContact(String arg0, String arg1,
					IWxCallback arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void registerContactsListener(IContactListListener arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void notifyContactProfileUpdate(String arg0, String arg1) {
				// TODO Auto-generated method stub

			}

			@Override
			public void notifyContactProfileUpdate() {
				// TODO Auto-generated method stub

			}

			@Override
			public boolean isBlackContact(String arg0, String arg1) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public IYWContact getWXIMContact(String arg0, String arg1) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public IYWContact getWXIMContact(String arg0) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public YWRichContentContact getRichContentContact(String arg0,
					String arg1) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public void getMsgRecFlagForContactFromServer(String arg0,
					String arg1, int arg2, IWxCallback arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public int getMsgRecFlagForContact(String arg0, String arg1) {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public int getMsgRecFlagForContact(IYWContact arg0) {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public IYWCrossContactProfileCallback getDeveloperDefineCrossContactProfileCallback() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public IYWContactProfileCallback getDeveloperDefineContactProfileCallback() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public List<IYWContact> getCrossContactProfileInfos(
					List<IYWContact> arg0) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public IYWCrossContactProfileCallback getCrossContactProfileCallback() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public List<IYWDBContact> getContactsFromCache() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public long getContactsChangeTimeStamp() {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public List<IYWContact> getContactProfileInfos(List<String> arg0,
					String arg1) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public IYWContact getContactProfileInfo(String arg0, String arg1) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public IYWContactProfileCallback getContactProfileCallback() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public IYWContactHeadClickCallback getContactHeadClickCallback() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public void fetchUserProfiles(List<String> arg0, IWxCallback arg1) {
				// TODO Auto-generated method stub

			}

			@Override
			public void fetchUserProfile(List<String> arg0, String arg1,
					IWxCallback arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void fetchCrossUserProfile(List<IYWContact> arg0,
					IWxCallback arg1) {
				// TODO Auto-generated method stub

			}

			@Override
			public void delContact(String arg0, String arg1, IWxCallback arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void clearContactInfoCache(String arg0, String arg1) {
				// TODO Auto-generated method stub

			}

			@Override
			public void clearAllContactInfoCache() {
				// TODO Auto-generated method stub

			}

			@Override
			public void chgContactRemark(String arg0, String arg1, String arg2,
					IWxCallback arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void asynchronousSyncContactsToCacheAndDB(
					List<IYWDBContact> arg0, IWxCallback arg1) {
				// TODO Auto-generated method stub

			}

			@Override
			public void addProfileUpdateListener(
					IContactProfileUpdateListener arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void addContactOperateNotifyListener(
					IYWContactOperateNotifyListener arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void addContactCacheUpdateListener(
					IYWContactCacheUpdateListener arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void addContact(String arg0, String arg1, String arg2,
					String arg3, IWxCallback arg4) {
				// TODO Auto-generated method stub

			}

			@Override
			public void addBlackContact(String arg0, String arg1,
					IWxCallback arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void ackAddContact(String arg0, String arg1, boolean arg2,
					String arg3, IWxCallback arg4) {
				// TODO Auto-generated method stub

			}
		};

	}

}
