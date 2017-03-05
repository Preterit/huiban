package com.feirui.feiyunbangong.entity;

/**
 * 公告：
 * 
 * @author feirui1
 *
 */
public class GongGao {

	private int id;// 公告id;
	private String head;
	private String name;
	private String time;
	private String content;
	private String phone;// 该成员手机号;

	@Override
	public String toString() {
		return "GongGao [head=" + head + ", name=" + name + ", time=" + time
				+ ", content=" + content + "]";
	}

	public GongGao() {
	}

	public GongGao(String head, String name, String time, String content) {
		super();
		this.head = head;
		this.name = name;
		this.time = time;
		this.content = content;
	}


	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getHead() {
		return head;
	}

	public void setHead(String head) {
		this.head = head;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
