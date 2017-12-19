package com.feirui.feiyunbangong.entity;

/**
 * Created by lice on 2017/12/14.
 */

public class RenwuOrderBean {


    /**
     * code : 200
     * msg : 成功
     * info : {"out_trade_no":"201712141513238435621","reward":"1"}
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
         * out_trade_no : 201712141513238435621
         * reward : 1
         */

        private String out_trade_no;
        private String reward;

        public String getOut_trade_no() {
            return out_trade_no;
        }

        public void setOut_trade_no(String out_trade_no) {
            this.out_trade_no = out_trade_no;
        }

        public String getReward() {
            return reward;
        }

        public void setReward(String reward) {
            this.reward = reward;
        }

        @Override
        public String toString() {
            return "InfoBean{" +
                    "out_trade_no='" + out_trade_no + '\'' +
                    ", reward='" + reward + '\'' +
                    '}';
        }
    }
    @Override
    public String toString() {
        return "RenwuOrderBean{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", info=" + info +
                '}';
    }
}
