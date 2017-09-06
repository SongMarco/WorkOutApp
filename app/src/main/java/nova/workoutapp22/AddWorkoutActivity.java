package nova.workoutapp22;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import nova.workoutapp22.subSources.BasicInfo;

public class AddWorkoutActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    EditText workoutName, workoutNum, workoutSet, etTimerSetting;
    String timerSetting, numOrTime;


    // EditText etWoName, etWoNum, etWoSet, etTimerSetting;

    int mIDForTransport;

    String strAddWoMode;
    boolean editFlag = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        clearMyPrefs();
        setContentView(R.layout.activity_add_workout);


        workoutName = (EditText) findViewById(R.id.editTextWorkOutName);
        workoutNum = (EditText) findViewById(R.id.editTextWNum);
        workoutSet = (EditText) findViewById(R.id.editTextWSet);



        Intent intent = getIntent();

        strAddWoMode = intent.getStringExtra(BasicInfo.KEY_ADDWO_MODE);

        setSpinnerTimer();

        setNumOrTime();


        //메모를 걍클릭 함(모드뷰), or 롱클릭 -> 수정누름 (MODE_MODIFY)
        // 기존 내용을 먼저 그려주고, 사용자의 입력을 저장해준다
        if (strAddWoMode.equals(BasicInfo.MODE_MODIFY) || strAddWoMode.equals(BasicInfo.MODE_VIEW)) {

            processIntent(getIntent());

        } else { // 새로 메모를 하려는 경우. 화면을 새로 그려준다.


            Button saveWoButton = (Button) findViewById(R.id.buttonSaveWo);
            saveWoButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {


                    saveAndSetResult();
                    Toast.makeText(getApplicationContext(), "입력 내용이 저장됩니다.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        }


    }

    //region @@@@ Spinner 관련 파트
    public void setNumOrTime() {
        Spinner spinner = (Spinner) findViewById(R.id.spinnerNumOrTime);
        spinner.setOnItemSelectedListener(this);


        ArrayAdapter<CharSequence> spinnerNumOrTime = ArrayAdapter.createFromResource(this,
                R.array.numOrTime, android.R.layout.simple_spinner_item);


        spinnerNumOrTime.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        spinner.setAdapter(spinnerNumOrTime);
    }

    public void setSpinnerTimer() {
        Spinner spinner = (Spinner) findViewById(R.id.spinnerTimerSetting);

        spinner.setOnItemSelectedListener(this);
        //스피너 레이아웃과 아이템 어레이를 적용
        ArrayAdapter<CharSequence> spinnerTimerAdapter = ArrayAdapter.createFromResource(this,
                R.array.timerSetting, android.R.layout.simple_spinner_item);

        //드롭다운 레이아웃을 적용한다.
        spinnerTimerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //어댑터를 스피너에 적용한다.
        spinner.setSelection(2);
        spinner.setAdapter(spinnerTimerAdapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        switch( parent.getId() ){
            case R.id.spinnerNumOrTime:

                numOrTime = (String)parent.getItemAtPosition(position);

                break;
            case R.id.spinnerTimerSetting:

                timerSetting = (String)parent.getItemAtPosition(position);

                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    //endregion


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (event.getAction() == KeyEvent.ACTION_DOWN) { //키 다운 액션 감지
            if (keyCode == KeyEvent.KEYCODE_BACK) { //BackKey 다운일 경우만 처리

                if (!(workoutName.getText().toString().equals("")))
                    editFlag = true;


                if (editFlag == false) { //메모부분이 비어있으면 저장하지 마라

                    // Toast.makeText(this, "editTextMemo.getText() = "+editTextMemo.getText(),Toast.LENGTH_SHORT).show();

                    Toast.makeText(this, "입력한 내용이 없어 저장되지 않습니다.", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    //  Toast.makeText(this, "editTextMemo.getText() = "+editTextMemo.getText(),Toast.LENGTH_SHORT).show();
                    saveAndSetResult();

                    finish();

                }


                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }


    public void processIntent(Intent intent) {

        workoutName.setText(intent.getExtras().getString("workoutName"));
        workoutNum.setText(intent.getExtras().getString("workoutNum"));
        workoutSet.setText(intent.getExtras().getString("workoutSet"));


        //TODO 타이머 정보 스피너로 만들어서 전달하기


        mIDForTransport = intent.getIntExtra("mID", 1);
        Log.v("mIDTrak", "mID = " + mIDForTransport);

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

        Toast.makeText(getApplicationContext(), "입력 내용이 저장됩니다.", Toast.LENGTH_SHORT).show();

        Intent intentForSave = new Intent();

        ///////////////////////////주의!!!! 반드시 mID를 돌려줘라!!

        intentForSave.putExtra("mID", mIDForTransport);

        intentForSave.putExtra("workoutName", workoutName.getText().toString());
        intentForSave.putExtra("workoutNum", workoutNum.getText().toString());
        intentForSave.putExtra("workoutSet", workoutSet.getText().toString());
        intentForSave.putExtra("timerSetting", timerSetting );
        intentForSave.putExtra("numOrTime", numOrTime );

        Log.wtf("spinnerLog", "numorTime = "+numOrTime);
     //   Toast.makeText(getApplicationContext(), "timerSetting = "+timerSetting, Toast.LENGTH_SHORT).show();



        setResult(RESULT_OK, intentForSave);
    }

    /////////////////////////////////////////생명주기 관련 파트////////
    ////////// onPause에서 입력하던 상태가 저장되며, onResume에서 입력하던 상태가 복원된다.

    //region Save/restore pref 관련 파트
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
        // saveStateWithGson();
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

        editor.putString("workoutName", workoutName.getText().toString());
        editor.putString("workoutNum", workoutNum.getText().toString());
        editor.putString("workoutSet", workoutSet.getText().toString());

        //TODO 타이머 스톱워치 관련 정보 추가할 것!!
        editor.putString("timerSetting", "스톱워치");


        editor.commit();

    }

    protected void restoreState() {

        Toast.makeText(getApplicationContext(), "restore called", Toast.LENGTH_SHORT).show();
        SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);

        if ((pref != null) && (pref.contains("workoutName"))) {
            String wName = pref.getString("workoutName", "");

            workoutName.setText(wName);
        }

        if ((pref != null) && (pref.contains("workoutNum"))) {

            String wNum = pref.getString("workoutNum", "");
            workoutNum.setText(wNum);

        }

        if ((pref != null) && pref.contains("workoutSet")) {

            String wSet = pref.getString("workoutSet", "");
            workoutSet.setText(wSet);

        }

        if (pref != null && pref.contains("timerSetting")) {

            String eTimer = pref.getString("timerSetting", "");

        }


    }


    protected void clearMyPrefs() {
        SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.commit();
    }
    //endregion
}
