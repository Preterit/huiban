package com.feirui.feiyunbangong.dialog;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.feirui.feiyunbangong.R;

/**
 * 使用指南
 * 
 * @author feirui1
 *
 */
public class UseMessageDialog extends MyBaseDialog {

	Context context;
	// 抬头,内容,版本号
	TextView tv_title, tv_content, tv_banbenhao;

	public UseMessageDialog(final Context context) {
		super(context);
		this.context = context;

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		View view = getLayoutInflater().inflate(R.layout.dialog_score, null);
		setContentView(view);
		tv_title = (TextView) view.findViewById(R.id.tv_title);
		tv_banbenhao = (TextView) view.findViewById(R.id.tv_banbenhao);
		tv_banbenhao.setText("版本号：" + getVersionCode());
		tv_content = (TextView) view.findViewById(R.id.tv_content);
		// 设置抬头
		tv_title.setText("使用指南");
		// 设置内容
		tv_content
				.setText("\t\t北京飞锐科技有限公司是一家高新技术企业，为各类企事业单位提供互联网技术开发和信息化建设服务。公司总部位于北京市朝阳区望京科技园区。\n\t\t我司主要从事Android/iOS手机客户端、JAVA/PHP服务端、SQL系列数据库、HTML5、C/C#等方面的软硬件开发，提供电子商务、电子政务、移动办公、O2O、社交聊天、智能硬件、物联网、智慧应用等各种定制的软硬件开发和系统集成服务。\n\t\t在中国引领移动互联网浪潮的背景下，飞锐科技全情投入，助力信息化强国战略的实施。公司致力于“实业+互联网”的创造性发展，以技术改变世界，以创新成就商机。愿与各界精英一道，携手共创辉煌。\n\t\t会办APP是飞锐科技推出的个人关系管理平台，将为个人和企业提供第一手的信息化手段，让您的日常事务处理更加顺心顺手。平台广交天下友，招募运营合作伙伴，详情请联络：13366664598。");

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	/**
	 * 获取版本号改为版本名
	 * 
	 * @return
	 */
	public String getVersionCode() {
		PackageManager manager = context.getPackageManager();// 获取包管理器
		try {
			// 通过当前的包名获取包的信息
			PackageInfo info = manager.getPackageInfo(context.getPackageName(),
					0);// 获取包对象信息
			return info.versionName;
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
}
