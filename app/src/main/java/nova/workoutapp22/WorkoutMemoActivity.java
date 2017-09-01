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
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static nova.workoutapp22.BasicInfo.REQ_ADDMEMO_ACTIVITY;
import static nova.workoutapp22.BasicInfo.REQ_VIEW_ACTIVITY;
import static nova.workoutapp22.timeController.getTime;


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



    EditText editText;

    ListView listView;
    MemoAdaptor memoadapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        clearMyPrefs();

        setContentView(R.layout.activity_workoutmemo);

        listView = (ListView) findViewById(R.id.listView);




        listView.setAdapter(memoadapter);

////////////////////////////// 새로운 메모를 만든다.
        Button button = (Button) findViewById(R.id.buttonAddMemo);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), AddMemoActivity.class);
                intent.putExtra(BasicInfo.KEY_MEMO_MODE, BasicInfo.MODE_ADD);
                startActivityForResult(intent, REQ_ADDMEMO_ACTIVITY);



                /*
                adapter.addItem(new MemoItem(name, mobile, age, R.drawable.singer3));
                adapter.notifyDataSetChanged();
                */
            }
        });


/////////////////////////////// 메모아이템을 수정한다.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {


                MemoItem item = (MemoItem) memoadapter.getItem(position);

                // 수정 -- 메모 보기 액티비티 띄우기
                Intent intent = new Intent(getApplicationContext(), AddMemoActivity.class);
                intent.putExtra(BasicInfo.KEY_MEMO_MODE, BasicInfo.MODE_VIEW);



                intent.putExtra("mID", item.getmID());
                intent.putExtra("imageUri", item.getUri());


                intent.putExtra("memo", item.getMemo());
                intent.putExtra("date", item.getDate());
                intent.putExtra("resId", item.getResId());

                startActivityForResult(intent, REQ_VIEW_ACTIVITY);
                //////////////////

            }
        });


        ///////////////롱클릭을 통한 수정 / 삭제 메뉴를 추가해야 한다.
        //////*
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {

                MemoItem item = (MemoItem) memoadapter.getItem(position);

                showMessage(item);


                return true;
            }
        });
    }
    public void showMessage(final MemoItem item){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("삭제");
        builder.setMessage("삭제하시겠습니까?");
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setPositiveButton("예", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {

                memoadapter.removeItem(item);

                memoadapter.notifyDataSetChanged();
            }

        });

        builder.setNeutralButton("취소", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which){
            dialog.dismiss();
            }

        });


        AlertDialog dialog = builder.create();
        dialog.show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        //새로운 메모 작성이 완료되었다.

        if(requestCode == REQ_ADDMEMO_ACTIVITY){
            //Toast.makeText(getApplicationContext(),"onActivResult 호출됨, 요청 코드 : "+requestCode+
              //      ", 결과 코드 : " +resultCode, Toast.LENGTH_SHORT).show();

            if(resultCode == RESULT_OK){

                MemoItem newmit = setItemFromIntent(data);

                memoadapter.addItem( newmit );

                memoadapter.notifyDataSetChanged();
            }
        }

        ////////////////// 수정을 완료한 상태가 되었다!

        else if(requestCode == REQ_VIEW_ACTIVITY){
           // Toast.makeText(getApplicationContext(),"onActivResult 호출됨, 요청 코드 : "+requestCode+
            //        ", 결과 코드 : " +resultCode, Toast.LENGTH_SHORT).show();

            if(resultCode == RESULT_OK){

                MemoItem newmit = setItemFromIntent(data);

                int mmID = data.getExtras().getInt("mID");

                ////////////////////////////
                ////////////////////////////
                ////////////////////////////

                //////////////////////주의 주의 3시간 이상 삽질한 문제 : new 아이템 만들고 ID를 초기화 안함
                // -> 계쏙해서 잘못된 mID를 전달하게 됨.
                // 인텐트의 전달이 계속 잘못되면 인텐트 관련 메소드를 살피자. 이것도 인텐트 관련 메소드다.

                newmit.setmID(mmID); //////////////ㄹㅇ 정신나간 코드임;

                memoadapter.setItem(mmID, newmit);

                memoadapter.notifyDataSetChanged();
            }
        }
    }


    public MemoItem setItemFromIntent(Intent data){
        String memo = data.getExtras().getString("memo");

        Uri uri=Uri.parse(data.getStringExtra("imageUri"));

        String date = getTime();


        return new MemoItem(memo, date, uri);

    }


    class MemoAdaptor extends BaseAdapter {
        ArrayList<MemoItem> items = new ArrayList<>();

        @Override
        public int getCount() {
            return items.size();
        }
////////////////////////////////////////////////////////////
        public void addItem(MemoItem item) {

            items.add(item);
            item.mID = getCount()-1;
            Log.v("mIDTag33", "mID" + item.mID);
        }
////////////////////////////////

        public void setItem(int mID, MemoItem item) {



            items.set(mID, item);

        }

        public void removeItem(MemoItem item){ ///////리무브 코드 에러날 가능성 크다.

            /////// 1, 2, 3, 4, 5에서 3을 지우면 1, 2, 4, 5가 된다. -> 추후 어레이리스트 관리에 문제 발생
            /// 끝자락이 아니라면 무조건 mID를 재정렬해주어야 한다...
            items.remove(item);
            if(item.mID != getCount() ){ // 3은 끝자락이 아니다. (4가 아님)
                //처음부터 끝(0부터 3까지 재정렬 해주면되겠네 .
                for(int i = 0; i < getCount(); i++){
                    ((MemoItem)getItem(i)).mID = i;
                }
            }

        }


        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            MemoItemView view = new MemoItemView(getApplicationContext());

            MemoItem item = items.get(position);
            view.setMemo(item.getMemo());
            view.setDate(item.getDate());
            view.setImageWithUri(item.getUri());

            return view;
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


    public void saveState(){

        Toast.makeText(getApplicationContext(), "saveState Called", Toast.LENGTH_SHORT).show();

        SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        Gson gson = new Gson();
        ArrayList<MemoItem> saveArray = memoadapter.items;

        String json = gson.toJson(saveArray);

        editor.putString("tagtag", json);
        editor.commit();


    }

    public void restoreState(){
        Toast.makeText(getApplicationContext(), "restorestate Called", Toast.LENGTH_SHORT).show();

        memoadapter = new MemoAdaptor();

        String resDrawableUri = "android.resource://"+getApplicationContext().getPackageName()+"/drawable/basicimage";
        memoadapter.addItem(new MemoItem("메모내용 예시", getTime(), Uri.parse(resDrawableUri)) );


        SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);

        Gson gson = new Gson();
        String json = pref.getString("tagtag", null);

        Type type = new TypeToken<ArrayList<MemoItem>>() {}.getType();

        ArrayList<MemoItem> loadArray = gson.fromJson(json, type);

        memoadapter.items = loadArray;

    }



    protected void clearMyPrefs() {
        Toast.makeText(getApplicationContext(), "pref cleared", Toast.LENGTH_SHORT).show();

        SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.commit();
    }




}
