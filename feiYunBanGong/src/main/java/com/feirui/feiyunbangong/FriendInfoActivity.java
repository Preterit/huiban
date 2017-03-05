package com.feirui.feiyunbangong;

import com.feirui.feiyunbangong.activity.BaseActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

/**
 * 好友资料页面
 * 
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
		// Inflate the menu; this ads items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.friend_info, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
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
