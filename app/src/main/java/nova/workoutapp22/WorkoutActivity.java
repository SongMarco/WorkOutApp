package nova.workoutapp22;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import nova.workoutapp22.subSources.BasicInfo;

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

}
