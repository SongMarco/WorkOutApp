package nova.workoutapp22.subSources;

import android.app.Application;
import android.content.Context;

/**
 * Created by Administrator on 2017-09-02.
 */

public class MyApplication extends Application {

    private static Context context;

    public void onCreate() {
        super.onCreate();
        MyApplication.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return MyApplication.context;
    }
}