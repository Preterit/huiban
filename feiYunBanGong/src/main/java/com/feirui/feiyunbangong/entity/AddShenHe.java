package com.feirui.feiyunbangong.entity;


public class AddShenHe {

	private String name1;
	private String id;
	
	
	public AddShenHe(String name1) {
		super();
		this.name1 = name1;
	}
	
	
	
	public AddShenHe(String name1, String id) {
		super();
		this.name1 = name1;
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public AddShenHe() {
	}
	
	public String getName1() {
		return name1;
	}
	public void setName1(String name1) {
		this.name1 = name1;
	}
	
	
	
}
