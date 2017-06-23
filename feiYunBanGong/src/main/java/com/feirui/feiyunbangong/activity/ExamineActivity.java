package com.feirui.feiyunbangong.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.entity.JsonBean;
import com.feirui.feiyunbangong.utils.AsyncHttpServiceHelper;
import com.feirui.feiyunbangong.utils.JsonUtils;
import com.feirui.feiyunbangong.utils.UrlTools;
import com.feirui.feiyunbangong.view.PView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

/**
 * 审批
 * 
 * @author admina
 *
 */
public class ExamineActivity extends BaseActivity {

	public static int count = 0;
	// 待审批，操作记录，请假，报销，外出，付款，采购，其他
	@PView(click = "onClick")
	LinearLayout ll_record, ll_leave, ll_refund, ll_goOut, ll_payment, ll_purchase, ll_else;
	@PView(click = "onClick")
	RelativeLayout ll_wait;

	View view;
	TextView bar_num;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_examine);
		bar_num = (TextView)findViewById(R.id.bar_num_dsp);
		bar_num.setVisibility(view.GONE);
		loadData();
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

	public void loadData() {

		RequestParams params = new RequestParams();
		String url = UrlTools.url + UrlTools.APPROVAL_APPROVAL;
		Log.d("提示数字模块--审批URL", "url: " + url);
		AsyncHttpServiceHelper.post(url, params, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
				super.onSuccess(statusCode, headers, responseBody);

				JsonBean jsonBean = JsonUtils.getMessage(new String(responseBody));
				Log.d("获得待审批jsonBean", "onSuccess:" + jsonBean.toString());
				//获得待审批条目数量
				count = jsonBean.getInfor().size();
				if (count != 0) {
					//bar_num = (TextView) findViewById(R.id.bar_num);
					bar_num.setVisibility(view.VISIBLE);
					bar_num.setText(count + "");
				}
				Log.d("获得待审批条目数量", "count: " + count);
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		loadData();//
	}
}
