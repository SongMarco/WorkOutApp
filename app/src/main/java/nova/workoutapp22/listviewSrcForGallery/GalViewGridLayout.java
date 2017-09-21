package nova.workoutapp22.listviewSrcForGallery;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.GridLayout;
import android.widget.ImageView;

import nova.workoutapp22.R;

/**
 * Created by jamsy on 2017-09-20.
 */

public class GalViewGridLayout extends GridLayout implements Checkable {

    ImageView imageView;


    CheckBox checkBox;




    public GalViewGridLayout(Context context) {


        super(context);


        init(context);
    }



    public void init(Context context){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.gal_item, this, true);

        imageView = (ImageView)findViewById(R.id.imageViewGal);

        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        checkBox = (CheckBox) findViewById(R.id.checkBoxGal);



    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

    @Override
    public void setChecked(boolean checked) {
        CheckBox cb = (CheckBox) findViewById(R.id.checkBoxGal) ;

        if (cb.isChecked() != checked) {
            cb.setChecked(checked) ;
        }


    }

    @Override
    public boolean isChecked() {
        CheckBox cb = (CheckBox) findViewById(R.id.checkBoxGal) ;

        return cb.isChecked() ;
        // return mIsChecked ;
    }

    @Override
    public void toggle() {
        CheckBox cb = (CheckBox) findViewById(R.id.checkBoxGal) ;

        setChecked(!cb.isChecked()) ;
        // setChecked(mIsChecked ? false : true) ;
    }

    public void setImageFromUri(Uri uri) {

        imageView.setImageURI(uri);
    }
}
