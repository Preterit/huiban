package com.feirui.feiyunbangong.entity;

public class SetGuanLiYuan {

	private String id;
	private String name;
	private String head;
	private boolean isGuanLiYuan;
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
	public boolean isGuanLiYuan() {
		return isGuanLiYuan;
	}
	public void setGuanLiYuan(boolean isGuanLiYuan) {
		this.isGuanLiYuan = isGuanLiYuan;
	}
	@Override
	public String toString() {
		return "SetGuanLiYuan [id=" + id + ", name=" + name + ", head=" + head
				+ ", isGuanLiYuan=" + isGuanLiYuan + "]";
	}
	public SetGuanLiYuan(String id, String name, String head,
			boolean isGuanLiYuan) {
		super();
		this.id = id;
		this.name = name;
		this.head = head;
		this.isGuanLiYuan = isGuanLiYuan;
	}
	
	
	
	public SetGuanLiYuan() {
	}
	
	
	
}
