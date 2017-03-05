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
import android.widget.ListView;
import android.widget.TextView;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.entity.JsonBean;
import com.feirui.feiyunbangong.utils.HttpUtils.CallBack;

/**
 * 
 * @author feirui1
 *
 */
public class BiaoQianDialog extends MyBaseDialog {
	private Context context;
	private ArrayList<JsonBean> list;
	private TextView tv_title, textView;
	ListView listView1;
	Adp1 adp1;

	public BiaoQianDialog(String title, final ArrayList<JsonBean> list,
			Context context, final CallBack callback) {
		super(context); // 注意调这个父类的构造方法，给对话框设置一个样式！！！！！！

		requestWindowFeature(Window.FEATURE_NO_TITLE);// 无标题：
		setContentView(R.layout.dialog_select_old);
		this.context = context;
		this.list = list;
		tv_title = (TextView) findViewById(R.id.title);
		tv_title.setText(title);
		listView1 = (ListView) findViewById(R.id.lv_old_man);
		adp1 = new Adp1(list, BiaoQianDialog.this.context);
		listView1.setAdapter(adp1);
		adp1.notifyDataSetChanged();
		listView1.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				callback.onRequestComplete(list.get(position).getMsg());
				dismiss();
			}
		});
	}

	class Adp1 extends BaseAdapter {

		ArrayList<JsonBean> list;
		int select = 0;
		Context context;

		public Adp1(ArrayList<JsonBean> list, Context context) {
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
			textView.setText(list.get(position).getMsg());
			return convertView;
		}

	}
}
