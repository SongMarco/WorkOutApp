package nova.workoutapp22;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    public static final int REQUEST_CODE_MOTIVE = 101;
    public static final int REQUEST_CODE_WORKOUT = 102;
    public static final int REQUEST_CODE_DIARY = 103;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolBarMainActivity);

        setSupportActionBar(myToolbar);

        findViewById(R.id.mainToMotiveButton).setOnClickListener(mClickListener);
        findViewById(R.id.mainToWorkoutButton).setOnClickListener(mClickListener);
        findViewById(R.id.mainToDiaryButton).setOnClickListener(mClickListener);

    }

    Button.OnClickListener mClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            Intent intent;
            switch (v.getId()) {
                case R.id.mainToMotiveButton:
                    intent = new Intent(getApplicationContext(), MotivationActivity.class);
                    startActivityForResult(intent, REQUEST_CODE_MOTIVE);
                    break;
                case R.id.mainToWorkoutButton:
                    intent = new Intent(getApplicationContext(), WorkoutActivity.class);
                    startActivityForResult(intent, REQUEST_CODE_WORKOUT);
                    break;
                case R.id.mainToDiaryButton:
                    intent = new Intent(getApplicationContext(), WorkoutMemoActivity.class);
                    startActivityForResult(intent, REQUEST_CODE_DIARY);
                    break;
            }
        }
    };
/*
    @Override
    protected void onStart(){
        super.onStart();

        Toast.makeText(this,"onStart called", Toast.LENGTH_LONG).show();
    }
    @Override
    protected void onStop(){
        super.onStop();

        Toast.makeText(this,"onStop called", Toast.LENGTH_LONG).show();
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();

        Toast.makeText(this,"onDestroy called", Toast.LENGTH_LONG).show();
    }
    @Override
    protected void onRestart(){
        super.onRestart();
        Toast.makeText(this,"onRestart called", Toast.LENGTH_LONG).show();
    }
    @Override
    protected void onPause(){
        super.onPause();

        Toast.makeText(this,"onPause called", Toast.LENGTH_LONG).show();
    }
    @Override
    protected void onResume(){
        super.onResume();

        Toast.makeText(this,"onResume called", Toast.LENGTH_LONG).show();
    }
*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
/*
        if (requestCode == REQUEST_CODE_MENU) {
            Toast.makeText(getApplicationContext(),
                    "onActivityResult 메소드 호출됨. 요청 코드 : " + requestCode +
                            ", 결과 코드 : " + resultCode, Toast.LENGTH_LONG).show();

            if (resultCode == RESULT_OK){
                String name = data.getExtras().getString("name");
                Toast.makeText(getApplicationContext(), "응답으로 전달된 name : " +name ,
                        Toast.LENGTH_LONG).show();
            }

        }
        */
    }

}
