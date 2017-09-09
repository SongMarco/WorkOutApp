package nova.workoutapp22.listviewSrcForGallery;

import android.net.Uri;

/**
 * Created by Administrator on 2017-09-09.
 */

public class PictureItem {

    public int pictureID;
    Uri picturUri;




    public PictureItem(Uri uri){

        this.picturUri = uri;

    }


    public int getPictureID(){ return pictureID; }
    public void setPictureID(int mID){ this.pictureID = mID;}

    public Uri getUri(){ return picturUri;}
    public void setUri(Uri uri) {this.picturUri = uri;}



}
