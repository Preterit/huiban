package com.feirui.feiyunbangong.im;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.mobileim.YWIMKit;
import com.alibaba.mobileim.contact.IYWContact;
import com.alibaba.mobileim.contact.IYWContactHeadClickCallback;
import com.alibaba.mobileim.contact.IYWContactHeadClickListener;
import com.alibaba.mobileim.contact.IYWContactService;
import com.alibaba.mobileim.contact.IYWCrossContactProfileCallback;
import com.alibaba.mobileim.conversation.YWConversation;
import com.alibaba.mobileim.lib.model.contact.Contact;
import com.feirui.feiyunbangong.activity.HaoYouZiLiaoActivity;
import com.feirui.feiyunbangong.dialog.LoadingDialog;
import com.feirui.feiyunbangong.entity.JsonBean;
import com.feirui.feiyunbangong.state.AppStore;
import com.feirui.feiyunbangong.state.Constant;
import com.feirui.feiyunbangong.utils.UrlTools;
import com.feirui.feiyunbangong.utils.Utils;
import com.feirui.feiyunbangong.utils.Utils.HttpCallBack;
import com.loopj.android.http.RequestParams;

/**
 * 自定义用户信息：
 */
public class MyUserProfileSampleHelper {

	public static Activity activity;

	// 初始化，建议放在登录之前
	public static void initProfileCallback() {

		YWIMKit imKit = AppStore.mIMKit;
		// 非空校验：
		if (imKit == null) {
			return;
		}
		final IYWContactService contactManager = imKit.getContactService();

		// 私聊头像点击的回调（开发者可以按需设置）
		contactManager
				.setContactHeadClickListener(new IYWContactHeadClickListener() {

					// 点击用户头像回调：
					@Override
					public void onUserHeadClick(Fragment fragment,
							YWConversation arg1, String user_id, String arg3,
							boolean arg4) {
						// 跳转到好友资料页面：
						Intent intent = new Intent(fragment.getActivity(),
								HaoYouZiLiaoActivity.class);
						intent.putExtra("phone", user_id);
						fragment.getActivity().startActivity(intent);
					}

					// 点击群头像回调：
					@Override
					public void onTribeHeadClick(Fragment arg0,
							YWConversation arg1, long arg2) {

					}

					// 点击自定义会话头像回调：
					@Override
					public void onCustomHeadClick(Fragment arg0,
							YWConversation arg1) {

					}
				});

		// 设置用户信息回调，如果开发者已经把用户信息导入了IM服务器，则不需要再调用该方法，IMSDK会自动到IM服务器获取用户信息
		contactManager
				.setCrossContactProfileCallback(new IYWCrossContactProfileCallback() {
					/**
					 * 设置头像点击事件, 该方法已废弃，后续请使用
					 * {@link IYWContactService#setContactHeadClickCallback(IYWContactHeadClickCallback)}
					 * 
					 * @param userId
					 *            需要打开页面的用户
					 * @param appKey
					 *            需要返回个人信息的用户所属站点
					 * @return
					 * @deprecated
					 */
					@Override
					public void updateContactInfo(Contact arg0) {
						// TODO Auto-generated method stub

					}

					@Override
					public Intent onShowProfileActivity(String arg0, String arg1) {
						// TODO Auto-generated method stub
						return null;
					}

					// 此方法会在SDK需要显示头像和昵称的时候，调用。同一个用户会被多次调用的情况。
					// 比如显示会话列表，显示聊天窗口时同一个用户都会被调用到。
					@Override
					public IYWContact onFetchContactInfo(final String userId,
							final String appKey) {
						// 首先从内存中获取用户信息
						// todo
						// 由于每次更新UI都会调用该方法，所以必现创建一个内存缓存，先从内存中拿用户信息，内存中没有才访问数据库或者网络，如果不创建内存缓存，而是每次都访问数据库或者网络，会导致死循环！！
						final IYWContact userInfo = mUserInfo.get(userId);
						// 若内存中有用户信息，则直接返回该用户信息
						if (userInfo != null) {
							return userInfo;
						} else {
							// 若内存中没有用户信息则从服务器获取用户信息
							// 先创建一个临时的IYWContact对象保存到mUserInfo，这是为了避免服务器请求成功之前SDK又一次调用了onFetchContactInfo()方法，从而导致再次触发服务器请求
							IYWContact temp = new UserInfo(userId, null,
									userId, appKey);
							mUserInfo.put(userId, temp);
							List<String> uids = new ArrayList<String>();
							uids.add(userId);

							// 从自己的服务器获取用户信息：
							String url = UrlTools.url + UrlTools.REMARK_FRIEND;
							RequestParams params = new RequestParams();
							params.put("mobile", userId);
							Utils.doPost(null, activity, url, params,
									new HttpCallBack() {

										@Override
										public void success(JsonBean bean) {

											HashMap<String, Object> hm = bean
													.getInfor().get(0);
											String name = hm.get("staff_name")
													+ "";
											String staff_head = hm
													.get("staff_head") + "";
											String remark = hm.get("remark")
													+ "";
											String nick = null;

											if (!TextUtils.isEmpty(remark)) {
												nick = remark;
											} else {
												nick = name;
											}

											IYWContact contact = new UserInfo(
													nick, staff_head, userId,
													appKey);

											Log.e("TAG", contact.toString());

											// todo
											// 更新内存中的用户信息，这里必须要更新内存中的数据，以便IMSDK刷新UI时可以直接从内存中拿到数据
											mUserInfo.remove(userId); // 移除临时的IYWContact对象
											mUserInfo.put(userId, contact); // 保存从服务器获取到的数据

											contactManager
													.notifyContactProfileUpdate(
															contact.getUserId(),
															appKey);

										}

										@Override
										public void failure(String msg) {
											// 移除临时的IYWContact对象，从而保证SDK再次调用onFetchContactInfo()时可用再次触发服务器请求
											mUserInfo.remove(userId);
										}

										@Override
										public void finish() {

										}

									});

							return temp;
						}
					}
				});

	}

