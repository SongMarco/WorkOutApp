package nova.workoutapp22;


import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import static nova.workoutapp22.PlayWorkoutActivity.currentSet;

public class RestTimerTask extends AsyncTask<Void, Void, String> {
    private static final String RESULT_SUCCESS = "1";
    private static final String RESULT_FAIL = "0";
    private static final int TEXT_COLOR_NORMAL = 0xFF000000;
    private static final int TEXT_COLOR_FINISHED = 0XFFFF0000;


    private TextView timer = null;
    private TextView countDown = null;

    private Button buttonStart, buttonSetDone;



    private int time = -1;

    private int timerMode = -1;

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

    }

    public void setTime(int time) {
        this.time = time;
    }

    @Override
    protected void onPreExecute() {
        timer.setText("쉬는 시간 \n" + formatTime(time));
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
            MediaPlayer.create(PlayWorkoutActivity.getInstance(), R.raw.go3).start();
            isCountDone = true;
        }

//        if(time>0 && time <=3){
//            countDown.setText( String.valueOf(time));
//        }
      if(time ==0){

          countDown.setText( "운동하세요!!!");

          timer.setText("운동시간 세팅 안함");
          return;

        }


        timer.setText("쉬는 시간 \n" + formatTime(time));

    }

    @Override
    protected void onPostExecute(String result) {

        buttonSetDone.setText(currentSet+"세트 완료!");
        buttonStart.setVisibility(View.INVISIBLE);
        buttonSetDone.setVisibility(View.VISIBLE);

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
