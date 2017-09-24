package nova.workoutapp22.AsyncTaskSrc;

import android.animation.ObjectAnimator;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import nova.workoutapp22.PlayWorkoutActivity;
import nova.workoutapp22.R;
import nova.workoutapp22.subSources.KeySet;

import static nova.workoutapp22.PlayWorkoutActivity.buttonPause;
import static nova.workoutapp22.PlayWorkoutActivity.buttonResume;
import static nova.workoutapp22.PlayWorkoutActivity.currentSet;
import static nova.workoutapp22.PlayWorkoutActivity.donutProgress;
import static nova.workoutapp22.PlayWorkoutActivity.restTimerTask;
import static nova.workoutapp22.PlayWorkoutActivity.stTotalWorkoutTime;
import static nova.workoutapp22.PlayWorkoutActivity.taskMode;
import static nova.workoutapp22.PlayWorkoutActivity.totalSet;
import static nova.workoutapp22.PlayWorkoutActivity.workoutIsFirst;
import static nova.workoutapp22.subSources.KeySet.INT_SENTISECOND;
import static nova.workoutapp22.subSources.KeySet.LIMIT_ZERO;
import static nova.workoutapp22.subSources.KeySet.key_workoutName;

/**
 * Created by Administrator on 2017-09-15.
 */

public class WorkoutTimerTask extends AsyncTask<Void, Void, String> {
    private static final String RESULT_SUCCESS = "1";
    private static final String RESULT_FAIL = "0";
    private static final int TEXT_COLOR_NORMAL = 0xFF000000;
    private static final int TEXT_COLOR_FINISHED = 0XFFFF0000;

    public static ObjectAnimator animatorWorkout ;
    private TextView tvTimer = null;
    private TextView countDown = null;

    private TextView woSetPl = null;
    private TextView tvTitle = null;
    TextView tvTimeTitle;
    private int totalRestSec = 0;

    private Button buttonStart, buttonSetDone;



    private int time = -1;

    private int totalWorkoutTime = -1;
    private int timerMode = -1;

    private String timerSetting;



    boolean isCountDone = false;
    boolean isResumed = false;


    MediaPlayer mp;

    public void setTimerMode(int timerMode){

        this.timerMode = timerMode;
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void setView() {
        //퍼즈되지 않았다면 새로만들라 / 일시정지 -> 안만듬

        //시작되었다면 -> 안들어감 시작 안됨 -> 들어감
        if( animatorWorkout==null || !animatorWorkout.isStarted()){
            animatorWorkout= ObjectAnimator.ofFloat(donutProgress, "progress", 100, 0);
            animatorWorkout.setInterpolator(new LinearInterpolator());

        }



        tvTimer = (TextView) PlayWorkoutActivity.getInstance().findViewById(R.id.textViewTimerSetPl);
        countDown = (TextView) PlayWorkoutActivity.getInstance().findViewById(R.id.textViewCountDown);

        buttonStart = (Button)PlayWorkoutActivity.getInstance().findViewById(R.id.buttonStartWoPl);
        buttonSetDone = (Button)PlayWorkoutActivity.getInstance().findViewById(R.id.buttonSetDonePl);

        woSetPl = (TextView)PlayWorkoutActivity.getInstance().findViewById(R.id.textViewSetPl);
        tvTitle = (TextView)PlayWorkoutActivity.getInstance().findViewById(R.id.textViewCountDown);


        tvTimeTitle = (TextView)PlayWorkoutActivity.getInstance().findViewById(R.id.textViewTimeTitlePl);


    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void setWorkoutTime(int time) {
        stTotalWorkoutTime = time*1000;
        this.totalWorkoutTime = stTotalWorkoutTime;
        this.time = stTotalWorkoutTime;

        if(animatorWorkout != null && !animatorWorkout.isPaused()){
            animatorWorkout.setDuration(stTotalWorkoutTime);

        }


    }

    public void resumeWorkoutTime(int time){

        this.totalWorkoutTime = stTotalWorkoutTime;
        this.time = time;

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onPreExecute() {

        taskMode = KeySet.MODE_TIMER;
        tvTimeTitle.setText("타이머");
        tvTimer.setText(formatTime(time));
//        tvTimer.setText("타이머 \n" + formatTime(time));
//        tvTimer.setTextColor(TEXT_COLOR_NORMAL);


        buttonResume.setVisibility(View.GONE);
        buttonPause.setVisibility(View.VISIBLE);


        ///////////도넛츠 초기화

//        donutProgress.setProgress( ((float)time/(float) totalWorkoutTime)*100  );
        donutProgress.setVisibility(View.VISIBLE);

        if(animatorWorkout!=null && animatorWorkout.isPaused()){
            animatorWorkout.resume();

        }


//todo animation 시간 기준으로 sync 맞추기


    }

    @Override
    protected String doInBackground(Void... params) {



        while (time > LIMIT_ZERO) {
            try {



                Thread.sleep(INT_SENTISECOND);
                Log.wtf("timeTag", "time = "+time);
                time = (int)(totalWorkoutTime - animatorWorkout.getCurrentPlayTime() );




                publishProgress();
            } catch (InterruptedException e) {
                return RESULT_FAIL;
            }
        }

        return RESULT_SUCCESS;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        if(!animatorWorkout.isStarted() && workoutIsFirst ){

            animatorWorkout.start();

            workoutIsFirst = false;
        }



        if(time ==0){
            tvTimer.setText("쉬는 시간!");
            return;
        }

        tvTimer.setText(formatTime(time));

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onPostExecute(String result) {

        if(currentSet == totalSet){

            Toast.makeText(PlayWorkoutActivity.getInstance(), PlayWorkoutActivity.getInstance().
                    getIntent().getStringExtra(key_workoutName) + " 운동 프로그램이 끝났습니다.", Toast.LENGTH_LONG).show();
            PlayWorkoutActivity.getInstance().finish();

        }
        else{
            buttonSetDone.setText(currentSet+"세트 완료!");
            buttonStart.setVisibility(View.INVISIBLE);
            buttonSetDone.setVisibility(View.INVISIBLE);


            restTimerTask = new RestTimerTask();
            restTimerTask.setViewAndTimerSetting();
            totalRestSec = PlayWorkoutActivity.getInstance().getTotalRestSec();
            Log.v("tsc", "workout::restsec ="+totalRestSec);
            restTimerTask.setTime(totalRestSec);

            restTimerTask.execute();
        }

        workoutIsFirst = true;
//        if (result.equals(RESULT_SUCCESS))
//            tvTimer.setTextColor(TEXT_COLOR_FINISHED);
    }

    public void makeBeep() {
//        mp = MediaPlayer.create(PlayWorkoutActivity.getInstance(), R.raw.beep);
//        mp.start();

    }

    protected void releaseBeep() {
//        mp.release();

    }

    String formatTime(int time) {


//        String sEll = String.format("%02d:%02d:%02d",  time/100/60, time / 100, time % 100);



        //분:초:0.몇초
        String sEll = String.format("%02d:%02d:%02d", time/1000/60, (time/1000)%60, (time%1000)/10  );
        return sEll;

    }

    public int getTime() {
        return time;
    }

}


