package com.feirui.feiyunbangong.entity;

import android.graphics.Bitmap;

/**
 * 商品实体类：
 */
public class Good {

	private String good_name;// 商品名称；
	private String privce;// 商品价格；
	private Bitmap good_head_title;// 商品缩略图；
	private String imgUrl;


	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getGood_name() {
		return good_name;
	}

	public void setGood_name(String good_name) {
		this.good_name = good_name;
	}

	public String getPrivce() {
		return privce;
	}

	public void setPrivce(String privce) {
		this.privce = privce;
	}

	public Bitmap getGood_head_title() {
		return good_head_title;
	}

	public void setGood_head_title(Bitmap good_head_title) {
		this.good_head_title = good_head_title;
	}

	public Good() {
	}

	public Good(String good_name, String privce, Bitmap good_head_title) {
		super();
		this.good_name = good_name;
		this.privce = privce;
		this.good_head_title = good_head_title;
	}

	@Override
	public String toString() {
		return "Good [good_name=" + good_name + ", privce=" + privce + ", good_head_title=" + good_head_title + "]";
	}

}
