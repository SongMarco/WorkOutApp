package nova.workoutapp22;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import nova.workoutapp22.subSources.BasicInfo;

import static nova.workoutapp22.subSources.BasicInfo.REQ_ADD_WORKOUT;
import static nova.workoutapp22.subSources.BasicInfo.REQ_MODIFY_WORKOUT;

public class WorkoutActivity extends AppCompatActivity {
    public static final int REQUEST_CODE_APHRODITE = 201;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);


        findViewById(R.id.buttonAddWO).setOnClickListener(mClickListener);
    }
    public void onAphroClicked(View v){
        Intent intent = new Intent(getApplicationContext(), WorkoutAphroActivity.class);
        startActivityForResult(intent, REQUEST_CODE_APHRODITE);
    }

    Button.OnClickListener mClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            Intent intent;
            switch (v.getId()) {
                case R.id.buttonAddWO:
                    intent = new Intent(getApplicationContext(), AddWorkoutActivity.class);
                    startActivityForResult(intent, BasicInfo.REQ_ADD_WORKOUT);
                    break;

            }
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQ_ADD_WORKOUT){

            if(resultCode == RESULT_OK){

                // 리스트뷰에 해당 내용을 담아 추가한다.
                /*
                MemoItem newmit = setItemFromIntent(data);

                memoAdapter.addItem( newmit );

                memoAdapter.notifyDataSetChanged();
                */

            }
        }

        else if(requestCode == REQ_MODIFY_WORKOUT){

            if(resultCode == RESULT_OK){

                //수정된 데이터를 삽입
                /*
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
                */
            }

        }


    }


}
