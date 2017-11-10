package com.feirui.feiyunbangong.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.adapter.AddShenHeUpdateAdapter;
import com.feirui.feiyunbangong.entity.ChildItem;
import com.feirui.feiyunbangong.entity.JsonBean;
import com.feirui.feiyunbangong.state.AppStore;
import com.feirui.feiyunbangong.utils.AsyncHttpServiceHelper;
import com.feirui.feiyunbangong.utils.BitmapToBase64;
import com.feirui.feiyunbangong.utils.DateTimePickDialogUtil;
import com.feirui.feiyunbangong.utils.DateTimePickDialogUtil.DialogCallBack;
import com.feirui.feiyunbangong.utils.JsonUtils;
import com.feirui.feiyunbangong.utils.T;
import com.feirui.feiyunbangong.utils.UrlTools;
import com.feirui.feiyunbangong.view.HorizontalListView;
import com.feirui.feiyunbangong.view.PView;
import com.feirui.feiyunbangong.view.SelectPicPopupWindow;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 审批-外出
 * 
 * @author admina
 *
 */
public class WaiChuActivity extends BaseActivity implements OnClickListener {
	@PView(click = "onClick")
	Button btn_submit;// 提交
	@PView(click = "onClick")
	TextView tv_kaishishijian, tv_jieshushijian;// 开始时间，结束时间
	@PView
	EditText et_time, et_shiyou;// 外出时间，外出事由
	private SelectPicPopupWindow window;// 弹出图片选择框；
	private int select;// 点击上传图片位置；
	private Bitmap bitmap1, bitmap2, bitmap3;
	// 添加审批人
	@PView(click = "onClick")
	ImageView iv_add, iv_01, iv_tupian1, iv_tupian2, iv_tupian3;
	private ArrayList<JsonBean> list1 ;

	@PView
	HorizontalListView lv_add_shenpiren;
	@PView
	HorizontalListView lv_add_chaosong;
//	AddShenHeAdapter adapter;
	AddShenHeUpdateAdapter adapter;
	AddShenHeUpdateAdapter adapter1;
	@PView
	ScrollView sv_caigou;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_waichu);
		initView();
	}

	private void initView() {
		initTitle();
		setLeftDrawable(R.drawable.arrows_left);
		setCenterString("外出");
		setRightVisibility(false);

//		adapter = new AddShenHeAdapter(getLayoutInflater(), WaiChuActivity.this);
		adapter = new AddShenHeUpdateAdapter(getLayoutInflater(),list1,WaiChuActivity.this);
		lv_add_shenpiren.setAdapter(adapter);
		lv_add_shenpiren.setOnTouchListener(new OnTouchListener() {

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
		adapter1 = new AddShenHeUpdateAdapter(getLayoutInflater(),list1,WaiChuActivity.this);
		lv_add_chaosong.setAdapter(adapter1);
		lv_add_chaosong.setOnTouchListener(new OnTouchListener() {

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
		iv_tupian1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				select = 0;
				toastChoicePic();
			}
		});
		iv_tupian2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				select = 1;
				toastChoicePic();
			}
		});
		iv_tupian3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				select = 2;
				toastChoicePic();
			}
		});
	}

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
			//去掉自己
			childs.remove(0);
			if (childs != null && childs.size() > 0) {
				adapter.addList(childs);
			}
			Log.d("mytag","添加人员："+childs.get(0).toString());
		}

		switch (requestCode) {
//		case 101:
//			ShenPiRen spr = (ShenPiRen) data.getSerializableExtra("shenpiren");
//			if (spr.getId() == 0) {
//				return;
//			}
//			AddShenHe ash = new AddShenHe(spr.getName(), spr.getId());
//			adapter.add(ash);
//			break;
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
						iv_tupian1.setImageBitmap(bitmap1);
						iv_tupian2.setVisibility(View.VISIBLE);
						break;
					case 1:
						bitmap2 = extras.getParcelable("data");
						iv_tupian2.setImageBitmap(bitmap2);
						iv_tupian3.setVisibility(View.VISIBLE);
						break;
					case 2:
						bitmap3 = extras.getParcelable("data");
						iv_tupian3.setImageBitmap(bitmap3);
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

	// 图片选择：
	public void toastChoicePic() {
		window = new SelectPicPopupWindow(this, this);
		// 显示窗口
		window.showAtLocation(findViewById(R.id.activity_waichu),
				Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置
	}

	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.view_camero_rl_takephoto:
			window.dismiss();
			Intent i1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			i1.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(
					Environment.getExternalStorageDirectory(), "caigou.jpg")));
			startActivityForResult(i1, 2);
			break;
		case R.id.view_camero_rl_selectphoto:
			window.dismiss();
			Intent i2 = new Intent(Intent.ACTION_PICK, null);
			i2.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
					"image/*");
			startActivityForResult(i2, 1);
			break;
		case R.id.iv_01:
