package com.feirui.feiyunbangong.entity;

import java.util.ArrayList;

public class Province {
	private String provinceName;
	private ArrayList<String> cityList;
	public String getProvinceName() {
		return provinceName;
	}
	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}
	public ArrayList<String> getCityList() {
		return cityList;
	}
	public void setCityList(ArrayList<String> cityList) {
		this.cityList = cityList;
	}
	@Override
	public String toString() {
		return "Province [provinceName=" + provinceName + ", cityList=" + cityList + "]";
	}

	
	
}
