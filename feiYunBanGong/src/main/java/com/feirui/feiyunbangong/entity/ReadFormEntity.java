package com.feirui.feiyunbangong.entity;

import java.util.List;

/**
 * Created by rubing on 2017/3/25.
 * rubingem@163.com
 */

public class ReadFormEntity {
  /**
   * code : 200 msg : 成功 infor : [{"id":291,"staff_id":376,"option_one":"165165156","option_two":"22626","option_three":"26526265","picture":"/public/img/376/15e07a08ae2baea4e49cc1b98be23ea0.jpeg,","remarks":"2+665256566556","form_time":"2017-03-24
   * 20:32:07","company_id":100146,"type_id":1,"name":"zzzzkkkk","pic":"/public/static/staff_head/376/80a795754c0e8ce8eee8577268b75eff.jpeg","staff_duties":null},{"id":290,"staff_id":376,"option_one":"165165156","option_two":"22626","option_three":"26526265","picture":"/public/img/376/571564bd8b7e19a0246d3eaff544033e.jpeg,","remarks":"2+665256566556","form_time":"2017-03-24
   * 20:32:04","company_id":100146,"type_id":1,"name":"zzzzkkkk","pic":"/public/static/staff_head/376/80a795754c0e8ce8eee8577268b75eff.jpeg","staff_duties":null},{"id":289,"staff_id":376,"option_one":"165165156","option_two":"22626","option_three":"26526265","picture":"/public/img/376/074181ff44dad019b3aa6d412125e9de.jpeg,","remarks":"2+665256566556","form_time":"2017-03-24
   * 20:31:37","company_id":100146,"type_id":1,"name":"zzzzkkkk","pic":"/public/static/staff_head/376/80a795754c0e8ce8eee8577268b75eff.jpeg","staff_duties":null},{"id":288,"staff_id":376,"option_one":"165165156","option_two":"22626","option_three":"26526265","picture":"/public/img/376/b09eb17aee87dd571b8fc691a3eaa8d9.jpeg,","remarks":"2+665256566556","form_time":"2017-03-24
   * 20:31:33","company_id":100146,"type_id":1,"name":"zzzzkkkk","pic":"/public/static/staff_head/376/80a795754c0e8ce8eee8577268b75eff.jpeg","staff_duties":null},{"id":287,"staff_id":376,"option_one":"165165156","option_two":"22626","option_three":"26526265","picture":"/public/img/376/6fe3f9d2c5d19ec122c6b16af457da5c.jpeg,","remarks":"2+665256566556","form_time":"2017-03-24
   * 20:31:30","company_id":100146,"type_id":1,"name":"zzzzkkkk","pic":"/public/static/staff_head/376/80a795754c0e8ce8eee8577268b75eff.jpeg","staff_duties":null},{"id":286,"staff_id":376,"option_one":"165165156","option_two":"22626","option_three":"26526265","picture":"/public/img/376/6daec561f84a60fcd77f865683905d70.jpeg,","remarks":"2+665256566556","form_time":"2017-03-24
   * 20:31:21","company_id":100146,"type_id":1,"name":"zzzzkkkk","pic":"/public/static/staff_head/376/80a795754c0e8ce8eee8577268b75eff.jpeg","staff_duties":null},{"id":285,"staff_id":376,"option_one":"165165156","option_two":"22626","option_three":"26526265","picture":"/public/img/376/361a2d44cad4647fa11cd59f179fbc41.jpeg,","remarks":"2+665256566556","form_time":"2017-03-24
   * 20:31:17","company_id":100146,"type_id":1,"name":"zzzzkkkk","pic":"/public/static/staff_head/376/80a795754c0e8ce8eee8577268b75eff.jpeg","staff_duties":null},{"id":284,"staff_id":376,"option_one":"165165156","option_two":"22626","option_three":"26526265","picture":"/public/img/376/82cb4a9ef9b3465b3a6442822d84162d.jpeg,","remarks":"2+665256566556","form_time":"2017-03-24
   * 20:31:10","company_id":100146,"type_id":1,"name":"zzzzkkkk","pic":"/public/static/staff_head/376/80a795754c0e8ce8eee8577268b75eff.jpeg","staff_duties":null},{"id":283,"staff_id":376,"option_one":"165165156","option_two":"22626","option_three":"26526265","picture":"/public/img/376/3114c6144e4c2bd56cf0ef635eaf5ea6.jpeg,","remarks":"2+665256566556","form_time":"2017-03-24
   * 20:31:03","company_id":100146,"type_id":1,"name":"zzzzkkkk","pic":"/public/static/staff_head/376/80a795754c0e8ce8eee8577268b75eff.jpeg","staff_duties":null},{"id":282,"staff_id":376,"option_one":"165165156","option_two":"22626","option_three":"26526265","picture":"/public/img/376/9745638768624503949bd81332fe6413.jpeg,","remarks":"2+665256566556","form_time":"2017-03-24
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
     * id : 291
     * staff_id : 376
     * option_one : 165165156
     * option_two : 22626
     * option_three : 26526265
     * picture : /public/img/376/15e07a08ae2baea4e49cc1b98be23ea0.jpeg,
     * remarks : 2+665256566556
     * form_time : 2017-03-24 20:32:07
     * company_id : 100146
     * type_id : 1
     * name : zzzzkkkk
     * pic : /public/static/staff_head/376/80a795754c0e8ce8eee8577268b75eff.jpeg
     * staff_duties : null
     */

    private int id;
    private int staff_id;
    private String option_one;
    private String option_two;
    private String option_three;
    private String picture;
    private String remarks;
    private String form_time;
    private int company_id;
    private int type_id;
    private String name;
    private String pic;
    private Object staff_duties;

    public int getId() {
      return id;
    }

    public void setId(int id) {
      this.id = id;
    }

    public int getStaff_id() {
      return staff_id;
    }

    public void setStaff_id(int staff_id) {
      this.staff_id = staff_id;
    }

    public String getOption_one() {
      return option_one;
    }

    public void setOption_one(String option_one) {
      this.option_one = option_one;
    }

    public String getOption_two() {
      return option_two;
    }

    public void setOption_two(String option_two) {
      this.option_two = option_two;
    }

    public String getOption_three() {
      return option_three;
    }

    public void setOption_three(String option_three) {
      this.option_three = option_three;
    }

    public String getPicture() {
      return picture;
    }

    public void setPicture(String picture) {
      this.picture = picture;
    }

    public String getRemarks() {
      return remarks;
    }

    public void setRemarks(String remarks) {
      this.remarks = remarks;
    }

    public String getForm_time() {
      return form_time;
    }

    public void setForm_time(String form_time) {
      this.form_time = form_time;
    }

    public int getCompany_id() {
      return company_id;
    }

    public void setCompany_id(int company_id) {
      this.company_id = company_id;
    }

    public int getType_id() {
      return type_id;
    }

    public void setType_id(int type_id) {
      this.type_id = type_id;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public String getPic() {
      return pic;
    }

    public void setPic(String pic) {
      this.pic = pic;
    }

    public Object getStaff_duties() {
      return staff_duties;
    }

    public void setStaff_duties(Object staff_duties) {
      this.staff_duties = staff_duties;
    }
  }
}
