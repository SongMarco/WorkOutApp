package nova.workoutapp22.listviewSrcForWorkOut;

/**
 * Created by Administrator on 2017-09-02.
 */

import android.net.Uri;

/**
 * Created by user on 2016-08-10.
 */
public class WorkoutItem {

    String woName, woNum, woSet, timerSetting;


    public int mID;
    String memo;
    String date;
    int resId;
    Uri uri;

    public WorkoutItem(String woName, String woNum, String woSet, String timerSetting) {
        this.woName = woName;
        this.woNum = woNum;
        this.woSet = woSet;
        this.timerSetting = timerSetting;
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
        this.woName = woNum;
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

    public int getmID(){ return mID; }

    public void setmID(int mID){ this.mID = mID;}

}
