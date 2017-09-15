package nova.workoutapp22.AsyncTaskSrc;

import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import nova.workoutapp22.PlayWorkoutActivity;
import nova.workoutapp22.R;

import static nova.workoutapp22.PlayWorkoutActivity.currentSet;
import static nova.workoutapp22.PlayWorkoutActivity.restTimerTask;
import static nova.workoutapp22.PlayWorkoutActivity.totalSet;
import static nova.workoutapp22.subSources.KeySet.key_workoutName;

/**
 * Created by Administrator on 2017-09-15.
 */

public class WorkoutTimerTask extends AsyncTask<Void, Void, String> {
    private static final String RESULT_SUCCESS = "1";
    private static final String RESULT_FAIL = "0";
    private static final int TEXT_COLOR_NORMAL = 0xFF000000;
    private static final int TEXT_COLOR_FINISHED = 0XFFFF0000;


    private TextView timer = null;
    private TextView countDown = null;

    private TextView woSetPl = null;
    private TextView tvTitle = null;
    private int totalRestSec = 0;

    private Button buttonStart, buttonSetDone;



    private int time = -1;

    private int timerMode = -1;

    private String timerSetting;

    boolean isFirst = true;
    boolean isCountDone = false;

    MediaPlayer mp;

    public void setTimerMode(int timerMode){

        this.timerMode = timerMode;
    }


    public void setView() {
        timer = (TextView) PlayWorkoutActivity.getInstance().findViewById(R.id.textViewTimerSetPl);
        countDown = (TextView) PlayWorkoutActivity.getInstance().findViewById(R.id.textViewCountDown);

        buttonStart = (Button)PlayWorkoutActivity.getInstance().findViewById(R.id.buttonStartWoPl);
        buttonSetDone = (Button)PlayWorkoutActivity.getInstance().findViewById(R.id.buttonSetDonePl);

        woSetPl = (TextView)PlayWorkoutActivity.getInstance().findViewById(R.id.textViewSetPl);
        tvTitle = (TextView)PlayWorkoutActivity.getInstance().findViewById(R.id.textViewCountDown);
        totalRestSec = PlayWorkoutActivity.getInstance().getTotalRestSec();

    }

    public void setWorkoutTime(int time) {
        this.time = time;
    }

    @Override
    protected void onPreExecute() {
        timer.setText("타이머 \n" + formatTime(time));
        timer.setTextColor(TEXT_COLOR_NORMAL);
        isFirst = true;
    }

    @Override
    protected String doInBackground(Void... params) {



        while (time > 0) {
            try {


                Thread.sleep(1000);
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

        if (time <= 3 && !isCountDone) {
            //TODO 3 2 1 삐삐삐소리 추가해주기

            isCountDone = true;
        }

//        if(time>0 && time <=3){
//            countDown.setText( String.valueOf(time));
//        }
        if(time ==0){


            timer.setText("쉬는 시간!");



            return;

        }


        timer.setText("운동 시간 \n" + formatTime(time));

    }

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
            restTimerTask.setTime(totalRestSec);

            restTimerTask.execute();
        }


//        if (result.equals(RESULT_SUCCESS))
//            timer.setTextColor(TEXT_COLOR_FINISHED);
    }

    public void makeBeep() {
//        mp = MediaPlayer.create(PlayWorkoutActivity.getInstance(), R.raw.beep);
//        mp.start();

    }

    protected void releaseBeep() {
//        mp.release();

    }

    String formatTime(int time) {


        String sEll = String.format("%02d:%02d", time / 60, time % 60);

        return sEll;

    }

    public int getTime() {
        return time;
    }

}


