package nova.workoutapp22.listviewSrcForMemo;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import nova.workoutapp22.R;

/**
 * Created by user on 2016-08-10.
 */
public class MemoItemView extends LinearLayout {

    TextView textView2;


    TextView textViewMemo;
    TextView textViewDate;


    ImageView imageView;

    public MemoItemView(Context context) {
        super(context);

        init(context);
    }

    public MemoItemView(Context context, AttributeSet attrs) {
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
    }


    ///내가 만든 신메소드



    public void setMemo(String memo){ textViewMemo.setText(memo); }

    public void setDate(String date){ textViewDate.setText(date); }



    ///////////////////////// 구버전 set메소드들



    public void setImageWithUri(Uri uri) {

        imageView.setImageURI(uri);
    }



}
