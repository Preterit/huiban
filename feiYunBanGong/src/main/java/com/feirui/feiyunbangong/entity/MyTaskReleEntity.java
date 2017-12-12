package com.feirui.feiyunbangong.entity;


import java.util.List;

public class MyTaskReleEntity {

    public class ReleaseInfo {
        private String task_txt;

        private int id;

        private String release_time;

        private String subject;

        private int type;

        private int statue;
        private String staff_name;
        private String staff_head;
        private String reward_status;

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

        public String getReward_status() {
            return reward_status;
        }

        public void setReward_status(String reward_status) {
            this.reward_status = reward_status;
        }

        public void setTask_txt(String task_txt) {
            this.task_txt = task_txt;
        }

        public String getTask_txt() {
            return this.task_txt;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getId() {
            return this.id;
        }

        public String getRelease_time() {
            return release_time;
        }

        public void setRelease_time(String release_time) {
            this.release_time = release_time;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }

        public String getSubject() {
            return this.subject;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getType() {
            return this.type;
        }

        public void setStatue(int statue) {
            this.statue = statue;
        }

        public int getStatue() {
            return this.statue;
        }

    }

    public class Root {
        private int code;

        private String msg;

        private List<ReleaseInfo> info;

        public void setCode(int code) {
            this.code = code;
        }

        public int getCode() {
            return this.code;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getMsg() {
            return this.msg;
        }

        public void setInfo(List<ReleaseInfo> info) {
            this.info = info;
        }

        public List<ReleaseInfo> getInfo() {
            return this.info;
        }

    }
}
