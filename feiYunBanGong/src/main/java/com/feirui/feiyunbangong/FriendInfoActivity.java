package com.feirui.feiyunbangong;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.feirui.feiyunbangong.activity.BaseActivity;

/**
 * 好友资料页面
 * @author rubing
 *
 */
public class FriendInfoActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friend_info);
		initView();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.friend_info, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void initView() {
		// TODO Auto-generated method stub
		initTitle();
		setLeftDrawable(R.drawable.arrows_left);
		setCenterString("好友资料");
		setRightVisibility(false);
	}
}
