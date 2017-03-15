package com.feirui.feiyunbangong.entity;

import java.util.ArrayList;
import java.util.List;

//工作圈消息item;
public class ItemEntity {
    private String id;// 信息id;
    private String staffId;//用户id
    private String avatar; // 用户头像URL
    private String title; // 标题
    private String content; // 内容
    private ArrayList<String> imageUrls; // 九宫格图片的URL集合
    private boolean picShow = false;// 发表的内容图片是否显示；
    private boolean isZan = false;//是否赞过；
    private String zan_name;//显示所有赞的人名称；
    private List<PingLunItem> plis;//评论列表；

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public ItemEntity(String avatar, String title, String content,
                      ArrayList<String> imageUrls) {
        super();
        this.avatar = avatar;
        this.title = title;
        this.content = content;
        this.imageUrls = imageUrls;
    }

    public ItemEntity() {
    }


    public ItemEntity(String id, String avatar, String title, String content,
                      ArrayList<String> imageUrls) {
        super();
        this.id = id;
        this.avatar = avatar;
        this.title = title;
        this.content = content;
        this.imageUrls = imageUrls;
    }


    public List<PingLunItem> getPlis() {
        return plis;
    }

    public void setPlis(List<PingLunItem> plis) {
        this.plis = plis;
    }

    public String getZan_name() {
        return zan_name;
    }

    public void setZan_name(String zan_name) {
        this.zan_name = zan_name;
    }

    public boolean isZan() {
        return isZan;
    }

    public void setZan(boolean isZan) {
        this.isZan = isZan;
    }

    public boolean isPicShow() {
        return picShow;
    }

    public void setPicShow(boolean picShow) {
        this.picShow = picShow;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ArrayList<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(ArrayList<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    @Override
    public String toString() {
        return "ItemEntity [id=" + id + ", avatar=" + avatar + ", title="
                + title + ", content=" + content + ", imageUrls=" + imageUrls
                + ", picShow=" + picShow + ", isZan=" + isZan + ", zan_name="
                + zan_name + ", plis=" + plis + "]";
    }


}
