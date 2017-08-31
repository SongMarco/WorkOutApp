package nova.workoutapp22;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class WorkoutActivity extends AppCompatActivity {
    public static final int REQUEST_CODE_APHRODITE = 201;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);
    }
    public void onAphroClicked(View v){
        Intent intent = new Intent(getApplicationContext(), WorkoutAphroActivity.class);
        startActivityForResult(intent, REQUEST_CODE_APHRODITE);
    }
}
