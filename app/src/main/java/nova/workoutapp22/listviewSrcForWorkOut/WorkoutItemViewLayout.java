package nova.workoutapp22.listviewSrcForWorkOut;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.LinearLayout;
import android.widget.TextView;

import nova.workoutapp22.R;

import static nova.workoutapp22.R.id.textViewTimerSetting;
import static nova.workoutapp22.R.id.textViewWoName;
import static nova.workoutapp22.R.id.textViewWoNum;
import static nova.workoutapp22.R.id.textViewWoSet;

/**
 * Created by Administrator on 2017-09-02.
 */

public class WorkoutItemViewLayout extends LinearLayout implements Checkable {

    TextView woName, woNum, woSet, timerSetting;

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

    public void setWoNumInLayout(String wNum){woNum.setText(wNum);}

    public void setWoSetInLayout(String wSet){woSet.setText(wSet);}

    public void setTimerSettingInLayout(String tSetting){timerSetting.setText(tSetting);}

    ///////////////////////// 구버전 set메소드들



}
