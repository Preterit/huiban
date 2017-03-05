package com.feirui.feiyunbangong.entity;

import java.io.Serializable;

public class LianXiRen implements Serializable{

	private String name;
	private String phone;
	private String type;
	private String head;
	private String id;
	private String state;
	
	
	public LianXiRen(String name, String phone, String type, String head) {
		super();
		this.name = name;
		this.phone = phone;
		this.type = type;
		this.head = head;
	}
	
	
	public LianXiRen() {
	}
	


	public LianXiRen(String name, String phone, String type, String head,
			String id) {
		super();
		this.name = name;
		this.phone = phone;
		this.type = type;
		this.head = head;
		this.id = id;
	}



	public LianXiRen(String name, String phone, String type, String head,
			String id, String state) {
		super();
		this.name = name;
		this.phone = phone;
		this.type = type;
		this.head = head;
		this.id = id;
		this.state = state;
	}


	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}

	@Override
	public boolean equals(Object o) {
		// TODO Auto-generated method stub
		return super.equals(o);
	}





	@Override
	protected void finalize() throws Throwable {
		// TODO Auto-generated method stub
		super.finalize();
	}


	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return super.hashCode();
	}


	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getHead() {
		return head;
	}
	public void setHead(String head) {
		this.head = head;
	}


	public String getState() {
		return state;
	}


	public void setState(String state) {
		this.state = state;
	}
	
	
	
	
	
	
	
}
