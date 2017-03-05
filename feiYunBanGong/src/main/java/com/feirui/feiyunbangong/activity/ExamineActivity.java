package com.feirui.feiyunbangong.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.view.PView;

/**
 * 审批
 * 
 * @author admina
 *
 */
public class ExamineActivity extends BaseActivity {
	// 待审批，操作记录，请假，报销，外出，付款，采购，其他
	@PView(click = "onClick")
	LinearLayout ll_wait, ll_record, ll_leave, ll_refund, ll_goOut, ll_payment,
			ll_purchase, ll_else;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_examine);
		initView();
	}

	private void initView() {
		initTitle();
		setLeftDrawable(R.drawable.arrows_left);
		setCenterString("审批");
		setRightVisibility(false);
	}

	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.ll_wait: // 待审批
			startActivity(new Intent(ExamineActivity.this,
					DaiShenPiActivity.class));
			break;
		case R.id.ll_record: // 操作记录
			startActivity(new Intent(ExamineActivity.this,
					CaoZuoJiLuActivity.class));
			break;
		case R.id.ll_leave: // 请假
			startActivity(new Intent(ExamineActivity.this,
					QingJiaActivity.class));
			break;
		case R.id.ll_refund: // 报销
			startActivity(new Intent(ExamineActivity.this,
					BaoXiaoActivity.class));
			break;
		case R.id.ll_goOut: // 外出
			startActivity(new Intent(ExamineActivity.this, WaiChuActivity.class));
			break;
		case R.id.ll_payment: // 付款
			startActivity(new Intent(ExamineActivity.this, FuKuanActivity.class));
			break;
		case R.id.ll_purchase: // 采购
			startActivity(new Intent(ExamineActivity.this, CaiGouActivity.class));
			break;
		case R.id.ll_else: // 其他
			startActivity(new Intent(ExamineActivity.this, QiTaActivity.class));
			break;
		}
		overridePendingTransition(R.anim.aty_zoomin, R.anim.aty_zoomout);
	}
}
