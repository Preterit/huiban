package com.feirui.feiyunbangong.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by 志恒 on 2018/1/22.
 */

public class ShowCardbeen {


    /**
     * code : 200
     * msg : 成功
     * info : [{"card_id":12,"staff_id":"49975","certificate_type":"身份证","id_number":"372930199306122195","bank_number":"1234123412341234567","card_name":"宋志恒","bank_type":"建设银行","default":1}]
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
         * card_id : 12
         * staff_id : 49975
         * certificate_type : 身份证
         * id_number : 372930199306122195
         * bank_number : 1234123412341234567
         * card_name : 宋志恒
         * bank_type : 建设银行
         * default : 1
         */

        private int card_id;
        private String staff_id;
        private String certificate_type;
        private String id_number;
        private String bank_number;
        private String card_name;
        private String bank_type;
        @SerializedName("default")
        private int defaultX;

        public int getCard_id() {
            return card_id;
        }

        public void setCard_id(int card_id) {
            this.card_id = card_id;
        }

        public String getStaff_id() {
            return staff_id;
        }

        public void setStaff_id(String staff_id) {
            this.staff_id = staff_id;
        }

        public String getCertificate_type() {
            return certificate_type;
        }

        public void setCertificate_type(String certificate_type) {
            this.certificate_type = certificate_type;
        }

        public String getId_number() {
            return id_number;
        }

        public void setId_number(String id_number) {
            this.id_number = id_number;
        }

        public String getBank_number() {
            return bank_number;
        }

        public void setBank_number(String bank_number) {
            this.bank_number = bank_number;
        }

        public String getCard_name() {
            return card_name;
        }

        public void setCard_name(String card_name) {
            this.card_name = card_name;
        }

        public String getBank_type() {
            return bank_type;
        }

        public void setBank_type(String bank_type) {
            this.bank_type = bank_type;
        }

        public int getDefaultX() {
            return defaultX;
        }

        public void setDefaultX(int defaultX) {
            this.defaultX = defaultX;
        }
    }
}
