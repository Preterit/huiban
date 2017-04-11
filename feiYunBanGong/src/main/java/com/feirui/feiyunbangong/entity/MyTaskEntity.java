package com.feirui.feiyunbangong.entity;


import java.util.List;

public class MyTaskEntity {

    public class MyTaskInfo {
        private int id;

        private String subject;

        private String task_txt;

        private String time;

        private int staff_id;

        private int type;

        private String name;

        public void setId(int id){
            this.id = id;
        }
        public int getId(){
            return this.id;
        }
        public void setSubject(String subject){
            this.subject = subject;
        }
        public String getSubject(){
            return this.subject;
        }
        public void setTask_txt(String task_txt){
            this.task_txt = task_txt;
        }
        public String getTask_txt(){
            return this.task_txt;
        }
        public void setTime(String time){
            this.time = time;
        }
        public String getTime(){
            return this.time;
        }
        public void setStaff_id(int staff_id){
            this.staff_id = staff_id;
        }
        public int getStaff_id(){
            return this.staff_id;
        }
        public void setType(int type){
            this.type = type;
        }
        public int getType(){
            return this.type;
        }
        public void setName(String name){
            this.name = name;
        }
        public String getName(){
            return this.name;
        }

    }


    public class MyTaskRoot {
        private int code;

        private String msg;

        private List<MyTaskInfo> info ;

        public void setCode(int code){
            this.code = code;
        }
        public int getCode(){
            return this.code;
        }
        public void setMsg(String msg){
            this.msg = msg;
        }
        public String getMsg(){
            return this.msg;
        }
        public void setInfo(List<MyTaskInfo> info){
            this.info = info;
        }
        public List<MyTaskInfo> getInfo(){
            return this.info;
        }

    }

}
