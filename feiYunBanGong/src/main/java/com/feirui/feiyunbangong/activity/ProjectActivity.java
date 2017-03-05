package com.feirui.feiyunbangong.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.fragment.FragmentProject1;
import com.feirui.feiyunbangong.fragment.FragmentProject2;
import com.feirui.feiyunbangong.view.PView;

/**
 * 项目
 * 
 * @author admina
 *
 */
public class ProjectActivity extends BaseActivity {
	@PView
	TextView text1, text2;
	// 页面切换
	@PView
	ViewPager viewpager;
	private FragmentManager fm;
	private FragmentTransaction ft;
	// 进行中，已完成的fragment
	private Fragment FragmentP1, FragmentP2;
	private List<Fragment> fragments;

	// 当前显示的位置
	private int selectedPositon = 0;
	private int position = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_project);
		initView();
	}

	private void initView() {
		initTitle();
		setLeftDrawable(R.drawable.arrows_left);
		setCenterString("项目");
		setRightDrawable(R.drawable.jia);
		rightll.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(ProjectActivity.this,
						ProjectAddActivity.class));
				overridePendingTransition(R.anim.aty_zoomin, R.anim.aty_zoomout);
			}
		});
		// 初始化fragment
		FragmentP1 = new FragmentProject1();
		FragmentP2 = new FragmentProject2();
		fragments = new ArrayList<Fragment>();
		fragments.add(FragmentP1);
		fragments.add(FragmentP2);
		fm = getSupportFragmentManager();
		ft = fm.beginTransaction();
		ft.add(R.id.activity_select_p, fragments.get(0));
		ft.show(fragments.get(0));
		ft.commit();

		text1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				position = 0;
				setPager();
				// 点击了进行中
				text1.setBackgroundResource(R.drawable.hong);
				text1.setTextColor(getResources().getColor(R.color.ffffff));
				text2.setBackgroundResource(R.color.ffffff);
				text2.setTextColor(getResources().getColor(R.color.huise));
			}
		});

		text2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				position = 1;
				setPager();
				// 点击已完成
				text2.setBackgroundResource(R.drawable.hong);
				text2.setTextColor(getResources().getColor(R.color.ffffff));
				text1.setBackgroundResource(R.color.ffffff);
				text1.setTextColor(getResources().getColor(R.color.huise));
			}
		});
	}

	private void setPager() {
		if (position != selectedPositon) {

			ft = fm.beginTransaction();
			ft.hide(fragments.get(selectedPositon));
			if (!fragments.get(position).isAdded()) {
				ft.add(R.id.activity_select_p, fragments.get(position));
			}
			ft.show(fragments.get(position));
			ft.commit();
			selectedPositon = position;
		}

	}
}
