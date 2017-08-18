package com.feirui.feiyunbangong.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.alibaba.mobileim.YWIMKit;
import com.alibaba.mobileim.gingko.model.tribe.YWTribe;
import com.alibaba.mobileim.tribe.IYWTribeService;
import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.dialog.EtDialog;
import com.feirui.feiyunbangong.dialog.LoadingDialog;
import com.feirui.feiyunbangong.entity.JsonBean;
import com.feirui.feiyunbangong.entity.TuanDui;
import com.feirui.feiyunbangong.state.AppStore;
import com.feirui.feiyunbangong.utils.AsyncHttpServiceHelper;
import com.feirui.feiyunbangong.utils.JsonUtils;
import com.feirui.feiyunbangong.utils.L;
import com.feirui.feiyunbangong.utils.T;
import com.feirui.feiyunbangong.utils.UrlTools;
import com.feirui.feiyunbangong.utils.Utils;
import com.feirui.feiyunbangong.utils.Utils.HttpCallBack;
import com.feirui.feiyunbangong.view.PView;
import com.google.zxing.WriterException;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.zxing.encoding.EncodingHandler;

import org.apache.http.Header;

/**
 * 团队——加
 * 
 * @author admina
 *(团队退出按钮)
 */
public class TuanDuiJiaActivity extends BaseActivity implements OnClickListener {
	@PView(click = "onClick")
	LinearLayout ll_saoma, ll_tuiguang, ll_guanli, ll_send_msg,ll_send_talk;// 扫码，推广，管理,短信邀请；团队聊天
	private TuanDui td;
	private Button bt_out_team;//退出团队；

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tuan_dui_jia);
		initView();
		setListener();
	}

	private void setListener() {
		bt_out_team.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				out();
			}
		});
	}

	private void initView() {
		AppStore.acts.add(this);

		Intent intent = getIntent();
		//传过来的团队
		td = (TuanDui) intent.getSerializableExtra("td");
		bt_out_team=(Button) findViewById(R.id.bt_out_team);

		setManage(td);

		initTitle();
		setLeftDrawable(R.drawable.arrows_left);

		
		if (td.getName().length() > 10) {
			setCenterString(td.getName().substring(0, 9) + "...");
		} else {
			setCenterString(td.getName());
		}
		setRightVisibility(false);
	}

	// 设置管理员的显示与隐藏：
	private void setManage(TuanDui td) {
		if (td.getGuanli_id() != null
				&& String.valueOf(td.getGuanli_id()).equals(
						String.valueOf(AppStore.user.getInfor().get(0)
								.get("id")))) {
			ll_guanli.setVisibility(View.VISIBLE);
			bt_out_team.setVisibility(View.INVISIBLE);//管理员不显示退出团队；
		} else {
			bt_out_team.setVisibility(View.VISIBLE);
			ll_guanli.setVisibility(View.GONE);
		}
	}

	@SuppressLint("InflateParams")
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.ll_saoma:
			Dialog dialog = new Dialog(this);
			View v = getLayoutInflater().inflate(R.layout.ll_dialog_erweima,
					null);
			ImageView iv = (ImageView) v.findViewById(R.id.iv_erweima2);
			String id = "T" + td.getId();
			// 根据字符串生成二维码图片并显示在界面上，第二个参数为图片的大小（350*350）
			try {
				Bitmap erweima = EncodingHandler.createQRCode(id, 350);
				iv.setImageBitmap(erweima);
			} catch (WriterException e) {
				e.printStackTrace();
			}
			dialog.setContentView(v);
			dialog.setTitle("扫码加入团队");
			dialog.show();
			break;
		case R.id.ll_tuiguang:
			RequestParams params = new RequestParams();

			params.put("teamid", td.getId());
			String url = UrlTools.url + UrlTools.CIRCLE_ADDTEAMCIRCLE;
			L.e("推广——工作圈url" + url + " params" + params);
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
										T.showShort(TuanDuiJiaActivity.this,
												json.getMsg());
									}
								});

							} else {
								T.showShort(TuanDuiJiaActivity.this,
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

		case R.id.ll_guanli:  //团长管理团队 将整个团队传过去
			Intent intent = new Intent(this, TuanDuiGuanLiActivity.class);
			intent.putExtra("td", td);
			startActivityForResult(intent, 500);
			break;
		case R.id.ll_send_msg:

			EtDialog dialog1 = new EtDialog("短信邀请", "请输入手机号",
					TuanDuiJiaActivity.this, new EtDialog.AlertCallBack1() {

						@Override
						public void onOK(String name) {

							if (Utils.isPhone(name)) {

								String url = UrlTools.url + UrlTools.SEND_MSG;

								RequestParams params = new RequestParams();
								params.put("staff_mobile", name);
								params.put("teamnum", td.getId());
								Utils.doPost(LoadingDialog
										.getInstance(TuanDuiJiaActivity.this),
										TuanDuiJiaActivity.this, url, params,
										new HttpCallBack() {

											@Override
											public void success(JsonBean bean) {
												T.showShort(
														TuanDuiJiaActivity.this,
														"发送成功！");
											}

											@Override
											public void failure(String msg) {
												T.showShort(
														TuanDuiJiaActivity.this,
														msg);
											}

											@Override
											public void finish() {
												// TODO Auto-generated method stub
												
											}
										});

							} else {

							}

						}

						@Override
						public void onCancel() {

						}
					});

			dialog1.show();

			break;
        case R.id.ll_send_talk:
            YWIMKit mIMKit = AppStore.mIMKit;
            IYWTribeService tribeService = mIMKit.getTribeService();  //获取群管理器;

//            YWTribe tribe = tribeService.getAllTribes() ;
//            YWIMKit imKit = AppStore.mIMKit;
//            //参数为群ID号
//            Intent intent = imKit.getTribeChattingActivityIntent(tribe.getTribeId());
//            startActivity(intent);
            break;
		}
	}

	// 退出团队：
		private void out() {
			String url = UrlTools.url + UrlTools.OUT_TEAM;
			RequestParams params = new RequestParams();
			params.put("team_id", td.getId());
			Log.e("TAG", td.getId() + "td.getid()");
			Utils.doPost(LoadingDialog.getInstance(this), this, url, params,
					new HttpCallBack() {
						@Override
						public void success(JsonBean bean) {
							T.showShort(TuanDuiJiaActivity.this, "退出成功！");
							for(int i=0;i<AppStore.acts.size();i++){
								AppStore.acts.get(i).finish();
							}
						}
						@Override
						public void failure(String msg) {
							T.showShort(TuanDuiJiaActivity.this, msg);
						}
						@Override
						public void finish() {
							// TODO Auto-generated method stub
							
						}
					});

		}
	
	
	
	
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		// 已经移交了管理员：
		if (requestCode == 500 && resultCode == 100) {
			ll_guanli.setVisibility(View.GONE);
		}
		super.onActivityResult(requestCode, resultCode, intent);
	}

}