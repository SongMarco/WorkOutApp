package nova.workoutapp22.listviewSrcForVid;

import android.net.Uri;

/**
 * Created by Administrator on 2017-09-23.
 */

public class VidItem {

    public int mID;

    Uri uri;
    String url;
    String vidTitle;
    String youtubeID;



    public VidItem(String vidTitle, Uri uri){
        this.vidTitle = vidTitle;
        this.uri = uri;
    }
    public VidItem(String vidTitle, String url){
        this.vidTitle = vidTitle;
        this.url= url;
    }

    public VidItem(String vidTitle, String url, String youtubeID){
        this.vidTitle = vidTitle;
        this.url= url;
        this.youtubeID = youtubeID;
    }


    public int getmID(){ return mID; }
    public void setmID(int mID){ this.mID = mID;}

    public Uri getUri(){ return uri;}
    public void setUri(Uri uri) {this.uri = uri;}

    public String getUrl(){ return url;}
    public void setUrl(String url){this.url = url;}



    public String getVidTitle(){ return vidTitle;}
    public void setVidTitle(String vidTitle){this.vidTitle = vidTitle;}


}