//			adapter.reduce(((AddShenHe) view.getTag()));// 删除审批人；
			break;
		case R.id.iv_add:
			final Intent intent = new Intent(this, AddChengYuanActivity.class);
			startActivityForResult(intent, 200);
			overridePendingTransition(R.anim.aty_zoomin, R.anim.aty_zoomout);
//			startActivityForResult(intent, 101);// 请求码；
			break;
			case R.id.iv_add_chaosong:
				final Intent intent1 = new Intent(this, AddChengYuanActivity.class);
				startActivityForResult(intent1, 200);
				overridePendingTransition(R.anim.aty_zoomin, R.anim.aty_zoomout);
//			startActivityForResult(intent, 101);// 请求码；
				break;
		case R.id.tv_kaishishijian:
			// 点击了选择日期按钮
			DateTimePickDialogUtil kaishishijian = new DateTimePickDialogUtil(
					this, "");
			kaishishijian.dateTimePicKDialog(tv_kaishishijian,
					new DialogCallBack() {
						@Override
						public void callBack() {
						}
					});
			break;
		case R.id.tv_jieshushijian:
			// 点击了选择日期按钮
			DateTimePickDialogUtil jieshushijian = new DateTimePickDialogUtil(
					this, "");
			jieshushijian.dateTimePicKDialog(tv_jieshushijian,
					new DialogCallBack() {
						@Override
						public void callBack() {
						}
					});

			break;
		case R.id.btn_submit: // 提交

			RequestParams params = new RequestParams();

			params.put("out_start", tv_kaishishijian.getText().toString()
					.trim());
			params.put("out_end", tv_jieshushijian.getText().toString().trim());
			params.put("out_time", et_time.getText().toString().trim());
			params.put("out_reason", et_shiyou.getText().toString().trim());

			//从适配器中取出审批人集合
			List<ChildItem> shenPi = adapter.getList();
			StringBuffer sb_id = new StringBuffer();
			// 循环拼接添加成员id,每个id后加逗号
			for (int i = 0; i < shenPi.size(); i++) {
				sb_id.append(shenPi.get(i).getId());
				sb_id.append(",");
				Log.d("adapterTag","适配器上的数据"+sb_id);
			}

//			for (int i = 0; i < adapter.getCount(); i++) {
//				AddShenHe ash = (AddShenHe) adapter.getItem(i);
//				sb_id.append(ash.getId() + ",");
//			}
			params.put("approvers", sb_id.deleteCharAt(sb_id.length() - 1)
					.toString());
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
			String url = UrlTools.url + UrlTools.OUT_ADD_OUT;
			Log.e("审批-外出url", "url+params: "+url+params );
			AsyncHttpServiceHelper.post(url, params,
					new AsyncHttpResponseHandler() {
						@Override
						public void onSuccess(int arg0, Header[] arg1,
								byte[] arg2) {
							super.onSuccess(arg0, arg1, arg2);
							final JsonBean json = JsonUtils
									.getMessage(new String(arg2));
							if ("200".equals(json.getCode())) {
								runOnUiThread(new Runnable() {
									public void run() {
										T.showShort(WaiChuActivity.this,
												json.getMsg());
										finish();
										overridePendingTransition(
												R.anim.aty_zoomclosein,
												R.anim.aty_zoomcloseout);
									}
								});

							} else {

								Log.e("外出-提交失败", "json: "+json.toString() );
								T.showShort(WaiChuActivity.this, json.getMsg());
							}
						}

						@Override
						public void onFailure(int arg0, Header[] arg1,
								byte[] arg2, Throwable arg3) {
							super.onFailure(arg0, arg1, arg2, arg3);

						}
					});

			break;

		}
	}
}