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
import android.widget.Adapter;
import android.widget.AdapterView;
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

import java.lang.reflect.Type;
import java.util.ArrayList;

import nova.workoutapp22.listviewSrcForMemo.MemoAdapter;
import nova.workoutapp22.listviewSrcForMemo.MemoItem;
import nova.workoutapp22.subSources.BasicInfo;

import static nova.workoutapp22.MainActivity.fadeIn;
import static nova.workoutapp22.subSources.BasicInfo.REQ_ADD_MEMO;
import static nova.workoutapp22.subSources.BasicInfo.REQ_MODIFY_MEMO;
import static nova.workoutapp22.subSources.timeController.getTime;


/////////////////////
/*
리스트뷰 구현하는법.TimeCapsule

1. 추가하기
리스트뷰에 들어갈 아이템 xml 작성

리스트뷰 아이템 추가 액티비티 xml + 자바 코드작성

추가 액티비티+메인액티비티 버튼이벤트로 연결

리스트뷰 아이템을 memoItemView 자바가 inflate해준다.(객체화)

메모아이템에는 리스트뷰 아이템을 관리하기위한 메소드가 정의됨.

-> 이거를 메인 메소드의 맨 밑에서 가져다쓴다. (미래의 나여...)


2. 수정하기

3. 삭제하기





 */

public class WorkoutMemoActivity extends AppCompatActivity {

    ListView listViewForMemo;
    MemoAdapter memoAdapter;
    Toolbar memoToolbar;

    boolean isMultMode = false;
    String memoMenuState = BasicInfo.MENU_WO_NORMAL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_workoutmemo);


        memoAdapter = new MemoAdapter();

        String resDrawableUri = "android.resource://" + getApplicationContext().getPackageName() + "/drawable/basicimage";
        memoAdapter.addItem(new MemoItem("메모내용 예시1", getTime(), Uri.parse(resDrawableUri)));
        memoAdapter.addItem(new MemoItem("메모내용 예시2", getTime(), Uri.parse(resDrawableUri)));
        memoAdapter.addItem(new MemoItem("메모내용 예시3", getTime(), Uri.parse(resDrawableUri)));

        listViewForMemo = (ListView) findViewById(R.id.listViewForMemo);


        listViewForMemo.setAdapter(memoAdapter);
        setItemClick();
        setItemLongClicker(listViewForMemo, memoAdapter);


        ///////////////////////툴바를 만듭니다
        memoToolbar = (Toolbar) findViewById(R.id.toolBarMemoActivity);
        setSupportActionBar(memoToolbar);
/////////////////////////////////////////

