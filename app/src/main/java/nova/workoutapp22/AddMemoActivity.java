package nova.workoutapp22;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import static nova.workoutapp22.timeController.getTimeCutSec;

public class AddMemoActivity extends AppCompatActivity {

    EditText editTextMemo;
    TextView strDate;
    ImageView photo;
    int mIDForTransport;

    String strMemoMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_memo);

        photo = (ImageView)findViewById(R.id.imageViewMemo);
        editTextMemo = (EditText) findViewById(R.id.editTextMemo);
        strDate = (TextView)findViewById(R.id.textViewDate);
        strDate.setText(getTimeCutSec());

        Intent intent = getIntent();




        Log.v("tagaa", "editTextMemo.text = "+editTextMemo.getText());

        strMemoMode = intent.getStringExtra(BasicInfo.KEY_MEMO_MODE);

        //메모를 걍클릭 함(모드뷰), or 롱클릭 -> 수정누름 (MODE_MODIFY)
        // 기존 내용을 먼저 그려주고, 사용자의 입력을 저장해준다
        if (strMemoMode.equals(BasicInfo.MODE_MODIFY) || strMemoMode.equals(BasicInfo.MODE_VIEW) ) {


            processIntent(intent);



        }


        else { // 새로 메모를 하려는 경우. 화면을 새로 그려준다.


            Button button = (Button) findViewById(R.id.buttonSaveMemo);
            button.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){

                    Intent intent = new Intent();
                    intent.putExtra("memo", editTextMemo.getText().toString());


                    //굳이 날짜시간은 주고받을 필요 없지. 단순히 시간취하면 되잖아?

                    /*
                    intent.putExtra("date", strDate.getText() );
                   */
                    setResult(RESULT_OK, intent);

                    finish();
                }
            });

        }
    }
    @Override
    protected void onStart(){
        super.onStart();
        editTextMemo.setText(getIntent().getExtras().getString("memo"));

    }
////////////////////// 아이템을 수정하여 담아주는 인텐트 :: 수정할 때에는 ID를 같이 보내주어야 교체가 가능해진다.
    public void processIntent(Intent intent){

        editTextMemo.setText(intent.getStringExtra(BasicInfo.KEY_MEMO_TEXT));

        strDate.setText(getTimeCutSec());

        mIDForTransport = intent.getIntExtra("mID",1);


        Button button = (Button) findViewById(R.id.buttonSaveMemo);
        button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                Intent secondIntent = new Intent();
                secondIntent.putExtra("memo", editTextMemo.getText().toString());

                secondIntent.putExtra("date", strDate.getText() );

                secondIntent.putExtra("mID", mIDForTransport);



                setResult(RESULT_OK, secondIntent);

                finish();
            }
        });


        // 사진에 대한 코드를 추가할것!!! photo.setImageintent.getStringExtra("resId");

        /*
        mMemoId = intent.getStringExtra(BasicInfo.KEY_MEMO_ID);
        mMemoEdit.setText(intent.getStringExtra(BasicInfo.KEY_MEMO_TEXT));
        mMediaPhotoId = intent.getStringExtra(BasicInfo.KEY_ID_PHOTO);
        mMediaPhotoUri = intent.getStringExtra(BasicInfo.KEY_URI_PHOTO);
        mMediaVideoId = intent.getStringExtra(BasicInfo.KEY_ID_VIDEO);
        mMediaVideoUri = intent.getStringExtra(BasicInfo.KEY_URI_VIDEO);
        mMediaVoiceId = intent.getStringExtra(BasicInfo.KEY_ID_VOICE);
        mMediaVoiceUri = intent.getStringExtra(BasicInfo.KEY_URI_VOICE);
        mMediaHandwritingId = intent.getStringExtra(BasicInfo.KEY_ID_HANDWRITING);
        mMediaHandwritingUri = intent.getStringExtra(BasicInfo.KEY_URI_HANDWRITING);

*/



    }





    /////////////////////////////////////////생명주기 관련 파트////////
    ////////// onPause에서 입력하던 상태가 저장되며, onResume에서 입력하던 상태가 복원된다.

    @Override
    protected void onPause(){
        super.onPause();



       // Toast.makeText(this, "onPause called", Toast.LENGTH_SHORT).show();
       // saveState();
    }

    @Override
    protected void onResume(){
        super.onResume();

       // Toast.makeText(this, "onPause called", Toast.LENGTH_SHORT).show();
       // restoreState();
        //clearMyPrefs();

    }

    protected void restoreState() {
        SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        if ((pref != null) && (pref.contains("memo")) ){
            String name = pref.getString("memo", "");
            editTextMemo.setText(name);
        }
    }

    protected void saveState() {
        SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString( "memo", editTextMemo.getText().toString() );
        editor.commit();
    }

    protected void clearMyPrefs() {
        SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.commit();
    }
    ////////////////////////////////////////////////////////////////////////////////////

}
