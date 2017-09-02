package nova.workoutapp22;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddWorkoutActivity extends AppCompatActivity {
    EditText workoutName, workoutNum, workoutSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_workout);




        workoutName = (EditText)findViewById(R.id.editTextWorkOutName);
        workoutNum = (EditText)findViewById(R.id.editTextWNum);
        workoutSet = (EditText)findViewById(R.id.editTextWSet);


        (findViewById(R.id.buttonAddCompleteWO)).setOnClickListener(mClickListener);
    }

    Button.OnClickListener mClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            Intent intent;
            switch (v.getId()) {
                case R.id.buttonAddCompleteWO:
                    intent = new Intent(getApplicationContext(), WorkoutActivity.class);

                    intent.putExtra("workoutName", workoutName.getText().toString() );
                    intent.putExtra("workoutNum", workoutNum.getText().toString() );
                    intent.putExtra("workoutSet", workoutSet.getText().toString() );


                    setResult(RESULT_OK , intent);
                    finish();
                    break;
            }
        }
    };
}
