package com.feirui.feiyunbangong.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.feirui.feiyunbangong.dialog.DownLoadDialog;
import com.feirui.feiyunbangong.dialog.DownLoadDialog.DownLoadCallback;
import com.feirui.feiyunbangong.dialog.UpdateDialog;
import com.feirui.feiyunbangong.dialog.UpdateDialog.UpdataCallback;
import com.feirui.feiyunbangong.entity.JsonBean;
import com.feirui.feiyunbangong.utils.Utils.HttpCallBack;
import com.loopj.android.http.RequestParams;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

/**
 * @author coolszy
 * @date 2012-4-26
 * @blog http://blog.92coding.com
 */

public class UpdateManager {
	private static final int DOWNLOAD = 1;
	private static final int DOWNLOAD_FINISH = 2;
	HashMap<String, String> mHashMap;
	private String mSavePath;
	private int progress;
	private boolean cancelUpdate = false;

	private Activity mContext;
	private ProgressBar mProgress;
	private TextView tv_progress;

	private Dialog mDownloadDialog;
	private String path;
	private String content;

	private boolean canClick = true;
	private DownLoadDialog dialog;

	Handler han = new Handler();
	Runnable run = new Runnable() {
		public void run() {
			canClick = true;
		}
	};

	private IsUpdate update;

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {

			case DOWNLOAD:
				dialog.setProgress(progress);
				break;
			case DOWNLOAD_FINISH:
				installApk();
				break;
			default:
				break;
			}
		}
	};

	public UpdateManager(Activity context) {
		this.mContext = context;
	}

	boolean flag = false;

	/**
	 * 妫€镆ユ槸钖﹂渶瑕佹洿鏂帮细
	 *
	 * @return
	 */
	public void isUpdate(final IsUpdate iu) {
		this.update = iu;

		final String versionCode = String.valueOf(getVersionCode(mContext));

		final RequestParams params = new RequestParams();
		params.put("version", versionCode + "");
		params.put("type", 1 + "");
		Log.e("版本更新参数输出", params.toString());
		Utils.doPost(null, this.mContext, UrlTools.url + UrlTools.UPDATE_URL,
				params, new HttpCallBack() {
					@Override
					public void success(JsonBean bean) {
						path = bean.getInfor().get(0).get("address") + "";
						content = bean.getInfor().get(0).get("content") + "";
						update.canUpdate();
					}

					@Override
					public void failure(String msg) {
						update.canNotUpdate();
					}

					@Override
					public void finish() {
						Log.e("TAG", "finish()...................");
					}
				});
	}

	/**
	 *
	 * @param context
	 * @return
	 */
	private int getVersionCode(Context context) {
		int versionCode = 999;
		try {
			versionCode = context.getPackageManager().getPackageInfo(
					"com.feirui.feiyunbangong", 0).versionCode;
		} catch (Exception e) {
			Log.e("TAG", e.getMessage());
			return versionCode;
		}
		return versionCode;
	}

	/*
	 */
	public void showNoticeDialog() {
		UpdateDialog dialog = new UpdateDialog(mContext, new UpdataCallback() {
			@Override
			public void onOK() {
				if (!canClick) {
					return;
				}
				han.postDelayed(run, 6000);
				showDownloadDialog();
				canClick = false;
			}

			@Override
			public void onCancel() {
				update.cancel();
			}
		}, content);
		dialog.setCancelable(false);
		dialog.show();
	}

	/**
	 */
	private void showDownloadDialog() {
		dialog = new DownLoadDialog(mContext, new DownLoadCallback() {
			@Override
			public void onCancel() {
				cancelUpdate = true;
				update.cancel();
			}
		});
		dialog.setCancelable(false);
		dialog.show();

		downloadApk();
	}

	/**
	 */
	private void downloadApk() {
		new downloadApkThread().start();
	}

	/**
	 *
	 * @author coolszy
	 * @date 2012-4-26
	 * @blog http://blog.92coding.com
	 */
	private class downloadApkThread extends Thread {

		@Override
		public void run() {
			if (path == null) {
				return;
			}
			try {
				if (Environment.getExternalStorageState().equals(
						Environment.MEDIA_MOUNTED)) {
					String sdpath = Environment.getExternalStorageDirectory()
							+ "/";
					mSavePath = sdpath + "download";
					URL url = new URL(path);
					HttpURLConnection conn = (HttpURLConnection) url
							.openConnection();
					conn.connect();
					int length = conn.getContentLength();
					InputStream is = conn.getInputStream();

					File file = new File(mSavePath);
					if (!file.exists()) {
						file.mkdir();
					}
					File apkFile = new File(mSavePath, "fybg");
					FileOutputStream fos = new FileOutputStream(apkFile);
					int count = 0;
					byte buf[] = new byte[1024];
					do {
						int numread = is.read(buf);
						count += numread;
						progress = (int) (((float) count / length) * 100);
						mHandler.sendEmptyMessage(DOWNLOAD);
						if (numread <= 0) {
							mHandler.sendEmptyMessage(DOWNLOAD_FINISH);
							break;
						}
						fos.write(buf, 0, numread);
					} while (!cancelUpdate);

					fos.close();
					is.close();
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			dialog.dismiss();
		}
	};

	/**
	 * 阎庣僾憔抽ˉ澶綪K褒哄睬娲ｅ▎锟?
	 */
	private void installApk() {
		File apkfile = new File(mSavePath, "fybg");
		if (!apkfile.exists()) {
			return;
		}
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setDataAndType(Uri.parse("file://" + apkfile.toString()),
				"application/vnd.android.package-archive");
		mContext.startActivity(i);
	}

	public interface IsUpdate {

		public void canUpdate();

		public void canNotUpdate();

		public void complete();

		public void cancel();
	}

}
