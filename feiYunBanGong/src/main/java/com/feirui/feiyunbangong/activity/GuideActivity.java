package com.feirui.feiyunbangong.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.adapter.GuideAdapter;
import com.feirui.feiyunbangong.utils.ImageUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 新手指导页
 * 
 * @author admina
 *
 */
public class GuideActivity extends BaseActivity implements OnClickListener,
		OnPageChangeListener {

	private ViewPager vp;
	private GuideAdapter vpAdapter;
	private List<View> views;
	private Button button;

	// 引导图片资源
	private static final int[] pics = { R.drawable.guide4, R.drawable.guide5 ,R.drawable.guide6};

	// 底部小点图片
	private ImageView[] dots;

	// 记录当前选中位置
	private int currentIndex;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide);
		findView();
		addListener();

		views = new ArrayList<View>();

		LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);

		// 初始化引导图片列表
		for (int i = 0; i < pics.length; i++) {
			ImageView iv = new ImageView(this);
			iv.setLayoutParams(mParams);
			iv.setScaleType(ScaleType.FIT_XY);
			iv.setImageBitmap(ImageUtil.decodeSampledBitmapFromResource(
					getResources(), pics[i], 1000, 1500));
			views.add(iv);
		}
		vp = (ViewPager) findViewById(R.id.viewpager_guide);
		// 初始化Adapter
		vpAdapter = new GuideAdapter(views);
		vp.setAdapter(vpAdapter);
		// 绑定回调
		vp.setOnPageChangeListener(this);
		button = (Button) findViewById(R.id.button_guide);
		// 初始化底部小点
		initDots();

	}

	private void findView() {
		button = (Button) findViewById(R.id.button_guide);
	}

	private void addListener() {
		button.setOnClickListener(this);
	}

	private void initDots() {
		LinearLayout ll = (LinearLayout) findViewById(R.id.ll_guide);

		dots = new ImageView[pics.length];
		Log.e("欢迎页面", "pics.length"+pics.length );
		// 循环取得小点图片
		for (int i = 0; i < pics.length; i++) {
			Log.e("欢迎页面", "dots[i]"+dots[i] );
			dots[i] = (ImageView) ll.getChildAt(i);
			//dots[i].setEnabled(true);// 都设为灰色
			dots[i].setOnClickListener(this);
			dots[i].setTag(i);// 设置位置tag，方便取出与当前位置对应
		}

		currentIndex = 0;
		dots[currentIndex].setEnabled(false);// 设置为白色，即选中状态
	}

	/**
	 * 设置当前的引导页
	 */
	private void setCurView(int position) {
		if (position < 0 || position >= pics.length) {
			return;
		}

		vp.setCurrentItem(position);
	}

	/**
	 * 这只当前引导小点的选中
	 */
	private void setCurDot(int positon) {
		if (positon < 0 || positon > pics.length - 1 || currentIndex == positon) {
			return;
		}

		dots[positon].setEnabled(false);
		dots[currentIndex].setEnabled(true);

		currentIndex = positon;
	}

	// 当滑动状态改变时调用
	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	// 当当前页面被滑动时调用
	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	// 当新的页面被选中时调用
	@Override
	public void onPageSelected(int arg0) {
		// 设置底部小点选中状态
		setCurDot(arg0);
		if (arg0 == 2) {
			button.setVisibility(View.VISIBLE);
		} else {
			button.setVisibility(View.GONE);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_guide:
			startActivity(new Intent(GuideActivity.this, LoginActivity.class));
			overridePendingTransition(R.anim.aty_zoomin, R.anim.aty_zoomout);
			finish();
			break;
		}
	}

}
