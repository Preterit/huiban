package com.feirui.feiyunbangong.entity;

import org.litepal.crud.DataSupport;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 团队：
 * 
 * @author feirui1
 *
 */
public class TuanDui  extends DataSupport implements Serializable {

	/**
	 */
	private static final long serialVersionUID = 1L;
	private String tid;
	private String name;//团队名称
	private String pinyin;//团队名称的拼音；
	private String headword;//团队名称拼音的首字母；
	private String guanli_id;// 团长id;
	private boolean isHave;// 是否含有公告消息；
	private ArrayList<String> Dcmoes=new ArrayList<>();// 副团长；
	private int notice_number;//团队消息数量；
	
	
	public String getPinyin() {
		return pinyin;
	}

	public void setPinyin(String pinyin) {
		String upperCase = pinyin.toUpperCase();//转为大写
		this.pinyin = upperCase;
	}

	public String getTid() {
		return tid;
	}

	public void setTid(String tid) {
		this.tid = tid;
	}

	public String getHeadword() {
		return headword;
	}

	public void setHeadword(String headword) {
		this.headword = headword;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public int getNotice_number() {
		return notice_number;
	}

	public void setNotice_number(int notice_number) {
		this.notice_number = notice_number;
	}

	public boolean isHave() {
		return isHave;
	}

	public void setHave(boolean isHave) {
		this.isHave = isHave;
	}

	public String getGuanli_id() {
		return guanli_id;
	}

	public void setGuanli_id(String guanli_id) {
		this.guanli_id = guanli_id;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
	
	public ArrayList<String> getDcmoes() {
		return Dcmoes;
	}

	public void setDcmoes(ArrayList<String> dcmoes) {
		Dcmoes = dcmoes;
	}

	@Override
	public String toString() {
		return "TuanDui [tid=" + tid + ", name=" + name + "]";
	}

	public TuanDui(String tid, String name) {
		super();
		this.tid = tid;
		this.name = name;
	}

	public TuanDui() {
	}

}
