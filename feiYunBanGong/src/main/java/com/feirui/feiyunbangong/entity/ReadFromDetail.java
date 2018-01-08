package com.feirui.feiyunbangong.entity;

/**
 * Created by lice on 2018/1/7.
 */

public class ReadFromDetail {

    /**
     * code : 200
     * msg : 成功
     * infor : {"id":1395,"company_id":100172,"remarks":"","picture":"","form_check":"66,589","form_time":"2018-01-05 17:50:45","state":2,"looktime":1515081600,"staff_name":"策()\t","staff_head":"http://123.57.45.74/feiybg1/public/static/staff_head/415/f23adb100be91fa334c874236172b24f.jpeg","option_one_value":"修改添加好友界面，取消扫一扫和短信邀请","option_two_value":"","option_three_value":"修改会办紧急问题","type_name":"日报","option_one_name":"今日完成工作","option_two_name":"今日未完成工作","option_three_name":"明日工作计划"}
     */

    private int code;
    private String msg;
    private InforBean infor;

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

    public InforBean getInfor() {
        return infor;
    }

    public void setInfor(InforBean infor) {
        this.infor = infor;
    }

    public static class InforBean {
        /**
         * id : 1395
         * company_id : 100172
         * remarks :
         * picture :
         * form_check : 66,589
         * form_time : 2018-01-05 17:50:45
         * state : 2
         * looktime : 1515081600
         * staff_name : 策()
         * staff_head : http://123.57.45.74/feiybg1/public/static/staff_head/415/f23adb100be91fa334c874236172b24f.jpeg
         * option_one_value : 修改添加好友界面，取消扫一扫和短信邀请
         * option_two_value :
         * option_three_value : 修改会办紧急问题
         * type_name : 日报
         * option_one_name : 今日完成工作
         * option_two_name : 今日未完成工作
         * option_three_name : 明日工作计划
         */

        private int id;
        private int company_id;
        private String remarks;
        private String picture;
        private String form_check;
        private String form_time;
        private int state;
        private int looktime;
        private String staff_name;
        private String staff_head;
        private String option_one_value;
        private String option_two_value;
        private String option_three_value;
        private String type_name;
        private String option_one_name;
        private String option_two_name;
        private String option_three_name;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getCompany_id() {
            return company_id;
        }

        public void setCompany_id(int company_id) {
            this.company_id = company_id;
        }

        public String getRemarks() {
            return remarks;
        }

        public void setRemarks(String remarks) {
            this.remarks = remarks;
        }

        public String getPicture() {
            return picture;
        }

        public void setPicture(String picture) {
            this.picture = picture;
        }

        public String getForm_check() {
            return form_check;
        }

        public void setForm_check(String form_check) {
            this.form_check = form_check;
        }

        public String getForm_time() {
            return form_time;
        }

        public void setForm_time(String form_time) {
            this.form_time = form_time;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public int getLooktime() {
            return looktime;
        }

        public void setLooktime(int looktime) {
            this.looktime = looktime;
        }

        public String getStaff_name() {
            return staff_name;
        }

        public void setStaff_name(String staff_name) {
            this.staff_name = staff_name;
        }

        public String getStaff_head() {
            return staff_head;
        }

        public void setStaff_head(String staff_head) {
            this.staff_head = staff_head;
        }

        public String getOption_one_value() {
            return option_one_value;
        }

        public void setOption_one_value(String option_one_value) {
            this.option_one_value = option_one_value;
        }

        public String getOption_two_value() {
            return option_two_value;
        }

        public void setOption_two_value(String option_two_value) {
            this.option_two_value = option_two_value;
        }

        public String getOption_three_value() {
            return option_three_value;
        }

        public void setOption_three_value(String option_three_value) {
            this.option_three_value = option_three_value;
        }

        public String getType_name() {
            return type_name;
        }

        public void setType_name(String type_name) {
            this.type_name = type_name;
        }

        public String getOption_one_name() {
            return option_one_name;
        }

        public void setOption_one_name(String option_one_name) {
            this.option_one_name = option_one_name;
        }

        public String getOption_two_name() {
            return option_two_name;
        }

        public void setOption_two_name(String option_two_name) {
            this.option_two_name = option_two_name;
        }

        public String getOption_three_name() {
            return option_three_name;
        }

        public void setOption_three_name(String option_three_name) {
            this.option_three_name = option_three_name;
        }
    }
}
