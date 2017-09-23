package nova.workoutapp22.listviewSrcForVid;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.LinearLayout;

import nova.workoutapp22.R;

/**
 * Created by Administrator on 2017-09-23.
 */

public class VidItemLayout extends LinearLayout implements Checkable {

    public VidItemLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        // mIsChecked = false ;
    }

    public VidItemLayout(Context context) {
        super(context);
    }



    @Override
    public void setChecked(boolean checked) {
        CheckBox cb = (CheckBox) findViewById(R.id.checkBoxVid) ;

        if (cb.isChecked() != checked) {
            cb.setChecked(checked) ;
        }
    }

    @Override
    public boolean isChecked() {
        CheckBox cb = (CheckBox) findViewById(R.id.checkBoxVid) ;

        return cb.isChecked() ;
        // return mIsChecked ;
    }

    @Override
    public void toggle() {
        CheckBox cb = (CheckBox) findViewById(R.id.checkBoxVid) ;

        setChecked(!cb.isChecked()) ;
        // setChecked(mIsChecked ? false : true) ;
    }
}
