package com.feirui.feiyunbangong.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.adapter.ChuangJianTuanDuiAdapter;
import com.feirui.feiyunbangong.adapter.GridAdapter;
import com.feirui.feiyunbangong.dialog.LoadingDialog;
import com.feirui.feiyunbangong.dialog.TianJiaBIaoQianDialog;
import com.feirui.feiyunbangong.dialog.TianJiaBIaoQianDialog.AlertCallBack1;
import com.feirui.feiyunbangong.entity.ChildItem;
import com.feirui.feiyunbangong.entity.JsonBean;
import com.feirui.feiyunbangong.state.AppStore;
import com.feirui.feiyunbangong.utils.T;
import com.feirui.feiyunbangong.utils.UrlTools;
import com.feirui.feiyunbangong.utils.Utils;
import com.feirui.feiyunbangong.utils.Utils.HttpCallBack;
import com.loopj.android.http.RequestParams;

/**
 * 创建团队：
 * 
 * @author feirui1
 *
 */

// TODO:添加一条自己的信息；

public class ChuangJianTuanDuiActivity extends BaseActivity implements
		OnClickListener {

	private ImageView iv_jia;
	private ListView lv_zuyuan;
	private ChuangJianTuanDuiAdapter adapter;
	private EditText et_name_tuandui;// 团队名称！
	private LinearLayout ll_add_chengyuan;// 添加成员；
	private Button bt_submit;
	private GridView gv;
	private GridAdapter adapter1;
	private ArrayList<JsonBean> list1 = new ArrayList<>();
	private List<TextView> tvs = new ArrayList<>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chuang_jian_tuan_dui);
		initView();
		setListener();
		setListView();
	}

	private void initView() {
		initTitle();
		setLeftDrawable(R.drawable.arrows_left);
		setCenterString("创建团队");
		setRightVisibility(false);
		gv = (GridView) findViewById(R.id.gridView);
		iv_jia = (ImageView) findViewById(R.id.iv_jia);
		lv_zuyuan = (ListView) findViewById(R.id.lv_zuyuan);
		et_name_tuandui = (EditText) findViewById(R.id.et_name_tuandui);
		ll_add_chengyuan = (LinearLayout) findViewById(R.id.ll_add_chengyuan);
		bt_submit = (Button) findViewById(R.id.bt_submit);
	}

	private void setListener() {
		ll_add_chengyuan.setOnClickListener(this);
		bt_submit.setOnClickListener(this);
		iv_jia.setOnClickListener(this);
	}

	private void setListView() {

		list1 = new ArrayList<JsonBean>();
		list1.add(new JsonBean("其他"));
		adapter1 = new GridAdapter(ChuangJianTuanDuiActivity.this, list1);
		gv.setAdapter(adapter1);

		adapter = new ChuangJianTuanDuiAdapter(getLayoutInflater(), list1,
				ChuangJianTuanDuiActivity.this);
		lv_zuyuan.setAdapter(adapter);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_add_chengyuan:
			Intent intent = new Intent(this, AddChengYuanActivity.class);
			startActivityForResult(intent, 200);
			overridePendingTransition(R.anim.aty_zoomin, R.anim.aty_zoomout);
			break;
		case R.id.bt_submit:
			submit();
			break;
		case R.id.iv_jia:// 加标签
			TianJiaBIaoQianDialog tianjia = new TianJiaBIaoQianDialog("新增标签",
					"添加", "例如：北京", ChuangJianTuanDuiActivity.this,
					new AlertCallBack1() {

						@Override
						public void onOK(final String name) {
							list1.add(new JsonBean(name));
							adapter1.notifyDataSetChanged();
							adapter.notifyDataSetChanged();
						}

						@Override
						public void onCancel() {
						}
					});
			tianjia.show();
			break;
		}
	}

	private void submit() {

		if (TextUtils.isEmpty(et_name_tuandui.getText().toString())) {
			Toast.makeText(this, "请输入团队名称！", 0).show();
			return;
		}
		String url = UrlTools.url + UrlTools.CHUANGIJAN_TUANDUI;

		StringBuffer sb01 = new StringBuffer();

		for (int i = 0; i < tvs.size(); i++) {
			sb01.append(tvs.get(i).getText());
			sb01.append(",");
		}

		Log.e("TAG", sb01.toString());

		RequestParams params = new RequestParams();
		List<ChildItem> list = adapter.getList();
		if (list.size() < 2) {
			Toast.makeText(this, "请至少选择两名成员！", 0).show();
			return;
		}
		StringBuffer sb = new StringBuffer();
		// 循环拼接添加成员id,每个id后加逗号
		for (int i = 0; i < list.size(); i++) {
			sb.append(list.get(i).getId());
			sb.append(",");
		}
		// 循环拼接标签名,每个标签后加逗号
		StringBuffer sb1 = new StringBuffer();
		for (int i = 0; i < list1.size(); i++) {
			sb1.append(list1.get(i).getMsg());
			sb1.append(",");
		}
		// 拼接每个成员对应的标签名：
		StringBuffer sb2 = new StringBuffer();
		for (int i = 0; i < adapter.getTags().size(); i++) {
			sb2.append(adapter.getTags().get(i));
			sb2.append(",");
		}

		params.put("team_name", et_name_tuandui.getText().toString());
		params.put("staff_id", sb.toString());
		params.put("tag_name", sb1.toString());
		params.put("tag_id", sb2.toString());

		Log.e("TAG", params.toString());

		Utils.doPost(LoadingDialog.getInstance(this), this, url, params,
				new HttpCallBack() {
					@Override
					public void success(JsonBean bean) {
						T.showShort(ChuangJianTuanDuiActivity.this,
								bean.getMsg());
						ChuangJianTuanDuiActivity.this.finish();
						overridePendingTransition(R.anim.aty_zoomin,
								R.anim.aty_zoomout);
					}

					@Override
					public void failure(String msg) {
						Toast.makeText(ChuangJianTuanDuiActivity.this, msg, 0)
								.show();
					}

					@Override
					public void finish() {

					}
				});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 200 && resultCode == 100) {
			@SuppressWarnings("unchecked")
			ArrayList<ChildItem> childs = (ArrayList<ChildItem>) data
					.getSerializableExtra("childs");
			HashMap<String, Object> hm = AppStore.user.getInfor().get(0);
			childs.add(
					0,
					new ChildItem(hm.get("staff_name") + "", hm
							.get("staff_head") + "", hm.get("staff_mobile")
							+ "", hm.get("id") + "", 0));
			if (childs != null && childs.size() > 0) {
				adapter.addList(childs);
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

}
