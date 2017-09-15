package nova.workoutapp22.AsyncTaskSrc;


import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import nova.workoutapp22.PlayWorkoutActivity;
import nova.workoutapp22.R;
import nova.workoutapp22.subSources.KeySet;

import static nova.workoutapp22.PlayWorkoutActivity.currentSet;
import static nova.workoutapp22.PlayWorkoutActivity.totalSet;
import static nova.workoutapp22.PlayWorkoutActivity.workoutTimerTask;

public class RestTimerTask extends AsyncTask<Void, Void, String> {
    private static final String RESULT_SUCCESS = "1";
    private static final String RESULT_FAIL = "0";
    private static final int TEXT_COLOR_NORMAL = 0xFF000000;
    private static final int TEXT_COLOR_FINISHED = 0XFFFF0000;


    private TextView tvTimer = null;
    private TextView countDown = null;

    TextView woSetPl;
    TextView tvTitle;
    private Button buttonStart, buttonSetDone;


    private int time = -1;

    private int timerMode = -1;

    int totalWorkoutTime;

    boolean isFirst = true;
    boolean isCountDone = false;

    String timerSetting;

    MediaPlayer mp;

    public void setTimerMode(int timerMode) {

        this.timerMode = timerMode;
    }

    //쉬는 시간의 경우 타이머 / 스톱워치 / 아무것도 안씀을 구분해야!!
    public void setViewAndTimerSetting() {
        tvTimer = (TextView) PlayWorkoutActivity.getInstance().findViewById(R.id.textViewTimerSetPl);
        countDown = (TextView) PlayWorkoutActivity.getInstance().findViewById(R.id.textViewCountDown);

        buttonStart = (Button) PlayWorkoutActivity.getInstance().findViewById(R.id.buttonStartWoPl);
        buttonSetDone = (Button) PlayWorkoutActivity.getInstance().findViewById(R.id.buttonSetDonePl);

        woSetPl = (TextView) PlayWorkoutActivity.getInstance().findViewById(R.id.textViewSetPl);
        tvTitle = (TextView) PlayWorkoutActivity.getInstance().findViewById(R.id.textViewCountDown);

        totalWorkoutTime = PlayWorkoutActivity.getInstance().getTotalWorkoutTime();
        timerSetting = PlayWorkoutActivity.getInstance().getTimerSetting();
    }

    public void setTime(int time) {
        this.time = time;
    }

    @Override
    protected void onPreExecute() {
        tvTimer.setText("쉬는 시간 \n" + formatTime(time));
        tvTimer.setTextColor(TEXT_COLOR_NORMAL);
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

        if (time <= 3 && !isCountDone && (currentSet != totalSet)) {
            MediaPlayer.create(PlayWorkoutActivity.getInstance(), R.raw.go3).start();
            isCountDone = true;
        }

//        if(time>0 && time <=3){
//            countDown.setText( String.valueOf(time));
//        }
        if (time == 0) {

            countDown.setText("운동하세요!!!");


            return;

        }


        tvTimer.setText("쉬는 시간 \n" + formatTime(time));

    }

    @Override
    protected void onPostExecute(String result) {

        if (timerSetting.equals(KeySet.STRING_NOTIMER)) {
            buttonSetDone.setText(currentSet + "세트 완료!");
            buttonStart.setVisibility(View.INVISIBLE);
            buttonSetDone.setVisibility(View.VISIBLE);
        } else if (timerSetting.equals(KeySet.STRING_TIMER)) {


            //아직 운동중이다!

            tvTimer.setText("GO!!!");

            currentSet++;
            woSetPl.setText("세트 : " + currentSet + "/" + totalSet);
            buttonStart.setText(currentSet + "세트 운동 시작!");

            tvTitle.setText(currentSet + "세트를 준비하세요!");

            buttonStart.setVisibility(View.VISIBLE);
            buttonSetDone.setVisibility(View.INVISIBLE);

            workoutTimerTask = new WorkoutTimerTask();
            workoutTimerTask.setView();
            workoutTimerTask.setWorkoutTime(totalWorkoutTime);
            workoutTimerTask.execute();


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


        String sEll = String.format("%02d:%02d", time / 60, time % 60);

        return sEll;

    }

    public int getTime() {
        return time;
    }

    public void setIsCountdone(Boolean bool){
        this.isCountDone= bool;
    }

}
