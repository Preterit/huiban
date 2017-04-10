package com.feirui.feiyunbangong.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by rubing on 2017/3/24.
 * rubingem@163.com
 */

public class TaskListEntity  implements Serializable{


  /**
   * code : 200
   * msg : 成功
   * info : [{"id":21,"task_txt":"任务内容","subject":"任务主题","time":"2017-03-24 16:04:05","staff_id":379,"choose_team":"111,113","chooser":"376","name":"白晓鑫"},{"id":20,"task_txt":"任务内容","subject":"任务主题","time":"2017-03-24 13:44:16","staff_id":376,"choose_team":"100137,100133,100131,100132","chooser":"379,62","name":"zzzzkkkk"}]
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

  public static class InfoBean implements  Serializable{

    /**
     * id : 21
     * task_txt : 任务内容
     * subject : 任务主题
     * time : 2017-03-24 16:04:05
     * staff_id : 379
     * choose_team : 111,113
     * chooser : 376
     * name : 白晓鑫
     * staff_head :头像的地址
     * type :请求的状态码
     */

    private int id;
    private String task_txt;
    private String subject;
    private String time;
    private int staff_id;
    private String choose_team;
    private String chooser;
    private String name;
    private String staff_head;
    private int type;
    private int statue; //状态

      public int getStatue() {
          return statue;
      }

      public void setStatue(int statue) {
          this.statue = statue;
      }

      public int getType() {
      return type;
    }

    public void setType(int type) {
      this.type = type;
    }

    public String getStaff_head() {
      return staff_head;
    }

    public void setStaff_head(String staff_head) {
      this.staff_head = staff_head;
    }

    public int getId() {
      return id;
    }

    public void setId(int id) {
      this.id = id;
    }

    public String getTask_txt() {
      return task_txt;
    }

    public void setTask_txt(String task_txt) {
      this.task_txt = task_txt;
    }

    public String getSubject() {
      return subject;
    }

    public void setSubject(String subject) {
      this.subject = subject;
    }

    public String getTime() {
      return time;
    }

    public void setTime(String time) {
      this.time = time;
    }

    public int getStaff_id() {
      return staff_id;
    }

    public void setStaff_id(int staff_id) {
      this.staff_id = staff_id;
    }

    public String getChoose_team() {
      return choose_team;
    }

    public void setChoose_team(String choose_team) {
      this.choose_team = choose_team;
    }

    public String getChooser() {
      return chooser;
    }

    public void setChooser(String chooser) {
      this.chooser = chooser;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }
  }
}
