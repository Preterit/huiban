package com.feirui.feiyunbangong.entity;

import java.io.Serializable;

public class Friend implements Serializable{

	private String name; //名字
	private String phone;
	private String address;//地址
	private String head; //头像
	private String brithday;//出生年
	private String sex; //性别
	private String key1; //关键词
	private String key2;
	private String key3;
	private String distence;//距离
	private String shopUrl;//小店地址
	private String state;//是否是好友

	public String getBrithday() {
		return brithday;
	}

	public void setBrithday(String brithday) {
		this.brithday = brithday;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
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

	public String getDistence() {
		return distence;
	}

	public void setDistence(String distence) {
		this.distence = distence;
	}

	public String getShopUrl() {
		return shopUrl;
	}

	public void setShopUrl(String shopUrl) {
		this.shopUrl = shopUrl;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getHead() {
		return head;
	}
	public void setHead(String head) {
		this.head = head;
	}
	public Friend(String name, String phone, String address, String head) {
		super();
		this.name = name;
		this.phone = phone;
		this.address = address;
		this.head = head;
	}
	public Friend(String name,String address,String head,String sex,String birthday,
				  String key1,String key2,String key3,String distence,String shopUrl,String state){
		this.name = name;
		this.address = address;
		this.head = head;
		this.sex = sex;
		this.brithday = birthday;
		this.key1 = key1;
		this.key2 = key2;
		this.key3 = key3;
		this.distence = distence;
		this.shopUrl = shopUrl;
		this.state = state;
	}

	@Override
	public String toString() {
		return "Friend{" +
				"name='" + name + '\'' +
				", address='" + address + '\'' +
				", head='" + head + '\'' +
				", brithday='" + brithday + '\'' +
				", sex='" + sex + '\'' +
				", key1='" + key1 + '\'' +
				", key2='" + key2 + '\'' +
				", key3='" + key3 + '\'' +
				", distence='" + distence + '\'' +
				", shopUrl='" + shopUrl + '\'' +
				", state='" + state + '\'' +
				'}';
	}
}
