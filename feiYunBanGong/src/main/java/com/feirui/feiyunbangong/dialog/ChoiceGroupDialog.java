package com.feirui.feiyunbangong.dialog;

import java.util.ArrayList;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.feirui.feiyunbangong.R;

/**
 * 
 * @author feirui1
 *
 */
public class ChoiceGroupDialog extends MyBaseDialog {
	private Context context;
	private ArrayList<String> list;
	LinearLayout contentLayout;
	TextView titleview;
	String title;
	Adp1 adp1;
	ListView listView1;
	TextView textView, tv_show;
	CallBack listener;

	// private static ChoiceGroupDialog dialog;

	/*
	 * public static ChoiceGroupDialog getInstance(CallBack listener, Context
	 * context, ArrayList<String> list, String title) { if (dialog == null) {
	 * dialog = new ChoiceGroupDialog(listener, context, list, title); } return
	 * dialog; }
	 */

	public ChoiceGroupDialog(CallBack listener, Context context,
			ArrayList<String> list, String title) {
		super(context, R.style.mydialog); // 注意调这个父类的构造方法，给对话框设置一个样式！！！！！！
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.context = context;
		this.list = list;
		this.title = title;
		this.listener = listener;
		setContentView(R.layout.dialog_select_old);

		findView();
		InitData();

	}

	private void findView() {
		listView1 = (ListView) findViewById(R.id.lv_old_man);
		titleview = (TextView) findViewById(R.id.title);
		this.titleview.setText(title);
	}

	private void InitData() {
		adp1 = new Adp1(list, ChoiceGroupDialog.this.context);
		listView1.setAdapter(adp1);
		adp1.notifyDataSetChanged();
		listView1.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				listener.OnResultMsg(list.get(position));
				dismiss();
			}
		});
	}

	class Adp1 extends BaseAdapter {

		ArrayList<String> list;
		int select = 0;
		Context context;

		public Adp1(ArrayList<String> list, Context context) {
			super();
			this.list = list;
			this.context = context;
			inflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		LayoutInflater inflater;

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = inflater.inflate(
						R.layout.dialog_city_choose_item, null);
				textView = (TextView) convertView.findViewById(R.id.citytext);
				convertView.setTag(textView);
			} else {
				textView = (TextView) convertView.getTag();
			}
			textView.setText(list.get(position));
			return convertView;
		}

	}

	public interface CallBack {

		public void OnResultMsg(String res);

	}

}
