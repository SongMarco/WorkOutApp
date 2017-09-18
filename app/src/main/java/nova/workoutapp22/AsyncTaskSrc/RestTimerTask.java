package nova.workoutapp22.AsyncTaskSrc;


import android.animation.ObjectAnimator;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.TextView;

import nova.workoutapp22.PlayWorkoutActivity;
import nova.workoutapp22.R;
import nova.workoutapp22.subSources.KeySet;

import static nova.workoutapp22.PlayWorkoutActivity.buttonPause;
import static nova.workoutapp22.PlayWorkoutActivity.buttonReset;
import static nova.workoutapp22.PlayWorkoutActivity.buttonResume;
import static nova.workoutapp22.PlayWorkoutActivity.currentSet;
import static nova.workoutapp22.PlayWorkoutActivity.donutProgress;
import static nova.workoutapp22.PlayWorkoutActivity.stTotalRestTime;
import static nova.workoutapp22.PlayWorkoutActivity.stopWatchTask;
import static nova.workoutapp22.PlayWorkoutActivity.taskMode;
import static nova.workoutapp22.PlayWorkoutActivity.totalSet;
import static nova.workoutapp22.PlayWorkoutActivity.workoutTimerTask;
import static nova.workoutapp22.subSources.KeySet.INT_SWSECOND;

public class RestTimerTask extends AsyncTask<Void, Void, String> {
    private static final String RESULT_SUCCESS = "1";
    private static final String RESULT_FAIL = "0";
    private static final int TEXT_COLOR_NORMAL = 0xFF000000;
    private static final int TEXT_COLOR_FINISHED = 0XFFFF0000;

    public static ObjectAnimator animatorRest ;

    private TextView tvTimer = null;
    private TextView countDown = null;

    TextView woSetPl;
    TextView tvTitle, tvTimeTitle;
    private Button buttonStart, buttonSetDone;


    private int time = -1;
    private int totalRestTime = -1;

    private int timerMode = -1;

    int totalWorkoutTime;

    boolean isFirst = true;
    boolean isCountDone = false;
    boolean isResumed = false;

    String timerSetting;

    MediaPlayer mp;

    public void setTimerMode(int timerMode) {

        this.timerMode = timerMode;
    }

    //쉬는 시간의 경우 타이머 / 스톱워치 / 아무것도 안씀을 구분해야!!
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void setViewAndTimerSetting() {

        if(!animatorRest.isPaused()){
            animatorRest = ObjectAnimator.ofFloat(donutProgress, "progress", 100, 0);
            animatorRest.setInterpolator(new LinearInterpolator());
        }




        tvTimer = (TextView) PlayWorkoutActivity.getInstance().findViewById(R.id.textViewTimerSetPl);
        countDown = (TextView) PlayWorkoutActivity.getInstance().findViewById(R.id.textViewCountDown);

        buttonStart = (Button) PlayWorkoutActivity.getInstance().findViewById(R.id.buttonStartWoPl);
        buttonSetDone = (Button) PlayWorkoutActivity.getInstance().findViewById(R.id.buttonSetDonePl);

        woSetPl = (TextView) PlayWorkoutActivity.getInstance().findViewById(R.id.textViewSetPl);
        tvTitle = (TextView) PlayWorkoutActivity.getInstance().findViewById(R.id.textViewCountDown);

        tvTimeTitle = (TextView) PlayWorkoutActivity.getInstance().findViewById(R.id.textViewTimeTitlePl);

        totalWorkoutTime = PlayWorkoutActivity.getInstance().getTotalWorkoutTime();
        timerSetting = PlayWorkoutActivity.getInstance().getTimerSetting();
    }

