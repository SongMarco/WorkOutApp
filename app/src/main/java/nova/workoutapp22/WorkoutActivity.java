package nova.workoutapp22;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);


        workoutAdapter = new WorkoutAdapter();
        listViewForWorkout = (ListView) findViewById(R.id.listViewForWorkout);
        listViewForWorkout.setAdapter(workoutAdapter);

        workoutAdapter.addItem(new WorkoutItem(0, "벤치 프레스", "50개", "3세트", "타이머 사용"));
        workoutAdapter.addItem(new WorkoutItem(1, "팔굽혀 펴기", "20개", "5세트", "스톱워치 사용"));
        workoutAdapter.addItem(new WorkoutItem(2, "스쿼트", "100개", "2세트", "사용 안함"));

        workoutAdapter.notifyDataSetChanged();


// 시작 상태, 삭제한 상태, 다중->단일로 갈때는 체크박스를 gone으로. 아니면 보이게!
        workoutAdapter.setCheckBoxState(false);

        setItemClicker();

        findViewById(R.id.buttonAddWO).setOnClickListener(mClickListener);
        findViewById(R.id.buttonWoDelete).setOnClickListener(mClickListener);
        findViewById(R.id.buttonWoSelectAll).setOnClickListener(mClickListener);
        findViewById(R.id.buttonWoCancelSelect).setOnClickListener(mClickListener);
        findViewById(R.id.buttonWoSwitch).setOnClickListener(mClickListener);

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

    Button.OnClickListener mClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            Intent intent;
            int count;
            switch (v.getId()) {
                case R.id.buttonAddWO:
                    intent = new Intent(getApplicationContext(), AddWorkoutActivity.class);
                    intent.putExtra(BasicInfo.KEY_ADDWO_MODE, BasicInfo.MODE_MODIFY);

                    startActivityForResult(intent, BasicInfo.REQ_ADD_WORKOUT);

                    break;

                case (R.id.buttonWoSwitch):

                    if (listViewForWorkout.getChoiceMode() == ListView.CHOICE_MODE_MULTIPLE) {

                        setSingleChoice(listViewForWorkout);

                    } else {

                        setMultipleChoice(listViewForWorkout);

                    }
                    break;


                case R.id.buttonWoDelete:
                    SparseBooleanArray checkedItems = listViewForWorkout.getCheckedItemPositions();
                    count = workoutAdapter.getCount();

                    for (int i = count - 1; i >= 0; i--) {

                        //int i = count - 1;  0<=i; i--
                        if (checkedItems.get(i)) {
                            workoutAdapter.woItems.remove(i);
                        }
                    }
                    // 모든 선택 상태 초기화.
                    listViewForWorkout.clearChoices();
                    workoutAdapter.notifyDataSetChanged();
                    workoutAdapter.setCheckBoxState(false);
                    setSingleChoice(listViewForWorkout);
                    break;


                case R.id.buttonWoSelectAll:
                    Toast.makeText(getApplicationContext(), "전체선택 시작됨", Toast.LENGTH_SHORT).show();
                    count = workoutAdapter.getCount();

                    for (int i = 0; i < count; i++) {
                        listViewForWorkout.setItemChecked(i, true);
                    }
                    workoutAdapter.setCheckBoxState(true);


                    break;
                case R.id.buttonWoCancelSelect:
                    count = workoutAdapter.getCount();
                    for (int i = 0; i < count; i++) {

                        listViewForWorkout.setItemChecked(i, false);

                    }
                    break;

            }
        }
    };


    public void showMessage(final WorkoutItem item) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("삭제");
        builder.setMessage("삭제하시겠습니까?");
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                workoutAdapter.removeItem(item);

                workoutAdapter.notifyDataSetChanged();
            }

        });

        builder.setNeutralButton("취소", new DialogInterface.OnClickListener() {
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
    }

    public void setMultipleChoice(ListView lv) {
        // Toast.makeText(getApplicationContext(), "다중 선택 모드로 변경되었습니다.", Toast.LENGTH_SHORT).show();


        lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        workoutAdapter.setCheckBoxState(true);


        //아이템클릭리스너를 무효화한다.
        lv.setOnItemClickListener(null);
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
                intent.putExtra("workoutNum", item.getWoNum().toString());
                intent.putExtra("workoutSet", item.getWoSet().toString());
                intent.putExtra("timerSetting", item.getTimerSetting().toString());


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

                 //  saveStateWithGson();
                saveStateWithJson();

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

               //      saveStateWithGson();
                saveStateWithJson();

            }

        }


    }

    public WorkoutItem setItemFromIntent(Intent data) {

        String woName = data.getExtras().getString("workoutName");

        Log.v("nameTagg", "woName = " + woName);
        String woNum = data.getExtras().getString("workoutNum");
        String woSet = data.getExtras().getString("workoutSet");
        String timerSetting = data.getExtras().getString("timerSetting");


        return new WorkoutItem(woName, woNum, woSet, timerSetting);

    }


    @Override
    protected void onPause() {
        super.onPause();
      //  saveStateWithGson();
        saveStateWithJson();

    }

    @Override
    protected void onResume() {


        super.onResume();
        // restoreStateWithGson();
       restoreStateWithJson();
    }


    public void saveStateWithJson() {
        SharedPreferences prefForWo = getSharedPreferences("prefForWoWithJson", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefForWo.edit();
        int a;
////
        ArrayList<WorkoutItem> saveArray;
        WorkoutItem tempItem;
        saveArray = (ArrayList<WorkoutItem>) workoutAdapter.woItems.clone();

        JSONArray jsonArray = new JSONArray();

        for (int i = 0; i < saveArray.size(); i++) {
            tempItem = saveArray.get(i);

            jsonArray.put( tempItem.toJSON() );
            Log.wtf("saved", "saved Item : " + tempItem.toJSON());

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

        String jsonString = prefForWo.getString("arrayListItem", "Err:item not transferred");

        ArrayList<WorkoutItem> loadArray = new ArrayList<>();

        if (jsonString != null) {
            try {
                JSONArray jsonArray = new JSONArray(jsonString);
                Log.wtf("saved jsonArray : ", "loaded Item : " + jsonArray.toString());


                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject joLoaded = jsonArray.getJSONObject(i);

                    Log.wtf("loaded", "loaded Item : " + joLoaded.toString());

                    WorkoutItem tempItem = new WorkoutItem();
                    tempItem.setWoName( joLoaded.getString("woName")    );
                    tempItem.setWoNum( joLoaded.getString("woNum")    );
                    tempItem.setWoSet( joLoaded.getString("woSet")    );
                    tempItem.setTimerSetting( joLoaded.getString("timerSetting")    );

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
                e.printStackTrace();
            }
        }

        workoutAdapter.woItems = (ArrayList<WorkoutItem>) loadArray.clone();


        // mID를 세팅해줘야 아이템클릭(수정에 사용)이 제대로된다.
        for (int i = 0; i < workoutAdapter.getCount(); i++) {
            ((WorkoutItem) workoutAdapter.getItem(i)).mID = i;
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

        Log.wtf("wtf",json);
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
