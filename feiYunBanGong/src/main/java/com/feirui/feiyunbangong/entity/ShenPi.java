package com.feirui.feiyunbangong.entity;

public class ShenPi {

	private String name;
	private String bumen;
	private String leixing;
	
	
	public ShenPi() {
	}
	
	
	public ShenPi(String name, String bumen, String leixing) {
		super();
		this.name = name;
		this.bumen = bumen;
		this.leixing = leixing;
	}
	


	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getBumen() {
		return bumen;
	}
	public void setBumen(String bumen) {
		this.bumen = bumen;
	}
	public String getLeixing() {
		return leixing;
	}
	public void setLeixing(String leixing) {
		this.leixing = leixing;
	}
	
	
	
	
	
	
}
