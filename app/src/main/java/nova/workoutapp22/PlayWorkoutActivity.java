package nova.workoutapp22;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import nova.workoutapp22.AsyncTaskSrc.RestTimerTask;
import nova.workoutapp22.AsyncTaskSrc.StopWatchTask;
import nova.workoutapp22.AsyncTaskSrc.WorkoutTimerTask;

import static nova.workoutapp22.subSources.BasicInfo.RESULT_FAIL;
import static nova.workoutapp22.subSources.BasicInfo.RESULT_SUCCESS;
import static nova.workoutapp22.subSources.KeySet.MODE_NULL;
import static nova.workoutapp22.subSources.KeySet.MODE_STOPWATCH;
import static nova.workoutapp22.subSources.KeySet.STRING_STOPWATCH;
import static nova.workoutapp22.subSources.KeySet.STRING_TIMER;
import static nova.workoutapp22.subSources.KeySet.key_boolTimeSet;
import static nova.workoutapp22.subSources.KeySet.key_currentSet;
import static nova.workoutapp22.subSources.KeySet.key_hour;
import static nova.workoutapp22.subSources.KeySet.key_min;
import static nova.workoutapp22.subSources.KeySet.key_restMin;
import static nova.workoutapp22.subSources.KeySet.key_restSec;
import static nova.workoutapp22.subSources.KeySet.key_sec;
import static nova.workoutapp22.subSources.KeySet.key_timerMode;
import static nova.workoutapp22.subSources.KeySet.key_timerSetting;
import static nova.workoutapp22.subSources.KeySet.key_workoutName;
import static nova.workoutapp22.subSources.KeySet.key_workoutNum;
import static nova.workoutapp22.subSources.KeySet.key_workoutSet;

public class PlayWorkoutActivity extends AppCompatActivity {

    private static PlayWorkoutActivity instance;

    public static PlayWorkoutActivity getInstance() {
        return instance;
    }

    Toolbar myToolbar;

    TextView woNamePl, woNumTimePl, woSetPl;

    TextView tvTitle, tvTimer;

    public static WorkoutTimerTask workoutTimerTask;
    public static RestTimerTask restTimerTask;
    public static StopWatchTask stopWatchTask;
    WorkoutStartTask startTask;


    public static int currentSet, totalSet;
    int hour, min, sec;


    int restMin = 0;
    int restSec = 0;
    int totalRestSec = 0;
    int totalWorkoutTime = 0;
    int timerMode = -1;
    int pauseWoTime = -1;
    int pauseRestTime = -1;

    String timerSetting;


    Boolean isTimeSet;

    Button buttonStart;
    Button buttonSetDone, buttonPause, buttonReset, buttonResume;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //아래 문장이 없으면 getInstance시 null 반환함.
        instance = this;
        setContentView(R.layout.activity_play_workout);


////////// 툴바 관련 세팅
        myToolbar = (Toolbar) findViewById(R.id.toolbarPlayWoAct);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ///////////////

        //region 뷰 관련 세팅 리전
        /////////뷰 관련 세팅

        woNamePl = (TextView) findViewById(R.id.textViewWoNamePl);
        woSetPl = (TextView) findViewById(R.id.textViewSetPl);
        woNumTimePl = (TextView) findViewById(R.id.textViewNumPl);

        tvTitle = (TextView) findViewById(R.id.textViewCountDown);
        tvTimer = (TextView) findViewById(R.id.textViewTimerSetPl);


        Intent intentReceived = getIntent();


        woNamePl.setText(intentReceived.getStringExtra(key_workoutName) + " : ");


        currentSet = intentReceived.getIntExtra(key_currentSet, 0);
        totalSet = Integer.valueOf(intentReceived.getStringExtra(key_workoutSet));


        woSetPl.setText("세트 : " + currentSet + "/" + totalSet);


        isTimeSet = intentReceived.getBooleanExtra(key_boolTimeSet, false);

