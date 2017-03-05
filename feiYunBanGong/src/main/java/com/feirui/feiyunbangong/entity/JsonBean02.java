package com.feirui.feiyunbangong.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class JsonBean02 implements Serializable {

	/**
	 */
	private static final long serialVersionUID = -112121312L;
	private String code;
	private String msg;
	private ArrayList<HashMap<String, Object>> data;

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
		return data;
	}

	public void setInfor(ArrayList<HashMap<String, Object>> data) {
		this.data = data;
	}

}