	// 这个只是个示例，开发者需要自己管理用户昵称和头像
	public static Map<String, IYWContact> mUserInfo = new HashMap<String, IYWContact>();

	public static class UserInfo implements IYWContact {

		private String mUserNick; // 用户昵称
		private String mAvatarPath; // 用户头像URL
		private int mLocalResId;// 主要用于本地资源
		private String mUserId; // 用户id
		private String mAppKey; // 用户appKey

		public UserInfo(String nickName, String avatarPath, String userId,
				String appKey) {
			this.mUserNick = nickName;
			this.mAvatarPath = avatarPath;
			this.mUserId = userId;
			this.mAppKey = appKey;
		}

		public void setmUserNick(String mUserNick) {
			this.mUserNick = mUserNick;
		}

		public void setmAvatarPath(String mAvatarPath) {
			this.mAvatarPath = mAvatarPath;
		}

		public void setmLocalResId(int mLocalResId) {
			this.mLocalResId = mLocalResId;
		}

		public void setmUserId(String mUserId) {
			this.mUserId = mUserId;
		}

		public void setmAppKey(String mAppKey) {
			this.mAppKey = mAppKey;
		}

		@Override
		public String getAppKey() {
			return mAppKey;
		}

		@Override
		public String getAvatarPath() {
			if (mLocalResId != 0) {
				return mLocalResId + "";
			} else {
				return mAvatarPath;
			}
		}

		@Override
		public String getShowName() {
			return mUserNick;
		}

		@Override
		public String getUserId() {
			return mUserId;
		}

		@Override
		public String toString() {
			return "UserInfo{" + "mUserNick='" + mUserNick + '\''
					+ ", mAvatarPath='" + mAvatarPath + '\'' + ", mUserId='"
					+ mUserId + '\'' + ", mAppKey='" + mAppKey + '\''
					+ ", mLocalResId=" + mLocalResId + '}';
		}
	}

}
