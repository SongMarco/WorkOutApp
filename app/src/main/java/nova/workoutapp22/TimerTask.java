package nova.workoutapp22;


import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.widget.TextView;

public class TimerTask extends AsyncTask<Void, Void, String> {
    private static final String RESULT_SUCCESS = "1";
    private static final String RESULT_FAIL = "0";
    private static final int TEXT_COLOR_NORMAL = 0xFF000000;
    private static final int TEXT_COLOR_FINISHED = 0XFFFF0000;
    private TextView timer = null;
    private int time = -1;

    boolean isFirst = true;
    boolean isCountDone = false;

    MediaPlayer mp;
    public void setTextView(int textViewId) {
        timer = (TextView) PlayWorkoutActivity.getInstance().findViewById(R.id.textViewTimerSetPl);
    }

    public void setTime(int time) {
        this.time = time;
    }

    @Override
    protected void onPreExecute() {
        timer.setText(formatTime(time));
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
        if(time <= 3 && !isCountDone){
            MediaPlayer.create(PlayWorkoutActivity.getInstance(), R.raw.go3).start();
            isCountDone = true;
        }

        timer.setText(formatTime(time));

    }

    @Override
    protected void onPostExecute(String result) {
        if (result.equals(RESULT_SUCCESS))
            timer.setTextColor(TEXT_COLOR_FINISHED);
    }

    public void makeBeep() {
//        mp = MediaPlayer.create(PlayWorkoutActivity.getInstance(), R.raw.beep);
//        mp.start();

    }

    protected void releaseBeep(){
//        mp.release();

    }

    String formatTime(int time){


        String sEll = String.format("%02d:%02d:%02d",time/3600 ,time/ 60, time%60);

        return sEll;

    }

    public int getTime(){
        return time;
    }

}
