package nova.workoutapp22;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import nova.workoutapp22.listviewSrcForWorkOut.WorkoutAdapter;
import nova.workoutapp22.listviewSrcForWorkOut.WorkoutItem;
import nova.workoutapp22.subSources.BasicInfo;

import static nova.workoutapp22.subSources.BasicInfo.REQ_ADD_WORKOUT;
import static nova.workoutapp22.subSources.BasicInfo.REQ_MODIFY_WORKOUT;


public class WorkoutActivity extends AppCompatActivity {


    public static final int REQUEST_CODE_APHRODITE = 201;


    ListView listViewForWorkout;
    WorkoutAdapter workoutAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);




        workoutAdapter = new WorkoutAdapter();
        listViewForWorkout = (ListView) findViewById(R.id.listViewForWorkout);
        listViewForWorkout.setAdapter(workoutAdapter);

        workoutAdapter.addItem(new WorkoutItem("벤치 프레스", "50개", "3세트", "타이머 사용") );
        workoutAdapter.addItem(new WorkoutItem("팔굽혀 펴기", "20개", "5세트", "스톱워치 사용") );
        workoutAdapter.addItem(new WorkoutItem("스쿼트", "100개", "2세트", "사용 안함") );






        setItemClick();


        // delete button에 대한 이벤트 처리.
        Button deleteButton = (Button) findViewById(R.id.buttonWoDelete);
        deleteButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                SparseBooleanArray checkedItems = listViewForWorkout.getCheckedItemPositions();
                int count = workoutAdapter.getCount();

                for (int i = count - 1; i >= 0; i--) {
                    if (checkedItems.get(i)) {
                        workoutAdapter.woItems.remove(i);
                    }
                }

                // 모든 선택 상태 초기화.
                listViewForWorkout.clearChoices();

                workoutAdapter.notifyDataSetChanged();
            }
        });


        // selectAll button에 대한 이벤트 처리.
        Button selectAllButton = (Button)findViewById(R.id.buttonWoSelectAll) ;
        selectAllButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {

                int count = workoutAdapter.getCount() ;

                for (int i=0; i<count; i++) {
                    listViewForWorkout.setItemChecked(i, true) ;
                }

            }
        }) ;

        // selectAll button에 대한 이벤트 처리.
        Button clearButton = (Button)findViewById(R.id.buttonWoCancelSelect) ;
        clearButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {

                listViewForWorkout.clearChoices();
                workoutAdapter.notifyDataSetChanged();
            }
        }) ;










        findViewById(R.id.buttonAddWO).setOnClickListener(mClickListener);
    }


    ////////////////////////////////////////////새로운 메모를 만든다.

    Button.OnClickListener mClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            Intent intent;
            switch (v.getId()) {
                case R.id.buttonAddWO:
                    intent = new Intent(getApplicationContext(), AddWorkoutActivity.class);
                    intent.putExtra(BasicInfo.KEY_ADDWO_MODE, BasicInfo.MODE_MODIFY);

                    startActivityForResult(intent, BasicInfo.REQ_ADD_WORKOUT);
                    break;

            }
        }
    };


    public void onClick(View v) {
        switch (v.getId()) {
            case (R.id.buttonWoSwitch):
                ListView listView = (ListView) findViewById(R.id.listViewForWorkout);
                if (listView.getChoiceMode() == ListView.CHOICE_MODE_MULTIPLE) {

                    Toast.makeText(getApplicationContext(), "단일 선택 모드로 변경되었습니다.", Toast.LENGTH_SHORT).show();


                    listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

                    setItemClick();



                } else if (listView.getChoiceMode() == ListView.CHOICE_MODE_SINGLE) {
                    Toast.makeText(getApplicationContext(), "다중 선택 모드로 변경되었습니다.", Toast.LENGTH_SHORT).show();
                    listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);


                    //아이템클릭리스너를 무효화한다.
                    listView.setOnItemClickListener(null);

                }
                break;
        }
    }


    public void setItemClick(){
        listViewForWorkout.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {



                WorkoutItem item = (WorkoutItem) workoutAdapter.getItem(position);

                // 수정 -- 메모 보기 액티비티 띄우기
                Intent intent = new Intent(getApplicationContext(), AddWorkoutActivity.class);

               // intent.putExtra(BasicInfo.KEY_MEMO_MODE, BasicInfo.MODE_VIEW);
                intent.putExtra(BasicInfo.KEY_ADDWO_MODE, BasicInfo.MODE_MODIFY);


                intent.putExtra("mID", item.getmID());

                intent.putExtra("workoutName", item.getWoName().toString() );
                intent.putExtra("workoutNum", item.getWoNum().toString() );
                intent.putExtra("workoutSet", item.getWoSet().toString() );
                intent.putExtra("timerSetting", item.getTimerSetting().toString() );


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

        if(requestCode == REQ_ADD_WORKOUT){

            if(resultCode == RESULT_OK){



                // 리스트뷰에 해당 내용을 담아 추가한다.

                WorkoutItem newmit = setItemFromIntent(data);

                workoutAdapter.addItem( newmit );

                workoutAdapter.notifyDataSetChanged();


            }
        }

        else if(requestCode == REQ_MODIFY_WORKOUT){

            if(resultCode == RESULT_OK){

                //수정된 데이터를 삽입

//////////////////////주의 주의 3시간 이상 삽질한 문제 : new 아이템 만들고 ID를 초기화 안함
                // -> 계쏙해서 잘못된 mID를 전달하게 됨.
                // 인텐트의 전달이 계속 잘못되면 인텐트 관련 메소드를 살피자. 이것도 인텐트 관련 메소드다.

                WorkoutItem newmit = setItemFromIntent(data);

                int mmID = data.getExtras().getInt("mID");
                newmit.setmID(mmID);  //////////////ㄹㅇ 정신나간 코드임;


                workoutAdapter.setItem(mmID, newmit);

                workoutAdapter.notifyDataSetChanged();

            }

        }


    }

    public WorkoutItem setItemFromIntent(Intent data){

        String woName = data.getExtras().getString("workoutName");
        String woNum = data.getExtras().getString("workoutNum");
        String woSet = data.getExtras().getString("workoutSet");
        String timerSetting = data.getExtras().getString("timerSetting");


        return new WorkoutItem(woName, woNum, woSet, timerSetting);

    }



}
