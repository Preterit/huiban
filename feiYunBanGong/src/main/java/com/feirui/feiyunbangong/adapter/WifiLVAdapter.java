package com.feirui.feiyunbangong.adapter;

import com.feirui.feiyunbangong.R;

import android.graphics.Color;
import android.net.wifi.ScanResult;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class WifiLVAdapter extends MyBaseAdapter<ScanResult> {

	public WifiLVAdapter(LayoutInflater inflater) {
		super(inflater);
	}

	@Override
	public View getView(int position, View v, ViewGroup parent) {

		ViewHolder holder = null;
		if (v == null) {
			v = mInflater.inflate(R.layout.wifi_lv_item, null);
			holder = new ViewHolder();
			holder.tv_chanle = (TextView) v.findViewById(R.id.tv_chanle);
			holder.tv_name = (TextView) v.findViewById(R.id.tv_name);
			holder.tv_strength = (TextView) v.findViewById(R.id.tv_strength);
			v.setTag(holder);
		} else {
			holder = (ViewHolder) v.getTag();
		}
		ScanResult result = (ScanResult) getItem(position);
		String name = result.SSID;
		int chanel = getChannelByFrequency(result.frequency);
		int strength = result.level;

		holder.tv_name.setTextColor(Color.parseColor(colors[position]));
		holder.tv_chanle.setTextColor(Color.parseColor(colors[position]));
		holder.tv_strength.setTextColor(Color.parseColor(colors[position]));

		holder.tv_name.setText("名称：" + name);
		holder.tv_chanle.setText("信道：" + chanel);
		holder.tv_strength.setText("强度：" + strength);

		return v;
	}

	class ViewHolder {
		TextView tv_name, tv_chanle, tv_strength;
	}

	private int getChannelByFrequency(int frequency) {
		int channel = 1;
		switch (frequency) {
		case 2412:
			channel = 1;
			break;
		case 2417:
			channel = 2;
			break;
		case 2422:
			channel = 3;
			break;
		case 2427:
			channel = 4;
			break;
		case 2432:
			channel = 5;
			break;
		case 2437:
			channel = 6;
			break;
		case 2442:
			channel = 7;
			break;
		case 2447:
			channel = 8;
			break;
		case 2452:
			channel = 9;
			break;
		case 2457:
			channel = 10;
			break;
		case 2462:
			channel = 11;
			break;
		case 2467:
			channel = 12;
			break;
		case 2472:
			channel = 13;
			break;
		}
		return channel;
	}

	// 表示各个信道的颜色值
	public String[] colors = new String[] { "#ff784a", "#8fc31f", "#ae5da1",
			"#00a0e9", "#FF00FF", "#f19ec2", "#cfa972" };
}
