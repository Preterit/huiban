package com.feirui.feiyunbangong.entity;


import java.io.Serializable;
import java.util.List;

public class TeamList_entity implements Serializable {

    public class Infor implements Serializable {
        private int team_id;

        private String team_name;

        public void setTeam_id(int team_id){
            this.team_id = team_id;
        }
        public int getTeam_id(){
            return this.team_id;
        }
        public void setTeam_name(String team_name){
            this.team_name = team_name;
        }
        public String getTeam_name(){
            return this.team_name;
        }

    }


    public class Root implements Serializable{
        private int code;

        private String msg;

        private List<Infor> infor ;

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
        public void setInfor(List<Infor> infor){
            this.infor = infor;
        }
        public List<Infor> getInfor(){
            return this.infor;
        }

    }
}
