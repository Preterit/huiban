package com.feirui.feiyunbangong.entity;

import java.util.List;

/**
 * Created by szh on 2017/10/26.
 */

public class RenWuDanBean {
    /**
     * code : 200
     * msg : 成功
     * info : [{"chooser":66,"choose_team":"","choose_group":"","subject":"测试好","task_txt":"外边风真大啊","time":"2017-11-30 14:00:00","reward_limit":"无悬赏","number_limit":"限制","reward":"","number":"2","address":null,"addresslimit":"未开启"}]
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

    public static class InfoBean {
        /**
         * chooser : 66
         * choose_team :
         * choose_group :
         * subject : 测试好
         * task_txt : 外边风真大啊
         * time : 2017-11-30 14:00:00
         * reward_limit : 无悬赏
         * number_limit : 限制
         * reward :
         * number : 2
         * address : null
         * addresslimit : 未开启
         */

        private String chooser;
        private String choose_team;
        private String choose_group;
        private String subject;
        private String task_txt;
        private String release_time;
        private String begin_time;
        private String reward_limit;
        private String number_limit;
        private String reward;
        private String number;
        private Object address;
        private String addresslimit;

        public String getChooser() {
            return chooser;
        }

        public void setChooser(String chooser) {
            this.chooser = chooser;
        }

        public String getChoose_team() {
            return choose_team;
        }

        public void setChoose_team(String choose_team) {
            this.choose_team = choose_team;
        }

        public String getChoose_group() {
            return choose_group;
        }

        public void setChoose_group(String choose_group) {
            this.choose_group = choose_group;
        }

        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }

        public String getTask_txt() {
            return task_txt;
        }

        public void setTask_txt(String task_txt) {
            this.task_txt = task_txt;
        }

        public String getRelease_time() {
            return release_time;
        }

        public String getBegin_time() {
            return begin_time;
        }

        public void setBegin_time(String begin_time) {
            this.begin_time = begin_time;
        }

        public void setRelease_time(String release_time) {
            this.release_time = release_time;
        }

        public String getReward_limit() {
            return reward_limit;
        }

        public void setReward_limit(String reward_limit) {
            this.reward_limit = reward_limit;
        }

        public String getNumber_limit() {
            return number_limit;
        }

        public void setNumber_limit(String number_limit) {
            this.number_limit = number_limit;
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

        public Object getAddress() {
            return address;
        }

        public void setAddress(Object address) {
            this.address = address;
        }

        public String getAddresslimit() {
            return addresslimit;
        }

        public void setAddresslimit(String addresslimit) {
            this.addresslimit = addresslimit;
        }
    }

//
//    /**
//     * code : 200
//     * msg : 成功
//     * info : [{"chooser":438,"choose_team":"","subject":"我没钱呀","task_txt":"你猜猜我是谁","time":"0000-00-00 00:00:00","reward_limit":"无悬赏","number_limit":"限制","reward":"","number":2,"address":null,"addresslimit":"未开启"}]
//     */
//
//    private int code;
//    private String msg;
//    private List<InfoBean> info;
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
//    public List<InfoBean> getInfo() {
//        return info;
//    }
//
//    public void setInfo(List<InfoBean> info) {
//        this.info = info;
//    }
//
//    public static class InfoBean {
//        /**
//         * chooser : 438
//         * choose_team :
//         * subject : 我没钱呀
//         * task_txt : 你猜猜我是谁
//         * time : 0000-00-00 00:00:00
//         * reward_limit : 无悬赏
//         * number_limit : 限制
//         * reward :
//         * number : 2
//         * address : null
//         * addresslimit : 未开启
//         */
//
//        private int chooser;
//        private String choose_team;
//        private String subject;
//        private String task_txt;
//        private String time;
//        private String reward_limit;
//        private String number_limit;
//        private String reward;
//        private int number;
//        private String address;
//        private String addresslimit;
//
//        public int getChooser() {
//            return chooser;
//        }
//
//        public void setChooser(int chooser) {
//            this.chooser = chooser;
//        }
//
//        public String getChoose_team() {
//            return choose_team;
//        }
//
//        public void setChoose_team(String choose_team) {
//            this.choose_team = choose_team;
//        }
//
//        public String getSubject() {
//            return subject;
//        }
//
//        public void setSubject(String subject) {
//            this.subject = subject;
//        }
//
//        public String getTask_txt() {
//            return task_txt;
//        }
//
//        public void setTask_txt(String task_txt) {
//            this.task_txt = task_txt;
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
//        public String getReward_limit() {
//            return reward_limit;
//        }
//
//        public void setReward_limit(String reward_limit) {
//            this.reward_limit = reward_limit;
//        }
//
//        public String getNumber_limit() {
//            return number_limit;
//        }
//
//        public void setNumber_limit(String number_limit) {
//            this.number_limit = number_limit;
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
//
//        public Object getAddress() {
//            return address;
//        }
//
//        public void setAddress(String address) {
//            this.address = address;
//        }
//
//        public String getAddresslimit() {
//            return addresslimit;
//        }
//
//        public void setAddresslimit(String addresslimit) {
//            this.addresslimit = addresslimit;
//        }
//    }
}
