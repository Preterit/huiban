package com.feirui.feiyunbangong.entity;

public class MyUser {

	private String id;
	private String name;
	private String duty;
	private String head;
	private String department;
	private String sex;
	private String birthday;
	private String address;
	private String phone;

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
		return "MyUser [name=" + name + ", duty=" + duty + ", head=" + head
				+ ", department=" + department + ", sex=" + sex + ", birthday="
				+ birthday + ", address=" + address + ", phone=" + phone + "]";
	}

}
