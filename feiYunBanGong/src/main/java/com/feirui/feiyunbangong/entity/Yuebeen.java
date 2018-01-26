package com.feirui.feiyunbangong.entity;

/**
 * Created by 志恒 on 2018/1/22.
 */

public class Yuebeen {


    /**
     * code : 200
     * msg : 成功
     * info : {"amount":"0.02","start_time":"2017-12-29 12:57:30"}
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
         * amount : 0.02
         * start_time : 2017-12-29 12:57:30
         */

        private String amount;
        private String start_time;

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getStart_time() {
            return start_time;
        }

        public void setStart_time(String start_time) {
            this.start_time = start_time;
        }
    }
}
