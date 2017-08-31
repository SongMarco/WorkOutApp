package nova.workoutapp22;

import android.graphics.Bitmap;
import android.net.Uri;

/**
 * Created by user on 2016-08-10.
 */
public class MemoItem {

    int mID;
    String memo;
    String date;
    int resId;

    Uri uri;
    Bitmap bitmap;


    public MemoItem(String memo, String date, int resId) {
        this.memo = memo;
        this.date = date;
        this.resId = resId;
    }

    public MemoItem(String memo, String date, Bitmap bitmap) {
        this.memo = memo;
        this.date = date;
        this.bitmap = bitmap;
    }

    public MemoItem(String memo, String date, Uri uri) {
        this.memo = memo;
        this.date = date;
        this.uri = uri;
    }

    public int getmID(){ return mID; }

    public void setmID(int mID){ this.mID = mID;}


    public Bitmap getBitmap(){ return bitmap; }

    public void setBitmap(Bitmap bm){ this.bitmap = bm; }



    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

////////////////////////////////////////////////////////////// 메모 관련 내용들

    public Uri getUri(){ return uri;}

    public void setUri(Uri uri){this.uri = uri;}


    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getDate() {   return date;    }

    public void setDate(String date) {
        this.date = date;
    }
}

