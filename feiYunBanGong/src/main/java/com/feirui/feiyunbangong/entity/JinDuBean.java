package com.feirui.feiyunbangong.entity;

import java.util.List;

/**
 * Created by szh on 2017/10/27.
 */

public class JinDuBean {
    /**
     * code : 200
     * msg : 成功
     * info : {"accept_time":"2017-11-29 11:11:27","finsh_time":"","address":null,"reward":"","number":"2"}
     * info1 : [{"id":8,"time":"2017-11-29 12:09:20","message":"吕特特"}]
     */

    private int code;
    private String msg;
    private InfoBean info;
    private List<Info1Bean> info1;

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

    public List<Info1Bean> getInfo1() {
        return info1;
    }

    public void setInfo1(List<Info1Bean> info1) {
        this.info1 = info1;
    }

    public static class InfoBean {
        /**
         * accept_time : 2017-11-29 11:11:27
         * finsh_time :
         * address : null
         * reward :
         * number : 2
         */

        private String accept_time;
        private String finsh_time;
        private Object address;
        private String reward;
        private String number;

        public String getAccept_time() {
            return accept_time;
        }

        public void setAccept_time(String accept_time) {
            this.accept_time = accept_time;
        }

        public String getFinsh_time() {
            return finsh_time;
        }

        public void setFinsh_time(String finsh_time) {
            this.finsh_time = finsh_time;
        }

        public Object getAddress() {
            return address;
        }

        public void setAddress(Object address) {
            this.address = address;
        }

        public String getReward() {
            return reward;
        }

        public void setReward(String reward) {
            this.reward = reward;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }
    }

    public static class Info1Bean {
        /**
         * id : 8
         * time : 2017-11-29 12:09:20
         * message : 吕特特
         */

        private int id;
        private String time;
        private String message;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

//
//    /**
//     * code : 200
//     * msg : 成功
//     * info : {"accept_time":"2017-10-26 10:43:28","finsh_time":"2017-10-27 11:07:24","address":null,"reward":"","number":2}
//     * info1 : [{"id":1,"time":"2017-10-27 12:02:06","message":"咕咕咕咕   任务没完成呢"},{"id":2,"time":"2017-10-27 12:10:54","message":"咕咕咕咕   任务没完成呢"},{"id":3,"time":"2017-10-27 14:41:26","message":"咕咕咕咕   任务没完成呢"}]
//     */
//
//    private int code;
//    private String msg;
//    private InfoBean info;
//    private List<Info1Bean> info1;
//
//    public int getCode() {
//        return code;
//    }
//
//    public void setCode(int code) {
//        this.code = code;
//    }
//
//    public String getMsg() {
//        return msg;
//    }
//
//    public void setMsg(String msg) {
//        this.msg = msg;
//    }
//
//    public InfoBean getInfo() {
//        return info;
//    }
//
//    public void setInfo(InfoBean info) {
//        this.info = info;
//    }
//
//    public List<Info1Bean> getInfo1() {
//        return info1;
//    }
//
//    public void setInfo1(List<Info1Bean> info1) {
//        this.info1 = info1;
//    }
//
//    public static class InfoBean {
//        /**
//         * accept_time : 2017-10-26 10:43:28
//         * finsh_time : 2017-10-27 11:07:24
//         * address : null
//         * reward :
//         * number : 2
//         */
//
//        private String accept_time;
//        private String finsh_time;
//        private Object address;
//        private String reward;
//        private int number;
//
//        public String getAccept_time() {
//            return accept_time;
//        }
//
//        public void setAccept_time(String accept_time) {
//            this.accept_time = accept_time;
//        }
//
//        public String getFinsh_time() {
//            return finsh_time;
//        }
//
//        public void setFinsh_time(String finsh_time) {
//            this.finsh_time = finsh_time;
//        }
//
//        public Object getAddress() {
//            return address;
//        }
//
//        public void setAddress(Object address) {
//            this.address = address;
//        }
//
//        public String getReward() {
//            return reward;
//        }
//
//        public void setReward(String reward) {
//            this.reward = reward;
//        }
//
//        public int getNumber() {
//            return number;
//        }
//
//        public void setNumber(int number) {
//            this.number = number;
//        }
//    }
//
//    public static class Info1Bean {
//        /**
//         * id : 1
//         * time : 2017-10-27 12:02:06
//         * message : 咕咕咕咕   任务没完成呢
//         */
//
//        private int id;
//        private String time;
//        private String message;
//
//        public int getId() {
//            return id;
//        }
//
//        public void setId(int id) {
//            this.id = id;
//        }
//
//        public String getTime() {
//            return time;
//        }
//
//        public void setTime(String time) {
//            this.time = time;
//        }
//
//        public String getMessage() {
//            return message;
//        }
//
//        public void setMessage(String message) {
//            this.message = message;
//        }
//    }
}
