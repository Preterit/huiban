package com.feirui.feiyunbangong.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by rubing on 2017/3/22.
 * rubingem@163.com
 */

public class FriendShopBean implements Serializable {

    /**
     * code : 200
     * msg : 成功
     * info : [{"id":17,"city_id":" 东城区\n","content":"购物","staff_id":379,"store_name":"白小白","provice_id":"北京"}]
     */

    private int code;
    private String msg;
    private List<InfoBean> info;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<InfoBean> getInfo() {
        return info;
    }

    public void setInfo(List<InfoBean> info) {
        this.info = info;
    }

    public static class InfoBean implements Serializable {
        /**
         * id : 17
         * city_id :  东城区
         * <p>
         * content : 购物
         * staff_id : 379
         * store_name : 白小白
         * provice_id : 北京
         */

        private int id;
        private String city_id;
        private String content;
        private int staff_id;
        private String store_name;
        private String provice_id;

        private String targetHead;
        private String targetName;
        private String targetAddress;
        private String targetPhoe;

        public String getTargetHead() {
            return targetHead;
        }

        public void setTargetHead(String targetHead) {
            this.targetHead = targetHead;
        }

        public String getTargetName() {
            return targetName;
        }

        public void setTargetName(String targetName) {
            this.targetName = targetName;
        }

        public String getTargetAddress() {
            return targetAddress;
        }

        public void setTargetAddress(String targetAddress) {
            this.targetAddress = targetAddress;
        }

        public String getTargetPhoe() {
            return targetPhoe;
        }

        public void setTargetPhoe(String targetPhoe) {
            this.targetPhoe = targetPhoe;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getCity_id() {
            return city_id;
        }

        public void setCity_id(String city_id) {
            this.city_id = city_id;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getStaff_id() {
            return staff_id;
        }

        public void setStaff_id(int staff_id) {
            this.staff_id = staff_id;
        }

        public String getStore_name() {
            return store_name;
        }

        public void setStore_name(String store_name) {
            this.store_name = store_name;
        }

        public String getProvice_id() {
            return provice_id;
        }

        public void setProvice_id(String provice_id) {
            this.provice_id = provice_id;
        }
    }
}
