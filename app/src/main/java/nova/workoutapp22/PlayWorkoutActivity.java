package nova.workoutapp22;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

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

    int currentSet, totalSet;
    int hour, min, sec;
    Boolean isTimeSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_workout);


////////// 툴바 관련 세팅
        myToolbar = (Toolbar) findViewById(R.id.toolbarPlayWoAct);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ///////////////

        /////////뷰 관련 세팅

        woNamePl = (TextView)findViewById(R.id.textViewWoNamePl);
        woSetPl = (TextView)findViewById(R.id.textViewSetPl);
        woNumTimePl = (TextView)findViewById(R.id.textViewNumPl);

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










    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
