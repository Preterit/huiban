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
	private String key4;
	private String key5;
	private String type;//共享与否
	private String position;//固定位置
	private String limit_position;//实时位置

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
				  String shop,String key1,String key2,String key3,String key4,String key5,
				  String type,String position,String limit_position) {
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
		this.key4 = key4;
		this.key5 = key5;
		this.type = type;
		this.position = position;
		this.limit_position = limit_position;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getLimit_position() {
		return limit_position;
	}

	public void setLimit_position(String limit_position) {
		this.limit_position = limit_position;
	}

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
				"id='" + id + '\'' +
				", name='" + name + '\'' +
				", duty='" + duty + '\'' +
				", head='" + head + '\'' +
				", department='" + department + '\'' +
				", sex='" + sex + '\'' +
				", birthday='" + birthday + '\'' +
				", address='" + address + '\'' +
				", phone='" + phone + '\'' +
				", shop='" + shop + '\'' +
				", key1='" + key1 + '\'' +
				", key2='" + key2 + '\'' +
				", key3='" + key3 + '\'' +
				", key4='" + key4 + '\'' +
				", key5='" + key5 + '\'' +
				", type='" + type + '\'' +
				", position='" + position + '\'' +
				", limit_position='" + limit_position + '\'' +
				'}';
	}
}
