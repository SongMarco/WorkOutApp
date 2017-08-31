package nova.workoutapp22;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.IOException;

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



    public void setImage(int resId) {
        imageView.setImageResource(resId);
    }
    public void setImageByBitmap(Bitmap bm){
        imageView.setImageBitmap(bm);
    }

    public void setImageWithUri(Uri uri){
        Bitmap bm = null;
        try {
            bm = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), uri);
            imageView.setImageBitmap(bm);

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        imageView.setImageBitmap(bm);

    }


}
