package com.feirui.feiyunbangong.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.fragment.FragmentClient1;
import com.feirui.feiyunbangong.fragment.FragmentClient2;
import com.feirui.feiyunbangong.fragment.FragmentClient3;
import com.feirui.feiyunbangong.view.PView;

/**
 * 客户管理
 * 
 * @author admina
 *
 */
public class CustomerActivity extends BaseActivity {
	@PView(click = "onClick")
	TextView text1, text2, text3;
	// 页面切换
	@PView
	ViewPager viewpager;
	private FragmentManager fm;
	private FragmentTransaction ft;
	// 进行中，已完成，反馈的fragment
	private Fragment FragmentC1, FragmentC2, FragmentC3;
	private List<Fragment> fragments;

	// 当前显示的位置
	private int selectedPositon = 0;
	private int position = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_customer);
		initView();
	}

	private void initView() {
		initTitle();
		setLeftDrawable(R.drawable.arrows_left);
		setCenterString("客户管理");
		setRightVisibility(false);
		// 初始化fragment
		FragmentC1 = new FragmentClient1();
		FragmentC2 = new FragmentClient2();
		FragmentC3 = new FragmentClient3();
		fragments = new ArrayList<Fragment>();
		fragments.add(FragmentC1);
		fragments.add(FragmentC2);
		fragments.add(FragmentC3);
		fm = getSupportFragmentManager();
		ft = fm.beginTransaction();
		ft.add(R.id.activity_select_c, fragments.get(0));
		ft.show(fragments.get(0));
		ft.commit();

	}

	private void setPager() {
		if (position != selectedPositon) {

			ft = fm.beginTransaction();
			ft.hide(fragments.get(selectedPositon));
			if (!fragments.get(position).isAdded()) {
				ft.add(R.id.activity_select_c, fragments.get(position));
			}
			ft.show(fragments.get(position));
			ft.commit();
			selectedPositon = position;
		}

	}

	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.text1:
			position = 0;
			setPager();
			// 点击进行中
			text1.setBackgroundResource(R.drawable.jianbianhui);
			text2.setBackgroundResource(R.color.ffffff);
			text3.setBackgroundResource(R.color.ffffff);
			break;
		case R.id.text2:
			position = 1;
			setPager();
			// 点击已完成
			text1.setBackgroundResource(R.color.ffffff);
			text2.setBackgroundResource(R.drawable.jianbianhui);
			text3.setBackgroundResource(R.color.ffffff);
			break;
		case R.id.text3:
			position = 2;
			setPager();
			// 点击反馈
			text1.setBackgroundResource(R.color.ffffff);
			text2.setBackgroundResource(R.color.ffffff);
			text3.setBackgroundResource(R.drawable.jianbianhui);
			break;
		}
	}

}
