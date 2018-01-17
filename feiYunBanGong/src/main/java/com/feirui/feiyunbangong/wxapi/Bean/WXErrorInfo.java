package com.feirui.feiyunbangong.wxapi.Bean;

/**
 * Created by feirui1 on 2018-01-10.
 */

public class WXErrorInfo {

    /**
     * errcode : 40030
     * errmsg : invalid refresh_token
     */

    private int errcode;
    private String errmsg;

    public int getErrcode() {
        return errcode;
    }

    public void setErrcode(int errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    @Override
    public String toString() {
        return "WXErrorInfo{" +
                "errcode=" + errcode +
                ", errmsg='" + errmsg + '\'' +
                '}';
    }
}