/////////////////////////////// 메모아이템을 수정한다.


        ///////////////롱클릭을 통한 수정 / 삭제 메뉴를 추가해야 한다.
        //////*



    }

    public void setItemLongClicker(final ListView lv, final Adapter adapter) {

        //멀티모드가 아니었다. 멀티모드로 만든다.

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {

                if (isMultMode == false) {
                    lv.clearChoices();

                    setMultipleChoice(lv);


                    lv.setItemChecked(position, true);

                    isMultMode = true;


                    return true;
                }
                //멀티모드면 미리 선택된 아이템부터~ 선택한 아이템까지 모두 선택처리.
                else {

                    int lastPosition = -1;

                    for (int i = 0; i < adapter.getCount(); i++) {

                        if (lv.isItemChecked(i) == true) {
                            lastPosition = i;
                        }

                    }
                    //마지막에 선택한 아이템이 없을 경우
                    if (lastPosition == -1) {
                        lv.setItemChecked(position, true);
                        return true;
                    }

                    // 마지막에 선택한 아이템 존재! 얘부터 포지션까지 체크처리한다.
                    else {
                        // 마지막 선택 포지션이 지금 포지션과 비교해서 큰?작? 같?에 따라 다른가??

                        if (lastPosition < position) {
                            for (int i = lastPosition; i <= position; i++) {
                                lv.setItemChecked(i, true);
                            }

                        } else if (lastPosition > position) {
                            for (int i = lastPosition; i >= position; i--) {
                                lv.setItemChecked(i, true);
                            }

                        } else {
                            lv.setItemChecked(position, false);
                            return true;
                        }
                    }
                }

                return true;
            }
        });

        //멀티모드이므로 활성화되지 않음


    }


    //region @@@@@액션바 메뉴 관련 파트@@@@@
    ///
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_workout, menu);


        if (memoMenuState.equals(BasicInfo.MENU_WO_MULT)) {

            memoToolbar.getChildAt(1).startAnimation(fadeIn);
            menu.findItem(R.id.action_addItem).setVisible(false);
            menu.findItem(R.id.action_delete).setVisible(true);
            menu.findItem(R.id.action_selectAll).setVisible(true);
            menu.findItem(R.id.action_clearSelection).setVisible(true);

        } else {

            memoToolbar.getChildAt(1).startAnimation(fadeIn);
            menu.findItem(R.id.action_addItem).setVisible(true);
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

            case R.id.action_addItem:

                Intent intent = new Intent(getApplicationContext(), AddMemoActivity.class);
                intent.putExtra(BasicInfo.KEY_ADDMEMO_MODE, BasicInfo.MODE_ADD);

                startActivityForResult(intent, BasicInfo.REQ_ADD_MEMO);
                return true;

            case R.id.action_selectMult:


                //이미 멀티모드였다면 멀티모드를 비활성화하도록 할 것.
                if (isMultMode == true) {

                    setSingleChoice(listViewForMemo);

                }
                //멀티모드가 아니므로 멀티모드 활성화
                else {


                    setMultipleChoice(listViewForMemo);
                }

                return true;

            case R.id.action_delete:
                SparseBooleanArray checkedItems = listViewForMemo.getCheckedItemPositions();

                Boolean okToDelete = false;

                int count2 = memoAdapter.getCount();

                //아이템 선택을 하지 않았다면 토스트를 띄워주고 돌아간다.


                for (int i = count2 - 1; i >= 0; i--) {

                    //int i = count - 1;  0<=i; i--
                    if (checkedItems.get(i)) {
                        okToDelete = true;
                    }
                }
                if (okToDelete == false) {
                    Toast.makeText(getApplicationContext(), "삭제할 아이템을 선택하지 않으셨네요!", Toast.LENGTH_SHORT).show();
                    return true;
                }
                // 제거할 아이템이 있다. 제거를 물어보자
                else {
                    askDelete();

                    return true;
                }


            case R.id.action_selectAll:
                int count = memoAdapter.getCount();

                for (int i = 0; i < count; i++) {
                    listViewForMemo.setItemChecked(i, true);
                }

                return true;

            case R.id.action_clearSelection:
                count = memoAdapter.getCount();
                for (int i = 0; i < count; i++) {

                    listViewForMemo.setItemChecked(i, false);

                }
                return true;


            default:
                return true;
        }

    }
    //endregion@@@@@


    public void askDelete() {
        // 1. Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(WorkoutMemoActivity.this);

        builder.setMessage("정말 삭제하시겠습니까?")
                .setTitle("삭제 확인")
                .setIcon(R.drawable.ic_warning_black_48dp);

        // Add the buttons
        builder.setPositiveButton("삭제합니다", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                SparseBooleanArray checkedItems = listViewForMemo.getCheckedItemPositions();
                int count2 = memoAdapter.getCount();

                for (int i = count2 - 1; i >= 0; i--) {

                    //int i = count - 1;  0<=i; i--
                    if (checkedItems.get(i)) {
                        MemoItem item = memoAdapter.items.get(i);
                        memoAdapter.removeItem(item);
                    }
                }
                // 모든 선택 상태 초기화.
                listViewForMemo.clearChoices();
                memoAdapter.notifyDataSetChanged();
                memoAdapter.setCheckBoxState(false);
                setSingleChoice(listViewForMemo);

                memoMenuState = BasicInfo.MENU_WO_NORMAL;
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


    public void setSingleChoice(ListView lv) {

        //  Toast.makeText(getApplicationContext(), "단일 선택 모드로 변경되었습니다.", Toast.LENGTH_SHORT).show();

        lv.clearChoices();
        lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        memoAdapter.setCheckBoxState(false);

        setItemClick();

        memoMenuState = BasicInfo.MENU_WO_NORMAL;
        isMultMode = false;
        invalidateOptionsMenu();

    }

    public void setMultipleChoice(ListView lv) {
        // Toast.makeText(getApplicationContext(), "다중 선택 모드로 변경되었습니다.", Toast.LENGTH_SHORT).show();


        lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        memoAdapter.setCheckBoxState(true);


        //아이템클릭리스너를 무효화한다.
        lv.setOnItemClickListener(null);

        memoMenuState = BasicInfo.MENU_WO_MULT;
        isMultMode = true;
        invalidateOptionsMenu();
    }

    public void showDeleteMessage(final MemoItem item) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("삭제");
        builder.setMessage("삭제하시겠습니까?");
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                memoAdapter.removeItem(item);

                memoAdapter.notifyDataSetChanged();
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

    public void setItemClick() {
        listViewForMemo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {


                MemoItem item = (MemoItem) memoAdapter.getItem(position);

                // 수정 -- 메모 보기 액티비티 띄우기
                Intent intent = new Intent(getApplicationContext(), AddMemoActivity.class);
                intent.putExtra(BasicInfo.KEY_ADDMEMO_MODE, BasicInfo.MODE_VIEW);


                intent.putExtra("mID", item.getmID());
                intent.putExtra("imageUri", item.getUri());


                intent.putExtra("memo", item.getMemo());
                intent.putExtra("date", item.getDate());


                // 모든 선택 상태 초기화.
                listViewForMemo.clearChoices();
                memoAdapter.notifyDataSetChanged();

                startActivityForResult(intent, REQ_MODIFY_MEMO);
                //////////////////

            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        //새로운 메모 작성이 완료되었다.

        if (requestCode == REQ_ADD_MEMO) {
            //Toast.makeText(getApplicationContext(),"onActivResult 호출됨, 요청 코드 : "+requestCode+
            //      ", 결과 코드 : " +resultCode, Toast.LENGTH_SHORT).show();

            if (resultCode == RESULT_OK) {

                MemoItem newmit = setItemFromIntent(data);

                memoAdapter.addItem(newmit);

                memoAdapter.notifyDataSetChanged();
            }
        }

        ////////////////// 수정을 완료한 상태가 되었다!

        else if (requestCode == REQ_MODIFY_MEMO) {
            // Toast.makeText(getApplicationContext(),"onActivResult 호출됨, 요청 코드 : "+requestCode+
            //        ", 결과 코드 : " +resultCode, Toast.LENGTH_SHORT).show();

            if (resultCode == RESULT_OK) {

                MemoItem newmit = setItemFromIntent(data);

                int mmID = data.getExtras().getInt("mID");

                ////////////////////////////
                ////////////////////////////
                ////////////////////////////

                //////////////////////주의 주의 3시간 이상 삽질한 문제 : new 아이템 만들고 ID를 초기화 안함
                // -> 계쏙해서 잘못된 mID를 전달하게 됨.
                // 인텐트의 전달이 계속 잘못되면 인텐트 관련 메소드를 살피자. 이것도 인텐트 관련 메소드다.

                newmit.setmID(mmID); //////////////ㄹㅇ 정신나간 코드임;

                memoAdapter.setItem(mmID, newmit);

                memoAdapter.notifyDataSetChanged();
                Log.v("순서추적", "데이타 변경 완료");
            }
        }
    }


    public MemoItem setItemFromIntent(Intent data) {
        String memo = data.getExtras().getString("memo");

        Uri uri = Uri.parse(data.getStringExtra("imageUri"));

        String date = getTime();


        return new MemoItem(memo, date, uri);

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
        Log.v("순서추적", "restore진행됨");
    }


    public void saveState() {

        SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Uri.class, new UriSerializer())
                .create();

        ArrayList<MemoItem> saveArray;
        saveArray = (ArrayList<MemoItem>) memoAdapter.items.clone();


        String json = gson.toJson(saveArray);
        editor.putString("arrayList", json);
        editor.apply();


    }

    public void restoreState() {
        Toast.makeText(getApplicationContext(), "restore Called", Toast.LENGTH_SHORT).show();

        SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);

        if ((pref != null) && (pref.contains("arrayList"))) {

            Toast.makeText(getApplicationContext(), "restore executed", Toast.LENGTH_SHORT).show();
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(Uri.class, new UriDeserializer())
                    .create();


            String json = pref.getString("arrayList", null);

            //Type type = TypeToken.getParameterized( (ArrayList<MemoItem>) , ).getType();

            ArrayList<MemoItem> loadArray = null;

            loadArray = gson.fromJson(json, new TypeToken<ArrayList<MemoItem>>() {
            }.getType());

            memoAdapter.items = (ArrayList<MemoItem>) loadArray.clone();

            // mID를 세팅해줘야 아이템클릭(수정에 사용)이 제대로된다.
            for (int i = 0; i < memoAdapter.getCount(); i++) {
                ((MemoItem) memoAdapter.getItem(i)).mID = i;
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
