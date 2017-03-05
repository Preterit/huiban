package com.feirui.feiyunbangong.entity;

public class User {

	private String id;
	private String username;
	private String phone;
	private String head;
	
	

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getHead() {
		return head;
	}

	public void setHead(String head) {
		this.head = head;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + "]";
	}

	public User(String id, String username) {
		super();
		this.id = id;
		this.username = username;
	}

	public User() {
	}
	
	

}
