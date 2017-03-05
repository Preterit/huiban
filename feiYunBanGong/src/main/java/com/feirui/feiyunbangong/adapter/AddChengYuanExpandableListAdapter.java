package com.feirui.feiyunbangong.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.entity.ChildItem;
import com.feirui.feiyunbangong.entity.Group;
import com.feirui.feiyunbangong.myinterface.AllInterface.OnGroupStateChangedListener;
import com.feirui.feiyunbangong.utils.Utils;
import com.feirui.feiyunbangong.view.TextImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * ExpandListView的适配器，继承自BaseExpandableListAdapter
 *
 */
public class AddChengYuanExpandableListAdapter extends
		BaseExpandableListAdapter {

	private Context mContext;
	// 子项是一个map，key是group的id，每一个group对应一个ChildItem的list
	private Map<Integer, List<ChildItem>> childMap;
	private List<Group> groups;
	OnGroupStateChangedListener listener;

	public AddChengYuanExpandableListAdapter() {
	}

	public AddChengYuanExpandableListAdapter(List<Group> groups,
			Context context, Map<Integer, List<ChildItem>> childMap,
			OnGroupStateChangedListener listener) {
		this.groups = groups;
		this.mContext = context;
		this.childMap = childMap;
		this.listener = listener;
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

	ChildHolder childHolder;
	ChildItem childItem;

	/*
	 * Gets a View that displays the data for the given child within the given
	 * group
	 */
	@Override
	public View getChildView(final int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		childHolder = null;
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
		} else {
			childHolder = (ChildHolder) convertView.getTag();
		}
		childItem = childMap.get(groupPosition).get(childPosition);
		// 图片上加字：
		String name = Utils.getPicTitle(childItem.getTitle());
		if ("img/1_1.png".equals(childItem.getMarkerImgId())
				|| null == childItem.getMarkerImgId()) {
			childHolder.childImg.setText(name);
		} else {
			ImageLoader.getInstance().displayImage(childItem.getMarkerImgId(),
					childHolder.childImg);
		}

		childHolder.childText.setText(childItem.getTitle());
		childItem.setPosition(new int[] { groupPosition, childPosition });
		childHolder.rl_child.setTag(childItem);

		childHolder.rl_child.setBackgroundColor(Color.WHITE);

		for (int i = 0; i < groupPos.size(); i++) {
			if (groupPos.get(i) == groupPosition
					&& childPos.get(i) == childPosition) {
				childHolder.rl_child.setBackgroundColor(Color.YELLOW);
			}
		}

		return convertView;
	}

	public List<Integer> groupPos = new ArrayList<>();
	public List<Integer> childPos = new ArrayList<>();
	private List<String> location = new ArrayList<>();

	public List<Integer> getGp() {
		return groupPos;
	}

	public List<Integer> getCp() {
		return childPos;
	}

	public void setPos(int gp, int cp) {
		if (location.contains((gp + "" + cp))) {
			Object ob = gp;
			Object c = cp;
			// 需要转成Object否则当参数为Int时调remover(Integer int);方法；！！！
			groupPos.remove(ob);
			childPos.remove(c);
			location.remove(gp + "" + cp);
			notifyDataSetChanged();
			return;
		}
		groupPos.add(gp);
		childPos.add(cp);
		location.add(gp + "" + cp);
		notifyDataSetChanged();
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

	@Override
	public void onGroupCollapsed(int groupPosition) {
		listener.onGroupCollapsed(groupPosition);
	}

	@Override
	public void onGroupExpanded(int groupPosition) {
		listener.onGroupExpanded(groupPosition);
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

}
