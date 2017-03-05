package com.feirui.feiyunbangong.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.adapter.AddShenHeAdapter;
import com.feirui.feiyunbangong.dialog.SelectZTDialog;
import com.feirui.feiyunbangong.entity.AddShenHe;
import com.feirui.feiyunbangong.entity.ShenPiRen;
import com.feirui.feiyunbangong.utils.AsyncHttpServiceHelper;
import com.feirui.feiyunbangong.utils.BitmapToBase64;
import com.feirui.feiyunbangong.utils.DateTimePickDialogUtil;
import com.feirui.feiyunbangong.utils.DateTimePickDialogUtil.DialogCallBack;
import com.feirui.feiyunbangong.utils.FeiKongJianYaoUtil;
import com.feirui.feiyunbangong.utils.UrlTools;
import com.feirui.feiyunbangong.view.SelectPicPopupWindow;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class CaiGouActivity extends BaseActivity implements OnClickListener {

	private ListView lv_add;
	private ImageView iv_add, iv_add_pic_01, iv_add_pic_02, iv_add_pic_03;
	AddShenHeAdapter adapter;
	private ScrollView sv_caigou;
	private LinearLayout ll_add_mingxi;
	private TextView tv_add_mingxi, tv_title, tv_xuanze_zhifufangshi,
			tv_zhifufangshi, tv_qiwangriqi;
	private View v_add_mingxi;
	private SelectPicPopupWindow window;// 弹出图片选择框；
	private int select;// 点击上传图片位置；
	private Bitmap bitmap1;
	private Bitmap bitmap2;
	private Bitmap bitmap3;
	private Button btn_caigou_submit;// 提交采购信息按钮；

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cai_gou);
		initUI();
		setListener();
		setListView();
	}

	// 图片选择：
	public void toastChoicePic() {
		window = new SelectPicPopupWindow(this, this);
		// 显示窗口
		window.showAtLocation(findViewById(R.id.ll_caigou_main), Gravity.BOTTOM
				| Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置
	}

	int i = 0;

	@Override
	public void onClick(View v) {
		try {
			if (v.getId() == iv_add.getId()) {
				addShenPiRen();
			} else if (tv_add_mingxi.getId() == v.getId()) {
				addMingXi();// 添加明细；
			} else if (tv_title != null && tv_title.getId() == v.getId()) {
				ll_add_mingxi.removeView((View) v.getTag());// 移除明细；
			} else if (v.getId() == R.id.view_camero_rl_takephoto) {// 拍照；
				window.dismiss();
				paiZhao();
			} else if (v.getId() == R.id.view_camero_rl_selectphoto) {// 从相册选择；
				window.dismiss();
				xiangCe();
			} else if (v.getId() == btn_caigou_submit.getId()) {
				yanZhengSubmit();// 验证提交信息；
			} else if (v.getId() == tv_qiwangriqi.getId()) {
				showTimeDialog();
			} else if (v.getId() == tv_xuanze_zhifufangshi.getId()) {
				choicZhiFu();// 选择支付方式；
			} else {
				adapter.reduce(((AddShenHe) v.getTag()));// 删除审批人；
			}
		} catch (Exception e) {
			Log.i("TAG", e.getMessage() + "");
		}
	}

	private void choicZhiFu() {
		ArrayList<String> list = new ArrayList<>();
		list.add("支付宝");
		list.add("微信支付");
		list.add("快捷支付");
		SelectZTDialog dialog = new SelectZTDialog(this, "请选择支付方式", list,
				new SelectZTDialog.MyDailogCallback() {
					@Override
					public void onOK(String s) {
						tv_zhifufangshi.setText(s);
					}

					@Override
					public void onCancel() {
					}
				});
		dialog.show();
	}

	private void showTimeDialog() {
		// 点击了选择日期按钮
		DateTimePickDialogUtil dateTimePicker = new DateTimePickDialogUtil(
				this, "");
		dateTimePicker.dateTimePicKDialog(tv_qiwangriqi, new DialogCallBack() {
			@Override
			public void callBack() {
			}
		});
	}

	private List<EditText> ets = new ArrayList<>();
	private List<TextView> tvs = new ArrayList<>();

	private void yanZhengSubmit() {

		StringBuffer sb_name = new StringBuffer();
		StringBuffer sb_number = new StringBuffer();
		StringBuffer sb_price = new StringBuffer();

		int jianYan = FeiKongJianYaoUtil.jianYan(ets, tvs);
		switch (jianYan) {
		case 0:
			Toast.makeText(this, "请输入申请描述！", 0).show();
			return;
		case 1:
			Toast.makeText(this, "请输入采购类型！", 0).show();
			return;
		case 1000:
			Toast.makeText(this, "请输入期望日期！", 0).show();
			return;
		case 1001:
			Toast.makeText(this, "请选择支付方式！", 0).show();
			return;
		case 2:
			Toast.makeText(this, "请输入备注！", 0).show();
			return;
		}

		// 采购明细：
		for (int i = 0; i < ll_add_mingxi.getChildCount(); i++) {
			View v = ll_add_mingxi.getChildAt(i);
			EditText et_name = (EditText) v.findViewById(R.id.et_mingcheng);
			EditText et_number = (EditText) v.findViewById(R.id.et_number);
			EditText et_price = (EditText) v.findViewById(R.id.et_price);
			if (TextUtils.isEmpty(et_name.getText().toString().trim())) {
				Toast.makeText(this, "请输入采购名称！", 0).show();
				return;
			} else if (TextUtils.isEmpty(et_number.getText().toString().trim())) {
				Toast.makeText(this, "请输入采购数量！", 0).show();
				return;
			} else if (TextUtils.isEmpty(et_price.getText().toString().trim())) {
				Toast.makeText(this, "请输入采购价格！", 0).show();
				return;
			}
			sb_name.append(et_name.getText().toString().trim() + ",");
			sb_number.append(et_number.getText().toString().trim() + ",");
			sb_price.append(et_price.getText().toString().trim() + ",");
		}

		if (adapter.getCount() == 0) {
			Toast.makeText(this, "请选择审批人！", 0).show();
			return;
		}

		RequestParams params = new RequestParams();
		params.put("pur_describe", ets.get(0).getText().toString());
		params.put("pur_type", ets.get(1).getText().toString());
		params.put("pur_remarks", ets.get(2).getText().toString());
		params.put("pur_date", tvs.get(0).getText().toString());
		params.put("pur_pay_type", tvs.get(1).getText().toString());

		Log.i("TAG", params.toString() + "......................");
		StringBuffer sb_pic = new StringBuffer();
		if (bitmap1 != null) {
			sb_pic.append(BitmapToBase64.bitmapToBase64(bitmap1) + ",");
		}
		if (bitmap2 != null) {
			sb_pic.append(BitmapToBase64.bitmapToBase64(bitmap2) + ",");
		}
		if (bitmap3 != null) {
			sb_pic.append(BitmapToBase64.bitmapToBase64(bitmap3) + ",");
		}

		if (bitmap1 == null && bitmap2 == null && bitmap3 == null) {
			params.put("pic", "");
		} else {
			params.put("pic", sb_pic.deleteCharAt(sb_pic.length() - 1)
					.toString());
		}

		StringBuffer sb_id = new StringBuffer();
		for (int i = 0; i < adapter.getCount(); i++) {
			AddShenHe ash = (AddShenHe) adapter.getItem(i);
			sb_id.append(ash.getId() + ",");
		}
		params.put("approvers", sb_id.deleteCharAt(sb_id.length() - 1)
				.toString());

		params.put("pur_name", sb_name.deleteCharAt(sb_name.length() - 1)
				.toString());
		params.put("pur_number", sb_number.deleteCharAt(sb_number.length() - 1)
				.toString());
		params.put("pur_money", sb_price.deleteCharAt(sb_price.length() - 1)
				.toString());

		Log.i("TAG", params.toString());

		try {
			AsyncHttpServiceHelper.post(UrlTools.url
					+ UrlTools.ADD_CAIGOU_XINXI, params,
					new AsyncHttpResponseHandler() {
						@Override
						public void onSuccess(int arg0, Header[] arg1,
								byte[] arg2) {
							handler.sendEmptyMessage(0);
						}

						@Override
						public void onFailure(int arg0, Header[] arg1,
								byte[] arg2, Throwable arg3) {
							handler.sendEmptyMessage(1);
						}

					});
		} catch (Exception e) {
			Log.i("TAG", e.getMessage());
		}
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				Toast.makeText(CaiGouActivity.this, "提交成功！", 0).show();
				finish();
				overridePendingTransition(R.anim.aty_zoomclosein,
						R.anim.aty_zoomcloseout);
				break;
			case 1:
				Toast.makeText(CaiGouActivity.this, "提交失败！", 0).show();
				break;
			}
		};
	};

	// 从相册选择：
	private void xiangCe() {
		Intent i2 = new Intent(Intent.ACTION_PICK, null);
		i2.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				"image/*");
		startActivityForResult(i2, 1);
	}

	// 拍照：
	private void paiZhao() {
		Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment
				.getExternalStorageDirectory(), "caigou.jpg")));
		startActivityForResult(i, 2);
	}

	private void addMingXi() {
		v_add_mingxi = getLayoutInflater().inflate(R.layout.add_mingxi_caigou,
				null);
		tv_title = (TextView) v_add_mingxi.findViewById(R.id.tv_title);
		tv_title.setTag(v_add_mingxi);
		tv_title.setOnClickListener(this);
		ll_add_mingxi.addView(v_add_mingxi);
		tv_title.setText("删除明细");
		tv_title.setTextColor(Color.RED);
	}

	// 添加审批人：
	private void addShenPiRen() {
		Intent intent = new Intent(this, ShenPiRenActivity.class);
		startActivityForResult(intent, 101);// 请求码；
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		switch (requestCode) {
		case 101:
			ShenPiRen spr = (ShenPiRen) data.getSerializableExtra("shenpiren");
			if (spr.getId() == 0) {
				return;
			}
			AddShenHe ash = new AddShenHe(spr.getName(), spr.getId());
			adapter.add(ash);
			break;

		case 1:
			if (resultCode == Activity.RESULT_OK) {
				startPhotoZoom(data.getData());
			}
			break;

		case 2:
			if (resultCode == Activity.RESULT_OK) {
				File temp = new File(Environment.getExternalStorageDirectory()
						+ "/caigou.jpg");
				startPhotoZoom(Uri.fromFile(temp));
			}
			break;

		case 3:
			if (data != null) {
				Bundle extras = data.getExtras();
				if (extras != null) {
					switch (select) {
					case 0:
						bitmap1 = extras.getParcelable("data");
						iv_add_pic_01.setImageBitmap(bitmap1);
						iv_add_pic_02.setVisibility(View.VISIBLE);
						break;
					case 1:
						bitmap2 = extras.getParcelable("data");
						iv_add_pic_02.setImageBitmap(bitmap2);
						iv_add_pic_03.setVisibility(View.VISIBLE);
						break;
					case 2:
						bitmap3 = extras.getParcelable("data");
						iv_add_pic_03.setImageBitmap(bitmap3);
						break;
					}

				}
			}
			break;
		}
	};

	/**
	 * 调节图片大小工具
	 * 
	 * @param uri
	 */
	/*
	 * public void startPhotoZoom(Uri uri) { Intent intent = new
	 * Intent("com.android.camera.action.CROP"); intent.setDataAndType(uri,
	 * "image/*"); // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
	 * intent.putExtra("crop", "true"); // aspectX aspectY 是宽高的比例
	 * intent.putExtra("aspectX", 1); intent.putExtra("aspectY", 1); // outputX
	 * outputY 是裁剪图片宽高 intent.putExtra("outputX", 300);
	 * intent.putExtra("outputY", 300); intent.putExtra("return-data", true);
	 * startActivityForResult(intent, 3); }
	 */

	public void startPhotoZoom(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
		// intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("crop", "true");
		// // outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 250);
		intent.putExtra("outputY", 250);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, 3);
	}

	private void setListener() {
		iv_add.setOnClickListener(this);
		tv_qiwangriqi.setOnClickListener(this);
		tv_xuanze_zhifufangshi.setOnClickListener(this);
		lv_add.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				if (event.getAction() == MotionEvent.ACTION_UP) {
					sv_caigou.requestDisallowInterceptTouchEvent(false);
				} else {
					sv_caigou.requestDisallowInterceptTouchEvent(true);
				}
				return false;
			}
		});
		tv_add_mingxi.setOnClickListener(this);

		iv_add_pic_01.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				select = 0;
				toastChoicePic();
			}
		});
		iv_add_pic_02.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				select = 1;
				toastChoicePic();
			}
		});
		iv_add_pic_03.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				select = 2;
				toastChoicePic();
			}
		});

		btn_caigou_submit.setOnClickListener(this);

	}

	private void initUI() {
		initTitle();
		setLeftDrawable(R.drawable.arrows_left);
		setCenterString("采购");
		setRightVisibility(false);
		lv_add = (ListView) findViewById(R.id.lv_add_shenpiren);
		iv_add = (ImageView) findViewById(R.id.iv_add);
		sv_caigou = (ScrollView) findViewById(R.id.sv_caigou);
		tv_add_mingxi = (TextView) findViewById(R.id.tv_add_mingxi);
		ll_add_mingxi = (LinearLayout) findViewById(R.id.ll_add_mingxi);
		iv_add_pic_01 = (ImageView) findViewById(R.id.iv_add_pic_01);
		iv_add_pic_02 = (ImageView) findViewById(R.id.iv_add_pic_02);
		iv_add_pic_03 = (ImageView) findViewById(R.id.iv_add_pic_03);
		btn_caigou_submit = (Button) findViewById(R.id.btn_caigou_submit);

		ets.add((EditText) findViewById(R.id.et_shenqingmiaoshu));
		ets.add((EditText) findViewById(R.id.et_caigouleixing));
		ets.add((EditText) findViewById(R.id.et_beizhu));

		tvs.add((TextView) findViewById(R.id.tv_qiwangriqi));
		tvs.add((TextView) findViewById(R.id.tv_zhifufangshi));
		tv_qiwangriqi = (TextView) findViewById(R.id.tv_qiwangriqi);
		tv_zhifufangshi = (TextView) findViewById(R.id.tv_zhifufangshi);

		tv_xuanze_zhifufangshi = (TextView) findViewById(R.id.tv_xuanze_zhifufangshi);
	}

	private void setListView() {
		adapter = new AddShenHeAdapter(getLayoutInflater(), this);
		lv_add.setAdapter(adapter);
	}

}