        timerMode = intentReceived.getIntExtra(key_timerMode, MODE_NULL);
        timerSetting = intentReceived.getStringExtra(key_timerSetting);

//        Toast.makeText(instance, "timermode = "+timerMode, Toast.LENGTH_SHORT).show();

        //쉬는 시간 세팅이다.

        restMin = intentReceived.getIntExtra(key_restMin, 0);
        restSec = intentReceived.getIntExtra(key_restSec, 0);
        totalRestSec = 60 * restMin + restSec;


        String outputRestTime = "";

        if (restMin == 0 && restSec == 0) {
            outputRestTime = "없음";
        }

        if (restMin != 0) {
            outputRestTime = outputRestTime + restMin + "분";
        }
        if (restSec != 0) {
            outputRestTime = outputRestTime + restSec + "초";
        }


        ((TextView) findViewById(R.id.textViewRestTimePl)).setText("쉬는시간 : " + outputRestTime);


        //시간운동 세팅이다.
        if (isTimeSet == true) {

            hour = intentReceived.getIntExtra(key_hour, 0);
            min = intentReceived.getIntExtra(key_min, 0);
            sec = intentReceived.getIntExtra(key_sec, 0);

            String outputTime = "";

            if (hour != 0) {
                outputTime = outputTime + hour + "시간";
            }

            if (min != 0) {
                outputTime = outputTime + min + "분";
            }

            if (sec != 0) {
                outputTime = outputTime + sec + "초";
            }

            totalWorkoutTime = 3600 * hour + 60 * min + sec;

            String sEll = String.format("%02d:%02d:%02d", totalWorkoutTime / 3600, totalWorkoutTime / 60, totalWorkoutTime % 60);
            tvTimer.setText(sEll);

            woNumTimePl.setText("세트 당 " + outputTime + " 운동");
        }
        //갯수 세팅이다.
        //TODO 갯수 세팅이어도 갈라져야 한다. - 어떻게? SW/TMR/아무것도 X
        else {

            //타이머를 사용한 횟수운동
            if (timerSetting.equals(STRING_TIMER)) {

                hour = intentReceived.getIntExtra(key_hour, 0);
                min = intentReceived.getIntExtra(key_min, 0);
                sec = intentReceived.getIntExtra(key_sec, 0);

                totalWorkoutTime = 3600 * hour + 60 * min + sec;

                String sEll = String.format("%02d:%02d:%02d", totalWorkoutTime / 3600, totalWorkoutTime / 60, totalWorkoutTime % 60);
                tvTimer.setText(sEll);

            } else if (timerMode == MODE_STOPWATCH) {

            } else {

            }


            woNumTimePl.setText("세트 당 " + intentReceived.getStringExtra(key_workoutNum) + "회 운동");
        }
        //endregion

        buttonStart = (Button) findViewById(R.id.buttonStartWoPl);
        buttonSetDone = (Button) findViewById(R.id.buttonSetDonePl);
        buttonReset = (Button) findViewById(R.id.buttonResetPl);
        buttonPause = (Button) findViewById(R.id.buttonPausePl);
        buttonResume = (Button) findViewById(R.id.buttonResumePl);


