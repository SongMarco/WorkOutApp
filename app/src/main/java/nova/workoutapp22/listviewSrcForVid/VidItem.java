package nova.workoutapp22.listviewSrcForVid;

import android.net.Uri;

/**
 * Created by Administrator on 2017-09-23.
 */

public class VidItem {

    public int mID;

    Uri uri;
    String thumbUrl;
    String vidTitle;
    String youtubeID;



    public VidItem(String vidTitle, Uri uri){
        this.vidTitle = vidTitle;
        this.uri = uri;
    }


    public VidItem(String vidTitle, String url, String youtubeID){
        this.vidTitle = vidTitle;
        this.thumbUrl = url;
        this.youtubeID = youtubeID;
    }


    public VidItem(String vidTitle, String youtubeID){
        this.vidTitle = vidTitle;
        this.thumbUrl = "https://img.youtube.com/vi/" + youtubeID + "/0.jpg";
        this.youtubeID = youtubeID;
    }


    public int getmID(){ return mID; }
    public void setmID(int mID){ this.mID = mID;}

    public Uri getUri(){ return uri;}
    public void setUri(Uri uri) {this.uri = uri;}

    public String getThumbUrl(){ return thumbUrl;}
    public void setThumbUrl(String thumbUrl){this.thumbUrl = thumbUrl;}


    public String getYoutubeID(){ return youtubeID;}
    public void setYoutubeID(String youtubeID){this.youtubeID = youtubeID;}


    public String getVidTitle(){ return vidTitle;}
    public void setVidTitle(String vidTitle){this.vidTitle = vidTitle;}


}
