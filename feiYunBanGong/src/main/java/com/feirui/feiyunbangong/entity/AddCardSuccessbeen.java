package com.feirui.feiyunbangong.entity;

/**
 * Created by 志恒 on 2018/1/24.
 */

public class AddCardSuccessbeen {


    /**
     * code : 200
     * msg : 添加成功
     * info : 1
     */

    private int code;
    private String msg;
    private int info;

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

    public int getInfo() {
        return info;
    }

    public void setInfo(int info) {
        this.info = info;
    }

    @Override
    public String toString() {
        return "AddCardSuccessbeen{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", info=" + info +
                '}';
    }
}
