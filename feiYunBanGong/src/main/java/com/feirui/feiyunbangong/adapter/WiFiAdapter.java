package com.feirui.feiyunbangong.adapter;

import com.feirui.feiyunbangong.R;

import android.graphics.Color;
import android.net.wifi.ScanResult;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class WiFiAdapter extends MyBaseAdapter<ScanResult> {

	private ScanResult item = null;
	private ViewHolder holder = null;

	public WiFiAdapter(LayoutInflater inflater) {
		super(inflater);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.adapter_wifi, null);
			holder = new ViewHolder();
			holder.tv = (TextView) convertView.findViewById(R.id.tv_item);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		item = (ScanResult) getItem(position);
		holder.tv.setText("");
		holder.tv.setText("设备名称：" + item.SSID + "  信道"
				+ getChannelByFrequency(item.frequency) + "  信号强度："
				+ item.level);

		holder.tv.setTextColor(Color
				.parseColor(colors[getChannelByFrequency(item.frequency) - 1]));
		return convertView;
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

	class ViewHolder {
		TextView tv;

	}

	// 表示各个信道的颜色值
	public String[] colors = new String[] { "#5389B7", "#20ADC8", "#001CC4",
			"#7839A2", "#DF5348", "#E6471F", "#009900", "#CB9836", "#A1F8FF",
			"#FFCC00", "#000000", "#7DD43C", "#177617", "#917A0F" };
}