        findViewById(R.id.buttonStartWoPl).setOnClickListener(plClickListener);
        findViewById(R.id.buttonSetDonePl).setOnClickListener(plClickListener);
        findViewById(R.id.buttonPausePl).setOnClickListener(plClickListener);
        findViewById(R.id.buttonResetPl).setOnClickListener(plClickListener);
        buttonResume.setOnClickListener(plClickListener);
    }


    //region 버튼 클릭 관련 구역
    Button.OnClickListener plClickListener = new View.OnClickListener() {
        public void onClick(View v) {

            switch (v.getId()) {


                //스타트버튼을 눌렀다.
                case R.id.buttonStartWoPl:


                    buttonStart.setOnTouchListener(null);
                    buttonStart.setFocusable(false);

                    tvTitle.setText("NO PAIN, NO GAIN");


                    //카운트다운 태스크에서 운동을 시작해준다.
                    startTask = new WorkoutStartTask();
                    startTask.execute(Long.parseLong("3"));


                    break;
                case R.id.buttonSetDonePl:

                    //current = total이라면 운동이 완료된 것이다. 운동을 마치고 운동 화면으로 돌아가자.
                    if (currentSet == totalSet) {
                        buttonStart.setVisibility(View.VISIBLE);
                        buttonSetDone.setVisibility(View.INVISIBLE);

                        Toast.makeText(PlayWorkoutActivity.this, getIntent().getStringExtra(key_workoutName) + " 운동 프로그램이 끝났습니다.", Toast.LENGTH_LONG).show();
                        finish();

                    }


                    //아직 운동이 진행중이다.
                    else if (timerSetting.equals(STRING_STOPWATCH)) {

                        if(stopWatchTask!=null){
                            stopWatchTask.cancel(true);
                        }

                        woSetPl.setText("세트 : " + currentSet + "/" + totalSet);


                        buttonSetDone.setText((currentSet+1)+"세트 완료!");
                        tvTitle.setText((currentSet+1) + "세트를 준비하세요!");


                        buttonStart.setVisibility(View.INVISIBLE);
                        buttonSetDone.setVisibility(View.INVISIBLE);

                        RestTimerTask restTimer = new RestTimerTask();
                        restTimer.setViewAndTimerSetting();

                        restTimer.setTime(totalRestSec);

                        restTimer.execute();



                    } else {
                        currentSet++;
                        woSetPl.setText("세트 : " + currentSet + "/" + totalSet);
                        buttonStart.setText(currentSet + "세트 운동 시작!");

                        tvTitle.setText(currentSet + "세트를 준비하세요!");

                        buttonStart.setVisibility(View.VISIBLE);
                        buttonSetDone.setVisibility(View.INVISIBLE);

                        RestTimerTask restTimer = new RestTimerTask();
                        restTimer.setViewAndTimerSetting();

                        restTimer.setTime(totalRestSec);

                        restTimer.execute();


                        //TODO restTimer가 끝나면 바로 운동 시작이다


                    }


                    break;
                //일시정지
                case R.id.buttonPausePl:


                    startTask.cancel(true);

                    //
                    if (workoutTimerTask != null) {
                        Log.wtf("adad", "woTask canceled");

                        pauseWoTime = workoutTimerTask.getTime();
                        workoutTimerTask.cancel(true);


                    }
                    Log.wtf("adad", "restTask canceled");
                    if (restTimerTask != null) {

                        pauseRestTime = restTimerTask.getTime();

                        restTimerTask.cancel(true);
                    }


                    break;

                case R.id.buttonResumePl:

                    Toast.makeText(PlayWorkoutActivity.this, "운동이 일시정지되었습니다.", Toast.LENGTH_SHORT).show();
//                    Toast.makeText(PlayWorkoutActivity.this, "pausewotime = "+pauseWoTime, Toast.LENGTH_SHORT).show();
                    if (pauseWoTime != -1 && pauseWoTime != 0) {

                        Log.wtf("adad", "woTask resumed");

                        workoutTimerTask = new WorkoutTimerTask();
                        workoutTimerTask.setView();
                        workoutTimerTask.setWorkoutTime(pauseWoTime);
                        workoutTimerTask.execute();

                        pauseWoTime = -1;
                    } else if (pauseRestTime != -1) {

                        Log.wtf("adad", "restTask resumed");


                        restTimerTask = new RestTimerTask();
                        restTimerTask.setViewAndTimerSetting();

                        restTimerTask.setTime(pauseRestTime);

                        if (pauseRestTime <= 3) {
                            restTimerTask.setIsCountdone(true);
                        }

                        restTimerTask.execute();

                        pauseRestTime = -1;
                    }

                    break;

                case R.id.buttonResetPl:

                    Toast.makeText(PlayWorkoutActivity.this, "운동이 리셋되었습니다.", Toast.LENGTH_SHORT).show();

                    startTask.cancel(true);
                    if (workoutTimerTask != null) workoutTimerTask.cancel(true);


                    if (restTimerTask != null) restTimerTask.cancel(true);
                    initiationUI();


                    break;


            }
        }
    };

    public void initiationUI() {
        currentSet = 1;
        woSetPl.setText("세트 : " + currentSet + "/" + totalSet);

        //타이머를 사용한 횟수운동
        if (timerSetting.equals(STRING_TIMER)) {

            String sEll = String.format("%02d:%02d:%02d", totalWorkoutTime / 3600, totalWorkoutTime / 60, totalWorkoutTime % 60);
            tvTimer.setText(sEll);
        }


        tvTitle.setText("운동을 시작하세요!");

        buttonStart.setText("1세트 운동 시작!");
        buttonSetDone.setText("1세트 완료!");

        buttonStart.setVisibility(View.VISIBLE);
        buttonSetDone.setVisibility(View.INVISIBLE);


    }
    //endregion

    //region 툴바 아이템 선택 관련 구역
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    //endregion

    //region 카운트 다운 어싱크 관련 구역
    public class WorkoutStartTask extends AsyncTask<Long, Long, Long> {

        long time;

        MediaPlayer mp;

        @Override
        protected void onCancelled() {
            super.onCancelled();
            mp.release();

        }

        @Override
        protected void onPostExecute(Long result) {
            super.onPostExecute(result);

            tvTimer.setText("GO!!!");


            //여기서 타이머 모드 / 스탑워치 모드에 따라 다르게 뿌려줄 것이다.
            if (timerSetting.equals(STRING_TIMER)) {
//
//                tvTitle.setText("운동하세요!!!");
//                buttonSetDone.setText(currentSet + "세트 완료!");
//                buttonStart.setVisibility(View.INVISIBLE);
//                buttonSetDone.setVisibility(View.VISIBLE);


                workoutTimerTask = new WorkoutTimerTask();
                workoutTimerTask.setView();
                workoutTimerTask.setWorkoutTime(totalWorkoutTime);
                workoutTimerTask.execute();
            } else if (timerSetting.equals(STRING_STOPWATCH)) {


                buttonStart.setVisibility(View.INVISIBLE);
                buttonSetDone.setVisibility(View.VISIBLE);

                stopWatchTask = new StopWatchTask();
                stopWatchTask.setView();
                stopWatchTask.execute();


            } else {
                tvTimer.setText("운동시간 세팅 안함");
                tvTitle.setText("운동하세요!!!");

                buttonSetDone.setText(currentSet + "세트 완료!");
                buttonStart.setVisibility(View.INVISIBLE);
                buttonSetDone.setVisibility(View.VISIBLE);
            }


        }

        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Long... values) {
            super.onProgressUpdate(values);

            tvTimer.setText("" + time);

        }

        @Override
        protected Long doInBackground(Long... params) {
            time = params[0] + 1;

            mp = MediaPlayer.create(getApplicationContext(), R.raw.go);
            mp.start();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            while (time > 1) {
                try {
                    time--;            // decrement time
                    publishProgress();          // trigger onProgressUpdate()
                    Thread.sleep(1000);         // one second sleep


                } catch (InterruptedException e) {
                    Log.i("GUN", Log.getStackTraceString(e));
                    return RESULT_FAIL;
                }
            }
            return RESULT_SUCCESS;

//
////
////            for (long i = 1; i <= num; i++) {
////                result = result * i;
////            }
//
//            Log.d("test", "result:" + result);

        }
    }
    //endregion

    public int getTotalRestSec() {
        return totalRestSec;
    }

    public int getTotalWorkoutTime() {
        return totalWorkoutTime;
    }

    public String getTimerSetting() {
        return timerSetting;
    }
}
