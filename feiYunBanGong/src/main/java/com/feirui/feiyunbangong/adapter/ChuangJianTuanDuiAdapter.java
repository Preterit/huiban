package com.feirui.feiyunbangong.adapter;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.dialog.BiaoQianDialog;
import com.feirui.feiyunbangong.entity.ChildItem;
import com.feirui.feiyunbangong.entity.JsonBean;
import com.feirui.feiyunbangong.state.AppStore;
import com.feirui.feiyunbangong.utils.HttpUtils.CallBack;
import com.feirui.feiyunbangong.utils.Utils;
import com.feirui.feiyunbangong.view.TextImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 创建团队的成员适配器：
 * 
 * @author feirui1
 *
 */
public class ChuangJianTuanDuiAdapter extends MyBaseAdapter<ChildItem> {
	private ArrayList<JsonBean> list1;
	private Context context;
	private List<String> strs = new ArrayList<>();

	public ChuangJianTuanDuiAdapter(LayoutInflater inflater,
			ArrayList<JsonBean> list1, Context context) {
		super(inflater);
		this.context = context;
		this.list1 = list1;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View v, ViewGroup parent) {

		final ViewHolder holder;
		if (v == null) {
			holder = new ViewHolder();
			v = mInflater.inflate(R.layout.lv_item_chuangjiantuandui, null);
			holder.tv_name = (TextView) v.findViewById(R.id.tv_name);
			holder.iv_head = (TextImageView) v.findViewById(R.id.iv_head);
			holder.iv_jianhao = (ImageView) v.findViewById(R.id.iv_jianhao);
			holder.tv_biaoqian = (TextView) v.findViewById(R.id.tv_biaoqian);
			v.setTag(holder);

			holder.tv_biaoqian.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					BiaoQianDialog biaoQian = new BiaoQianDialog("选择标签", list1,
							context, new CallBack() {
								@Override
								public void onRequestComplete(String result) {
									int pos = (int) holder.tv_biaoqian.getTag();
									strs.set(pos, result);
									holder.tv_biaoqian.setText(strs.get(pos));
								}
							});
					biaoQian.show();
				}
			});

		} else {
			holder = (ViewHolder) v.getTag();
		}
		final ChildItem item = (ChildItem) getItem(position);

		holder.tv_name.setText(item.getTitle());

		holder.tv_biaoqian.setTag(position);

		if (item.getPhone().equals(
				AppStore.user.getInfor().get(0).get("staff_mobile"))) {
			holder.iv_jianhao.setVisibility(View.INVISIBLE);
		} else {
			holder.iv_jianhao.setVisibility(View.VISIBLE);
		}

		if (!strs.get(position).equals("其他")) {
			holder.tv_biaoqian.setText(strs.get(position));
		} else {
			holder.tv_biaoqian.setText("其他");
		}

		// 图片上加字：
		String name = Utils.getPicTitle(item.getTitle());
		if (item.getMarkerImgId() == null
				|| "img/1_1.png".equals(item.getMarkerImgId())
				|| item.getMarkerImgId().equals("")) {
			holder.iv_head.setText(name);
		} else {
			ImageLoader.getInstance().displayImage(item.getMarkerImgId(),
					holder.iv_head);
		}

		holder.iv_jianhao.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				reduce(item);
			}
		});

		return v;
	}

	public void reduce(ChildItem item) {
		if (list.contains(item)) {
			list.remove(item);
			notifyDataSetChanged();
		}
	}

	class ViewHolder {
		TextView tv_name, tv_biaoqian;
		TextImageView iv_head;
		ImageView iv_jianhao;
	}

	public List<ChildItem> getList() {
		return list;
	}

	// 查重并累加：
	public void addList(List<ChildItem> items) {

		for (int i = 0; i < items.size(); i++) {
			boolean isHave = false;

			for (int j = 0; j < list.size(); j++) {
				if (list.get(j).getId().equals(items.get(i).getId())) {
					isHave = true;
					break;
				}
			}
			if (!isHave) {
				list.add(items.get(i));
			}
		}

		strs.removeAll(strs);
		for (int i = 0; i < list.size(); i++) {
			strs.add("其他");
		}

		notifyDataSetChanged();
	}

	public List<String> getTags() {

		return strs;
	}

}
