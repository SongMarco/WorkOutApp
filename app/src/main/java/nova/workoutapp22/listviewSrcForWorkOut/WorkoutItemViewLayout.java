package nova.workoutapp22.listviewSrcForWorkOut;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.LinearLayout;
import android.widget.TextView;

import nova.workoutapp22.R;

import static nova.workoutapp22.R.id.textViewForTime;
import static nova.workoutapp22.R.id.textViewTimerSetting;
import static nova.workoutapp22.R.id.textViewWoName;
import static nova.workoutapp22.R.id.textViewWoNum;
import static nova.workoutapp22.R.id.textViewWoSet;

/**
 * Created by Administrator on 2017-09-02.
 */

public class WorkoutItemViewLayout extends LinearLayout implements Checkable {

    TextView woName, woNum, woSet, timerSetting, time;

    CheckBox checkBox;


    int checkBoxId = R.id.checkBoxForWo;

    public WorkoutItemViewLayout(Context context) {
        super(context);

        init(context);
    }

    public WorkoutItemViewLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    public void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.workout_item, this, true);

        woName = (TextView)findViewById(textViewWoName);
        woNum = (TextView)findViewById(textViewWoNum);
        woSet = (TextView)findViewById(textViewWoSet);

        timerSetting = (TextView)findViewById(textViewTimerSetting);
        time = (TextView)findViewById(textViewForTime);

        checkBox = (CheckBox) findViewById(checkBoxId);

    }


    @Override
    public boolean isChecked() {
        CheckBox cb = (CheckBox) findViewById(checkBoxId) ;

        return cb.isChecked() ;
        // return mIsChecked ;
    }

    @Override
    public void toggle() {
        CheckBox cb = (CheckBox) findViewById(checkBoxId) ;

        setChecked(!cb.isChecked()) ;
        // setChecked(mIsChecked ? false : true) ;
    }

    @Override
    public void setChecked(boolean checked) {
        CheckBox cb = (CheckBox) findViewById(checkBoxId) ;

        if (cb.isChecked() != checked) {
            cb.setChecked(checked) ;
        }

        // CheckBox 가 아닌 View의 상태 변경.
    }


    ///내가 만든 신메소드

    public void setWoNameInLayout(String wName){woName.setText(wName); }


    //////////////////////woNum과 세트는 따로 세트와 횟수를 추가해준다. 레이아웃에서만 처리하고, 텍스트를 변경하지 않음
    public void setWoNumInLayout(String wNum){
        woNum.setText(wNum + "회");}

    public void setWoSetInLayout(String wSet){woSet.setText(wSet + "세트");}




    public void setTimerSettingInLayout(String tSetting){timerSetting.setText(tSetting);}



    public void setTimeInLayout(int hour, int min, int sec){

        time.setText(hour+"시간"+min+"분"+sec+"초"   );

    }
    ///////////////////////// 구버전 set메소드들



}
