package com.feirui.feiyunbangong.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.entity.JsonBean;
import com.feirui.feiyunbangong.utils.AsyncHttpServiceHelper;
import com.feirui.feiyunbangong.utils.BitmapToBase64;
import com.feirui.feiyunbangong.utils.JsonUtils;
import com.feirui.feiyunbangong.utils.L;
import com.feirui.feiyunbangong.utils.T;
import com.feirui.feiyunbangong.utils.UrlTools;
import com.feirui.feiyunbangong.view.PView;
import com.feirui.feiyunbangong.view.SelectPicPopupWindow;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import java.io.File;

/**
 * 反馈-添加
 * 
 * @author admina
 *
 */
public class FeedbackAddActivity extends BaseActivity implements
		OnClickListener {
	@PView
	TextView tv_name;// 姓名
	@PView
	EditText et_1, et_2, et_3, et_4, et_5, et_6;// 公司名，职位，地址，联系电话，意向，说明
	@PView(click = "onClick")
	Button btn_submit;
	@PView(click = "onClick")
	ImageView iv_mingpian;
	private SelectPicPopupWindow window;// 弹出图片选择框；
	private Bitmap bitmap;
	String id = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feedback_add);
		initView();
	}

	private void initView() {
		id = getIntent().getStringExtra("id");
		tv_name.setText(getIntent().getStringExtra("kehu"));
		initTitle();
		setLeftDrawable(R.drawable.arrows_left);
		setCenterString("添加反馈");
		setRightVisibility(false);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		switch (requestCode) {
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
					bitmap = extras.getParcelable("data");
					iv_mingpian.setImageBitmap(bitmap);
					break;

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

	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.iv_mingpian: // 上传名片
			window = new SelectPicPopupWindow(this, this);
			// 显示窗口

			//隐藏软键盘
			((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
					hideSoftInputFromWindow(FeedbackAddActivity.this.getCurrentFocus().getWindowToken(),
							InputMethodManager.HIDE_NOT_ALWAYS);
			window.showAtLocation(findViewById(R.id.activity_feedback_add),
					Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置
			break;
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
		case R.id.btn_submit: // 提交

			RequestParams params = new RequestParams();

			params.put("customer_id", id);
			params.put("customer_company", et_1.getText().toString().trim());
			params.put("company_position", et_2.getText().toString().trim());
			params.put("customer_address", et_3.getText().toString().trim());
			params.put("customer_phone", et_4.getText().toString().trim());
			if (bitmap != null) {
				params.put("business_card",
						BitmapToBase64.bitmapToBase64(bitmap));
			}
			params.put("intention", et_5.getText().toString().trim());
			params.put("captions", et_6.getText().toString().trim());
			String url = UrlTools.url + UrlTools.CUSTOMER_CUSTOMER_FEEDBACK;
			L.e("反馈-添加url" + url + " params" + params);
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
										T.showShort(FeedbackAddActivity.this,
												json.getMsg());
										finish();
										overridePendingTransition(R.anim.aty_zoomin, R.anim.aty_zoomout);
									}
								});

							} else {
								T.showShort(FeedbackAddActivity.this,
										json.getMsg());
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
