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
	private String tag;// 成员标签与关键词是一样的 现在用的是关键词
	private String tag_name;
	private String store_url;//小店地址
	private String remark;// 备注；
	private int friendstate;// 是否是好友；0表示不是好友1表示是好友
	private String t_remark;// 团队备注名；
	private int state;// 1.是新的成员，2.是老的成员；
	private String team_member_list_id;// 团队成员改变状态的id;
	private String sex;  //性别
	private String birthday; //生日
	private String address; //地区
	private String key1; //关键词
	private String key2;
	private String key3;
	private String key4;
	private String key5;

	public String getKey4() {
		return key4;
	}

	public void setKey4(String key4) {
		this.key4 = key4;
	}

	public String getKey5() {
		return key5;
	}

	public void setKey5(String key5) {
		this.key5 = key5;
	}

	public String getKey1() {
		return key1;
	}

	public void setKey1(String key1) {
		this.key1 = key1;
	}

	public String getKey2() {
		return key2;
	}

	public void setKey2(String key2) {
		this.key2 = key2;
	}

	public String getKey3() {
		return key3;
	}

	public void setKey3(String key3) {
		this.key3 = key3;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

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
    public TuanDuiChengYuan(String id, String name, String head,String phone) {
        super();
        this.id = id;
        this.name = name;
        this.head = head;
        this.phone = phone;
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
	public String getStore_url() {
		return store_url;
	}

	public void setStore_url(String store_url) {
		this.store_url = store_url;
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
			String head, String type, String phone, String email,
			String tag,String introduction,String store_url,String sex,
			String birthday,String address,String key1,String key2,String key3,String key4,String key5) {
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
		this.store_url = store_url;
		this.sex = sex;
		this.birthday = birthday;
		this.address = address;
		this.key1 = key1;
		this.key2 = key2;
		this.key3 = key3;
		this.key4 = key4;
		this.key5 = key5;
	}

	public TuanDuiChengYuan( String name, String head, String phone,String store_url,String sex,
							 String birthday,String address,String key1,String key2,String key3,String key4,String key5,int friendstate) {
		super();
		this.name = name;
		this.head = head;
		this.phone = phone;
		this.store_url = store_url;
		this.sex = sex;
		this.birthday = birthday;
		this.address = address;
		this.key1 = key1;
		this.key2 = key2;
		this.key3 = key3;
		this.key4 = key4;
		this.key5 = key5;
		this.friendstate = friendstate;
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

	public TuanDuiChengYuan() {
	}

	@Override
	public String toString() {
		return "TuanDuiChengYuan{" +
				"id='" + id + '\'' +
				", staff_id='" + staff_id + '\'' +
				", name='" + name + '\'' +
				", introduction='" + introduction + '\'' +
				", head='" + head + '\'' +
				", type='" + type + '\'' +
				", phone='" + phone + '\'' +
				", email='" + email + '\'' +
				", tag='" + tag + '\'' +
				", tag_name='" + tag_name + '\'' +
				", store_url='" + store_url + '\'' +
				", remark='" + remark + '\'' +
				", friendstate=" + friendstate +
				", t_remark='" + t_remark + '\'' +
				", state=" + state +
				", team_member_list_id='" + team_member_list_id + '\'' +
				", sex='" + sex + '\'' +
				", birthday='" + birthday + '\'' +
				", address='" + address + '\'' +
				", key1='" + key1 + '\'' +
				", key2='" + key2 + '\'' +
				", key3='" + key3 + '\'' +
				", key4='" + key4 + '\'' +
				", key5='" + key5 + '\'' +
				'}';
	}
}
