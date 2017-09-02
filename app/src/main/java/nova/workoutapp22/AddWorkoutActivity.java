package nova.workoutapp22;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import nova.workoutapp22.subSources.BasicInfo;

public class AddWorkoutActivity extends AppCompatActivity {
    EditText workoutName, workoutNum, workoutSet, etTimerSetting;



   // EditText etWoName, etWoNum, etWoSet, etTimerSetting;

    int mIDForTransport;

    String strAddWoMode;
    boolean editFlag = false;








    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        clearMyPrefs();
        setContentView(R.layout.activity_add_workout);




        workoutName = (EditText)findViewById(R.id.editTextWorkOutName);
        workoutNum = (EditText)findViewById(R.id.editTextWNum);
        workoutSet = (EditText)findViewById(R.id.editTextWSet);


        Intent intent = getIntent();

        strAddWoMode = intent.getStringExtra(BasicInfo.KEY_ADDWO_MODE);

        //메모를 걍클릭 함(모드뷰), or 롱클릭 -> 수정누름 (MODE_MODIFY)
        // 기존 내용을 먼저 그려주고, 사용자의 입력을 저장해준다
        if (strAddWoMode.equals(BasicInfo.MODE_MODIFY) || strAddWoMode.equals(BasicInfo.MODE_VIEW)) {

            processIntent(intent);

        } else { // 새로 메모를 하려는 경우. 화면을 새로 그려준다.

            clearMyPrefs();

            Button saveMemoButton = (Button) findViewById(R.id.buttonSaveMemo);
            saveMemoButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {


                    saveAndSetResult();
                    Toast.makeText(getApplicationContext(), "입력 내용이 저장됩니다.",Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        }





        (findViewById(R.id.buttonSaveWo)).setOnClickListener(mClickListener);
    }


    Button.OnClickListener mClickListener = new View.OnClickListener() {
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.buttonSaveWo:

                    saveAndSetResult();
                    finish();
                    break;
            }
        }
    };
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if( event.getAction() == KeyEvent.ACTION_DOWN ){ //키 다운 액션 감지
            if( keyCode == KeyEvent.KEYCODE_BACK ){ //BackKey 다운일 경우만 처리

                if ( !(workoutName.getText().toString().equals("")) )
                    editFlag = true;



                if( editFlag ==false){ //메모부분이 비어있으면 저장하지 마라

                    // Toast.makeText(this, "editTextMemo.getText() = "+editTextMemo.getText(),Toast.LENGTH_SHORT).show();

                    Toast.makeText(this, "입력한 내용이 없어 저장되지 않습니다.",Toast.LENGTH_SHORT).show();
                    finish();
                }

                else{
                    //  Toast.makeText(this, "editTextMemo.getText() = "+editTextMemo.getText(),Toast.LENGTH_SHORT).show();
                    saveAndSetResult();

                    finish();

                }



                return true;
            }
        }
        return super.onKeyDown( keyCode, event );
    }



    public void processIntent(Intent intent) {

        workoutName.setText( intent.getExtras().getString("workoutName") );
        workoutNum.setText(intent.getExtras().getString("workoutNum"));
        workoutSet.setText(intent.getExtras().getString("workoutSet"));


        Toast.makeText(getApplicationContext(), "수정 - 로드 완료",Toast.LENGTH_SHORT).show();

        //TODO 타이머 정보 스피너로 만들어서 전달하기


        mIDForTransport = intent.getIntExtra("mID", 1);


        Button button = (Button) findViewById(R.id.buttonSaveWo);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                saveAndSetResult();

                finish();
            }
        });


    }



    public void saveAndSetResult() {
        clearMyPrefs();

        Toast.makeText(getApplicationContext(), "입력 내용이 저장됩니다.",Toast.LENGTH_SHORT).show();

        Intent intent = new Intent();

        intent.putExtra("workoutName", workoutName.getText().toString() );
        intent.putExtra("workoutNum", workoutNum.getText().toString() );
        intent.putExtra("workoutSet", workoutSet.getText().toString() );
        intent.putExtra("timerSetting", "스톱워치 사용" );

        setResult(RESULT_OK, intent);
    }

    /////////////////////////////////////////생명주기 관련 파트////////
    ////////// onPause에서 입력하던 상태가 저장되며, onResume에서 입력하던 상태가 복원된다.


    @Override
    protected void onStart() {
        super.onStart();

        workoutName.setText(getIntent().getStringExtra("workoutName"));

    }

    @Override
    protected void onPause() {
        super.onPause();


        saveState();

        //Toast.makeText(this, "onPause called", Toast.LENGTH_SHORT).show();
        // saveState();
    }

    @Override
    protected void onStop() {
        super.onStop();


        //Toast.makeText(this, "onStop called", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();

        restoreState();
        // Toast.makeText(this, "onPause called", Toast.LENGTH_SHORT).show();

        //clearMyPrefs();

    }

    protected void saveState() {


        SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        editor.putString("workoutName", workoutName.getText().toString() );
        editor.putString("workoutNum", workoutNum.getText().toString() );
        editor.putString("workoutSet", workoutSet.getText().toString() );

        //TODO 타이머 스톱워치 관련 정보 추가할 것!!
        editor.putString("timerSetting", "스톱워치" );



        editor.commit();

    }

    protected void restoreState() {

        Toast.makeText(getApplicationContext(),"restore called", Toast.LENGTH_SHORT).show();
        SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);

        if ((pref != null) && (pref.contains("workoutName"))   ) {
            String wName = pref.getString("workoutName", "");

            workoutName.setText(wName);
        }

        if( (pref != null) &&(pref.contains("workoutNum"))  ){

            String wNum = pref.getString("workoutNum", "");
            workoutNum.setText(wNum);

        }

        if( (pref!=null) && pref.contains("workoutSet")){

            String wSet = pref.getString("workoutSet","");
            workoutSet.setText(wSet);

        }

        if ( pref!=null && pref.contains("timerSetting") ){

            String eTimer = pref.getString("timerSetting", "");

        }


    }


    protected void clearMyPrefs() {
        SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.commit();
    }
}
