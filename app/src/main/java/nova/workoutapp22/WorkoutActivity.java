package nova.workoutapp22;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

import nova.workoutapp22.listviewSrcForWorkOut.WorkoutAdapter;
import nova.workoutapp22.listviewSrcForWorkOut.WorkoutItem;
import nova.workoutapp22.subSources.BasicInfo;

import static nova.workoutapp22.subSources.BasicInfo.REQ_ADD_WORKOUT;
import static nova.workoutapp22.subSources.BasicInfo.REQ_MODIFY_WORKOUT;


public class WorkoutActivity extends AppCompatActivity {

    ListView listViewForWorkout;
    WorkoutAdapter workoutAdapter;
    Toolbar myToolbar;

    String woMenuState = BasicInfo.MENU_WO_NORMAL;

    boolean isMultMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);


        ///////////////////////툴바를 만듭니다
        myToolbar = (Toolbar) findViewById(R.id.toolBarWoActivity);
        setSupportActionBar(myToolbar);
/////////////////////////////////////////


        workoutAdapter = new WorkoutAdapter();


        listViewForWorkout = (ListView) findViewById(R.id.listViewForWorkout);
        listViewForWorkout.setAdapter(workoutAdapter);

        workoutAdapter.addItem(new WorkoutItem(0, "벤치 프레스", "50", "3", "타이머 사용"));
        workoutAdapter.addItem(new WorkoutItem(1, "팔굽혀 펴기", "20", "5", "스톱워치 사용"));
        workoutAdapter.addItem(new WorkoutItem(2, "스쿼트", "100", "2", "사용 안함"));

        workoutAdapter.notifyDataSetChanged();


// 시작 상태, 삭제한 상태, 다중->단일로 갈때는 체크박스를 gone으로. 아니면 보이게!
        workoutAdapter.setCheckBoxState(false);

        setItemClicker();


        //setSingleChoice(listViewForWorkout);


