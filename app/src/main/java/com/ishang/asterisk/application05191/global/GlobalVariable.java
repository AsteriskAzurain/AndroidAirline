package com.ishang.asterisk.application05191.global;

/**
 * Created by Asterisk on 5/20/2020.
 */

public class GlobalVariable {

    public static int playingpos;

    public static int userid;
    public static String useremail;
    public static String username;

    public static String reqstr;
    public static String cabintype;

    public static String getReqstr() {
        return reqstr;
    }

    public static void setReqstr(String reqstr) {
        GlobalVariable.reqstr = reqstr;
    }

    public static String getCabintype() {
        return cabintype;
    }

    public static void setCabintype(String cabintype) {
        GlobalVariable.cabintype = cabintype;
    }

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        GlobalVariable.username = username;
    }

    public static String getUseremail() {
        return useremail;
    }

    public static void setUseremail(String useremail) {
        GlobalVariable.useremail = useremail;
    }

    public static int getUserid() {
        return userid;
    }

    public static void setUserid(int userid) {
        GlobalVariable.userid = userid;
    }

    public static int getPlayingpos() {
        return playingpos;
    }

    public static void setPlayingpos(int playingpos) {
        GlobalVariable.playingpos = playingpos;
    }


}
