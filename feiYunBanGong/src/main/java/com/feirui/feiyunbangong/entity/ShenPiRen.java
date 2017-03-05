package com.feirui.feiyunbangong.entity;

import java.io.Serializable;


public class ShenPiRen implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private String name;
	private String head;
	private String department;
	
	public ShenPiRen() {
		// TODO Auto-generated constructor stub
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
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
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public ShenPiRen(String name, String head, String department) {
		super();
		this.name = name;
		this.head = head;
		this.department = department;
	}
	public ShenPiRen(int id, String name, String head, String department) {
		super();
		this.id = id;
		this.name = name;
		this.head = head;
		this.department = department;
	}
	@Override
	public String toString() {
		return "ShenPiRen [id=" + id + ", name=" + name + ", head=" + head
				+ ", department=" + department + "]";
	}
	
	
	
	
}