    public void setTime(int timeInSec) {
        stTotalRestTime = timeInSec*100;
        this.totalRestTime = timeInSec*100;
        this.time = timeInSec*100;

        animatorRest.setDuration(totalRestTime*10);
    }
    public void resumeRestTime(int time){

        this.totalRestTime = stTotalRestTime;
        this.time = time;
    }
    @Override
    protected void onPreExecute() {

        taskMode = KeySet.MODE_REST_TIMER;
//        tvTimer.setTextColor(TEXT_COLOR_NORMAL);
        isFirst = true;


        MediaPlayer mp;
        tvTimeTitle.setText("쉬는 시간");
        tvTimer.setText(formatTime(time));
        buttonResume.setVisibility(View.GONE);
        buttonPause.setVisibility(View.VISIBLE);
        buttonReset.setVisibility(View.VISIBLE);


        currentSet++;
        tvTitle.setText(currentSet + "세트를 준비하세요!");

        ///////////도넛츠 초기화

//        donutProgress.setProgress( ((float)time/(float) totalRestTime)*100  );
        donutProgress.setVisibility(View.VISIBLE);

    }

    @Override
    protected String doInBackground(Void... params) {
        while (time > 0) {
            try {

//todo sleepthread 1000으로 고칠것, mp.start 주석 해제할것
                Thread.sleep(INT_SWSECOND);
                time--;


                publishProgress();
            } catch (InterruptedException e) {
                return RESULT_FAIL;
            }
        }
        return RESULT_SUCCESS;
    }

    @Override
    protected void onProgressUpdate(Void... values) {




        if (time <= 300 && !isCountDone && (currentSet <= totalSet)) {
            mp = MediaPlayer.create(PlayWorkoutActivity.getInstance(), R.raw.go3);
            mp.start();
            isCountDone = true;
        }
        if(!animatorRest.isRunning()){
            animatorRest.start();

        }



//        if(time>0 && time <=3){
//            countDown.setText( String.valueOf(time));
//        }
        if (time == 0) {


            countDown.setText(currentSet + "세트 운동하세요!!!");


            return;

        }


        tvTimer.setText(formatTime(time));

    }

    @Override
    protected void onPostExecute(String result) {

        tvTimer.setText("GO!!!");

        woSetPl.setText("세트 : " + currentSet + "/" + totalSet);

        buttonResume.setVisibility(View.GONE);
        buttonPause.setVisibility(View.GONE);
        buttonReset.setVisibility(View.GONE);

        if (timerSetting.equals(KeySet.STRING_NOTIMER)) {

            buttonSetDone.setText(currentSet + "세트 완료!");
            buttonStart.setVisibility(View.GONE);
            buttonSetDone.setVisibility(View.VISIBLE);
        } else if (timerSetting.equals(KeySet.STRING_TIMER)) {


            //아직 운동중이다!

            tvTimer.setText("GO!!!");

            woSetPl.setText("세트 : " + currentSet + "/" + totalSet);
//            buttonStart.setText(currentSet + "세트 운동 시작!");


            buttonSetDone.setVisibility(View.GONE);


            workoutTimerTask = new WorkoutTimerTask();
            workoutTimerTask.setView();
            workoutTimerTask.setWorkoutTime(totalWorkoutTime);
            workoutTimerTask.execute();

        } else if (timerSetting.equals(KeySet.STRING_STOPWATCH)) {


            tvTimeTitle.setText(KeySet.STRING_STOPWATCH);

            buttonStart.setText(currentSet + "세트 운동 시작!");



            buttonStart.setVisibility(View.GONE);
            buttonSetDone.setVisibility(View.VISIBLE);
            stopWatchTask = new StopWatchTask();
            stopWatchTask.setView();
            stopWatchTask.execute();

        }


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


        String sEll = String.format("%02d:%02d:%02d",  time/100/60, time / 100, time % 100);

        return sEll;

    }

    public int getTime() {
        return time;
    }

    public void setIsCountdone(Boolean bool) {
        this.isCountDone = bool;
    }

    public void pauseMp(){
//        mp.release();
    }

    public void setResumed(){

        isResumed = true;


    }

}
