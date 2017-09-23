package nova.workoutapp22.listviewSrcForVid;

import android.net.Uri;

/**
 * Created by Administrator on 2017-09-23.
 */

public class VidItem {

    public int mID;

    Uri uri;
    String vidTitle;




    public VidItem(String vidTitle, Uri uri){
        this.vidTitle = vidTitle;
        this.uri = uri;
    }

    public int getmID(){ return mID; }
    public void setmID(int mID){ this.mID = mID;}

    public Uri getUri(){ return uri;}
    public void setUri(Uri uri) {this.uri = uri;}

    public String getVidTitle(){ return vidTitle;}
    public void setVidTitle(String vidTitle){this.vidTitle = vidTitle;}


}
