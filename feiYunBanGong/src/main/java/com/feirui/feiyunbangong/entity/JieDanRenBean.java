package com.feirui.feiyunbangong.entity;

import java.util.List;

/**
 * Created by szh on 2017/10/24.
 */

public class JieDanRenBean {

    /**
     * code : 200
     * msg : 成功
     * infor : [{"staff_name":"吴雅雄","staff_head":"http://123.57.45.74/feiybg1/public/static/staff_head/379/8fe02470a4eb202cf7643a86f31fa39f.jpeg","accept_id":438,"state":"0"},{"staff_name":"会办小秘书","staff_head":"http://123.57.45.74/feiybg1/public/static/staff_head/150/6e67c9e61ec40e7cf28ed29e7439a8d5.jpeg","accept_id":150,"state":"0"},{"staff_name":"洛奇运动教练黄方舟","staff_head":"http://123.57.45.74/feiybg1/public/static/staff_head/16004/38a8cf84109af5a48b9a7e764c8b6599.jpeg","accept_id":16004,"state":"0"},{"staff_name":"李策","staff_head":"http://123.57.45.74/feiybg1/public/static/staff_head/415/f23adb100be91fa334c874236172b24f.jpeg","accept_id":415,"state":"0"}]
     */

    private int code;
    private String msg;
    private List<InforBean> infor;

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

    public List<InforBean> getInfor() {
        return infor;
    }

    public void setInfor(List<InforBean> infor) {
        this.infor = infor;
    }

    public static class InforBean {
        /**
         * staff_name : 吴雅雄
         * staff_head : http://123.57.45.74/feiybg1/public/static/staff_head/379/8fe02470a4eb202cf7643a86f31fa39f.jpeg
         * accept_id : 438
         * state : 0
         */

        private String staff_name;
        private String staff_head;
        private int accept_id;
        private String state;

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

        public int getAccept_id() {
            return accept_id;
        }

        public void setAccept_id(int accept_id) {
            this.accept_id = accept_id;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        @Override
        public String toString() {
            return "InforBean{" +
                    "staff_name='" + staff_name + '\'' +
                    ", staff_head='" + staff_head + '\'' +
                    ", accept_id=" + accept_id +
                    ", state='" + state + '\'' +
                    '}';
        }
    }
}
