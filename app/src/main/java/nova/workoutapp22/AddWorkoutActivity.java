package nova.workoutapp22;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import nova.workoutapp22.subSources.BasicInfo;

import static nova.workoutapp22.R.id.spinnerTimerSetting;

public class AddWorkoutActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    EditText workoutName, workoutNum, workoutSet, etTimerSetting;

    EditText etHour, etMin, etSec;

    String timerSetting, numOrTime;
    Spinner spinnerTimer, spinnerNumOrTime;

    boolean timeSetFlag;

    // EditText etWoName, etWoNum, etWoSet, etTimerSetting;

    int mIDForTransport;

    String strAddWoMode;
    boolean editFlag = false;
    FrameLayout frameTime;
    LinearLayout linearNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        clearMyPrefs();
        setContentView(R.layout.activity_add_workout);


        workoutName = (EditText) findViewById(R.id.editTextWorkOutName);
        workoutNum = (EditText) findViewById(R.id.editTextWNum);
        workoutSet = (EditText) findViewById(R.id.editTextWSet);

        etHour = (EditText) findViewById(R.id.editTextHour);
        etMin = (EditText) findViewById(R.id.editTextMin);
        etSec = (EditText) findViewById(R.id.editTextSec);
        frameTime = (FrameLayout) findViewById(R.id.FrameForTime);
        linearNum = (LinearLayout) findViewById(R.id.FrameForNum);

        timeSetFlag = false;

        spinnerTimer = (Spinner) findViewById(R.id.spinnerTimerSetting);
        spinnerNumOrTime = (Spinner) findViewById(R.id.spinnerNumOrTime);

        Intent intent = getIntent();

        strAddWoMode = intent.getStringExtra(BasicInfo.KEY_ADDWO_MODE);


        //메모를 걍클릭 함(모드뷰), or 롱클릭 -> 수정누름 (MODE_MODIFY)
        // 기존 내용을 먼저 그려주고, 사용자의 입력을 저장해준다
        if (strAddWoMode.equals(BasicInfo.MODE_MODIFY) || strAddWoMode.equals(BasicInfo.MODE_VIEW)) {

            processIntent(getIntent());

        } else { // 새로 메모를 하려는 경우. 화면을 새로 그려준다.

            setSpinnerTimer();

            setNumOrTime();

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


        // 스피너의 정보를 불러온다.
        if (numOrTime != null) {

            //Log.wtf("arrayTag", "timerSetting =="+timerSetting);

            String[] numOrTimeItems = getResources().getStringArray(R.array.numOrTime);

            for (int i = 0; i < numOrTimeItems.length; i++) {

                if (numOrTimeItems[i].equals(numOrTime)) {

                    spinner.setSelection(i);
                    break;
                }
            }
        }
    }

    public void setSpinnerTimer() {
        Spinner spinner = (Spinner) findViewById(spinnerTimerSetting);

        spinner.setOnItemSelectedListener(this);
        //스피너 레이아웃과 아이템 어레이를 적용
        ArrayAdapter<CharSequence> spinnerTimerAdapter = ArrayAdapter.createFromResource(this,
                R.array.timerSetting, android.R.layout.simple_spinner_item);

        //드롭다운 레이아웃을 적용한다.
        spinnerTimerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //어댑터를 스피너에 적용한다.

        spinner.setAdapter(spinnerTimerAdapter);

        if (timerSetting != null) {

            Log.wtf("arrayTag", "timerSetting ==" + timerSetting);

            String[] timerItems = getResources().getStringArray(R.array.timerSetting);

            for (int i = 0; i < timerItems.length; i++) {

                if (timerItems[i].equals(timerSetting)) {

                    spinner.setSelection(i);
                    break;
                }
            }
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        switch (parent.getId()) {
            case R.id.spinnerNumOrTime:

                numOrTime = (String) parent.getItemAtPosition(position);
                //if numOrTime이 시간설정일 경우
                if (numOrTime.equals(getResources().getStringArray(R.array.numOrTime)[1])) {
                    frameTime.setVisibility(View.VISIBLE);
                    linearNum.setVisibility(View.INVISIBLE);

                    spinnerTimer.setSelection(1);
                    spinnerTimer.setEnabled(false);

                 //   Toast.makeText(getApplicationContext(), "시간을 정하여 운동하므로 타이머 사용이 고정됩니다.", Toast.LENGTH_SHORT).show();
                }

                // numOrTime에서 횟수를 설정했다!
                else {
                    spinnerTimer.setEnabled(true);
                    frameTime.setVisibility(View.INVISIBLE);
                    linearNum.setVisibility(View.VISIBLE);

                }


                break;
            case spinnerTimerSetting:

                timerSetting = (String) parent.getItemAtPosition(position);

                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        switch (parent.getId()) {

            case spinnerTimerSetting:

                timerSetting = (String) parent.getItemAtPosition(2);

                break;
        }
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
                    //뒤로를 눌렀는데 내용이 존재한다. 내용을 저장할 것인지 물어본다.

                    // 1. Instantiate an AlertDialog.Builder with its constructor
                    AlertDialog.Builder builder = new AlertDialog.Builder(AddWorkoutActivity.this);

                    builder.setMessage("작성 내용을 저장하시겠습니까??")
                            .setTitle("저장 확인");

                    // Add the buttons
                    builder.setPositiveButton("저장", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            //  Toast.makeText(this, "editTextMemo.getText() = "+editTextMemo.getText(),Toast.LENGTH_SHORT).show();
                            saveAndSetResult();

                            finish();

                        }
                    });
                    builder.setNegativeButton("저장 안함", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();

                            finish();
                        }
                    });


                    // 2. Chain together various setter methods to set the dialog characteristics

                    // 3. Get the AlertDialog from create()
                    AlertDialog dialog = builder.create();

                    dialog.show();





                }


                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }


    public void processIntent(Intent intent) {

        setSpinnerTimer();

        setNumOrTime();


        boolean boolTimeSet = intent.getBooleanExtra("boolTimeSet", false);
        workoutName.setText(intent.getExtras().getString("workoutName"));
        timerSetting = intent.getStringExtra("timerSetting");
        numOrTime = intent.getStringExtra("numOrTime");


        //시간을 세팅하지 않았다면 횟수와 세트만 받아 뿌려준다.
        if(boolTimeSet == false){
            spinnerNumOrTime.setSelection(0);
            workoutNum.setText(intent.getExtras().getString("workoutNum"));
            workoutSet.setText(intent.getExtras().getString("workoutSet"));
        }

        //시간이 세팅된 상태이다. 시간을 뿌려준다. 시간/넘 스피너를 시간으로 해둔다.
        else{

            spinnerNumOrTime.setSelection(1);

            workoutSet.setText(intent.getExtras().getString("workoutSet"));

            etHour.setText( String.valueOf( intent.getIntExtra("hour", -1) )  );
            etMin.setText( String.valueOf( intent.getIntExtra("min", -1) ));
            etSec.setText( String.valueOf( intent.getIntExtra("sec", -1) ));

        }









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


    //입력 결과를 저장하고 result 세팅하는 메소드

    public void saveAndSetResult() {
        clearMyPrefs();

        Toast.makeText(getApplicationContext(), "입력 내용이 저장됩니다.", Toast.LENGTH_SHORT).show();

        Intent intentForSave = new Intent();

        ///////////////////////////주의!!!! 반드시 mID를 돌려줘라!!

        intentForSave.putExtra("mID", mIDForTransport);

        intentForSave.putExtra("workoutName", workoutName.getText().toString());

        // 시간제 운동을 선택했을 때에만 이 수치를 전달한다.


        //if 조건 뜻 : 시간, 분, 초 전부 안골랐다. 시간을 세팅하지 않았다.) 가 아니다 -> 세팅했다.



        // 시간 세팅을 하지 않은 경우, boolTimeSet를 false로 전달해서 리스트뷰에 보이지 않도록 하겠다.
        if( frameTime.getVisibility()==View.INVISIBLE ) {
            intentForSave.putExtra("boolTimeSet", false);

            intentForSave.putExtra("hour", -1);
            intentForSave.putExtra("min", -1);
            intentForSave.putExtra("sec", -1);
        }
        //시간 세팅을 한 상태이다. 시간+ 세트를 전달해준다.
        else {

            intentForSave.putExtra("boolTimeSet", true);

            if(!etHour.getText().toString().equals("")){
                intentForSave.putExtra("hour", Integer.parseInt( etHour.getText().toString() )  );
            }
            else {
                intentForSave.putExtra("hour", 0);
            }
            if(!etMin.getText().toString().equals("")){

                intentForSave.putExtra("min", Integer.parseInt(etMin.getText().toString()));
            }
            else{
                intentForSave.putExtra("min", 0);
            }
            if(!etSec.getText().toString().equals("")){
                intentForSave.putExtra("sec", Integer.parseInt(etSec.getText().toString()) );
            }
            else{
                intentForSave.putExtra("sec", 0 );
            }


        }
        Log.d ("ggwp", "boolTset = "+ intentForSave.getBooleanExtra("boolTimeSet", false) );
//        Log.d("ggwp", "hour = "+ intentForSave.getIntExtra("hour", 567));
//        Log.d("ggwp", "min = "+ intentForSave.getIntExtra("min", 567));
//        Log.d("ggwp", "sec = "+ intentForSave.getIntExtra("sec", 567));




        intentForSave.putExtra("timerSetting", timerSetting);
        intentForSave.putExtra("numOrTime", numOrTime);





        Log.v("strModeLog", "strAMode =" + strAddWoMode);
        //수정 모드임
        if ((strAddWoMode.equals(BasicInfo.MODE_MODIFY) || strAddWoMode.equals(BasicInfo.MODE_VIEW))) {
            intentForSave.putExtra("workoutNum", workoutNum.getText().toString());
            intentForSave.putExtra("workoutSet", workoutSet.getText().toString());

        // 메뉴의 신규 추가임
        } else {

            intentForSave.putExtra("workoutNum", workoutNum.getText().toString() );
            intentForSave.putExtra("workoutSet", workoutSet.getText().toString() );
        }




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


        editor.putString("timerSetting", "스톱워치");


        editor.commit();

    }

    protected void restoreState() {

      //  Toast.makeText(getApplicationContext(), "restore called", Toast.LENGTH_SHORT).show();
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
