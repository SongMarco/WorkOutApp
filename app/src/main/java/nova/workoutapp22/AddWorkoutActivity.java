package nova.workoutapp22;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import static nova.workoutapp22.subSources.KeySet.MODE_NULL;
import static nova.workoutapp22.subSources.KeySet.MODE_STOPWATCH;
import static nova.workoutapp22.subSources.KeySet.MODE_TIMER;
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

public class AddWorkoutActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    EditText workoutName, workoutNum, workoutSet, etTimerSetting;

    EditText etHour, etMin, etSec;

    EditText etRestMin, etRestSec;

    String timerSetting, numOrTime;
    Spinner spinnerTimer, spinnerNumOrTime;

    boolean isTimeSet;

    // EditText etWoName, etWoNum, etWoSet, etTimerSetting;

    int mIDForTransport;

    int timerMode = -1;

    String strAddWoMode;

    boolean editFlag = false;

    boolean isEditable = false;



    FrameLayout frameTime;
    LinearLayout linearNum;

    Toolbar myToolbar;
    String addWoMenuState = BasicInfo.MENU_ADDWO_NORMAL;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        clearMyPrefs();
        setContentView(R.layout.activity_add_workout);

        myToolbar = (Toolbar) findViewById(R.id.toolbarAddWorkout);
        setSupportActionBar(myToolbar);

        editFlag = false;
        workoutName = (EditText) findViewById(R.id.editTextWorkOutName);
        workoutNum = (EditText) findViewById(R.id.editTextWNum);
        workoutSet = (EditText) findViewById(R.id.editTextWSet);

        etHour = (EditText) findViewById(R.id.editTextHour);
        etMin = (EditText) findViewById(R.id.editTextMin);
        etSec = (EditText) findViewById(R.id.editTextSec);

        etRestMin = (EditText) findViewById(R.id.editTextRestMin);
        etRestSec = (EditText) findViewById(R.id.editTextRestSec);



        frameTime = (FrameLayout) findViewById(R.id.FrameForTime);
        linearNum = (LinearLayout) findViewById(R.id.FrameForNum);


        spinnerTimer = (Spinner) findViewById(R.id.spinnerTimerSetting);
        spinnerNumOrTime = (Spinner) findViewById(R.id.spinnerNumOrTime);

        Intent intentReceived = getIntent();

        strAddWoMode = intentReceived.getStringExtra(BasicInfo.KEY_ADDWO_MODE);



        //메모를 걍클릭 함(모드뷰), or 롱클릭 -> 수정누름 (MODE_MODIFY)
        // 기존 내용을 먼저 그려주고, 사용자의 입력을 저장해준다
        if (strAddWoMode.equals(BasicInfo.MODE_MODIFY) || strAddWoMode.equals(BasicInfo.MODE_VIEW)) {




            //processIntent가 정상적으로 끝나면 텍스트의 변화를 감지하여, editFlag를 변경한다 -> 이후 저장여부 결정함
           if( processIntent(intentReceived) ){

               setTextWatcher();
           }
           setNotEditable();

        } else { // 새로 메모를 하려는 경우. 화면을 새로 그려준다.

            setSpinnerTimer();

            setNumOrTime();

            Button saveWoButton = (Button) findViewById(R.id.buttonSaveWo);
            saveWoButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {


                    saveAndSetResult();
         //           Toast.makeText(getApplicationContext(), "입력 내용이 저장됩니다.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
            workoutName.addTextChangedListener( new CustomTextWatcher(workoutName) );
        }

        //TODO 스타트 버튼 눌럿을 때 필수정보를 확인하고 넘어가도록 세팅해야 한다.
        //region 스타트 버튼 세팅 부분
        Button startBtn = (Button) findViewById(R.id.buttonStartWo);
        startBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intentPlayWo = new Intent(getApplicationContext(), PlayWorkoutActivity.class);



                intentPlayWo.putExtra(key_workoutName, workoutName.getText().toString() );
                intentPlayWo.putExtra(key_workoutSet, workoutSet.getText().toString() );
                intentPlayWo.putExtra(key_currentSet, 1 );

                //쉬는시간 부분
                if(!etRestMin.getText().toString().equals("")){
                    intentPlayWo.putExtra(key_restMin, Integer.parseInt( etRestMin.getText().toString() ) );
                }
                else{
                    intentPlayWo.putExtra(key_restMin, 0);
                }
                if(!etRestSec.getText().toString().equals("")){
                    intentPlayWo.putExtra(key_restSec, Integer.parseInt( etRestSec.getText().toString() ) );
                }
                else{
                    intentPlayWo.putExtra(key_restSec, 0);
                }





                intentPlayWo.putExtra(key_boolTimeSet, isTimeSet);
                intentPlayWo.putExtra(key_timerMode, timerMode);




                //시간을 세팅하였다면 시간을 보내줘!
                if(isTimeSet == true){

                    if(!etHour.getText().toString().equals("")){
                        intentPlayWo.putExtra(key_hour, Integer.parseInt( etHour.getText().toString() )  );
                    }
                    else {
                        intentPlayWo.putExtra(key_hour, 0);
                    }
                    if(!etMin.getText().toString().equals("")){

                        intentPlayWo.putExtra(key_min, Integer.parseInt(etMin.getText().toString()));
                    }
                    else{
                        intentPlayWo.putExtra(key_min, 0);
                    }
                    if(!etSec.getText().toString().equals("")){
                        intentPlayWo.putExtra(key_sec, Integer.parseInt(etSec.getText().toString()) );
                    }
                    else{
                        intentPlayWo.putExtra(key_sec, 0 );
                    }
//                    Toast.makeText(AddWorkoutActivity.this, "hour min sec = "+etHour.getText().toString()+etMin.getText().toString()+
//                            etSec.getText().toString(), Toast.LENGTH_SHORT).show();
                }
                //갯수 세팅이다. 갯수만 보낸다.
                else{
                    intentPlayWo.putExtra(key_workoutNum, workoutNum.getText().toString()  );
//                    Toast.makeText(AddWorkoutActivity.this, "workoutnum = "+workoutNum.getText().toString(), Toast.LENGTH_SHORT).show();
                }





//                Toast.makeText(AddWorkoutActivity.this, "istimeset = "+isTimeSet, Toast.LENGTH_SHORT).show();


                startActivity(intentPlayWo);

                saveAndSetResult();
                finish();
            }
        });
        //endregion



    }

    public void setNotEditable(){

        workoutName.setFocusable(false);
        workoutNum.setFocusable(false);
        workoutSet.setFocusable(false);

        etHour.setFocusable(false);
        etMin.setFocusable(false);
        etSec.setFocusable(false);

        etRestMin.setFocusable(false);
        etRestSec.setFocusable(false);

        spinnerNumOrTime.setEnabled(false);
        spinnerTimer.setEnabled(false);

        isEditable = false;
    }

    public void setEditable(){

        workoutName.setFocusableInTouchMode(true);
        workoutNum.setFocusableInTouchMode(true);
        workoutSet.setFocusableInTouchMode(true);
        etHour.setFocusableInTouchMode(true);
        etMin.setFocusableInTouchMode(true);
        etSec.setFocusableInTouchMode(true);
        etRestMin.setFocusableInTouchMode(true);
        etRestSec.setFocusableInTouchMode(true);

        workoutName.setFocusable(true);
        workoutNum.setFocusable(true);
        workoutSet.setFocusable(true);

        etHour.setFocusable(true);
        etMin.setFocusable(true);
        etSec.setFocusable(true);

        etRestMin.setFocusable(true);
        etRestSec.setFocusable(true);

        spinnerNumOrTime.setEnabled(true);
        spinnerTimer.setEnabled(true);

        isEditable = true;
    }


    //region @@@@@액션바 메뉴 관련 파트@@@@@
    ///
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_addwo, menu);

        //노말모드일 경우 에딧버튼이 보이게!
        if(addWoMenuState.equals(BasicInfo.MENU_ADDWO_NORMAL) ){
            menu.findItem(R.id.action_editItem).setVisible(true);
            menu.findItem(R.id.action_editDone).setVisible(false);
        }
        else{
            menu.findItem(R.id.action_editItem).setVisible(false);
            menu.findItem(R.id.action_editDone).setVisible(true);
        }



        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);


        //        menu.findItem(R.id.start).setVisible(!isStarted);
        //        menu.findItem(R.id.stop).setVisible(isStarted);

        return true;

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_editItem:

                setEditable();

                if(getIntent().getBooleanExtra(key_boolTimeSet, false) == true){
                    spinnerTimer.setEnabled(false);
                }

                addWoMenuState = BasicInfo.MENU_ADDWO_EDIT;
                invalidateOptionsMenu();

                Toast.makeText(this, "운동을 수정합니다.", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.action_editDone:

                setNotEditable();
                addWoMenuState = BasicInfo.MENU_ADDWO_NORMAL;
                invalidateOptionsMenu();

                Toast.makeText(this, "수정이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                return true;


            default:
                return true;
        }

    }
    //endregion@@@@@

    public void setTextWatcher(){
        workoutName.addTextChangedListener( new CustomTextWatcher(workoutName) );

        workoutNum.addTextChangedListener( new CustomTextWatcher(workoutNum) );

        workoutSet.addTextChangedListener( new CustomTextWatcher(workoutSet) );

        etHour.addTextChangedListener( new CustomTextWatcher(etHour) );
        etMin.addTextChangedListener( new CustomTextWatcher(etMin) );
        etSec.addTextChangedListener( new CustomTextWatcher(etSec) );

        etRestMin.addTextChangedListener( new CustomTextWatcher(etRestMin) );
        etRestSec.addTextChangedListener( new CustomTextWatcher(etRestSec) );
    }

    // 이 textWatcher가 텍스트 변화를 감지한다. -> editFlag가 true로 되면 -> 저장 여부를 물어보게 된다.

    private class CustomTextWatcher implements TextWatcher {
        private EditText mEditText;
        String stringBefore;
        public CustomTextWatcher(EditText e) {
            mEditText = e;
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            stringBefore = s.toString();


        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        public void afterTextChanged(Editable afterText) {
            if (getCurrentFocus() == mEditText) {
                    editFlag = true;
            }
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
                    linearNum.setVisibility(View.GONE);

                    spinnerTimer.setSelection(1);
                    spinnerTimer.setEnabled(false);

                    isTimeSet = true;

                 //   Toast.makeText(getApplicationContext(), "시간을 정하여 운동하므로 타이머 사용이 고정됩니다.", Toast.LENGTH_SHORT).show();
                }

                // numOrTime에서 횟수를 설정했다!
                else {

                    if(isEditable == true){
                        spinnerTimer.setEnabled(true);
                    }

                    linearNum.setVisibility(View.VISIBLE);
                    if(!(timerMode == MODE_TIMER) ){
                        frameTime.setVisibility(View.GONE);
                    }

                    isTimeSet = false;
                }


                break;
            case spinnerTimerSetting:

                timerMode = position;

                if(timerMode == MODE_TIMER){
                    frameTime.setVisibility(View.VISIBLE);
                }
                else{
                    frameTime.setVisibility(View.GONE);
                }
                timerSetting = (String) parent.getItemAtPosition(position);






                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        switch (parent.getId()) {

            case spinnerTimerSetting:

                timerSetting = (String) parent.getItemAtPosition(2);
                timerMode = MODE_NULL;

                break;
        }
    }


    //endregion


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (event.getAction() == KeyEvent.ACTION_DOWN) { //키 다운 액션 감지
            if (keyCode == KeyEvent.KEYCODE_BACK) { //BackKey 다운일 경우만 처리





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


    // 워크아웃 액티비티로부터 인텐트를 받아 해당 정보를 에딧텍스트에 뿌려줍니다.
    public boolean processIntent(Intent intent) {

        setSpinnerTimer();

        setNumOrTime();


        final boolean boolTimeSet = intent.getBooleanExtra(key_boolTimeSet, false);
        workoutName.setText(intent.getExtras().getString("workoutName"));

        timerSetting = intent.getStringExtra(key_timerSetting);
        int timerMode = 2;

        if( timerSetting.equals("스톱워치") ){
            timerMode = MODE_STOPWATCH;
        }
        else if( timerSetting.equals("타이머") ){
            timerMode = MODE_TIMER;
        }
        else{
            timerMode = MODE_NULL;
        }



        numOrTime = intent.getStringExtra("numOrTime");

        etRestMin.setText( String.valueOf( intent.getIntExtra(key_restMin,0) ) );
        etRestSec.setText( String.valueOf (intent.getIntExtra(key_restSec,0) ) );


        //횟수 세팅이다. //  횟수와 세트만 받아 뿌려준다.
        if(boolTimeSet == false){
            spinnerNumOrTime.setSelection(0);
            spinnerTimer.setSelection(timerMode);
            spinnerTimer.setEnabled(false);
          //  Toast.makeText(this, "timer :: "+spinnerTimer.isEnabled(), Toast.LENGTH_SHORT).show();

            workoutNum.setText(intent.getExtras().getString("workoutNum"));
            workoutSet.setText(intent.getExtras().getString("workoutSet"));
        }

        //시간이 세팅된 상태이다. 시간을 뿌려준다. 시간/넘 스피너를 시간으로 해둔다.
        else{

            spinnerNumOrTime.setSelection(1);
            spinnerTimer.setSelection(timerMode);
            spinnerTimer.setEnabled(false);

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




        return true;
    }


    //입력 결과를 저장하고 result 세팅하는 메소드

    public void saveAndSetResult() {
        clearMyPrefs();

      //  Toast.makeText(getApplicationContext(), "입력 내용이 저장됩니다.", Toast.LENGTH_SHORT).show();

        Intent intentForSave = new Intent();

        ///////////////////////////주의!!!! 반드시 mID를 돌려줘라!!

        intentForSave.putExtra("mID", mIDForTransport);

        intentForSave.putExtra("workoutName", workoutName.getText().toString());

        intentForSave.putExtra(key_restMin, etRestMin.getText().toString() );
        intentForSave.putExtra(key_restSec, etRestSec.getText().toString() );

//        Toast.makeText(this, "Min and Sec = "+etRestMin.getText().toString()+"min"+etRestSec.getText().toString(), Toast.LENGTH_SHORT).show();

        // 시간제 운동을 선택했을 때에만 이 수치를 전달한다.


        //if 조건 뜻 : 시간, 분, 초 전부 안골랐다. 시간을 세팅하지 않았다.) 가 아니다 -> 세팅했다.



        intentForSave.putExtra("timerSetting", timerSetting);

        intentForSave.putExtra("numOrTime", numOrTime);

        // 갯수 운동이다. 타이머를 쓰는지 여부에 따라 달라짐
        // 시간 세팅을 하지 않은 경우, boolTimeSet를 false로 전달해서 리스트뷰에 보이지 않도록 하겠다.
        if( isTimeSet==false ) {
            intentForSave.putExtra("boolTimeSet", false);

            //타이머 모드에서 타이머를 사용한다! 시분초를 저장
            if(timerMode == MODE_TIMER){

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

                Toast.makeText(this, "saving time..."+intentForSave.getIntExtra("hour",0)+intentForSave.getIntExtra("min",0)+intentForSave.getIntExtra("sec",0), Toast.LENGTH_SHORT).show();

            }
            else{

                intentForSave.putExtra("hour", -1);
                intentForSave.putExtra("min", -1);
                intentForSave.putExtra("sec", -1);
            }

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






        Log.v("strModeLog", "strAMode =" + strAddWoMode);
        //수정 모드임
        if ((strAddWoMode.equals(BasicInfo.MODE_MODIFY) || strAddWoMode.equals(BasicInfo.MODE_VIEW))) {
            intentForSave.putExtra("workoutNum", workoutNum.getText().toString());
            intentForSave.putExtra("workoutSet", workoutSet.getText().toString());

        // 메뉴의 신규 추가임
        } else {

            intentForSave.putExtra("workoutNum", workoutNum.getText().toString());
            intentForSave.putExtra("workoutSet", workoutSet.getText().toString());
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



        editor.commit();

    }

    protected void restoreState() {

//        Toast.makeText(getApplicationContext(), "restore called", Toast.LENGTH_SHORT).show();
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
