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
				.setText("\t\t北京飞锐科技有限公司是一家专业的互联网技术服务提供商，在APP开发、网站建设、网络通信等领域具有超越性的领先地位。核心团队毕业于清华、北大、北航等名校，大部分成员具有十余年的软硬件研发经验。公司总部位于北京市朝阳区。\n\t\t我司主要从事Android/iOS手机客户端、PHP/Java服务端、HTML5等方面的软件开发，在移动互联网领域具有完备的端到端研发能力，根据客户需求提供深度定制的移动应用解决方案。\n\t\t公司面向各类行业客户，提供电子商务、O2O、地图定位、社交聊天、移动办公、物联网、工业互联网、蓝牙互联、WiFi室内定位、企业专网等各种定制的软硬件技术服务。\n\t\t飞锐科技致力于移动互联网产业的创造性发展，我们是发动机，我们是催化剂，为各行各业的生产生活和商业形态提供源源不断的科技动力，帮助传统产业全面升级。\n\t\t有人说：“懒是人类进步的阶梯。”这里的“懒”不是“懒惰”，而是创造方便。移动互联网的发展就是带领人类进入更加便利、更加快捷的世界。在中国引领全球互联网+浪潮的大背景下，飞锐科技全情投入，助力互联网强国战略的实施。");

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	/**
	 * 获取版本号
	 * 
	 * @return
	 */
	public int getVersionCode() {
		PackageManager manager = context.getPackageManager();// 获取包管理器
		try {
			// 通过当前的包名获取包的信息
			PackageInfo info = manager.getPackageInfo(context.getPackageName(),
					0);// 获取包对象信息
			return info.versionCode;
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		return 0;
	}
}
