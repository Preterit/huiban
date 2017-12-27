package com.feirui.feiyunbangong.entity;

/**
 * Created by lice on 2017/11/14.
 */

public class ShowAppCountBean {


    /**
     * code : 200
     * msg : 成功
     * info : {"task":"21","approval":"0","form":"15","num":"36"}
     */

    private int code;
    private String msg;
    private InfoBean info;

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

    public InfoBean getInfo() {
        return info;
    }

    public void setInfo(InfoBean info) {
        this.info = info;
    }

    public static class InfoBean {
        /**
         * task : 21
         * approval : 0
         * form : 15
         * num : 36
         */

        private String task;
        private String approval;
        private String form;
        private String num;

        public String getTask() {
            return task;
        }

        public void setTask(String task) {
            this.task = task;
        }

        public String getApproval() {
            return approval;
        }

        public void setApproval(String approval) {
            this.approval = approval;
        }

        public String getForm() {
            return form;
        }

        public void setForm(String form) {
            this.form = form;
        }

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }
    }
}
