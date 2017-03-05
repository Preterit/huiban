package com.feirui.feiyunbangong.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class JsonBean implements Serializable {

	/**
	 */
	private String code;
	private String msg;
	private ArrayList<HashMap<String, Object>> infor;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String Msg) {
		this.msg = Msg;
	}

	public ArrayList<HashMap<String, Object>> getInfor() {
		return infor;
	}

	public void setInfor(ArrayList<HashMap<String, Object>> infor) {
		this.infor = infor;
	}

	public JsonBean(String msg) {
		super();
		this.msg = msg;
	}

	public JsonBean() {
		super();
	}
}
