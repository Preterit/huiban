package com.feirui.feiyunbangong.entity;

import java.io.Serializable;

public class TuanDuiChengYuan implements Serializable {

	/**
	 */
	private static final long serialVersionUID = 1L;
	private String id;// 列表中的id;
	private String staff_id;// 员工id
	private String name;
	private String introduction;
	private String head;
	private String type;
	private String phone;
	private String email;
	private String tag;// 成员标签；
	private String tag_name;
	private String remark;// 备注；
	private int friendstate;// 是否是好友；0表示不是好友1表示是好友
	private String t_remark;// 团队备注名；
	private int state;// 1.是新的成员，2.是老的成员；
	private String team_member_list_id;// 团队成员改变状态的id;

	public String getTeam_member_list_id() {
		return team_member_list_id;
	}

	public void setTeam_member_list_id(String team_member_list_id) {
		this.team_member_list_id = team_member_list_id;
	}

	public String getT_remark() {
		return t_remark;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public void setT_remark(String t_remark) {
		this.t_remark = t_remark;
	}

	public int getFriendstate() {
		return friendstate;
	}

	public void setFriendstate(int friendstate) {
		this.friendstate = friendstate;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * @return the tag_name
	 */
	public String getTag_name() {
		return tag_name;
	}

	/**
	 * @param tag_name
	 *            the tag_name to set
	 */
	public void setTag_name(String tag_name) {
		this.tag_name = tag_name;
	}

	public String getStaff_id() {
		return staff_id;
	}

	public void setStaff_id(String staff_id) {
		this.staff_id = staff_id;
	}

	public TuanDuiChengYuan(String id, String name, String head) {
		super();
		this.id = id;
		this.name = name;
		this.head = head;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHead() {
		return head;
	}

	public void setHead(String head) {
		this.head = head;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public TuanDuiChengYuan(String id, String staff_id, String name,
			String head, String type, String phone, String email,String introduction) {
		super();
		this.id = id;
		this.introduction=introduction;
		this.staff_id = staff_id;
		this.name = name;
		this.head = head;
		this.type = type;
		this.phone = phone;
		this.email = email;
	}

	public TuanDuiChengYuan(String id, String staff_id, String name,
			String head, String type, String phone, String email, String tag,String introduction) {
		super();
		this.id = id;
		this.staff_id = staff_id;
		this.introduction=introduction;
		this.name = name;
		this.head = head;
		this.type = type;
		this.phone = phone;
		this.email = email;
		this.tag = tag;
	}

	public TuanDuiChengYuan(String id, String name, String head, String type,
			String phone) {
		super();
		this.id = id;
		this.name = name;
		this.head = head;
		this.type = type;
		this.phone = phone;
	}

	@Override
	public String toString() {
		return "TuanDuiChengYuan [id=" + id + ", staff_id=" + staff_id
				+ ", name=" + name + ", head=" + head + ", type=" + type
				+ ", phone=" + phone + ", email=" + email + ", tag=" + tag
				+ ", tag_name=" + tag_name +", introduction="+introduction+ "]";
	}

	public TuanDuiChengYuan() {
	}


}
