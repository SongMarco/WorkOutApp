package nova.workoutapp22.listviewSrcForGallery;

import android.net.Uri;

/**
 * Created by Administrator on 2017-09-09.
 */

public class GalItem {


    public int mID;
    Uri uri;

    String imgPath;



    public GalItem(Uri uri){

        this.uri = uri;

    }

    public GalItem(String imgPath){

        this.imgPath = imgPath;

    }



    public String getImgPath(){return imgPath;}
    public void setImgPath(String imgPath){this.imgPath = imgPath;}

    public int getmID(){ return mID; }
    public void setmID(int mID){ this.mID = mID;}

    public Uri getUri(){ return uri;}
    public void setUri(Uri uri) {this.uri = uri;}

}
