package com.feirui.feiyunbangong.entity;

import java.io.Serializable;

public class Friend implements Serializable{

	private String name;
	private String phone;
	private String address;
	private String head;
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
	
}
