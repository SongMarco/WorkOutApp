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


import static nova.workoutapp22.subSources.BasicInfo.RESULT_FAIL;
import static nova.workoutapp22.subSources.BasicInfo.RESULT_SUCCESS;
import static nova.workoutapp22.subSources.KeySet.key_boolTimeSet;
import static nova.workoutapp22.subSources.KeySet.key_currentSet;
import static nova.workoutapp22.subSources.KeySet.key_hour;
import static nova.workoutapp22.subSources.KeySet.key_min;
import static nova.workoutapp22.subSources.KeySet.key_sec;
import static nova.workoutapp22.subSources.KeySet.key_workoutName;
import static nova.workoutapp22.subSources.KeySet.key_workoutNum;
import static nova.workoutapp22.subSources.KeySet.key_workoutSet;

public class PlayWorkoutActivity extends AppCompatActivity {


    Toolbar myToolbar;

    TextView woNamePl, woNumTimePl, woSetPl;

    TextView tvCountDown;

    int currentSet, totalSet;
    int hour, min, sec;
    Boolean isTimeSet;

    Button buttonStart;
    Button buttonSetDone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_workout);


////////// 툴바 관련 세팅
        myToolbar = (Toolbar) findViewById(R.id.toolbarPlayWoAct);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ///////////////

        //region 뷰 관련 세팅 리전
        /////////뷰 관련 세팅

        woNamePl = (TextView)findViewById(R.id.textViewWoNamePl);
        woSetPl = (TextView)findViewById(R.id.textViewSetPl);
        woNumTimePl = (TextView)findViewById(R.id.textViewNumPl);

        tvCountDown = (TextView)findViewById(R.id.textViewCountDown);



        Intent intentReceived = getIntent();



        woNamePl.setText( intentReceived.getStringExtra(key_workoutName) + " : " );



        currentSet = intentReceived.getIntExtra(key_currentSet, 0);
        totalSet = Integer.valueOf(intentReceived.getStringExtra(key_workoutSet) );


        woSetPl.setText("세트 : "+ currentSet + "/" + totalSet );


        isTimeSet = intentReceived.getBooleanExtra(key_boolTimeSet, false);

        //시간 세팅이다.
        if(isTimeSet == true) {



            hour = intentReceived.getIntExtra(key_hour, 0);
            min = intentReceived.getIntExtra(key_min, 0);
            sec = intentReceived.getIntExtra(key_sec, 0);

            String outputTime = "";

            if( hour != 0){
                outputTime = outputTime+hour+"시간";
            }

            if( min != 0){
                outputTime = outputTime+min+"분";
            }

            if( sec != 0){
                outputTime = outputTime+sec+"초";
            }


            woNumTimePl.setText( "세트 당 "+outputTime+" 운동"  );
        }

        //갯수 세팅이다.
        else{

            woNumTimePl.setText( "세트 당 "+intentReceived.getStringExtra(key_workoutNum) +"회 운동" );
        }
        //endregion

        buttonStart = (Button)findViewById(R.id.buttonStartWo);
        buttonSetDone = (Button)findViewById(R.id.buttonSetDone);

        findViewById(R.id.buttonStartWo).setOnClickListener(plClickListener);
        findViewById(R.id.buttonSetDone).setOnClickListener(plClickListener);



    }


    //region 버튼 클릭 관련 구역
    Button.OnClickListener plClickListener = new View.OnClickListener() {
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.buttonStartWo:



                    new CountdownTask().execute(Long.parseLong("3"));

                    break;
                case R.id.buttonSetDone:

                    //current = total이라면 운동이 완료된 것이다. 운동을 마치고 운동 화면으로 돌아가자.
                    if(currentSet == totalSet){
                        buttonStart.setVisibility(View.VISIBLE);
                        buttonSetDone.setVisibility(View.INVISIBLE);
                        finish();

                    }


                    //아직 운동이 진행중이다.
                    else{
                        currentSet++;
                        woSetPl.setText("세트 : "+ currentSet + "/" + totalSet );
                        buttonStart.setText(currentSet+"세트 운동 시작!");

                        tvCountDown.setText(currentSet+"세트를 준비하세요!");

                        buttonStart.setVisibility(View.VISIBLE);
                        buttonSetDone.setVisibility(View.INVISIBLE);


                    }


                    break;

            }
        }
    };
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
    class CountdownTask extends AsyncTask<Long, Long, Long> {

        long time;
        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected void onPostExecute(Long result) {
            super.onPostExecute(result);
            tvCountDown.setText("GO !!!");

            buttonSetDone.setText(currentSet+"세트 완료!");
            buttonStart.setVisibility(View.INVISIBLE);
            buttonSetDone.setVisibility(View.VISIBLE);

        }

        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Long... values) {
            super.onProgressUpdate(values);

            tvCountDown.setText(""+time);

        }

        @Override
        protected Long doInBackground(Long... params) {
            time = params[0]+1;

            MediaPlayer.create(getApplicationContext(), R.raw.go).start();

            while(time > 0) {
                try {
                    Thread.sleep(900);         // one second sleep
                    time--;                     // decrement time
                    publishProgress();          // trigger onProgressUpdate()
                } catch(InterruptedException e) {
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

}
