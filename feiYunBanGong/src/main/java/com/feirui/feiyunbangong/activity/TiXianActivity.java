package com.feirui.feiyunbangong.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.fragment.TiXian1Fragment;
import com.feirui.feiyunbangong.fragment.TiXian2Fragment;
import com.feirui.feiyunbangong.view.PView;

/**
 * 奖励提现
 * 
 * @author admina
 *
 */
public class TiXianActivity extends BaseActivity {
	@PView
	TextView text1, text2;
	@PView
	View view_1, view_2;
	// 页面切换
	@PView
	ViewPager viewpager;
	private FragmentManager fm;
	private FragmentTransaction ft;
	// 提现说明，提现记录的fragment
	private Fragment whFragment, yeFragment;
	private List<Fragment> fragments;

	// 当前显示的位置
	private int selectedPositon = 0;
	private int position = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tixian);
		initView();
	}

	private void initView() { // 设置头部
		initTitle();
		setLeftDrawable(R.drawable.arrows_left);
		setCenterString("奖励提现");
		setRightVisibility(false);
		// 初始化fragment
		whFragment = new TiXian1Fragment();
		yeFragment = new TiXian2Fragment();
		fragments = new ArrayList<Fragment>();
		fragments.add(whFragment);
		fragments.add(yeFragment);
		fm = getSupportFragmentManager();
		ft = fm.beginTransaction();
		ft.add(R.id.activity_select_content, fragments.get(0));
		ft.show(fragments.get(0));
		ft.commit();

		text1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				position = 0;
				setPager();
				// 点击提现说明
				view_1.setVisibility(View.VISIBLE);
				view_2.setVisibility(View.INVISIBLE);
			}
		});

		text2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				position = 1;
				setPager();
				view_1.setVisibility(View.INVISIBLE);
				view_2.setVisibility(View.VISIBLE);
			}
		});
	}

	private void setPager() {
		if (position != selectedPositon) {

			ft = fm.beginTransaction();
			ft.hide(fragments.get(selectedPositon));
			if (!fragments.get(position).isAdded()) {
				ft.add(R.id.activity_select_content, fragments.get(position));
			}
			ft.show(fragments.get(position));
			ft.commit();
			selectedPositon = position;
		}

	}
}
