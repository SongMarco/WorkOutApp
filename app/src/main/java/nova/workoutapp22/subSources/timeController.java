package nova.workoutapp22.subSources;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by jamsy on 2017-08-31.
 */

public class timeController {


    public static String getTime(){
        String timeNow;

        String format = new String("yyyy년 MM월 dd일 HH:mm:ss");
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.KOREAN);

        timeNow = sdf.format(new Date());
        Log.v("timeLog", "time is = "+timeNow);

        return timeNow;


    }

    public static String getTimeCutSec(){
        String timeNowCut;

        String format = new String("yyyy년 MM월 dd일 HH:mm");
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.KOREAN);

        timeNowCut = sdf.format(new Date());
        Log.v("timeLog", "time is = "+timeNowCut);

        return timeNowCut;

    }
}
