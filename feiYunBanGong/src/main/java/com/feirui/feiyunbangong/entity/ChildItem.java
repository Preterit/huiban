package com.feirui.feiyunbangong.entity;

import java.io.Serializable;
import java.util.Arrays;

public class ChildItem implements Serializable {

	private String title;// 子项显示的文字 用户名
	private String markerImgId;// 每个子项的图标
	private String phone;
	private String id;// 子项id;
	private int type;// 0同事，1客户，2供应商；
	private int[] position;// 位置；
	private boolean isChoice;// 是否选中；
	private int person_id;// 好友在列表中的id;

	public ChildItem(String title, String markerImgId) {
		this.title = title;
		this.markerImgId = markerImgId;
	}

	public int getPerson_id() {
		return person_id;
	}

	public void setPerson_id(int person_id) {
		this.person_id = person_id;
	}

	public ChildItem() {
		// TODO Auto-generated constructor stub
	}

	public ChildItem(String title, String markerImgId, String phone, String id,
			int type) {
		super();
		this.title = title;
		this.markerImgId = markerImgId;
		this.phone = phone;
		this.id = id;
		this.type = type;
	}

	public boolean isChoice() {
		return isChoice;
	}

	public void setChoice(boolean isChoice) {
		this.isChoice = isChoice;
	}

	public int[] getPosition() {
		return position;
	}

	public void setPosition(int[] position) {
		this.position = position;
	}

	public ChildItem(String title, String markerImgId, String phone, String id) {
		super();
		this.title = title;
		this.markerImgId = markerImgId;
		this.phone = phone;
		this.id = id;
	}

	@Override
	public String toString() {
		return "ChildItem [title=" + title + ", markerImgId=" + markerImgId
				+ ", phone=" + phone + ", id=" + id + ", type=" + type
				+ ", position=" + Arrays.toString(position) + "]";
	}

	public ChildItem(String title, String markerImgId, String phone) {
		super();
		this.title = title;
		this.markerImgId = markerImgId;
		this.phone = phone;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMarkerImgId() {
		return markerImgId;
	}

	public void setMarkerImgId(String markerImgId) {
		this.markerImgId = markerImgId;
	}

}
