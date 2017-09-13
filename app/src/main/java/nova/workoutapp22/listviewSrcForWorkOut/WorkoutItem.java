package nova.workoutapp22.listviewSrcForWorkOut;

/**
 * Created by Administrator on 2017-09-02.
 */

import com.google.gson.JsonObject;

/**
 * Created by user on 2016-08-10.
 */
public class WorkoutItem {

    String woName,
            woNum,
            woSet,
           timerSetting;


    boolean boolTimeSet = false;

    int hour = -1;
    int min = -1;
    int sec = -1;

    int restMin = -1;
    int restSec = -1;


    public int mID;

    public WorkoutItem() {
    }

    public WorkoutItem(int mID) {
        this.mID = mID;
    }
    public WorkoutItem(String woName, String woNum, String woSet, String timerSetting) {
        this.woName = woName;
        this.woNum = woNum;
        this.woSet = woSet;
        this.timerSetting = timerSetting;
    }

    public WorkoutItem(String woName, String woNum, String woSet, String timerSetting, boolean boolTimeSet) {
        this.woName = woName;
        this.woNum = woNum;
        this.woSet = woSet;
        this.timerSetting = timerSetting;
        this.boolTimeSet = boolTimeSet;
    }

    public WorkoutItem(String woName, String woNum, String woSet, String timerSetting, boolean boolTimeSet, int restMin, int restSec) {
        this.woName = woName;
        this.woNum = woNum;
        this.woSet = woSet;
        this.timerSetting = timerSetting;
        this.boolTimeSet = boolTimeSet;

        this.restMin = restMin;
        this.restSec = restSec;
    }

    public WorkoutItem(String woName,  int hour, int min, int sec ,String timerSetting){
        this.woName = woName;
        this.timerSetting = timerSetting;
        this.hour = hour;
        this.min = min;
        this.sec = sec;
    }
    public WorkoutItem(String woName,  int hour, int min, int sec ,String timerSetting, boolean boolTimeSet){
        this.woName = woName;
        this.timerSetting = timerSetting;
        this.hour = hour;
        this.min = min;
        this.sec = sec;
        this.boolTimeSet = boolTimeSet;
    }

    public WorkoutItem(String woName, String woSet, int hour, int min, int sec ,String timerSetting, boolean boolTimeSet){
        this.woName = woName;
        this.timerSetting = timerSetting;
        this.woSet = woSet;
        this.hour = hour;
        this.min = min;
        this.sec = sec;
        this.boolTimeSet = boolTimeSet;
    }





    public WorkoutItem(int mID, String woName, String woNum, String woSet, String timerSetting) {
        this.mID = mID;
        this.woName = woName;
        this.woNum = woNum;
        this.woSet = woSet;
        this.timerSetting = timerSetting;
    }


    public final JsonObject toJson() {

        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("woName", getWoName());
        jsonObject.addProperty("woNum", getWoNum());
        jsonObject.addProperty("woSet", getWoSet());
        jsonObject.addProperty("timerSetting", getTimerSetting());

        jsonObject.addProperty("boolTimeSet", getBoolTimeSet() );

        jsonObject.addProperty("hour", getHour());
        jsonObject.addProperty("min", getMin());
        jsonObject.addProperty("sec", getSec() );

        return jsonObject;

    }


    public String getWoName() {
        return woName;
    }

    public void setWoName(String woName) {
        this.woName = woName;
    }

    public String getWoNum() {
        return woNum;
    }

    public void setWoNum(String woNum) {
        this.woNum = woNum;
    }

    public String getWoSet() {
        return woSet;
    }

    public void setWoSet(String woSet) {
        this.woSet = woSet;
    }

    public String getTimerSetting() {
        return timerSetting;
    }

    public void setTimerSetting(String timerSetting) {
        this.timerSetting = timerSetting;
    }

    public int getmID() {
        return mID;
    }

    public void setmID(int mID) {
        this.mID = mID;
    }


    //////////////////// 시간 관련 세팅

    public int getHour(){ return hour;}

    public void setHour(int hour) {this.hour = hour;}

    public int getMin(){ return min;}

    public void setMin(int min) {this.min = min;}

    public int getSec(){ return sec;}

    public void setSec(int sec) {this.sec = sec;}

    ////////////////////쉬는 시간 관련 세팅

    public int getRestMin(){return restMin;}

    public void setRestMin(int restMin){this.restMin = restMin;}

    public int getRestSec(){return restSec;}

    public void setRestSec(int restSec){this.restSec = restSec;}



    //////////////////////////

    public boolean getBoolTimeSet(){ return boolTimeSet; }

    public void setBoolTimeSet(Boolean boolTimeSet){ this.boolTimeSet = boolTimeSet; }

}
