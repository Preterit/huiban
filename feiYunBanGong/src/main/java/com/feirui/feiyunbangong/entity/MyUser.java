package com.feirui.feiyunbangong.entity;

import java.io.Serializable;

public class MyUser implements Serializable {

	private String id;
	private String name;
	private String duty;
	private String head;
	private String department;
	private String sex;
	private String birthday;
	private String address;
	private String phone;
	private String shop;
	private String key1;
	private String key2;
	private String key3;

	public MyUser(String id, String name, String duty, String head,
			String department, String sex, String birthday, String address,
			String phone) {
		super();
		this.id = id;
		this.name = name;
		this.duty = duty;
		this.head = head;
		this.department = department;
		this.sex = sex;
		this.birthday = birthday;
		this.address = address;
		this.phone = phone;
	}

	public MyUser(String name, String head,
				  String sex, String birthday, String address, String phone,
				  String shop,String key1,String key2,String key3) {
		super();
		this.name = name;
		this.head = head;
		this.sex = sex;
		this.birthday = birthday;
		this.address = address;
		this.phone = phone;
		this.shop = shop;
		this.key1 = key1;
		this.key2 = key2;
		this.key3 = key3;
	}

	public String getShop() {
		return shop;
	}

	public void setShop(String shop) {
		this.shop = shop;
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

	public String getDuty() {
		return duty;
	}

	public void setDuty(String duty) {
		this.duty = duty;
	}

	public String getHead() {
		return head;
	}

	public void setHead(String head) {
		this.head = head;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
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

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public MyUser(String name, String duty, String head, String department,
			String sex, String birthday, String address, String phone) {
		super();
		this.name = name;
		this.duty = duty;
		this.head = head;
		this.department = department;
		this.sex = sex;
		this.birthday = birthday;
		this.address = address;
		this.phone = phone;
	}

	@Override
	public String toString() {
		return "MyUser{" +
				"name='" + name + '\'' +
				", head='" + head + '\'' +
				", sex='" + sex + '\'' +
				", birthday='" + birthday + '\'' +
				", address='" + address + '\'' +
				", phone='" + phone + '\'' +
				", shop='" + shop + '\'' +
				", key1='" + key1 + '\'' +
				", key2='" + key2 + '\'' +
				", key3='" + key3 + '\'' +
				'}';
	}
}
