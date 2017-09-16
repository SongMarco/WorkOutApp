package nova.workoutapp22.AsyncTaskSrc;

import android.os.AsyncTask;
import android.widget.Button;
import android.widget.TextView;

import nova.workoutapp22.PlayWorkoutActivity;
import nova.workoutapp22.R;

/**
 * Created by jamsy on 2017-09-16.
 */

public class StopWatchTask extends AsyncTask<Void, Void, String> {
    private static final String RESULT_SUCCESS = "1";
    private static final String RESULT_FAIL = "0";
    private static final int TEXT_COLOR_NORMAL = 0xFF000000;
    private static final int TEXT_COLOR_FINISHED = 0XFFFF0000;


    private TextView timer = null;
    private TextView countDown = null;

    private TextView woSetPl = null;
    private TextView tvTitle, tvTimeTitle;
    private int totalRestSec = 0;

    private Button buttonStart, buttonSetDone;



    private int time = -1;


    boolean isFirst = true;
    boolean isCountDone = false;


    public void setView() {
        timer = (TextView) PlayWorkoutActivity.getInstance().findViewById(R.id.textViewTimerSetPl);
        countDown = (TextView) PlayWorkoutActivity.getInstance().findViewById(R.id.textViewCountDown);

        buttonStart = (Button)PlayWorkoutActivity.getInstance().findViewById(R.id.buttonStartWoPl);
        buttonSetDone = (Button)PlayWorkoutActivity.getInstance().findViewById(R.id.buttonSetDonePl);

        woSetPl = (TextView)PlayWorkoutActivity.getInstance().findViewById(R.id.textViewSetPl);
        tvTitle = (TextView)PlayWorkoutActivity.getInstance().findViewById(R.id.textViewCountDown);
        totalRestSec = PlayWorkoutActivity.getInstance().getTotalRestSec();
        tvTimeTitle = (TextView)PlayWorkoutActivity.getInstance().findViewById(R.id.textViewTimeTitlePl);
    }

    public void setWorkoutTime(int time) {
        this.time = time;
    }

    @Override
    protected void onPreExecute() {
//        timer.setText("타이머 \n" + formatTime(time));
        timer.setTextColor(TEXT_COLOR_NORMAL);
        isFirst = true;
    }

    @Override
    protected String doInBackground(Void... params) {



        while (true) {
            try {


                Thread.sleep(1000);
                time++;


                publishProgress();
            } catch (InterruptedException e) {
                return RESULT_FAIL;
            }
        }
    }

    @Override
    protected void onProgressUpdate(Void... values) {


        timer.setText(formatTime(time));

    }

    @Override
    protected void onPostExecute(String result) {





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