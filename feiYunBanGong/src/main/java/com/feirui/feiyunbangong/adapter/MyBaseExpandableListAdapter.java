package com.feirui.feiyunbangong.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.entity.ChildItem;
import com.feirui.feiyunbangong.entity.Group;
import com.feirui.feiyunbangong.myinterface.AllInterface;
import com.feirui.feiyunbangong.utils.Utils;
import com.feirui.feiyunbangong.view.TextImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * ExpandListView的适配器，继承自BaseExpandableListAdapter
 *
 */
public class MyBaseExpandableListAdapter extends BaseExpandableListAdapter {

	private Context mContext;
	// 子项是一个map，key是group的id，每一个group对应一个ChildItem的list
	private Map<Integer, List<ChildItem>> childMap;
	private OnLongClickListener longListener;
	private OnClickListener clickListener;
	private AllInterface.OnGroupStateChangedListener groupChangerdListener;
	private List<Group> groups = new ArrayList<>();

	public MyBaseExpandableListAdapter() {
	}

	public MyBaseExpandableListAdapter(Context context, List<Group> groups,
			Map<Integer, List<ChildItem>> childMap,
			OnLongClickListener longListener, OnClickListener clickListener,
			AllInterface.OnGroupStateChangedListener changedListener) {

		this.mContext = context;
		this.groups = groups;
		this.childMap = childMap;
		this.longListener = longListener;
		this.clickListener = clickListener;
		this.groupChangerdListener = changedListener;
	}

	/*
	 * Gets the data associated with the given child within the given group
	 */
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// 我们这里返回一下每个item的名称，以便单击item时显示
		return childMap.get(groupPosition).get(childPosition);
	}

	/*
	 * 取得给定分组中给定子视图的ID. 该组ID必须在组中是唯一的.必须不同于其他所有ID（分组及子项目的ID）
	 */
	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	// 分组展开：
	@Override
	public void onGroupExpanded(int groupPosition) {
		groupChangerdListener.onGroupExpanded(groupPosition);
	}

	// 分组合上；
	@Override
	public void onGroupCollapsed(int groupPosition) {
		groupChangerdListener.onGroupCollapsed(groupPosition);
	}

	/*
	 * Gets a View that displays the data for the given child within the given
	 * group
	 */
	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		ChildHolder childHolder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.childitem, null);
			childHolder = new ChildHolder();
			childHolder.childImg = (TextImageView) convertView
					.findViewById(R.id.img_child);
			childHolder.childText = (TextView) convertView
					.findViewById(R.id.tv_child_text);
			childHolder.rl_child = (RelativeLayout) convertView
					.findViewById(R.id.rl_child);
			convertView.setTag(childHolder);
			//设置初始头像
			childHolder.childImg.setImageResource(R.drawable.acquiesce_in);
		} else {
			childHolder = (ChildHolder) convertView.getTag();
			//设置初始头像
			childHolder.childImg.setImageResource(R.drawable.acquiesce_in);
		}
		ChildItem childItem = childMap.get(groupPosition).get(childPosition);
		// 图片上加字：
		String name = getPicTitle(childItem);
		if ("img/1_1.png".equals(childItem.getMarkerImgId())) {
			childHolder.childImg.setText(name);
		} else {
			ImageLoader.getInstance().displayImage(childItem.getMarkerImgId(),
					childHolder.childImg);
		}

		childHolder.childText.setText(childItem.getTitle());
		childItem.setPosition(new int[] { groupPosition, childPosition });
		
		Object obj[]=new Object[]{childItem,groupPosition};
		childHolder.rl_child.setTag(obj);
		
		childHolder.rl_child.setOnLongClickListener(longListener);
		childHolder.rl_child.setOnClickListener(clickListener);
		return convertView;
	}

	// 获得无头像显示的内容：
	public String getPicTitle(ChildItem item) {

		String name = "";

		String title = item.getTitle();
		StringBuffer sb = new StringBuffer(title);

		if (title != null && title.length() > 2) {
			String s = sb.substring(sb.length() - 2, sb.length());
			sb.delete(0, sb.length());
			sb.append(s);
		}

		if (Utils.isPhone(title)) {
			name = "手机";
		} else {
			name = sb.toString();
		}

		return name;
	}

	/*
	 * 取得指定分组的子元素数
	 */
	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return childMap.get(groupPosition).size();
	}

	/**
	 * 取得与给定分组关联的数据
	 */
	@Override
	public Object getGroup(int groupPosition) {
		return groups.get(groupPosition);
	}

	/**
	 * 取得分组数
	 */
	@Override
	public int getGroupCount() {
		return groups.size();
	}

	/**
	 * 取得指定分组的ID.该组ID必须在组中是唯一的.必须不同于其他所有ID（分组及子项目的ID）
	 */
	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	/*
	 * Gets a View that displays the given groupreturn: the View corresponding
	 * to the group at the specified position
	 */
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		GroupHolder groupHolder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.groupitem, null);
			groupHolder = new GroupHolder();
			groupHolder.groupImg = (ImageView) convertView
					.findViewById(R.id.img_indicator);
			groupHolder.groupText01 = (TextView) convertView
					.findViewById(R.id.tv_group_text01);
			groupHolder.groupText02 = (TextView) convertView
					.findViewById(R.id.tv_group_text02);
			groupHolder.groupLeft = (ImageView) convertView
					.findViewById(R.id.img_left);
			convertView.setTag(groupHolder);
		} else {
			groupHolder = (GroupHolder) convertView.getTag();
		}

		if (isExpanded) {
			groupHolder.groupImg.setBackgroundResource(R.drawable.jiantou_xia);
		} else {
			groupHolder.groupImg.setBackgroundResource(R.drawable.jiantou_you);
		}

		Log.e("TAG", "getView()");
		Group group = (Group) getGroup(groupPosition);

		groupHolder.groupText02.setText("(" + group.getCount() + ")");

		groupHolder.groupText01.setText(group.getName());

		groupHolder.groupLeft.setImageResource(R.drawable.tongshi);

		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	/**
	 * show the text on the child and group item
	 */
	private class GroupHolder {
		ImageView groupImg, groupLeft;
		TextView groupText01, groupText02;
	}

	private class ChildHolder {
		RelativeLayout rl_child;
		TextImageView childImg;
		TextView childText;
	}

	// 更新map集合中的List<ChildItem>数据：
	public void add(List<ChildItem> ci, int groupId) {
		List<ChildItem> cis = childMap.get(groupId);
		cis.removeAll(cis);

		if (ci != null && ci.size() > 0) {
			cis.addAll(ci);
		}
		notifyDataSetChanged();
	}

}
