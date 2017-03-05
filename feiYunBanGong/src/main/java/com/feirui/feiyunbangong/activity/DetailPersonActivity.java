package com.feirui.feiyunbangong.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.Header;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.entity.JsonBean;
import com.feirui.feiyunbangong.entity.MyUser;
import com.feirui.feiyunbangong.state.AppStore;
import com.feirui.feiyunbangong.utils.AsyncHttpServiceHelper;
import com.feirui.feiyunbangong.utils.BitmapToBase64;
import com.feirui.feiyunbangong.utils.JsonUtils;
import com.feirui.feiyunbangong.utils.T;
import com.feirui.feiyunbangong.utils.UrlTools;
import com.feirui.feiyunbangong.view.CircleImageView;
import com.feirui.feiyunbangong.view.SelectPicPopupWindow;
import com.google.zxing.WriterException;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zxing.encoding.EncodingHandler;

//个人信息：
@SuppressLint("InflateParams")
public class DetailPersonActivity extends BaseActivity implements
		OnClickListener {

	private ImageView iv_erweima;
	private CircleImageView iv_head;
	private List<EditText> ets = new ArrayList<>();
	private Button bt_xiugai;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail_person);
		initUI();
		setListener();
		shencheng();
	}

	private void shencheng() {
		if (AppStore.erweima != null) {
			iv_erweima.setImageBitmap(AppStore.erweima);
			return;
		}
		String phone = String.valueOf(AppStore.user.getInfor().get(0)
				.get("staff_mobile"));
		Log.e("TAG", phone);
		// 根据字符串生成二维码图片并显示在界面上，第二个参数为图片的大小（350*350）
		try {
			Bitmap erweima = EncodingHandler.createQRCode(phone, 350);
			iv_erweima.setImageBitmap(erweima);
			AppStore.erweima = erweima;
			Log.e("TAG", AppStore.erweima + "");
		} catch (WriterException e) {
			e.printStackTrace();
		}
	}

	private void setListener() {
		iv_erweima.setOnClickListener(this);
		bt_xiugai.setOnClickListener(this);
		iv_head.setOnClickListener(this);
	}

	private void initUI() {
		initTitle();
		setLeftDrawable(R.drawable.arrows_left);
		setCenterString("个人信息");
		setRightVisibility(false);
		iv_erweima = (ImageView) findViewById(R.id.iv_erweima);
		iv_head = (CircleImageView) findViewById(R.id.iv_head);
		ets.add((EditText) findViewById(R.id.et_name));
		ets.add((EditText) findViewById(R.id.et_phone));
		ets.add((EditText) findViewById(R.id.et_sex));
		ets.add((EditText) findViewById(R.id.et_birthday));
		ets.add((EditText) findViewById(R.id.et_city));

		bt_xiugai = (Button) findViewById(R.id.bt_xiugai);

		if (AppStore.user != null) {
			ets.get(0).setText(AppStore.myuser.getName());
			ets.get(1).setText(AppStore.myuser.getPhone());
			ets.get(2).setText(AppStore.myuser.getSex());
			ets.get(3).setText(AppStore.myuser.getBirthday());
			ets.get(4).setText(AppStore.myuser.getAddress());
		}
		if (AppStore.myuser.getHead() != null
				&& !"img/1_1.png".equals(AppStore.myuser.getHead())) {
			ImageLoader.getInstance().displayImage(AppStore.myuser.getHead(),
					iv_head);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_erweima:
			fangda();// 放大
			break;
		case R.id.bt_xiugai:
			xiugai();
			break;
		case R.id.iv_head:
			shangchuantouxiang();
			break;
		// 拍照；
		case R.id.view_camero_rl_takephoto:
			window.dismiss();
			paiZhao();
			break;
		// 从相册选择：
		case R.id.view_camero_rl_selectphoto:
			window.dismiss();
			xiangCe();
			break;
		}
	}

	// 修改：
	private void xiugai() {

		RequestParams params = new RequestParams();

		if (bitmap1 != null) {
			params.put("pic", BitmapToBase64.bitmapToBase64(bitmap1));
		}

		String name = ets.get(0).getText().toString();
		if (TextUtils.isEmpty(ets.get(0).getText())) {
			T.showShort(this, "请输入姓名！");
			return;
		}
		params.put("staff_name", name);

		String sex = ets.get(2).getText().toString();
		if (!TextUtils.isEmpty(sex)) {
			params.put("sex", sex);
		}
		String birthday = ets.get(3).getText().toString();
		if (!TextUtils.isEmpty(birthday)) {
			params.put("birthday", birthday);
		}
		String address = ets.get(4).getText().toString();
		if (!TextUtils.isEmpty(address)) {
			params.put("address", address);
		}

		String url = UrlTools.url + UrlTools.XIUGAI_GERENZILIAO;

		AsyncHttpServiceHelper.post(url, params,
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {

						final JsonBean bean = JsonUtils.getMessage(new String(
								arg2));
						runOnUiThread(new Runnable() {

							@Override
							public void run() {
								if ("200".equals(bean.getCode())) {
									T.showShort(DetailPersonActivity.this,
											"修改成功！");
									getUser();
								} else {
									T.showShort(DetailPersonActivity.this,
											bean.getMsg());
								}
							}
						});

						super.onSuccess(arg0, arg1, arg2);
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								T.showShort(DetailPersonActivity.this, "修改失败！");
							}
						});
						super.onFailure(arg0, arg1, arg2, arg3);
					}

				});

	}

	private void getUser() {
		Log.e("TAG", "开始获取用户信息.........");
		AsyncHttpServiceHelper.post(UrlTools.url + UrlTools.DETAIL_ME,
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						JsonBean bean = JsonUtils.getMessage(new String(arg2));
						if ("200".equals(bean.getCode())) {
							Message msg = new Message();
							msg.what = 0;
							msg.obj = bean;
							handler.sendMessage(msg);
						} else {
						}
						super.onSuccess(arg0, arg1, arg2);
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						super.onFailure(arg0, arg1, arg2, arg3);
					}
				});
	}

	private void fangda() {
		Dialog dialog = new Dialog(this);
		View v = getLayoutInflater().inflate(R.layout.ll_dialog_erweima, null);
		ImageView iv = (ImageView) v.findViewById(R.id.iv_erweima2);
		if (AppStore.erweima != null) {
			iv.setImageBitmap(AppStore.erweima);
		}
		dialog.setContentView(v);
		dialog.setTitle("扫我加好友");
		dialog.show();
	}

	private SelectPicPopupWindow window;// 弹出图片选择框；
	private Bitmap bitmap1;

	// 上传头像：
	private void shangchuantouxiang() {
		toastChoicePic();
	}

	// 图片选择：
	public void toastChoicePic() {
		window = new SelectPicPopupWindow(this, this);
		// 显示窗口
		window.showAtLocation(findViewById(R.id.ll_caigou_main), Gravity.BOTTOM
				| Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置
	}

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
					bitmap1 = extras.getParcelable("data");
					iv_head.setImageBitmap(bitmap1);
				}
			}
		}
	};

	Handler handler = new Handler() {
		@SuppressLint("HandlerLeak")
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				JsonBean bean = (JsonBean) msg.obj;
				ArrayList<HashMap<String, Object>> infor = bean.getInfor();
				HashMap<String, Object> hashMap = infor.get(0);
				String name = String.valueOf(hashMap.get("staff_name"));
				String duty = String.valueOf(hashMap.get("staff_duties"));
				String department = String.valueOf(hashMap
						.get("staff_department"));
				String head = String.valueOf(hashMap.get("staff_head"));
				String sex = String.valueOf(hashMap.get("sex"));
				String address = String.valueOf(hashMap.get("address"));
				String birthday = String.valueOf(hashMap.get("birthday"));
				String phone = String.valueOf(hashMap.get("staff_mobile"));

				MyUser user = new MyUser(name, duty, head, department, sex,
						birthday, address, phone);
				AppStore.myuser = user;
				break;
			}
		};
	};

}