/////////////////////////////// 메모아이템을 수정한다.


        ///////////////롱클릭을 통한 수정 / 삭제 메뉴를 추가해야 한다.
        //////*
        listViewForWorkout.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {

                WorkoutItem item = (WorkoutItem) workoutAdapter.getItem(position);

                showMessage(item);


                listViewForWorkout.clearChoices();
                return true;
            }
        });


    }


    //region @@@@@액션바 메뉴 관련 파트@@@@@
    ///
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_workout, menu);



        if(woMenuState.equals(BasicInfo.MENU_WO_MULT)){
            menu.findItem(R.id.action_addWorkout).setVisible(false);
            menu.findItem(R.id.action_delete).setVisible(true);
            menu.findItem(R.id.action_selectAll).setVisible(true);
            menu.findItem(R.id.action_clearSelection).setVisible(true);

        }
        else{
            menu.findItem(R.id.action_addWorkout).setVisible(true);
            menu.findItem(R.id.action_delete).setVisible(false);
            menu.findItem(R.id.action_selectAll).setVisible(false);
            menu.findItem(R.id.action_clearSelection).setVisible(false);
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

            case R.id.action_addWorkout:

                Intent intent = new Intent(getApplicationContext(), AddWorkoutActivity.class);
                intent.putExtra(BasicInfo.KEY_ADDWO_MODE, BasicInfo.MODE_MODIFY);

                startActivityForResult(intent, BasicInfo.REQ_ADD_WORKOUT);
                return true;

            case R.id.action_selectMult:


                //이미 멀티모드였다면 멀티모드를 비활성화하도록 할 것.
                if(isMultMode == true){

                    setSingleChoice(listViewForWorkout);

                }
                //멀티모드가 아니므로 멀티모드 활성화
                else{


                    setMultipleChoice(listViewForWorkout);
                }



//                if (listViewForWorkout.getChoiceMode() == ListView.CHOICE_MODE_MULTIPLE) {
//
//                    setSingleChoice(listViewForWorkout);
//
//                } else {
//
//                    setMultipleChoice(listViewForWorkout);
//
//                }
                return true;

            case R.id.action_delete:
                askDelete();

                return true;

            case R.id.action_selectAll:
                int count = workoutAdapter.getCount();

                for (int i = 0; i < count; i++) {
                    listViewForWorkout.setItemChecked(i, true);
                }
//                workoutAdapter.setCheckBoxState(true);
                return true;

            case R.id.action_clearSelection:
                count = workoutAdapter.getCount();
                for (int i = 0; i < count; i++) {

                    listViewForWorkout.setItemChecked(i, false);

                }
                return true;


            default:
                return true;
        }

    }
    //endregion@@@@@


    public void askDelete() {
        // 1. Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(WorkoutActivity.this);

        builder.setMessage("정말 삭제하시겠습니까?")
                .setTitle("삭제 확인")
                .setIcon(R.drawable.ic_warning_black_48dp);

        // Add the buttons
        builder.setPositiveButton("삭제합니다", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                SparseBooleanArray checkedItems = listViewForWorkout.getCheckedItemPositions();
                int count2 = workoutAdapter.getCount();

                for (int i = count2 - 1; i >= 0; i--) {

                    //int i = count - 1;  0<=i; i--
                    if (checkedItems.get(i)) {
                        WorkoutItem item = workoutAdapter.woItems.get(i);
                        workoutAdapter.removeItem(item);
                    }
                }
                // 모든 선택 상태 초기화.
                listViewForWorkout.clearChoices();
                workoutAdapter.notifyDataSetChanged();
                workoutAdapter.setCheckBoxState(false);
                setSingleChoice(listViewForWorkout);

                woMenuState = BasicInfo.MENU_WO_NORMAL;
                isMultMode = false;
                invalidateOptionsMenu();


            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                dialog.dismiss();
            }
        });


        // 2. Chain together various setter methods to set the dialog characteristics

        // 3. Get the AlertDialog from create()
        AlertDialog dialog = builder.create();

        dialog.show();
    }


    public void showMessage(final WorkoutItem item) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("삭제 확인")
                .setMessage("삭제하시겠습니까?")
                .setIcon(android.R.drawable.ic_dialog_alert);
        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                workoutAdapter.removeItem(item);

                workoutAdapter.notifyDataSetChanged();
            }

        });

        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }

        });


        AlertDialog dialog = builder.create();
        dialog.show();

    }


    /////////////////////////////단일 선택 / 다중 선택을 선택하는 모드.!!


    public void setSingleChoice(ListView lv) {

        //  Toast.makeText(getApplicationContext(), "단일 선택 모드로 변경되었습니다.", Toast.LENGTH_SHORT).show();

        lv.clearChoices();
        lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        workoutAdapter.setCheckBoxState(false);

        setItemClicker();

        woMenuState = BasicInfo.MENU_WO_NORMAL;
        isMultMode = false;
        invalidateOptionsMenu();
    }

    public void setMultipleChoice(ListView lv) {
        // Toast.makeText(getApplicationContext(), "다중 선택 모드로 변경되었습니다.", Toast.LENGTH_SHORT).show();


        lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        workoutAdapter.setCheckBoxState(true);


        //아이템클릭리스너를 무효화한다.
        lv.setOnItemClickListener(null);

        woMenuState = BasicInfo.MENU_WO_MULT;
        isMultMode = true;
        invalidateOptionsMenu();
    }

    // 아이템 클릭 리스너를 활성화해준다.

    public void setItemClicker() {
        listViewForWorkout.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {


                WorkoutItem item = (WorkoutItem) workoutAdapter.getItem(position);

                // 수정 -- 메모 보기 액티비티 띄우기
                Intent intent = new Intent(getApplicationContext(), AddWorkoutActivity.class);

                // intent.putExtra(BasicInfo.KEY_MEMO_MODE, BasicInfo.MODE_VIEW);
                intent.putExtra(BasicInfo.KEY_ADDWO_MODE, BasicInfo.MODE_MODIFY);


                intent.putExtra("mID", item.getmID());

                intent.putExtra("workoutName", item.getWoName().toString());
                intent.putExtra("timerSetting", item.getTimerSetting().toString());

                //시간을 세팅하지 않았다면 횟수와 세트만 전달하자.
                if (item.getBoolTimeSet() == false) {

                    intent.putExtra("boolTimeSet", item.getBoolTimeSet());
                    intent.putExtra("workoutNum", item.getWoNum());
                    intent.putExtra("workoutSet", item.getWoSet());
                }
                //시간을 세팅했다면 시간 + 세트를 전달해서 뿌려라.
                else {
                    Log.d("ggwp", "here im : booltimeset = " + item.getBoolTimeSet());

                    intent.putExtra("boolTimeSet", item.getBoolTimeSet());

                    intent.putExtra("workoutSet", item.getWoSet());

                    intent.putExtra("hour", item.getHour());
                    intent.putExtra("min", item.getMin());
                    intent.putExtra("sec", item.getSec());

                }


                // 모든 선택 상태 초기화.
                listViewForWorkout.clearChoices();
                workoutAdapter.notifyDataSetChanged();

                startActivityForResult(intent, REQ_MODIFY_WORKOUT);
                //////////////////


            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_ADD_WORKOUT) {

            if (resultCode == RESULT_OK) {

                Toast.makeText(getApplicationContext(), "리스트뷰에 내용 추가합니다", Toast.LENGTH_SHORT).show();

                // 리스트뷰에 해당 내용을 담아 추가한다.

                WorkoutItem newmit = setItemFromIntent(data);

                workoutAdapter.addItem(newmit);

                workoutAdapter.notifyDataSetChanged();

                saveState();

            }
        }

        ///////////주의!! 인텐트로 mID를 빠뜨리면 수정이 제대로 되지 않는다@@@@@@@ 주의해라 이걸로 삽질을 연속 2번함

        else if (requestCode == REQ_MODIFY_WORKOUT) {

            if (resultCode == RESULT_OK) {

                //수정된 데이터를 삽입
                Toast.makeText(getApplicationContext(), "리스트뷰 수정 완료", Toast.LENGTH_SHORT).show();
//////////////////////주의 주의 3시간 이상 삽질한 문제 : new 아이템 만들고 ID를 초기화 안함
                // -> 계쏙해서 잘못된 mID를 전달하게 됨.
                // 인텐트의 전달이 계속 잘못되면 인텐트 관련 메소드를 살피자. 이것도 인텐트 관련 메소드다.


                WorkoutItem newmit = setItemFromIntent(data);

                int mmID = data.getExtras().getInt("mID");


                newmit.setmID(mmID);  //////////////ㄹㅇ 정신나간 코드임;


                workoutAdapter.setItem(mmID, newmit);

                workoutAdapter.notifyDataSetChanged();

                saveState();

            }

        }


    }

    public WorkoutItem setItemFromIntent(Intent data) {

        String woName = data.getExtras().getString("workoutName");

        Log.v("nameTagg", "woName = " + woName);
        String woNum = data.getExtras().getString("workoutNum");
        String woSet = data.getExtras().getString("workoutSet");
        String numOrTime = data.getStringExtra("numOrTime");

        boolean boolTimeSet = data.getBooleanExtra("boolTimeSet", false);
        Log.d("ggwp", "boolTset = " + boolTimeSet);
        String timerSetting = data.getExtras().getString("timerSetting");

        //시간을 세팅하지 않아서 hour가 -1인 상태 -> 횟수 세트만 전달해주면 OK
        if (boolTimeSet == false) {

            return new WorkoutItem(woName, woNum, woSet, timerSetting, boolTimeSet);


        }
        //시간을 세팅 하였음. 시간과 세트를 전달한다.
        else {
            int loadedHour = data.getIntExtra("hour", -1);
            int loadedMin = data.getIntExtra("min", -1);
            int loadedSec = data.getIntExtra("sec", -1);

            return new WorkoutItem(woName, woSet, loadedHour, loadedMin, loadedSec, timerSetting, boolTimeSet);

        }


    }


    @Override
    protected void onPause() {
        super.onPause();
        saveState();

    }

    @Override
    protected void onResume() {


        super.onResume();
        restoreState();
    }


    public void saveState() {
        //   saveStateWithGson();
        saveStateWithJson();
    }

    public void restoreState() {

        //  restoreStateWithGson();


        // 주의 !! restore에서 오류가 많이 나는데,
        // 아이템을 추가할 경우 toJson도 손보아야 한다.

        restoreStateWithJson();
    }


    public void saveStateWithJson() {
        SharedPreferences prefForWo = getSharedPreferences("prefForWoWithJson", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefForWo.edit();


        ArrayList<WorkoutItem> saveArray;
        WorkoutItem tempItem;
        saveArray = (ArrayList<WorkoutItem>) workoutAdapter.woItems.clone();

        JsonArray jsonArray = new JsonArray();

        for (int i = 0; i < saveArray.size(); i++) {
            tempItem = saveArray.get(i);

            jsonArray.add(tempItem.toJson());
            Log.wtf("saved", "saved Item : " + tempItem.toJson());

        }

        if (!saveArray.isEmpty()) {

            editor.putString("arrayListItem", jsonArray.toString());
            Log.wtf("saved jsonArray : ", "saved Item : " + jsonArray.toString());
        } else {
            editor.putString("arrayListItem", null);
        }


        editor.apply();

    }

    public void restoreStateWithJson() {
        SharedPreferences prefForWo = getSharedPreferences("prefForWoWithJson", Activity.MODE_PRIVATE);

        String jsonString = prefForWo.getString("arrayListItem", null);

        // jsonString = jsonString.replaceAll("\\\\","");
        //    Log.wtf("saved jsonArray : ", "jsonString = " + jsonString);
        ArrayList<WorkoutItem> loadArray = new ArrayList<>();

        if (jsonString != null) {
            try {
                JSONArray jsonArray = new JSONArray(jsonString);
                //   Log.wtf("jsonArray","jasonArray = " +jsonArray );

                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject joLoaded = jsonArray.getJSONObject(i);
                    //      Log.wtf("loaded2233",""+joLoaded );

                    //Log.wtf("TagForJson = ", joLoaded.getString("woName"));
                    WorkoutItem tempItem = new WorkoutItem(i);

                    tempItem.setWoName(joLoaded.getString("woName"));


                    // 시간제 운동인가 아닌가?
                    //시간제가 아니라면 세트와 넘버만 취해라
                    if (joLoaded.getBoolean("boolTimeSet") == false) {

                        tempItem.setBoolTimeSet(joLoaded.getBoolean("boolTimeSet"));
                        tempItem.setWoNum(joLoaded.getString("woNum"));
                        tempItem.setWoSet(joLoaded.getString("woSet"));
                        tempItem.setTimerSetting(joLoaded.getString("timerSetting"));
                    } else {
                        tempItem.setBoolTimeSet(joLoaded.getBoolean("boolTimeSet"));

                        tempItem.setWoSet(joLoaded.getString("woSet"));
                        tempItem.setTimerSetting(joLoaded.getString("timerSetting"));

                        tempItem.setHour(joLoaded.getInt("hour"));
                        tempItem.setMin(joLoaded.getInt("min"));
                        tempItem.setSec(joLoaded.getInt("sec"));


                    }







              /*      Log.wtf("TagForJson", "temp Item describtion" +
                            "\twoName = " + tempItem.getWoName() +
                            "\twoNum = " + tempItem.getWoNum() +
                            "\twoSet = " + tempItem.getWoSet() +
                            "\ttimerSet = " + tempItem.getTimerSetting());
*/
                    loadArray.add(tempItem);
                }

                    /*
                for (int i = 0; i < recs.length(); ++i) {
                    JSONObject rec = recs.getJSONObject(i);
                    int id = rec.getInt("id");
                    String loc = rec.getString("loc");
                    // ...
                }*/
            } catch (JSONException e) {
                Log.wtf("err comes", "ERRERR");
                e.printStackTrace();
            }
            workoutAdapter.woItems = (ArrayList<WorkoutItem>) loadArray.clone();


            // mID를 세팅해줘야 아이템클릭(수정에 사용)이 제대로된다.
            for (int i = 0; i < workoutAdapter.getCount(); i++) {
                ((WorkoutItem) workoutAdapter.getItem(i)).mID = i;
            }
        }

        workoutAdapter.notifyDataSetChanged();
    }


    public void saveStateWithGson() {

        SharedPreferences prefForWo = getSharedPreferences("prefForWo", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefForWo.edit();

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Uri.class, new UriSerializer())
                .create();

        ArrayList<WorkoutItem> saveArray;
        saveArray = (ArrayList<WorkoutItem>) workoutAdapter.woItems.clone();


        String json = gson.toJson(saveArray);

        Log.wtf("wtf", json);
        editor.putString("arrayList", json);
        //apply vs commit

        //void apply () : API 9(2.3) 에서 추가. 호출만 하고 다음코드를 실행하므로 스레드가 block 되지 않는다. 함수가 곧바로 실행되지 않고 비동기 처리된다.
        // boolean commit () : 호출시 스레드는 block 되고 함수 종료시 처리결과를 true/false 로 반환한다.
        // 굳이 결과값이 필요 없다면 비동기로 처리하는 apply 를 사용하는게 반응성면에서 좋다.

        editor.apply();

    }


    public void restoreStateWithGson() {
        Toast.makeText(getApplicationContext(), "restore state Called", Toast.LENGTH_SHORT).show();

        SharedPreferences prefForWo = getSharedPreferences("prefForWo", Activity.MODE_PRIVATE);


        if ((prefForWo != null) && (prefForWo.contains("arrayList"))) {


            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(Uri.class, new UriDeserializer())
                    .create();


            String json = prefForWo.getString("arrayList", null);

            //Type type = TypeToken.getParameterized( (ArrayList<MemoItem>) , ).getType();

            ArrayList<WorkoutItem> loadArray = null;

            loadArray = gson.fromJson(json, new TypeToken<ArrayList<WorkoutItem>>() {
            }.getType());


            workoutAdapter.woItems = (ArrayList<WorkoutItem>) loadArray.clone();


            // mID를 세팅해줘야 아이템클릭(수정에 사용)이 제대로된다.
            for (int i = 0; i < workoutAdapter.getCount(); i++) {
                ((WorkoutItem) workoutAdapter.getItem(i)).mID = i;
            }
        }


    }


    protected void clearMyPrefs() {
        Toast.makeText(getApplicationContext(), "pref cleared", Toast.LENGTH_SHORT).show();

        SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.commit();
    }

    public class UriSerializer implements JsonSerializer<Uri> {
        public JsonElement serialize(Uri src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.toString());
        }
    }

    public class UriDeserializer implements JsonDeserializer<Uri> {
        @Override
        public Uri deserialize(final JsonElement src, final Type srcType,
                               final JsonDeserializationContext context) throws JsonParseException {
            return Uri.parse(src.getAsString());
        }
    }


}
