package nova.workoutapp22.listviewSrcForMemo;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import nova.workoutapp22.R;

/**
 * Created by user on 2016-08-10.
 */
public class MemoItemViewLinearLayout extends LinearLayout implements Checkable {

    TextView textView2;


    TextView textViewMemo;
    TextView textViewDate;
    CheckBox checkBox;

    ImageView imageView;

    public MemoItemViewLinearLayout(Context context) {
        super(context);

        init(context);
    }

    public MemoItemViewLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    public void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.memo_item, this, true);

        textView2 = (TextView) findViewById(R.id.textView2);
        textViewDate = (TextView) findViewById(R.id.textViewDate);
        textViewMemo = (TextView) findViewById(R.id.textViewMemo);
        imageView = (ImageView) findViewById(R.id.imageView);

        checkBox = (CheckBox) findViewById(R.id.checkBox);
    }

    @Override
    public boolean isChecked() {
        CheckBox cb = (CheckBox) findViewById(R.id.checkBox) ;

        return cb.isChecked() ;
        // return mIsChecked ;
    }

    @Override
    public void toggle() {
        CheckBox cb = (CheckBox) findViewById(R.id.checkBox) ;

        setChecked(!cb.isChecked()) ;
        // setChecked(mIsChecked ? false : true) ;
    }

    @Override
    public void setChecked(boolean checked) {
        CheckBox cb = (CheckBox) findViewById(R.id.checkBox) ;

        if (cb.isChecked() != checked) {
            cb.setChecked(checked) ;
        }

        // CheckBox 가 아닌 View의 상태 변경.
    }


    ///내가 만든 신메소드



    public void setMemoInLayout(String memo){ textViewMemo.setText(memo); }

    public void SetDateInLayout(String date){ textViewDate.setText(date); }

    ///////////////////////// 구버전 set메소드들



    public void setImageFromUri(Uri uri) {

        imageView.setImageURI(uri);
    }

}
