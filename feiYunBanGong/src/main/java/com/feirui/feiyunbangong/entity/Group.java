package com.feirui.feiyunbangong.entity;

import java.io.Serializable;

public class Group implements Serializable {

	private int id;
	private String name;
	private int default_num;// 默认数，当为1时不可删除，可以修改名字，当为2时可以删除和修改
	private int count;//

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

	public int getDefault_num() {
		return default_num;
	}

	public void setDefault_num(int default_num) {
		this.default_num = default_num;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	@Override
	public String toString() {
		return "Group [id=" + id + ", name=" + name + ", default_num="
				+ default_num + ", count=" + count  + "]";
	}

	public Group(int id, String name, int default_num, int count) {
		super();
		this.id = id;
		this.name = name;
		this.default_num = default_num;
		this.count = count;
	}

	public Group() {
		// TODO Auto-generated constructor stub
	}

}
