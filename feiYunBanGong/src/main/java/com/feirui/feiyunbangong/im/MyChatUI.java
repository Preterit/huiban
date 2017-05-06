package com.feirui.feiyunbangong.im;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.mobileim.YWIMKit;
import com.alibaba.mobileim.aop.Pointcut;
import com.alibaba.mobileim.aop.custom.IMChattingPageUI;
import com.alibaba.mobileim.channel.util.AccountUtils;
import com.alibaba.mobileim.conversation.YWConversation;
import com.alibaba.mobileim.conversation.YWConversationType;
import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.state.AppStore;
import com.feirui.feiyunbangong.utils.Utils;

import static com.alibaba.mobileim.conversation.YWConversationType.P2P;

//聊天页面自定义；
public class MyChatUI extends IMChattingPageUI {

	private YWConversation conversation;

	public MyChatUI(Pointcut pointcut) {
		super(pointcut);
	}

	/**
	 * 是否隐藏标题栏
	 * 
	 * @param fragment
	 * @param conversation
	 * @return true: 隐藏标题栏 false：不隐藏标题栏
	 */
	@Override
	public boolean needHideTitleView(Fragment fragment,
			YWConversation conversation) {
		this.conversation = conversation;
		return true;
	}

	/**
	 * 是否需要在聊天界面展示顶部自定义View
	 * 
	 * @param fragment
	 *            聊天界面的Fragment
	 * @param intent
	 *            打开聊天界面Activity的Intent
	 * @return
	 */
	@Override
	public boolean isUseChattingCustomViewAdvice(Fragment fragment,
			Intent intent) {
		return true;
	}



	/**
	 * 聊天界面顶部展示的自定义View,这里的具体场景是当群消息屏蔽时展示的提示条
	 *
	 * @param fragment
	 *            聊天界面的Fragment
	 * @param intent
	 *            打开聊天界面Activity的Intent
	 * @return 返回要展示的View
	 */
	@SuppressLint("InflateParams")
	@Override
	public View getChattingFragmentCustomViewAdvice(Fragment fragment,
			Intent intent) {

		YWIMKit mIMKit = AppStore.mIMKit;
		if (mIMKit == null) {
			return null;
		}

		final Activity context = fragment.getActivity();
		View v = context.getLayoutInflater()
				.inflate(R.layout.ll_chat_top, null);
		final TextView tv_name = (TextView) v.findViewById(R.id.tv_name);
		ImageView iv_back = (ImageView) v.findViewById(R.id.iv_back);
		iv_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				context.finish();
			}
		});

        Log.e("TAG", "SHOP--------conversation.getConversationId()"+conversation.getConversationType());
		String conversationId = conversation.getConversationId();// 获取到联系人手机号或者群号；

		if(conversation.getConversationType() ==YWConversationType.P2P){
			final String phone = conversationId.substring(
					conversationId.length() - 11, conversationId.length());
			Log.e("TAG", phone + "P2P--------conversation.getConversationId()"+ P2P);

			String name = MyUserProfileSampleHelper.mUserInfo.get(phone)
					.getShowName();

			// 如果不是手机号的话则直接显示：
			if (!Utils.isPhone(name)) {
				tv_name.setText(name);
			}

			// 0.5秒后再次显示昵称，0.5秒的时间应该可以从后台获取到昵称了，刚开始显示的可能是手机号：
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					String name = MyUserProfileSampleHelper.mUserInfo.get(phone)
							.getShowName();
					tv_name.setText(name);
				}
			}, 300);

		} else if(conversation.getConversationType() == YWConversationType.Tribe){
			final  String tribeId = conversationId.substring(conversationId.length() - 9,conversationId.length());
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    String  title = AccountUtils.getShortUserID(tribeId);
                    tv_name.setText(title);
                    Log.d("tag","一个聊天的------"+title);
                }
            }, 300);


		}




		return v;
	}

}
