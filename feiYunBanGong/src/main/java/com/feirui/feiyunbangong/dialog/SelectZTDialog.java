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

/**
 * 
 */

/**
 * @author Lesgod
 *
 */
public class SelectZTDialog extends MyBaseDialog {

	ListView listview;

	/**
	 * @param context
	 */
	public SelectZTDialog(final Context context, String title,
			final ArrayList<String> list, final MyDailogCallback callback) {
		super(context, R.style.mydialog); // 注意调这个父类的构造方法，给对话框设置一个样式！！！！！！

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.selelct_zt_dialog);
		TextView text = (TextView) findViewById(R.id.textView1);
		listview = (ListView) findViewById(R.id.listView1);
		text.setText(title);
		listview.setAdapter(new BaseAdapter() {

			@Override
			public View getView(int arg0, View arg1, ViewGroup arg2) {
				// TODO Auto-generated method stub

				final TextView textView;

				if (arg1 == null) {

					arg1 = LayoutInflater.from(context).inflate(
							R.layout.selelct_zt_dialog_text, null);
					textView = (TextView) arg1.findViewById(R.id.textView1);
					arg1.setTag(textView);
				} else {
					textView = (TextView) arg1.getTag();
				}

				textView.setText(list.get(arg0));

				return arg1;
			}

			@Override
			public long getItemId(int arg0) {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public Object getItem(int arg0) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return list.size();
			}
		});

		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				callback.onOK(list.get(arg2));
				dismiss();

			}
		});

	}

	public interface MyDailogCallback {

		void onOK(String s);

		void onCancel();

	}
}
