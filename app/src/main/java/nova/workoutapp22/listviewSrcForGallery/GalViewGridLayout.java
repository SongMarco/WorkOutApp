package nova.workoutapp22.listviewSrcForGallery;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.widget.Checkable;
import android.widget.GridLayout;
import android.widget.ImageView;

import nova.workoutapp22.R;

/**
 * Created by jamsy on 2017-09-20.
 */

public class GalViewGridLayout extends GridLayout implements Checkable {

    ImageView imageView;






    public GalViewGridLayout(Context context) {


        super(context);


        init(context);
    }



    public void init(Context context){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.gal_item, this, true);

        imageView = (ImageView)findViewById(R.id.imageViewGal);



    }



    @Override
    public void setChecked(boolean checked) {

    }

    @Override
    public boolean isChecked() {
        return false;
    }

    @Override
    public void toggle() {

    }

    public void setImageFromUri(Uri uri) {

        imageView.setImageURI(uri);
    }
}
