package com.feirui.feiyunbangong.entity;


import java.util.List;

public class MyTaskReleEntity {

    public class ReleaseInfo {
        private String task_txt;

        private int id;

        private String time;

        private String subject;

        private int type;

        private int statue;

        public void setTask_txt(String task_txt){
            this.task_txt = task_txt;
        }
        public String getTask_txt(){
            return this.task_txt;
        }
        public void setId(int id){
            this.id = id;
        }
        public int getId(){
            return this.id;
        }
        public void setTime(String time){
            this.time = time;
        }
        public String getTime(){
            return this.time;
        }
        public void setSubject(String subject){
            this.subject = subject;
        }
        public String getSubject(){
            return this.subject;
        }
        public void setType(int type){
            this.type = type;
        }
        public int getType(){
            return this.type;
        }
        public void setStatue(int statue){
            this.statue = statue;
        }
        public int getStatue(){
            return this.statue;
        }

    }

    public class Root {
        private int code;

        private String msg;

        private List<ReleaseInfo> info ;

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
        public void setInfo(List<ReleaseInfo> info){
            this.info = info;
        }
        public List<ReleaseInfo> getInfo(){
            return this.info;
        }

    }
}
